package nl.tue.cs.patterndiscovery.filemanager;

import java.io.IOException;

import nl.tue.cs.patterndiscovery.model.CLNoteLossless;
import nl.tue.cs.patterndiscovery.model.CollinsLispNote;
import nl.tue.cs.patterndiscovery.model.Fraction;
import nl.tue.cs.patterndiscovery.model.Note;

/*
 * Class for reading notes, supports Collins Lisp format and tuples of onset and pitch.
 * May require safety nets in the form of exceptions, when the input is not of the expected form.
 */
public class NoteReader {

	/*
	 * @return The lossy representation of the presented Collins Lisp format note, using doubles instead of fractions.
	 */
	public static CollinsLispNote readLispNote(String lisp){
    	String[] values  = splitLispString(lisp);
		return new CollinsLispNote(
				new Fraction(values[0]).toDouble(),
				Integer.parseInt(values[1]),
				Integer.parseInt(values[2]),
				new Fraction(values[3]).toDouble(),
				Integer.parseInt(values[4])
				);
	}
	
	/*
	 * @return The lossless representation of the presented Collins Lisp format note, using fractions instead of doubles.
	 */
	public static CLNoteLossless readLispNoteLossless(String lisp){
    	String[] values  = splitLispString(lisp);
		return new CLNoteLossless(
				new Fraction(values[0]),
				Integer.parseInt(values[1]),
				Integer.parseInt(values[2]),
				new Fraction(values[3]),
				Integer.parseInt(values[4])
				);
	}
	
	/*
	 * @return The lossless representation of the presented Collins Lisp format note, using fractions instead of doubles.
	 */
	public static Note readPlainNote(String plain){
    	String[] values  = plain.split(",");
		return new Note(
				Double.parseDouble(values[0]),
				(int) Double.parseDouble(values[1]) //Make safer?
				);
	}
	
	/*
	 * @return True iff Collins Lisp format note contains fractions of the form "a/b" for onset or duration.
	 */
	public static boolean lispIsLossless(String lisp){
    	return lisp.contains("/");
	}
	
	/*
	 * @return An array of strings created out of the original string, containing exactly the 5 needed values in sequence, see readLispNote.
	 */
	public static String[] splitLispString(String lisp){
		String s = lisp.replaceAll("[\\(\\)]", "");
    	String[] values = s.split(" ");
    	return values;
	}
}
