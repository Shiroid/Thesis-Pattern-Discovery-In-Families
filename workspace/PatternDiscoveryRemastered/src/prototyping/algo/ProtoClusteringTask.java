package prototyping.algo;

import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.TuneFamily;

public class ProtoClusteringTask {


	protected TuneFamily tf;
	protected PatternSet ps;
	protected double gs;
	protected double mr;
	
	protected int maxRepsPerSong = 3;
	protected double subCoverageLeeway = 1;

	protected Map<Note, Double> advanceLengths;
	protected Map<Note, List<PatternOccurrence>> occurrenceHits;
	
	public ProtoClusteringTask(TuneFamily tf, PatternSet ps, double gs, double mr){
		this.tf = tf;
		this.gs = gs;
		this.mr = mr;
		this.ps = ps;
	}
	
	public ProtoClusteringTask(TuneFamily tf, PatternSet ps, double gs, double mr, int maxRepsPerSong, double subCoverageLeeway){
		this(tf, ps, gs, mr);
		this.maxRepsPerSong = maxRepsPerSong;
		this.subCoverageLeeway = subCoverageLeeway;
	}
	
	public PatternSet run(){
		PatternSet result = new PatternSet();
		Map<PatternOccurrence, Pattern> occBelong = new HashMap<PatternOccurrence, Pattern>();
		occurrenceHits = new HashMap<Note, List<PatternOccurrence>>();
		for(Song s: tf){
			for(Note n: s){
				occurrenceHits.put(n, new ArrayList<PatternOccurrence>());
			}
		}
		for(Pattern p: ps){
			for(PatternOccurrence o: p){
				Collections.sort(o.getNotes());
				occBelong.put(o, p);
				/*List<Note> hitNotes = o.getSong().getNotes();
				for(int i = o.getStartNote().getNoteID(); i <= o.getEndNote().getNoteID(); i++){
					occurrenceHits.get(hitNotes.get(i)).add(o);
				}*/
				for(Note n: o.getNotes()){
					occurrenceHits.get(n).add(o);
				}
			}
		}
		
		advanceLengths = new HashMap<Note, Double>();
		double numReqSongs = tf.getSongs().size()*(mr+1)/2;
		for(Song s: tf){
			for(Note n: s){
				Collections.sort(occurrenceHits.get(n), new OccurrenceEndComparator());
				HashSet<Song> reppedSongs = new HashSet<Song>();
				double latestSharedEnd = 0;
				boolean isSufficient = false;
				for(PatternOccurrence o: occurrenceHits.get(n)){
					for(PatternOccurrence oPrime: occBelong.get(o)){
						reppedSongs.add(oPrime.getSong());
					}
					latestSharedEnd = o.getEndTime();
					isSufficient = true;
					if(reppedSongs.size() >= numReqSongs) break;
					isSufficient = false;
				}
				if(!isSufficient) latestSharedEnd = n.getOnset();
				advanceLengths.put(n, latestSharedEnd - n.getOnset());
				
			}
		}
		
		List<Note> allNotes = new ArrayList<Note>();
		for(Song s: tf)
			allNotes.addAll(s.getNotes());
		Collections.sort(allNotes, new NoteAdvanceComparator());
		
		int patID = 0;
		for(Note n: allNotes){
			if(advanceLengths.get(n) == 0) break;
			int occID = 0;
			Pattern newPat = new Pattern(patID++);

			HashSet<Song> reppedSongs = new HashSet<Song>();
			reppedSongs.add(n.getSong());
			HashMap<Song, Integer> reppedSongsCount = new HashMap<Song, Integer>();
			double lastAdvance = 0;

			boolean isSufficient = false;
			boolean firstDone = false;
			for(PatternOccurrence o: occurrenceHits.get(n)){
				
				List<Note> hitNotes = o.getSong().getNotes();
				for(int i = o.getStartNote().getNoteID(); i <= o.getEndNote().getNoteID(); i++){
					if(hitNotes.get(i) != n)
						occurrenceHits.get(hitNotes.get(i)).remove(o);
				}
				
				PatternOccurrence other = o;
				for(PatternOccurrence oPrime: occBelong.get(o)){
					if(oPrime != o) other = oPrime;
				}
				List<Note> oNotes = o.getNotes();
				List<Note> otherOccNotes = other.getNotes();
				List<Note> otherSongNotes = other.getSong().getNotes();
				int occNoteIndex = 0;
				int lastUsableIndex = Integer.MAX_VALUE;

				PatternOccurrence newOcc = new PatternOccurrence(occID++);
				PatternOccurrence newOccOther = new PatternOccurrence(occID++);
				for(int i = o.getStartNote().getNoteID(); i <= o.getEndNote().getNoteID(); i++){
					Note curNote = hitNotes.get(i);
					if(curNote != n)
						occurrenceHits.get(curNote).remove(o);
					if(i >= n.getNoteID() && curNote.getOnset() <= n.getOnset() + advanceLengths.get(n)){
						newOcc.addNote(curNote);
						if(oNotes.get(occNoteIndex) == curNote){
							int usableIndex = otherOccNotes.get(occNoteIndex).getNoteID();
							for(int j = usableIndex; j > lastUsableIndex; j--)
								newOccOther.addNote(otherSongNotes.get(j));
							lastUsableIndex = usableIndex;
							occNoteIndex++;
						}
					}
				}
				
				
				isSufficient = true;
				if(!reppedSongsCount.keySet().contains(other.getSong()))
					reppedSongsCount.put(other.getSong(), 1);
				else reppedSongsCount.put(other.getSong(), 1 + reppedSongsCount.get(other.getSong()));
				reppedSongs.add(other.getSong());
				
				if(!firstDone)
					newPat.addOccurrence(newOcc);
				firstDone = true;
				if(reppedSongsCount.get(other.getSong()) <= maxRepsPerSong && newOccOther.getNotes().size() > 0)
						newPat.addOccurrence(newOccOther);

				if(reppedSongs.size() >= numReqSongs && lastAdvance == 0) lastAdvance = o.getEndTime() - n.getOnset();
				if(reppedSongs.size() >= numReqSongs && 
						n.getOnset() + lastAdvance*subCoverageLeeway <= o.getEndTime()) break;
				if(reppedSongs.size() < numReqSongs)
					isSufficient = false;
			}
			if(isSufficient)
				result.addPattern(newPat);
		}
		
		return result;
	}
	
	class OccurrenceEndComparator implements Comparator<PatternOccurrence>{
		@Override
		public int compare(PatternOccurrence arg0, PatternOccurrence arg1) {
			return (int) Math.signum( arg1.getEndTime() - arg0.getEndTime() );
		}		
	}
	
	class NoteAdvanceComparator implements Comparator<Note>{
		
		@Override
		public int compare(Note arg0, Note arg1) {
			return (int) Math.signum( advanceLengths.get(arg1) - advanceLengths.get(arg0) );
		}		
	}
	
	class NoteAdvanceCoverageComparator implements Comparator<Note>{
		
		@Override
		public int compare(Note arg0, Note arg1) {
			return (int) Math.signum( getScore(arg1) - getScore(arg0) );
		}		
		
		private double getScore(Note n){
			double result = 0;
			int numOccs = 1;
			for(PatternOccurrence o: occurrenceHits.get(n)){
				if(o.getEndTime() < n.getOnset() + advanceLengths.get(n)) break;
				result = (result*numOccs-1 + (o.getNotes().size()/(o.getEndNote().getNoteID()-o.getStartNote().getNoteID()+1)))/numOccs;
				numOccs++;
			}
			return result*advanceLengths.get(n);
		}
	}

}
