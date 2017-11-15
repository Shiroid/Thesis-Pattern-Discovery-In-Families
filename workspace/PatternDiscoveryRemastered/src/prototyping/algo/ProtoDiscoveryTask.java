package prototyping.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.TuneFamily;

public class ProtoDiscoveryTask {

	protected TuneFamily tf;
	protected double gs;
	protected double rr;
	protected int patsPerEnd = 3;
	protected int maxEmbellish = 1;
	
	public ProtoDiscoveryTask(TuneFamily tf, double gs, double rr){
		this.tf = tf;
		this.gs = gs;
		this.rr = rr;
	}
	
	public ProtoDiscoveryTask(TuneFamily tf, double gs, double rr, int patsPerEnd){
		this(tf, gs, rr);
		this.setPatsPerEnd(patsPerEnd);
	}
	
	public void setPatsPerEnd(int patsPerEnd){
		this.patsPerEnd = patsPerEnd;
	}
	
	public PatternSet run(){
		PatternSet result = new PatternSet();
		
		//Determine the short vectors
		List<Song> songs = tf.getSongs();
		Map<Song, Map<Integer, ArrayList<ProtoNotePair>>> shortVectors = new HashMap<Song, Map<Integer, ArrayList<ProtoNotePair>>>();
		for(Song s: songs){
			Map<Integer, ArrayList<ProtoNotePair>> songVectors = new HashMap<Integer, ArrayList<ProtoNotePair>>();
			shortVectors.put(s, songVectors);
			List<Note> notes = s.getNotes();
			for(int i = notes.size()-1; i > 0; i--){
				int j = i-1;
				Note iNote = notes.get(i);
				Note jNote = notes.get(j);
				while(iNote.getOnset() - jNote.getOnset() <= gs && j >= 0 && i - j <= maxEmbellish){
					jNote = notes.get(j);
					int pitchDiff = iNote.getDiatonicPitch() - jNote.getDiatonicPitch();
					ArrayList<ProtoNotePair> diffSet;
					if(!songVectors.containsKey(pitchDiff)){
						diffSet = new ArrayList<ProtoNotePair>();
					} else{
						diffSet = songVectors.get(pitchDiff);
					}
					songVectors.put(pitchDiff, diffSet);
					diffSet.add(new ProtoNotePair(jNote, iNote));
					j--;
				}
			}
		}
		
		int patternID = 0;
		
		for(int i = songs.size() - 1; i >= 0; i--){
			for(int j = i; j >= 0; j--){
				Song iSong = songs.get(i);
				Song jSong = songs.get(j);
				Map<Integer, ArrayList<ProtoNotePair>> iShortVectors = shortVectors.get(iSong);
				Map<Integer, ArrayList<ProtoNotePair>> jShortVectors = shortVectors.get(jSong);
				
				//Get tuple matches
				Map<Integer, ArrayList<ProtoNotePair>> tupleMatches = new HashMap<Integer, ArrayList<ProtoNotePair>>();
				for(int k: iShortVectors.keySet()){
					if(jShortVectors.containsKey(k)){
						ArrayList<ProtoNotePair> kiShortVectors = iShortVectors.get(k);
						ArrayList<ProtoNotePair> kjShortVectors = jShortVectors.get(k);
						Collections.sort(kiShortVectors);
						Collections.sort(kjShortVectors);
						int bottom = kjShortVectors.size() - 1, top = kjShortVectors.size() - 1;
						for(int n = kiShortVectors.size() - 1; n >= 0; n--){
							ProtoNotePair cur = kiShortVectors.get(n);
							while(top >= 0){
								if(kjShortVectors.get(top).getOnsetDiff() > cur.getOnsetDiff()/rr)
									top--;
								else break;
							}
							while(bottom >= 0){
								if(kjShortVectors.get(bottom).getOnsetDiff() >= cur.getOnsetDiff()*rr)
									bottom--;
								else break;
							}
							for(int a = top; a > bottom && a >= 0; a--){
								ProtoNotePair other = kjShortVectors.get(a);
								int pitchDiff = other.getNote1().getDiatonicPitch() - cur.getNote1().getDiatonicPitch();
								ArrayList<ProtoNotePair> pairs;
								if(!tupleMatches.containsKey(pitchDiff)){
									pairs = new ArrayList<ProtoNotePair>();
									tupleMatches.put(pitchDiff, pairs);
								} else {
									pairs = tupleMatches.get(pitchDiff);
								}
								pairs.add(new ProtoNotePair(cur.getNote1(), other.getNote1()));
								pairs.add(new ProtoNotePair(cur.getNote2(), other.getNote2()));
							}
						}
					}
				}

				//Determine the patterns
				for(ArrayList<ProtoNotePair> list: tupleMatches.values()){
					Collections.sort(list, new PairSorter());
					
					Map<ProtoNotePair, ArrayList<Pattern>> status = new HashMap<ProtoNotePair, ArrayList<Pattern>>();
					for(ProtoNotePair p1: list){
						ArrayList<Pattern> extendables = new ArrayList<Pattern>();
						ArrayList<ProtoNotePair> removals = new ArrayList<ProtoNotePair>();
						for(ProtoNotePair p2: status.keySet()){
							double onsetDiff1 = p1.getNote1().getOnset() - p2.getNote1().getOnset();
							double onsetDiff2 = p1.getNote2().getOnset() - p2.getNote2().getOnset();
							if(onsetDiff1 > gs){
								removals.add(p2);
								for(Pattern newPat: status.get(p2))
									result.addPattern(newPat);
							} else{
								if(onsetDiff2 <= gs && onsetDiff2 > 0 && onsetDiff1 >= onsetDiff2*rr && onsetDiff1 <= onsetDiff2/rr){
									extendables.addAll(status.get(p2));
									removals.add(p2);
								}
							}
						}
						for(ProtoNotePair p2: removals){
							status.remove(p2);
						}
						if(extendables.isEmpty()){
							Pattern newPat = new Pattern(patternID++);
							newPat.addOccurrence(new PatternOccurrence(0));
							newPat.addOccurrence(new PatternOccurrence(1));
							extendables.add(newPat);
						}
						for(Pattern pat: extendables){
							pat.getOccurrences().get(0).addNote(p1.getNote1());
							pat.getOccurrences().get(1).addNote(p1.getNote2());
						}
						Collections.sort(extendables, new PatComparator());
						for(int remi = extendables.size()-1; remi >= patsPerEnd; remi--)
							extendables.remove(remi);
						status.put(p1, extendables);
					}
					for(List<Pattern> patList: status.values()){
						for(Pattern pat: patList){
							result.addPattern(pat);
						}
					}
				}
			}
		}

		return removeShortPatterns(result, 3);
	}
	
