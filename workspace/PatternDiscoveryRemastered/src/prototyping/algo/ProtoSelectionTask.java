package prototyping.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.TuneFamily;

public class ProtoSelectionTask {
	
	protected TuneFamily tf;
	protected PatternSet ps;
	protected double mr;
	
	protected double pwsBias = 0.05;
	protected double isBias = 0.5;
	protected int numIterations = 10;
	protected double skipCost = 1;
	protected boolean noOtherOccs = true;
	
	protected PatternSet result;
	protected PatternSet filledResult;
	
	public ProtoSelectionTask(TuneFamily tf, PatternSet ps, double mr){
		this.tf = tf;
		this.mr = mr;
		this.ps = ps;
	}
	
	public ProtoSelectionTask(TuneFamily tf, PatternSet ps, double mr,
			double pwsBias, double isBias, int numIterations, double skipCost, boolean noOtherOccs){
		this(tf, ps, mr);
		this.pwsBias = pwsBias;
		this.isBias = isBias;
		this.numIterations = numIterations;
		this.skipCost = skipCost;
		this.noOtherOccs = noOtherOccs;
	}
	
	public PatternSet getResult(){
		return result;
	}
	
	public PatternSet getFilledResult(){
		return filledResult;
	}
	
	public PatternSet run(){
		result = pairWiseSelection(ps, 0);
		
		for(int i = 1; i <= numIterations; i++){
			result = pairWiseSelection(result, pwsBias);
			result = individualSelection(result, isBias);
			result = removeInsufficientPats(result, i);
		}
		
		this.noOtherOccs = false;
		filledResult = pairWiseSelection(result, pwsBias);
		filledResult = individualSelection(filledResult, isBias);
		
		return result;
	}
	
	public PatternSet removeInsufficientPats(PatternSet prev, int iteration){
		PatternSet cropped = new PatternSet();
		for(Pattern p: prev){
			//System.out.println(tf.getSongs().size()*mr*i/numIterations);
			if(tf.getSongs().size()*mr*iteration/numIterations <= p.countSongs())
				cropped.addPattern(p);
		}
		return cropped;
	}
	
