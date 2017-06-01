package nl.tue.cs.patterndiscovery.view.util.colors;

import java.awt.Color;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternHasher;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;

public class CCWrapper extends ColorChooser{

	protected ColorChooser cc;
	
	public CCWrapper(ColorChooser cc){
		setColorChooser(cc);
	}
	
	@Override
	public Color getColor(ModelSubTree subtree, int numOccurrences) {
		return cc.getColor(subtree, numOccurrences);
	}
	
	public void setColorChooser(ColorChooser cc){
		this.cc = cc;
	}
	
	public ColorChooser getColorChooser(){
		return this.cc;
	}
	
	public void setNumColors(int numColors){
		cc.setNumColors(numColors);;
	}
	
	public void increaseNumColors(int numColors){
		cc.increaseNumColors(numColors);;
	}
	
	public int getNumColors(){
		return cc.numColors;
	}

	public PatternHasher getHasher() {
		return cc.getHighlightTracker().getHasher();
	}

	public void setHasher(PatternHasher hasher) {
		cc.getHighlightTracker().setHasher(hasher);
	}
	
	public HighlightTracker getHighlightTracker(){
		return cc.getHighlightTracker();
	}
	
	public void setHighlightTracker(HighlightTracker tracker){
		cc.setHighlightTracker(tracker);
	}
	
	public void setTrackerAndHasher(HighlightTracker tracker){
		cc.setTrackerAndHasher(tracker);
	}
	
	public void setTrackerAndHasher(HighlightTracker tracker, PatternHasher hasher){
		cc.setTrackerAndHasher(tracker, hasher);
	}

}
