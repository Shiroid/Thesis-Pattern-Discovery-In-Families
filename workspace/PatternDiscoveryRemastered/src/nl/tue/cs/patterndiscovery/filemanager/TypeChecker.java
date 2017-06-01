package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//Checks the type of file being supplied, from possible song or pattern formats
public class TypeChecker {
	
	/*
	 * Returns a guess of what kind of file is being supplied, depending on the way the content is formatted.
	 * May not be entirely correct, so use exceptions while reading through.
	 */
	public static FileType checkFileType(File file) throws java.io.IOException {

    	String[] lines;
		try {
			//Put the lines of the file into an array, for easy reading.
			lines = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath()))).split("[\n\r]");
			//If the file starts with a 5 tuple of numbers between parenthesis, it is likely a Collins Lisp format song file.
			if(lines[0].matches("\\((([\\d/\\.])*\\s*){5}\\)")) return FileType.COLLINSLISPSONG;
			//If the file starts with introducing patterns, it is likely a Mirex pattern file.
			if(lines[0].matches("pattern(\\d)+")) return FileType.MIREXPATTERNS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//If none of the file types was found, we do not know what file type it should be.
		return FileType.UNKNOWN;
	}
}