	public PatternSet pairWiseSelection(PatternSet prevSet, double bias){
		Map<PatternOccurrence, Double> scores = calculateScores(prevSet, bias);
		Map<Note, Map<Note, List<PairBridge>>> bridges = new HashMap<Note, Map<Note, List<PairBridge>>>();
		for(Song s0: tf){
			for(Note n0: s0){
				bridges.put(n0, new HashMap<Note, List<PairBridge>>());
				for(Song s1: tf){
					for(Note n1: s1){
						bridges.get(n0).put(n1, new ArrayList<PairBridge>());
					}
				}
			}
		}
		for(Pattern p: prevSet){
			Pattern fullPat = ps.getPattern(p);
			for(PatternOccurrence o: fullPat.getOccurrences()){
				for(PatternOccurrence o1: fullPat.getOccurrences()){
					if(o != o1){
						Note useNote = o.getEndNote();
						Note useNote1 = o1.getEndNote();
						if(useNote != o.getSong().getEndNote()) 
							useNote = o.getSong().getNotes().get(useNote.getNoteID()+1);
						if(useNote1 != o1.getSong().getEndNote())
							useNote1 = o1.getSong().getNotes().get(useNote1.getNoteID());
						bridges.get(useNote).get(useNote1).add(new PairBridge(scores.get(o) + scores.get(o1), o, o1, fullPat.getPatternID()));
					}
				}
			}
		}
		
		PatternSet result = new PatternSet();
		List<Song> songs = tf.getSongs();
		for(int si = songs.size()-1; si > 0; si--){
			for(int sj = si-1; sj >= 0; sj--){
				Song s0 = songs.get(si);
				Song s1 = songs.get(sj);
				List<Note> notes0 = s0.getNotes();
				List<Note> notes1 = s1.getNotes();
				Collections.sort(notes0);
				Collections.sort(notes1);
				int length0 = notes0.size();
				int length1 = notes1.size();
				PairCellData[][] cellScores = new PairCellData[length0][length1];
				cellScores[0][0] = new PairCellData(0, null, null, -1, -1, -1);
				for(int i = 1; i < length0; i++){
					cellScores[i][0] = new PairCellData(i*-skipCost, null, null, -1, i-1, 0);
				}
				for(int i = 1; i < length1; i++){
					cellScores[0][i] = new PairCellData(i*-skipCost, null, null, -1, 0, i-1);
				}
				
				for(int i = 1; i < length0; i++){
					for(int j = 1; j < length1; j++){
						cellScores[i][j] = new PairCellData(cellScores[i-1][j-1].score - skipCost, null, null, -1, i-1, j-1);
						if(cellScores[i][j].score < cellScores[i-1][j].score -skipCost){
							cellScores[i][j] = new PairCellData(cellScores[i-1][j].score - skipCost, null, null, -1, i-1, j);
						}
						if(cellScores[i][j].score < cellScores[i][j-1].score -skipCost){
							cellScores[i][j] = new PairCellData(cellScores[i][j-1].score - skipCost, null, null, -1, i, j-1);
						}
						for(PairBridge b: bridges.get(notes0.get(i)).get(notes1.get(j))){
							if(cellScores[i][j].score < cellScores[b.occurrence0.getStartNote().getNoteID()][b.occurrence1.getStartNote().getNoteID()].score + b.score){
								cellScores[i][j] = new PairCellData(cellScores[b.occurrence0.getStartNote().getNoteID()][b.occurrence1.getStartNote().getNoteID()].score + b.score,
										b.occurrence0, b.occurrence1, b.patternID, b.occurrence0.getStartNote().getNoteID(), b.occurrence1.getStartNote().getNoteID());
							}
						}
					}
				}
				int i0 = length0-1;
				int i1 = length1-1;
				while(i0 > 0 && i1 > 0){
					if(cellScores[i0][i1].occurrence0 != null){
						Pattern pNew = new Pattern(cellScores[i0][i1].patternID);
						Pattern pOld = result.getPattern(pNew);
						if(pOld != null){
							pNew = pOld;
						} else result.addPattern(pNew);
						boolean contains0 = false;
						boolean contains1 = false;
						for(PatternOccurrence o: pNew){
							if(o.getOccurrenceID() == cellScores[i0][i1].occurrence0.getOccurrenceID()) contains0 = true;
							if(o.getOccurrenceID() == cellScores[i0][i1].occurrence1.getOccurrenceID()) contains1 = true;
						}
						if(!contains0) pNew.addOccurrence(cellScores[i0][i1].occurrence0);
						if(!contains1) pNew.addOccurrence(cellScores[i0][i1].occurrence1);
					}
					i0 = cellScores[i0][i1].prevCell0;
					i1 = cellScores[i0][i1].prevCell1;
				}
			}
		}
		return result;
	}
	
	public PatternSet individualSelection(PatternSet prevSet, double bias){
		Map<PatternOccurrence, Double> scores = calculateScores(prevSet, bias);
		Map<Note, List<IndividualBridge>> bridges = new HashMap<Note, List<IndividualBridge>>();
		PatternSet refSet = ps;
		if(noOtherOccs){
			refSet = prevSet;
		}
		for(Song s0: tf){
			for(Note n0: s0){
				bridges.put(n0, new ArrayList<IndividualBridge>());
			}
		}
		for(Pattern p: refSet){
			for(PatternOccurrence o: p.getOccurrences()){
				Note useNote = o.getEndNote();
				if(useNote != o.getSong().getEndNote()) useNote = o.getSong().getNotes().get(useNote.getNoteID()+1);
				bridges.get(useNote).add(new IndividualBridge(scores.get(o), o, p.getPatternID()));
			}
		}
		
		PatternSet result = new PatternSet();
		for(Song s: tf){
			List<Note> notes = s.getNotes();
			Collections.sort(notes);
			int length = notes.size();
			IndividualCellData[] cellScores = new IndividualCellData[length];
			cellScores[0] = new IndividualCellData(0, null, -1, -1);
			for(int i = 1; i < length; i++){
				cellScores[i] = new IndividualCellData(cellScores[i-1].prevCell - skipCost, null, -1, i-1);
				for(IndividualBridge b: bridges.get(notes.get(i))){
					if(cellScores[i].score < cellScores[b.occurrence.getStartNote().getNoteID()].score + b.score){
						cellScores[i] = new IndividualCellData(cellScores[b.occurrence.getStartNote().getNoteID()].score + b.score, b.occurrence, b.patternID, b.occurrence.getStartNote().getNoteID());
					}
				}
			}
			int i = length-1;
			while(i > 0){
				if(cellScores[i].occurrence != null){
					Pattern pNew = new Pattern(cellScores[i].patternID);
					Pattern pOld = result.getPattern(pNew);
					if(pOld != null){
						pNew = pOld;
					} else result.addPattern(pNew);
					pNew.addOccurrence(cellScores[i].occurrence);
				}
				i = cellScores[i].prevCell;
			}
		}
		return result;
	}
	
