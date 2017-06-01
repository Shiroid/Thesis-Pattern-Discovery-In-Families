package nl.tue.cs.patterndiscovery.model;

import java.io.File;
import java.util.*;

import nl.tue.cs.patterndiscovery.filemanager.SongReader;
import nl.tue.cs.patterndiscovery.model.util.FamilyExtractorSingleton;

/*
 * Song constructed out of notes in Collins Lisp format.
 */
public abstract class Song extends NoteSequence{
	
	//File path to the song, if any
	protected String filePath = "";
	
	//ID of this specific song, used for matching songs
	protected String songName = "";

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if(this.filePath.isEmpty())
			this.filePath = filePath;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		if(this.songName.isEmpty())
			this.songName = songName;
	}

	/*
	 * Adds a note to this song, used for building the song initially.
	 */
	public abstract void addNote(CollinsLispNote n);
	
	/*
	 * Gets the CollinsLispNote version of n, if present in this song, returns null otherwise.
	 */
	public abstract CollinsLispNote getNote(Note n);

	/*
	 * Adds a note to this song, used for building the song initially.
	 */
	public abstract void addPatternSet(PatternSet s);
	
	/*
	 * Gets the pattern set corresponding s using matching criteria, if present in this song, returns null otherwise.
	 */
	public abstract PatternSet getPatternSet(PatternSet s);
	
	/*
	 * Gets the pattern set corresponding patSetKey using matching criteria, if present in this song, returns null otherwise.
	 */
	public abstract PatternSet getPatternSet(String patSetKey);
	
	/*
	 * Returns an iterable of the pattern sets.
	 */
	public abstract List<PatternSet> getPatternSets();
	
	/*
	 * Returns the string representation in Collins Lisp format
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
	
	@Override
	public boolean equals(Object o){
		if(filePath == null && songName == null) return false;
		if(o instanceof Song){
			if(filePath != null) return filePath.equals(((Song) o).getFilePath());
			if(songName != null) return songName.equals(((Song) o).getSongName());
		}
		return false;
	}
	
	/*
	 * Returns the tune family name of this song, using the currently configured Family Extractor.
	 */
	public String getFamilyName(){
		return FamilyExtractorSingleton.familyNameFromName(this.songName);
	}
	
	public void load(){
		if(!this.filePath.isEmpty() && this.getNotes().isEmpty()){
			SongReader.readLispSong(new File(this.filePath), this);
		}
	}
}
