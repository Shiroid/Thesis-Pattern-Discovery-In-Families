package nl.tue.cs.patterndiscovery.view.util.highlights;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternHasher;

public abstract class HighlightTracker {
	
	protected PatternHasher hasher;
	
	public abstract ModelSubTree getHighlightedTree();

	public abstract boolean hasHighlight();

	public abstract Highlight getHighlight(ModelSubTree subtree);

	public abstract boolean isHighlighted(ModelSubTree subtree);

	public abstract void highlight(ModelSubTree subtree);

	public abstract void unhighlight(ModelSubTree subtree);

	public abstract void unhighlightAll();

	public void toggleHighlight(ModelSubTree subtree){
		if(isHighlighted(subtree)) unhighlight(subtree);
		else highlight(subtree);
	}

	public PatternHasher getHasher() {
		return hasher;
	}

	public void setHasher(PatternHasher hasher) {
		this.hasher = hasher;
	}
}
