package nl.tue.cs.patterndiscovery.model;

public class Note implements Comparable<Note> {

	protected int chromaticPitch;
	protected double onset;
	
	public int getChromaticPitch() {
		return chromaticPitch;
	}
	
	public double getDuration() {
		return 1;
	}

	public double getOnset() {
		return onset;
	}

	public int getDiatonicPitch() {
		return diatonicFromChromatic();
	}

	public int getVoice() {
		return 0;
	}
	
	public Note(double onset, int chromaticPitch){
		this.chromaticPitch = chromaticPitch;
		this.onset = onset;
	}
	
	/*
	 * A representation of the chromatic pitch, ignoring which octave the note is, known as the pitch class.
	 * @return Pitch class of this note, defined as chromatic pitch modulo 12.
	 */
	public int getPitchClass() {
		return chromaticPitch % 12;
	}
	
	/*
	 * Determines the diatonic pitch from a chromatic pitch, assumes sharp notation, so C# is taken rather than Db.
	 * @return Diatonic pitch, sharps.
	 */
	public int diatonicFromChromatic() {
		//Determine diatonic pitch through sharps only, centered around MIDI number 60
		int octave = (int) Math.floor((double) (this.chromaticPitch) / 12);
		int octaveCenter = 5;
		int chromaticCenter = 60;
		int diatonicBase = chromaticCenter + (octave - octaveCenter)*7;
		int chromaticOffset = this.chromaticPitch%12;
		int diatonicOffset = 0;
		switch (chromaticOffset){
		case 0:
		case 1: diatonicOffset = 0; //C and C#
		case 2:
		case 3: diatonicOffset = 1; //D and D#
		case 4: diatonicOffset = 2; //E
		case 5:
		case 6:	diatonicOffset = 3; //F and F#
		case 7:
		case 8:	diatonicOffset = 4; //G and G#
		case 9:
		case 10: diatonicOffset = 5; //A and A#
		case 11: diatonicOffset = 6; //B
		}
		return diatonicBase + diatonicOffset;
	}

	/*
	 * Compares notes to sort in lexicographic order of onset, then chromatic pitch.
	 */
	@Override
	public int compareTo(Note other) {
		//Round onsets up to 2 decimal
		if (Math.round(this.onset*100) > Math.round(other.getOnset()*100)) return 2;
		else if(Math.round(this.onset*10) < Math.round(other.getOnset()*10)) return -2;
		else if(this.chromaticPitch > other.getChromaticPitch()) return 1;
		else if(this.chromaticPitch < other.getChromaticPitch()) return -1;
		else return 0;
	}
	
	/*
	 * Considers notes of same onset and chromatic pitch to be equal, used for sorting and hashing.
	 * Intended to prevent memory bloat from duplicate notes in pattern occurrences.
	 */
	@Override
	public boolean equals(Object other){
		if (other instanceof Note){
			return compareTo((Note) other) == 0;
		}
		return false;
	}
	
	/*
	 * @return A string of format "onset, chromatic pitch", where both values are doubles
	 */
	@Override
	public String toString(){
		return asStringTuple();
	}

	
	/*
	 * @return A string of format "onset, chromatic pitch", where both values are doubles
	 */
	public String asStringTuple(){
		return onset + ", " + ((double) chromaticPitch);
	}

	
	//Additional data for algo
	private Song song;
	
	public Song getSong(){
		return song;
	}
	
	public void setSong(Song s){
		this.song = s;
	}
	
	protected double timeMult;
	protected int diatonicAdd;
	protected int chromAdd;
	protected boolean normApplied = false;
	
	public void setNormalization(double timeMult, int diatonicAdd, int chromAdd){
		revertNormalization();
		this.timeMult = timeMult;
		this.diatonicAdd = diatonicAdd;
		this.chromAdd = chromAdd;
	}
	
	public void applyNormalization(){
		normApplied = true;
	}
	
	public void revertNormalization(){
		normApplied = false;
	}
	
	private int noteID;
	
	public void setNoteID(int noteID){
		this.noteID = noteID;
	}
	
	public int getNoteID(){
		return this.noteID;
	}
	
	public double getTimeMult(){
		return this.timeMult;
	}
	
	public double getDiatonicAdd(){
		return this.diatonicAdd;
	}
	
}
