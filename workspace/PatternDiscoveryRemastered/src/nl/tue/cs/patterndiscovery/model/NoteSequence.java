package nl.tue.cs.patterndiscovery.model;

import java.util.Iterator;
import java.util.List;

public abstract class NoteSequence implements Iterable<Note>{

	protected int minPitch = Integer.MAX_VALUE, maxPitch = Integer.MIN_VALUE;
	protected double startTime = Double.MAX_VALUE, endTime = Double.MIN_VALUE;

	public int getMinPitch() {
		return minPitch;
	}

	public int getMaxPitch() {
		return maxPitch;
	}

	public double getStartTime() {
		return startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	/*
	 * Returns an unordered iterator.
	 * @see java.lang.Iterable#iterator()
	 */
	public abstract Iterator<Note> iterator();
	
	/*
	 * Returns an ordered list.
	 */
	public abstract List<? extends Note> sortedList();
	
	/*
	 * Returns an ordered list.
	 */
	public abstract List<Note> getNotes();

	/*
	 * Returns this note sequence as a string consisting of its notes.
	 */
	public String toNoteString() {
		String result = "";
		String newLine = System.lineSeparator();
		boolean hasNonCL = false;
		for(Note n: sortedList()){
			if (!(n instanceof CollinsLispNote)) {
				hasNonCL = false;
				break;
			}
		}
		for(Note n: sortedList()){
			result += (hasNonCL?n.asStringTuple():n) + newLine;
		}
		return result;
	}
}
