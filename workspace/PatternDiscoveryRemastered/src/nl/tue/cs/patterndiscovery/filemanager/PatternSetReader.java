package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import nl.tue.cs.patterndiscovery.model.CollinsLispNote;
import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;

public class PatternSetReader {

	/*
	 * Returns a pattern set read from file f, using simple Note objects.
	 */
	public static PatternSet readPatternSet(File f){
		return readPatternSet(f, null);
	}

	/*
	 * Returns a pattern set read from file f, using references to the notes already found in song s,
	 * assuming the patterns are from song s.
	 * If s is null, defaults to Note objects.
	 * 
	 * This function may be sensitive to s not containing the exact notes in f, test for this!
	 */
	public static PatternSet readPatternSet(File f, Song s){
		PatternSet result = new PatternSet();
		return readPatternSet(f, s, result);
	}

	public static PatternSet readPatternSet(File f, Song s, PatternSet result){
		String[] lines = LinesGetter.getLinesFromFile(f);
		
		Pattern p = new Pattern();
		PatternOccurrence o = new PatternOccurrence();
		
		for(int i = 0; i < lines.length; i++){
			//Check if the line introduces a pattern
			if(lines[i].matches("pattern(\\d)+")){
				int patternID = Integer.parseInt(lines[i].substring(7));
				if(p != null){ //Deal with duplicate introductions of the same pattern (happens with COSIATEC)
					if(p.getPatternID() != patternID){
						p = new Pattern();
						p.setPatternID(patternID);
						result.addPattern(p);
					}
				}
			}
			//Check if the line introduces an occurrence of a pattern
			else if(lines[i].matches("occurrence(\\d)+")){
				o = new PatternOccurrence();
				o.setOccurrenceID(Integer.parseInt(lines[i].substring(10)));
				p.addOccurrence(o);
			}
			//Check if the line introduces a note
			else if(lines[i].matches("(\\d)+\\.(\\d)+(\\s)*,(\\s)*(\\d)+\\.(\\d)+(\\s)*")){
				if(s != null) {
					Note n = NoteReader.readPlainNote(lines[i]);
					Note sn = s.getNote(n);
					if(sn == null) addPossibleMorpheticNote(s, o, n); //Make this faster
					else o.addNote(sn);
				}
				else o.addNote(NoteReader.readPlainNote(lines[i]));
			}
			//Skip any other lines
		}
		
		setInfoFromFile(result, f);		

		if(s != null) s.addPatternSet(result);
		
		return result;
	}
	
	public static void addPossibleMorpheticNote(Song s, PatternOccurrence o, Note n){
		for(Note m: s){
			if(m.getOnset() == n.getOnset()){
				if(m instanceof CollinsLispNote){
					if(((CollinsLispNote) m).getDiatonicPitch() == n.getChromaticPitch()){
						o.addNote(m);
						return;
					}
				}
			}
		}
		o.addNote(n);
	}
	
	public static void setInfoFromFilePath(PatternSet s){
		File current = new File(s.getFilePath());
		//File is currently the discovery file itself
		
		current = current.getParentFile();
		//File is now the discovery folder
		
		current = current.getParentFile();
		//File is now the parameters folder
		s.setParameters(current.getName());
		
		if(s.getParameters().equals("AnnotatedMotifs")){
			s.setAlgorithm(s.getParameters());
			s.setScope(s.getParameters());
		} else{
			current = current.getParentFile();
			//File is now the algorithm folder
			s.setAlgorithm(current.getName());
			
			current = current.getParentFile();
			//File is now the scope folder
			s.setScope(current.getName());
		}
	}
	
	public static void setInfoFromFile(PatternSet ps, File f){
		try {
			ps.setFilePath(f.getCanonicalPath());
			setInfoFromFilePath(ps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
