package nl.tue.cs.patterndiscovery.patterngrouping;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;

public abstract class PatternHasher {
	
	public abstract int getHash(ModelSubTree subtree);
	
}
