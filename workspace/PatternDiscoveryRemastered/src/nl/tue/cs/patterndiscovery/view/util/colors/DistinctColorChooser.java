package nl.tue.cs.patterndiscovery.view.util.colors;

import java.awt.Color;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternHasher;
import nl.tue.cs.patterndiscovery.view.util.highlights.*;

public class DistinctColorChooser extends ColorChooser {
	
	private Color[] colors = new Color[]{
			new Color(166,206,227),
			new Color(31,120,180),
			new Color(178,223,138),
			new Color(51,160,44),
			new Color(251,154,153),
			new Color(227,26,28),
			new Color(253,191,111),
			new Color(255,127,0),
			new Color(202,178,214),
			new Color(106,61,154),
			new Color(255,255,153),
			new Color(177,89,40)
			};
	
	public DistinctColorChooser(){
		super();
		this.numColors = colors.length;
	}
	
	public DistinctColorChooser(HighlightTracker tracker){
		super(tracker);
		this.numColors = colors.length;
	}
	
	public DistinctColorChooser(HighlightTracker tracker, PatternHasher hasher){
		super(tracker, hasher);
		this.numColors = colors.length;
	}
	
	@Override
	public void setNumColors(int numColors){
		//Disallow change
	}

	@Override
	public Color getColor(ModelSubTree subtree, int numOccurrences) {
		int index = this.getHasher().getHash(subtree);
		if(index < 0) return Color.DARK_GRAY;
		Color result = colors[index % colors.length];
		switch(this.getHighlightTracker().getHighlight(subtree)){
		case FADED: 
			result = result.darker();
			break;
		case HIGHLIGHTED:
			result = result.brighter();
			break;
		}
		return result;
	}

}
