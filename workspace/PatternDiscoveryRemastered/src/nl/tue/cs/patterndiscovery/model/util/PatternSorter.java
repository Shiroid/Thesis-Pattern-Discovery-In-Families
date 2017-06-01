package nl.tue.cs.patterndiscovery.model.util;

import java.util.Comparator;

import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;

public class PatternSorter  implements Comparator<Pattern>{

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
