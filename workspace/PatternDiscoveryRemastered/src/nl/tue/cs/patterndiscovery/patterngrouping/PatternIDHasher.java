package nl.tue.cs.patterndiscovery.patterngrouping;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;

public class PatternIDHasher extends PatternHasher {

	@Override
	public int getHash(ModelSubTree subtree) {
		if(subtree.pattern != null) return subtree.pattern.getPatternID();
		return -1;
	}

}
