package nl.tue.cs.patterndiscovery.patterngrouping;

import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;

public class PatternCountHasher extends PatternHasher{

	@Override
	public int getHash(ModelSubTree subtree) {
		if(subtree.patternSet != null && subtree.pattern != null){
			int counter = 0;
			for(Pattern p: subtree.patternSet){
				if(p.equals(subtree.pattern)) return counter;
				counter++;
			}
		}
		return -1;
	}

}
