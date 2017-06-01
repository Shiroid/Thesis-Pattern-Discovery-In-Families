package nl.tue.cs.patterndiscovery.view.util.highlights;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.player.SequencePlayer;

public class PlayingPatternTracker extends PatternTracker{
	
	protected SequencePlayer seqPlayer;
	
	public PlayingPatternTracker(){
		super();
		seqPlayer = new SequencePlayer();
	}
	
	public PlayingPatternTracker(boolean doSpecific){
		super(doSpecific);
		seqPlayer = new SequencePlayer();
	}
	
	@Override
	public void highlight(ModelSubTree subtree) {
		if(subtree != null) {
			this.highlightedPattern = subtree;
			if(subtree.patternOccurrence != null){
				seqPlayer.Stop();
				seqPlayer.setSequence(subtree.patternOccurrence);
				seqPlayer.Play();
			}
		}
	}

	@Override
	public void unhighlight(ModelSubTree subtree) {
		seqPlayer.Stop();
		super.unhighlight(subtree);
	}

	@Override
	public void unhighlightAll() {
		seqPlayer.Stop();
		super.unhighlightAll();
	}

}
