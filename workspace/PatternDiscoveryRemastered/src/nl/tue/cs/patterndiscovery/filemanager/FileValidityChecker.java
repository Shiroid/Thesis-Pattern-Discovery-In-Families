package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;

import nl.tue.cs.patterndiscovery.config.Config;

public class FileValidityChecker {

	/*
	 * Returns if the file is valid for reading or not
	 */
	public static boolean isValid(File f){
		String name = f.getName();
		int pos = name.lastIndexOf('.');
		String extension = "";
		if(pos >= 0){
			extension = name.substring(name.lastIndexOf('.'));
		}
		return Config.isValidExtension(extension);
	}
}
