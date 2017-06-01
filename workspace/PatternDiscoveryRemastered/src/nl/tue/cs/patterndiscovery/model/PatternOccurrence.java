package nl.tue.cs.patterndiscovery.model;

import java.util.*;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

//Single occurrence of some pattern
public class PatternOccurrence extends NoteSequence{
	
	protected boolean isSorted = false;
	
	//List of the notes in this occurrence
	protected List<Note> notes;
	
	//ID number associated with this occurrence, used for matching to other pattern sets
	protected int occurrenceID = -1;

	//Constructs empty pattern occurrence
	public PatternOccurrence(){
		this.notes = new ArrayList<Note>();
	}

	//Constructs empty pattern occurrence with ID
	public PatternOccurrence(int id){
		this.notes = new ArrayList<Note>();
		this.occurrenceID = id;
	}

	//Constructs pattern occurrence with ID from existing collection of notes
	public PatternOccurrence(int id, Collection<Note> notes){
		this.notes = new ArrayList<Note>();
		this.occurrenceID = id;
		for(Note n: notes){
			this.addNote(n);
		}
	}
	
	public int getOccurrenceID(){
		return occurrenceID;
	}
	
	//Sets the occurrence ID, one time only
	public void setOccurrenceID(int id){
		if(occurrenceID < 0) 
			occurrenceID = id;
	}
	
	//Adds a note to this pattern occurrence
	public void addNote(Note n){
		notes.add(n);
		this.maxPitch = Math.max(n.getChromaticPitch(), this.maxPitch);
		this.minPitch = Math.min(n.getChromaticPitch(), this.minPitch);
		this.endTime = Math.max(n.getOnset()+n.getDuration(), this.endTime);
		this.startTime = Math.min(n.getOnset(), this.startTime);
		this.isSorted = false;
	}
	
	@Override
	public List<Note> getNotes(){
		return notes;
	}
	
	public Note getNote(Note n){
		return CollectionElementGetter.getFromCollection(notes, n);
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}

	@Override
	public List<? extends Note> sortedList() {
		Collections.sort(notes);
		this.isSorted = true;
		return notes;
	}
	
	@Override
	public boolean equals(Object o){
		if(occurrenceID == -1) return false;
		if(o instanceof PatternOccurrence){
			return occurrenceID == ((PatternOccurrence) o).getOccurrenceID();
		}
		return false;
	}

}
