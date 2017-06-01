package nl.tue.cs.patterndiscovery.view.util.colors;

import java.awt.Color;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternHasher;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;

public class HeatColorChooser extends ColorChooser {
	
	public HeatColorChooser(){
		super();
	}
	
	public HeatColorChooser(HighlightTracker tracker){
		super(tracker);
	}
	
	public HeatColorChooser(HighlightTracker tracker, PatternHasher hasher){
		super(tracker, hasher);
	}

	@Override
	public Color getColor(ModelSubTree subtree, int numOccurrences) {
		if(numColors == 0 || numOccurrences == 0) return Color.DARK_GRAY;
		float ratio = ((float) numOccurrences) / ((float) numColors);
		return new Color(Math.min(ratio, 1), Math.max(1-ratio, 0), 0);
	}

}
