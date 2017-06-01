package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LinesGetter {

	/*
	 * Returns the lines of file f in the form of an array of strings, each element represents one line.
	 */
	public static String[] getLinesFromFile(File f){
		String[] lines = new String[0];
		
		try {
			lines = new String(Files.readAllBytes(Paths.get(f.getCanonicalPath()))).split("[\n\r]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
}
