package nl.tue.cs.patterndiscovery.view.util.highlights;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;

public class NoteTracker extends HighlightTracker {

	ModelSubTree highlightedNote;
	
	@Override
	public boolean hasHighlight() {
		return this.highlightedNote != null;
	}

	@Override
	public boolean isHighlighted(ModelSubTree subtree) {
		if(this.hasHighlight()){
			return this.highlightedNote.note.equals(subtree.note);
		}
		return false;
	}

	@Override
	public Highlight getHighlight(ModelSubTree subtree) {
		if(this.hasHighlight()){
			return isHighlighted(subtree)?Highlight.HIGHLIGHTED:Highlight.FADED;
		}
		return Highlight.UNHIGHLIGHTED;
	}

	@Override
	public void highlight(ModelSubTree subtree) {
		if(subtree.note != null) this.highlightedNote = subtree;
	}

	@Override
	public void unhighlight(ModelSubTree subtree) {
		if(isHighlighted(subtree)) this.highlightedNote = null;
	}

	@Override
	public void unhighlightAll() {
		this.highlightedNote = null;
	}

	@Override
	public ModelSubTree getHighlightedTree() {
		if(highlightedNote == null)
			return null;
		else
			return SubTreeFactory.createFromMST(highlightedNote);
	}
}
