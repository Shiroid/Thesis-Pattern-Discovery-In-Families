package nl.tue.cs.patterndiscovery.model.util;

import java.util.Comparator;

import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;;

public class OccurrenceSorter implements Comparator<PatternOccurrence>{

	@Override
	public int compare(PatternOccurrence o1, PatternOccurrence o2) {
		return (int) Math.ceil(o1.getStartTime() - o2.getStartTime());
	}
}
