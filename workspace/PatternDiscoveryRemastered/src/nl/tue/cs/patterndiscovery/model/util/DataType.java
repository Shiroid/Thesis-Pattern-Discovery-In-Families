package nl.tue.cs.patterndiscovery.model.util;

public enum DataType {
	NONE, NOTE, PATTERNOCCURRENCE, PATTERN, PATTERNSET, SONG, TUNEFAMILY, DATASET;
	
	public boolean greaterThanEqual(DataType other){
		return this.getRank() >= other.getRank();
	}
	
	public int getRank(){
		switch(this){
		case NONE: return 0;
		case NOTE: return 1;
		case PATTERNOCCURRENCE: return 2;
		case PATTERN: return 3;
		case PATTERNSET: return 4;
		case SONG: return 5;
		case TUNEFAMILY: return 6;
		case DATASET: return 7;
		default: return -1;
		}
	}
}