	public Map<PatternOccurrence, Double> calculateScores(PatternSet current, double bias){
		Map<PatternOccurrence, Double> result = new HashMap<PatternOccurrence, Double>();
		List<Pattern> psPats = ps.getPatterns();
		Collections.sort(psPats, new PatternComparator());
		List<Pattern> curPats = current.getPatterns();
		Collections.sort(curPats, new PatternComparator());
		int j = curPats.size()-1;
		for(int i = psPats.size()-1; i >= 0; i--){
			Pattern p = psPats.get(i);
			Pattern pEquiv = null;
			while(j >= 0){
				Pattern pCur = curPats.get(j);
				if(pCur.getPatternID() == p.getPatternID()){
					pEquiv = pCur;
					break;
				} else{
					if(pCur.getPatternID() > p.getPatternID())
						break;
				}
				j--;
			}
			if(pEquiv == null){
				for(PatternOccurrence o: p.getOccurrences()){
					result.put(o, -10000000.0*skipCost);
				}
			}
			else{
				double curPatSpread = pEquiv.countSongs();
				double fullPatSpread = p.countSongs();
				for(PatternOccurrence o: p.getOccurrences()){
					double fullOccSize = o.getEndNote().getNoteID() - o.getStartNote().getNoteID() +1;
					double occSize = o.getNotes().size();
					result.put(o, (1+bias*curPatSpread)*(Math.log(occSize)*occSize*occSize/fullOccSize)*fullPatSpread);
				}
			}
		}
		return result;
	}
	
	class IndividualCellData{
		double score;
		PatternOccurrence occurrence;
		int patternID;
		int prevCell;
		
		IndividualCellData(double score, PatternOccurrence occurrence, int patternID, int prevCell){
			this.score = score;
			this.occurrence = occurrence;
			this.patternID = patternID;
			this.prevCell = prevCell;
		}
	}
	
	class IndividualBridge{
		PatternOccurrence occurrence;
		int patternID;
		double score;
		
		IndividualBridge(double score, PatternOccurrence occurrence, int patternID){
			this.score = score;
			this.occurrence = occurrence;
			this.patternID = patternID;
		}
	}
	
	class PairCellData{
		double score;
		PatternOccurrence occurrence0;
		PatternOccurrence occurrence1;
		int patternID;
		int prevCell0;
		int prevCell1;
		
		PairCellData(double score, PatternOccurrence occurrence0, PatternOccurrence occurrence1, int patternID, int prevCell0, int prevCell1){
			this.score = score;
			this.occurrence0 = occurrence0;
			this.occurrence1 = occurrence1;
			this.patternID = patternID;
			this.prevCell0 = prevCell0;
			this.prevCell1 = prevCell1;
		}
	}
	
	class PairBridge{
		PatternOccurrence occurrence0;
		PatternOccurrence occurrence1;
		int patternID;
		double score;
		
		PairBridge(double score, PatternOccurrence occurrence0, PatternOccurrence occurrence1, int patternID){
			this.score = score;
			this.occurrence0 = occurrence0;
			this.occurrence1 = occurrence1;
			this.patternID = patternID;
		}
	}
	
	
	class PatternComparator implements Comparator<Pattern>{

		@Override
		public int compare(Pattern o1, Pattern o2) {
			return o2.getPatternID() - o1.getPatternID();
		}
		
	}
}
