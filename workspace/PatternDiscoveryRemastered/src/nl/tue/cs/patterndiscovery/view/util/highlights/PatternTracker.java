package nl.tue.cs.patterndiscovery.view.util.highlights;

import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;

public class PatternTracker extends HighlightTracker {

	protected ModelSubTree highlightedPattern;
	protected boolean doSpecific = false;
	
	public PatternTracker(){
		super();
	}
	
	public PatternTracker(boolean doSpecific){
		super();
		this.doSpecific = doSpecific;
	}
	
	@Override
	public boolean hasHighlight() {
		return this.highlightedPattern != null;
	}

	@Override
	public boolean isHighlighted(ModelSubTree subtree) {
		if(this.hasHighlight() && subtree != null){
			return (subtree.contains(this.highlightedPattern) || !doSpecific);
		}
		return false;
	}

	@Override
	public Highlight getHighlight(ModelSubTree subtree) {
		if(this.hasHighlight()){
			return this.hasher.getHash(subtree) == this.hasher.getHash(highlightedPattern)?
					(isHighlighted(subtree)?
							Highlight.HIGHLIGHTED:Highlight.UNHIGHLIGHTED):Highlight.FADED;
		}
		return Highlight.UNHIGHLIGHTED;
	}

	@Override
	public void highlight(ModelSubTree subtree) {
		if(subtree.pattern != null) this.highlightedPattern = subtree;
	}

	@Override
	public void unhighlight(ModelSubTree subtree) {
		if(isHighlighted(subtree)) this.highlightedPattern = null;
	}

	@Override
	public void unhighlightAll() {
		this.highlightedPattern = null;
	}

	@Override
	public ModelSubTree getHighlightedTree() {
		if(highlightedPattern == null)
			return null;
		else
			return SubTreeFactory.createFromMST(highlightedPattern);
	}
}

