package nl.tue.cs.patterndiscovery.patterngrouping;

import java.util.Comparator;
import java.util.List;

import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.PatternSorter;

public class SortedPatternCountHasher extends PatternHasher{

	@Override
	public int getHash(ModelSubTree subtree) {
		if(subtree.patternSet != null && subtree.pattern != null){
			List<Pattern> patterns = subtree.patternSet.getPatterns();
			patterns.sort(new PatternSorter());
			int counter = 0;
			for(Pattern p: patterns){
				if(p.equals(subtree.pattern)) return counter;
				counter++;
			}
		}
		return -1;
	}
	
	private class patComp implements Comparator<Pattern>{

		@Override
		public int compare(Pattern arg0, Pattern arg1) {
			double start0 = Double.MAX_VALUE, start1 = Double.MAX_VALUE;
			for(PatternOccurrence o: arg0){
				start0 = Math.min(o.getStartTime(), start0);
			}
			for(PatternOccurrence o: arg1){
				start1 = Math.min(o.getStartTime(), start1);
			}
			return (int) Math.ceil(start0 - start1);
		}
		
	}

}
