package nl.tue.cs.patterndiscovery.model;

import java.util.*;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

/*
 * Pattern found in a single song, can be discovered for multiple songs, in which case the ID matches.
 */
public class Pattern implements Iterable<PatternOccurrence>{
		
	//The ID of this pattern, is consistent across songs for class pattern discovery
	protected int patternID = -1;
	
	//The occurrences of this pattern
	protected List<PatternOccurrence> occurrences;
	
	//Construct empty list of occurrences
	public Pattern(){
		this.occurrences = new ArrayList<PatternOccurrence>();
	}
	
	//Construct with ID and empty list of occurrences
	public Pattern(int patternID){
		setPatternID(patternID);
		this.occurrences = new ArrayList<PatternOccurrence>();
	}

	//Construct with ID and from existing collection of occurrences
	public Pattern(int patternID, Collection<PatternOccurrence> occurrences){
		setPatternID(patternID);
		this.occurrences = new ArrayList<PatternOccurrence>(occurrences);
	}
	
	public int getPatternID(){
		return patternID;
	}
	
	//Sets pattern ID, only once
	public void setPatternID(int ID){
		if(patternID < 0)
			patternID = ID;
	}
	
	public void addOccurrence(PatternOccurrence o){
		occurrences.add(o);
	}
	
	public List<PatternOccurrence> getOccurrences(){
		return occurrences;
	}

	@Override
	public Iterator<PatternOccurrence> iterator() {
		return occurrences.iterator();
	}
	
	public PatternOccurrence getOccurrence(PatternOccurrence o){
		return CollectionElementGetter.getFromCollection(occurrences, o);
	}
	
	@Override
	public boolean equals(Object o){
		if(patternID == -1) return false;
		if(o instanceof Pattern){
			return patternID == ((Pattern) o).getPatternID();
		}
		return false;
	}
	
	@Override
	public String toString(){
		return Integer.toString(this.patternID);
	}
}
