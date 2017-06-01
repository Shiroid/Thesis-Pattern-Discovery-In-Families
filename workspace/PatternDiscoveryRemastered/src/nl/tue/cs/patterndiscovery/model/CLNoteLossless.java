package nl.tue.cs.patterndiscovery.model;

public class CLNoteLossless extends CollinsLispNote {

	protected Fraction losslessOnset;
	protected Fraction losslessDuration;
	
	
	
	public Fraction getLosslessOnset() {
		return losslessOnset;
	}



	public Fraction getLosslessDuration() {
		return losslessDuration;
	}



	public CLNoteLossless(Fraction onset, int chromaticPitch, int diatonicPitch, Fraction duration, int voice){
		super(onset.toDouble(), chromaticPitch, diatonicPitch, duration.toDouble(), voice);
		this.losslessOnset = onset;
		this.losslessDuration = duration;
	}
	
	/*
	 * @return Collins Lisp representation of this note, in parenthesis.
	 */
	@Override
	public String toString(){
		return "(" + losslessOnset + " " + chromaticPitch + " " + diatonicPitch + " " + losslessDuration + " " + voice + ")";
	}
}
