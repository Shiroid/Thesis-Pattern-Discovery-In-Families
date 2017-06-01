package nl.tue.cs.patterndiscovery.view.util.colors;

import java.awt.Color;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternHasher;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;

public abstract class ColorChooser {
	
	protected int numColors;
	protected HighlightTracker highlightTracker;
	
	public ColorChooser(HighlightTracker tracker){
		this.setHighlightTracker(tracker);
	}
	
	public ColorChooser(){
	}
	
	public ColorChooser(HighlightTracker tracker, PatternHasher hasher){
		this.setTrackerAndHasher(tracker, hasher);
	}

	public abstract Color getColor(ModelSubTree subtree, int numOccurrences);
	
	public void setNumColors(int numColors){
		this.numColors = numColors;
	}
	
	public void increaseNumColors(int numColors){
		if(this.numColors < numColors) this.numColors = numColors;
	}
	
	public int getNumColors(){
		return this.numColors;
	}

	public PatternHasher getHasher() {
		return highlightTracker.getHasher();
	}

	public void setHasher(PatternHasher hasher) {
		highlightTracker.setHasher(hasher);
	}
	
	public HighlightTracker getHighlightTracker(){
		return highlightTracker;
	}
	
	public void setHighlightTracker(HighlightTracker tracker){
		PatternHasher ph = this.highlightTracker.getHasher();
		this.highlightTracker = tracker;
		tracker.setHasher(ph);
	}
	
	public void setTrackerAndHasher(HighlightTracker tracker){
		this.highlightTracker = tracker;
	}
	
	public void setTrackerAndHasher(HighlightTracker tracker, PatternHasher hasher){
		this.highlightTracker = tracker;
		this.highlightTracker.setHasher(hasher);
	}
	
}
