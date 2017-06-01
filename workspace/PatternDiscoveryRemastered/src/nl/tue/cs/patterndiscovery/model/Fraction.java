package nl.tue.cs.patterndiscovery.model;

/*
 * Class representing a fraction, either as an actual fraction or as a lossy double. 
 * Reading and writing fractions of either kind is managed within this class.
 */
public class Fraction {
	private int numerator;
	private int denominator;
	private boolean lossy;
	private double lossyVal;
	
	public Fraction(int n, int d){
		this.numerator = n;
		this.denominator = d;
		this.lossy = false;
	}
	
	public Fraction(double a){
		this.lossyVal = a;
		this.lossy = true;
	}
	
	//Reads the fraction of the string form "a/b" or "a", or defaults to double if of the form "a.b"
	public Fraction(String s){
		this.lossy = false;
		if(s.contains(".")){ //Read double
			this.lossyVal = Double.parseDouble(s);
			this.lossy = true;
		} else { //Read fraction
			String[] nd = s.split("/");
			if(nd.length > 0) this.numerator = Integer.parseInt(nd[0]);
			else this.numerator = 0;
			if(nd.length > 1) this.denominator = Integer.parseInt(nd[1]);
			else this.denominator = 1;
			lossyVal = ((double) numerator) / ((double) denominator);
		}
	}
	
	public boolean isLossy(){
		return lossy;
	}
	
	public double toDouble(){
		return lossyVal;
	}
	
	public String toString(){
		if (lossy) return Double.toString(lossyVal);
		else {
			if (denominator == 1) return Integer.toString(numerator);
			else return numerator + "/" + denominator;
		}
	}
}
