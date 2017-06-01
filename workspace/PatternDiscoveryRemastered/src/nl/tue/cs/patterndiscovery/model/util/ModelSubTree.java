package nl.tue.cs.patterndiscovery.model.util;

import nl.tue.cs.patterndiscovery.model.CollinsLispNote;
import nl.tue.cs.patterndiscovery.model.DataSet;
import nl.tue.cs.patterndiscovery.model.ModelWrapper;
import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.TuneFamily;

public class ModelSubTree {

	public Note note;
	public PatternOccurrence patternOccurrence;
	public Pattern pattern;
	public PatternSet patternSet;
	public Song song;
	public TuneFamily tuneFamily;
	public DataSet dataSet;
	
	protected ModelSubTree(DataSet dataSet, TuneFamily tuneFamily, Song song, PatternSet patternSet, 
			 Pattern pattern, PatternOccurrence patternOccurrence, Note note){
		this.note = note;
		this.patternOccurrence = patternOccurrence;
		this.pattern = pattern;
		this.patternSet = patternSet;
		this.song = song;
		this.tuneFamily = tuneFamily;
		this.dataSet = dataSet;
	}
	
	public boolean hasNote(){
		return note != null;
	}
	
	public DataType getTop(){
		if(dataSet != null) return DataType.DATASET;
		if(tuneFamily != null) return DataType.TUNEFAMILY;
		if(song != null) return DataType.SONG;
		if(patternSet != null) return DataType.PATTERNSET;
		if(pattern != null) return DataType.PATTERN;
		if(patternOccurrence != null) return DataType.PATTERNOCCURRENCE;
		if(note != null) return DataType.NOTE;
		return DataType.NONE;
	}
	
	public DataType getBottom(){
		if(patternOccurrence != null) return DataType.PATTERNOCCURRENCE;
		if(pattern != null) return DataType.PATTERN;
		if(patternSet != null) return DataType.PATTERNSET;
		if(song != null) return DataType.SONG;
		if(tuneFamily != null) return DataType.TUNEFAMILY;
		if(dataSet != null) return DataType.DATASET;
		return DataType.NONE;
	}
	
	public boolean isSubTree(){
		if(dataSet != null && tuneFamily != null) 
			if(!dataSet.getFamilies().contains(tuneFamily)) return false;
		if(song != null && tuneFamily != null) 
			if(!tuneFamily.getSongs().contains(song)) return false;
		if(song != null && patternSet != null) 
			if(!song.getPatternSets().contains(patternSet)) return false;
		if(pattern != null && patternSet != null) 
			if(!patternSet.getPatterns().contains(pattern)) return false;
		if(pattern != null && patternOccurrence != null) 
			if(!pattern.getOccurrences().contains(patternOccurrence)) return false;
		if(note != null && patternOccurrence != null) 
			if(!patternOccurrence.getNotes().contains(note)) return false;
		if(note != null && song != null) 
			if(!song.getNotes().contains(note)) return false;
		return true;
	}
	
	/*
	 * Forces the subtree to be true by adding all required edges, preferably left unused!
	 */
	public void forceTruth(){
		if(dataSet != null && tuneFamily != null) 
			if(!dataSet.getFamilies().contains(tuneFamily)) dataSet.addFamily(tuneFamily);
		if(song != null && tuneFamily != null) 
			if(!tuneFamily.getSongs().contains(song)) tuneFamily.addSong(song);;
		if(song != null && patternSet != null) 
			if(!song.getPatternSets().contains(patternSet)) song.addPatternSet(patternSet);;
		if(pattern != null && patternSet != null) 
			if(!patternSet.getPatterns().contains(pattern)) patternSet.addPattern(pattern);;
		if(pattern != null && patternOccurrence != null) 
			if(!pattern.getOccurrences().contains(patternOccurrence)) pattern.addOccurrence(patternOccurrence);;
		if(note != null && patternOccurrence != null) 
			if(!patternOccurrence.getNotes().contains(note)) patternOccurrence.addNote(note);;
		if(note != null && song != null) 
			if(!song.getNotes().contains(note)){
				if(note instanceof CollinsLispNote) song.addNote((CollinsLispNote) note);
				else song.addNote(new CollinsLispNote(note));
			}
	}

	
	/*
	 * Makes sure this model sub tree is up to date with the data model and vice versa.
	 */
	public void syncData(){
		if(dataSet != null){
			if(!ModelWrapper.isInitialized()) ModelWrapper.initWrapper();
			DataSet existingDS = ModelWrapper.getDataSet(dataSet);
			if(existingDS == null) ModelWrapper.addDataSet(dataSet);
			else dataSet = existingDS;
		}
		
		if(dataSet != null && tuneFamily != null) {
			TuneFamily existingTF = dataSet.getFamily(tuneFamily);
			if(existingTF == null) dataSet.addFamily(tuneFamily);
			else tuneFamily = existingTF;
		}
		
		if(song != null && tuneFamily != null) {
			Song existingSong = tuneFamily.getSong(song);
			if(existingSong == null) tuneFamily.addSong(song);
			else song = existingSong;
		}
			
		if(song != null && patternSet != null) {
			PatternSet existingPS = song.getPatternSet(patternSet);
			if(existingPS == null) song.addPatternSet(patternSet);
			else patternSet = existingPS;
		}
			
		if(pattern != null && patternSet != null) {
			Pattern existingPat = patternSet.getPattern(pattern);
			if(existingPat == null) patternSet.addPattern(pattern);
			else pattern = existingPat;
		}
		
		if(pattern != null && patternOccurrence != null) {
			PatternOccurrence existingOcc = pattern.getOccurrence(patternOccurrence);
			if(existingOcc == null) pattern.addOccurrence(patternOccurrence);
			else patternOccurrence = existingOcc;
		}
		
		if(note != null){
			
			//Try to get note from song
			boolean noteFound = false;
			Note existingNote;
			if(song != null) {
				existingNote = song.getNote(note);
				
				if(existingNote != null) {
					note = existingNote;
					noteFound = true;
				}
			}

			if(patternOccurrence != null) {
				existingNote = patternOccurrence.getNote(note);
				
				if(existingNote == null) patternOccurrence.addNote(note);
				else if(!noteFound) {
					//If no note gotten from song, get note from occurrence
					note = existingNote;
					noteFound = true;
				}
			}

			//If a note was found, use it for the song
			if(song != null && noteFound) {
				existingNote = song.getNote(note);
				if(existingNote == null) {
					if(note instanceof CollinsLispNote) song.addNote((CollinsLispNote) note);
					else song.addNote(new CollinsLispNote(note));
				}
			}
			
		}
	}

	/*
	 * Return whether or not this model sub tree contains all the aspects of another (possibly more). 
	 */
	public boolean contains(ModelSubTree other){
		boolean result = true;
		if(other.dataSet != null) result &= other.dataSet.equals(this.dataSet);
		if(other.tuneFamily != null) result &= other.tuneFamily.equals(this.tuneFamily);
		if(other.song != null) result &= other.song.equals(this.song);
		if(other.patternSet != null) result &= other.patternSet.equals(this.patternSet);
		if(other.pattern != null) result &= other.pattern.equals(this.pattern);
		if(other.patternOccurrence != null) result &= other.patternOccurrence.equals(this.patternOccurrence);
		if(other.note != null) result &= other.note.equals(this.note);
		return result;
	}
}
