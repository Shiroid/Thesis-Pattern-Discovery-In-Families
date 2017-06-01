package nl.tue.cs.patterndiscovery.model.util;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;

public class NoteRecurrenceCounter {

	public static int getMaxRecurrence(ModelSubTree mst){
		if(mst.note == null) return 0;
		else{
			if(mst.patternOccurrence != null) return getMaxRecurrence(mst.note, mst.patternOccurrence);
			else if(mst.pattern != null) return getMaxRecurrence(mst.note, mst.pattern);
			else if(mst.patternSet != null) return getMaxRecurrence(mst.note, mst.patternOccurrence);
			else return 0;
		}
	}

	public static int getMaxRecurrence(Note note, PatternSet set){
		int counter = 0;
		for(Pattern p: set){
			counter += getMaxRecurrence(note, p);
		}
		return counter;
	}
	
	public static int getMaxRecurrence(Note note, Pattern pattern){
		int counter = 0;
		for(PatternOccurrence o: pattern){
			counter += getMaxRecurrence(note, o);
		}
		return counter;
	}
	
	public static int getMaxRecurrence(Note note, PatternOccurrence occurrence){
		int counter = 0;
		for(Note n: occurrence){
			if(n.equals(note)) counter++;
		}
		return counter;
	}
}
