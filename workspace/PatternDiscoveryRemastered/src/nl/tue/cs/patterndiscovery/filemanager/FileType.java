package nl.tue.cs.patterndiscovery.filemanager;

public enum FileType {
	UNKNOWN, COLLINSLISPSONG, MIREXPATTERNS;
}
/*
 * Collins lisp song files are of the form "(A B C D E)\n", 
 * where B, C, E are natural numbers, and A, D can be fractions, natural numbers or doubles.
 * A indicates onset, B indicates chromatic pitch, C indicates diatonic pitch, D indicates duration and E indicates voice index.
 * 
 * Mirex patterns files are of the form "patternX\noccurrenceY\nA, B\nC, D\noccurrenceZ\n", occurrenceZ follows the same pattern as Y, 
 * where X,Y,Z are natural numbers and A, B, C, D are doubles, in which A and C are always round.
 * X indicates the pattern number, Y, Z indicate the occurrence number, A, D indicate onset and C, D indicate chromatic pitch.
 * Some instances of these files may have lines that are not of the described form, these lines can be ignored for most purposes.
 */
