package prototyping.algo;

import nl.tue.cs.patterndiscovery.model.Note;

public class ProtoNotePair implements Comparable<ProtoNotePair>{
	
	protected Note n1;
	protected Note n2;
	
	public ProtoNotePair(Note n1, Note n2){
		this.n1 = n1;
		this.n2 = n2;
	}
	
	public Note getNote1(){
		return n1;
	}
	
	public Note getNote2(){
		return n2;
	}
	
	public double getOnsetDiff(){
		return n2.getOnset() - n1.getOnset();
	}
	
	public int getPitchDiff(){
		return n2.getDiatonicPitch() - n1.getDiatonicPitch();
	}

	@Override
	public int compareTo(ProtoNotePair o) {
		return (int) Math.signum(o.getOnsetDiff() - getOnsetDiff());
	}

}
