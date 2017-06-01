package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import nl.tue.cs.patterndiscovery.model.*;

public class SongReader{

	/*
	 * Returns a Song object generated from the data in file f, assuming it is in Collins Lisp format.
	 */
	public static Song readLispSong(File f){
		Song song = new SongNoteSaving();
		return readLispSong(f, song);
	}

	public static Song readLispSong(File f, Song song){
		String[] lines = LinesGetter.getLinesFromFile(f);
		
		for(int i = 0; i < lines.length; i++){
			song.addNote(NoteReader.readLispNoteLossless(lines[i]));
		}
		
		setSongNameAndPath(song, f);
		return song;
	}
	
	public static void setSongNameAndPath(Song song, File f){
		setSongPath(song, f);
		setSongName(song, f);
	}

	
	public static void setSongPath(Song song, File f){
		try {
			song.setFilePath(f.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setSongName(Song song, File f){
		song.setSongName(songNameFromFile(f));
	}
	
	public static String songNameFromFile(File f){
		return FileExtensionStripper.getNameWithoutExt(f.getName());
	}
}