	public PatternSet removeShortPatterns(PatternSet ps, int minElements){
		PatternSet result = new PatternSet();
		for(Pattern p: ps.getPatterns()){
			if(p.getOccurrences().get(0).getNotes().size() >= minElements)
				result.addPattern(p);
		}
		return result;
	}
	
	
	class PairSorter implements Comparator<ProtoNotePair>{

		@Override
		public int compare(ProtoNotePair arg0, ProtoNotePair arg1) {
			if(arg0.getNote1().getOnset() == arg1.getNote1().getOnset()){
				return (int) Math.signum(arg0.getNote2().getOnset() - arg1.getNote2().getOnset());
			}
			return (int) Math.signum(arg0.getNote1().getOnset() - arg1.getNote1().getOnset());
		}
	}
	
	
	class PatComparator implements Comparator<Pattern>{

		@Override
		public int compare(Pattern arg0, Pattern arg1) {
			PatternOccurrence o1 = arg0.getOccurrences().get(0);
			PatternOccurrence o0 = arg1.getOccurrences().get(0);
			int sizeDiff = o0.getNotes().size() - o1.getNotes().size();
			double lengthDiff = (o0.getEndTime() - o0.getStartTime()) - (o1.getEndTime() - o1.getStartTime());
			if(sizeDiff == 0){
				return (int) Math.signum(lengthDiff);
			}
			return sizeDiff;
		}
	}
}
