package nl.tue.cs.patterndiscovery.model.util;

public class FamilyExtractorSingleton {

	public static FamilyExtractor extractor = new NonLetterStripper();

	
	/*
	 * Determines the tune family name of a song given its name, using the extractor assigned to this.
	 */
	public static String familyNameFromName(String name){
		return extractor.familyNameFromName(name);
	}
}
