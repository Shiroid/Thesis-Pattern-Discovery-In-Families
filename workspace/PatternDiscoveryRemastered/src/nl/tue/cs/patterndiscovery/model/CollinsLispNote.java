package nl.tue.cs.patterndiscovery.model;

/*
 * Class representing a note in Collins Lisp format. 
 * This includes the onset time, chromatic pitch, diatonic pitch, duration and a voice index.
 * Additionally, the pitch class can be retrieved.
 */
public class CollinsLispNote extends Note implements Comparable<Note> {
	
	protected int diatonicPitch;
	protected double duration;
	protected int voice;

	@Override
	public int getDiatonicPitch() {
		return diatonicPitch;
	}

	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public int getVoice() {
		return voice;
	}


	public CollinsLispNote(Note note)
	{
		super(note.getOnset(), note.getChromaticPitch());
		initialize(note.diatonicFromChromatic(), 1, 0);
	}
	
	public CollinsLispNote(double onset, int chromaticPitch, int diatonicPitch, double duration)
	{
		super(onset, chromaticPitch);
		initialize(diatonicPitch, duration, 0);
	}
	
	public CollinsLispNote(double onset, int chromaticPitch, int diatonicPitch, double duration, int voice)
	{
		super(onset, chromaticPitch);
		initialize(diatonicPitch, duration, voice);
	}
	
	private void initialize(int diatonicPitch, double duration, int voice){
		this.diatonicPitch = diatonicPitch;
		this.duration = duration;
		this.voice = voice;
	}
	
	/*
	 * @return Collins Lisp representation of this note, in parenthesis.
	 */
	@Override
	public String toString(){
		return "(" + onset + " " + chromaticPitch + " " + diatonicPitch + " " + duration + " " + voice + ")";
	}
	
	//Additional algorithm stuff
	@Override
	public void applyNormalization(){
		if(!normApplied){
			this.diatonicPitch += this.diatonicAdd;
			this.chromaticPitch += this.chromAdd;
			this.onset *= this.timeMult;
			this.duration *= this.timeMult;
		}
		normApplied = true;
	}

	@Override
	public void revertNormalization(){
		if(normApplied){
			this.diatonicPitch -= this.diatonicAdd;
			this.chromaticPitch -= this.chromAdd;
			this.onset /= this.timeMult;
			this.duration /= this.timeMult;
		}
		normApplied = false;
	}
}
