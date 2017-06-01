package nl.tue.cs.patterndiscovery.model.util;

public class NonLetterStripper extends FamilyExtractor{


	
	/*
	 * Strips the given name of all non-letters at the end, representing the tune family name.
	 */
	@Override
	public String familyNameFromName(String name){
		int last = name.length() - 1;
		while(!Character.isLetter(name.charAt(last))){
			last--;
		}
		return name.substring(0, last+1);
	}

}
