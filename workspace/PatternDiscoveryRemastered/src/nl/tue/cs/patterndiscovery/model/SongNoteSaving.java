package nl.tue.cs.patterndiscovery.model;

import java.util.*;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

/*
 * Implementation of the Song class.
 * Uses symmetric hash map to find existing CollinsLispNotes.
 * Allows notes to refer to the same instance, rather than saving duplicates.
 * Similarly, pattern sets are mapped to easily relate to sets in other songs.
 */
public class SongNoteSaving extends Song {

	// All notes in this song, mapped with duplicates to retrieve CollinsLispNotes from Notes.
	private ArrayList<CollinsLispNote> notes;
	private boolean notesSorted = false;

	// All pattern sets in this song, mapped with keys to retrieve match sets.
	private HashMap<String, PatternSet> patternSets;

	//Construct with empty note set.
	public SongNoteSaving(){
		this.patternSets = new HashMap<String, PatternSet>();
		this.notes = new ArrayList<CollinsLispNote>();
	}
	
	//Constructor from given note set.
	public SongNoteSaving(Collection<CollinsLispNote> noteSet){
		this.patternSets = new HashMap<String, PatternSet>();
		this.notes = new ArrayList<CollinsLispNote>();
		for(CollinsLispNote n: noteSet){
			addNote(n);
		}
	}
	
	//Constructor from given note set and pattern set.
	public SongNoteSaving(Collection<CollinsLispNote> noteSet, Collection<PatternSet> patternSets){
		this.patternSets = new HashMap<String, PatternSet>();
		this.notes = new ArrayList<CollinsLispNote>(noteSet);
		for(CollinsLispNote n: noteSet){
			addNote(n);
		}
		for(PatternSet s: patternSets){
			addPatternSet(s);
		}
	}

	@Override
	public void addNote(CollinsLispNote n){
		if(!notes.contains(n)) notes.add(n);
		this.maxPitch = Math.max(n.getChromaticPitch(), this.maxPitch);
		this.minPitch = Math.min(n.getChromaticPitch(), this.minPitch);
		this.endTime = Math.max(n.getOnset()+n.getDuration(), this.endTime);
		this.startTime = Math.min(n.getOnset(), this.startTime);
		this.notesSorted = false;
	}

	@Override
	public CollinsLispNote getNote(Note n){
		return (CollinsLispNote) CollectionElementGetter.getFromCollection(notes, n);
	}
	
	@Override
	public Iterator<Note> iterator() {
		return new ArrayList<Note>(notes).iterator();
	}

	@Override
	public List<? extends Note> sortedList() {
		if(!notesSorted) Collections.sort(notes);
		notesSorted = true;
		return notes;
	}

	@Override
	public void addPatternSet(PatternSet s){
		patternSets.put(s.getIntraSongKey(), s);
	}

	@Override
	public PatternSet getPatternSet(PatternSet s){
		return patternSets.get(s.getIntraSongKey());
	}

	@Override
	public PatternSet getPatternSet(String patSetKey){
		return patternSets.get(patSetKey);
	}

	@Override
	public List<PatternSet> getPatternSets(){
		return new ArrayList<PatternSet>(patternSets.values());
	}

	@Override
	public List<Note> getNotes(){
		return new ArrayList<Note>(notes);
	}
	
	@Override
	public String toString(){
		return this.songName;
	}
	
}
