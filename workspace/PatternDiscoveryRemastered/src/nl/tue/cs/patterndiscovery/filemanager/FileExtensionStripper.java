package nl.tue.cs.patterndiscovery.filemanager;

public class FileExtensionStripper {
	
	/*
	 * Returns the file name without file extension
	 */
	public static String getNameWithoutExt(String name){
		int pos = name.length();
		while(pos >= 0){
			name = name.substring(0, pos);
			pos = name.lastIndexOf(".");
		}
		return name;
	}
}
