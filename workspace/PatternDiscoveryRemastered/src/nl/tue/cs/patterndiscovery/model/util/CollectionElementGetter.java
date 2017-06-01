package nl.tue.cs.patterndiscovery.model.util;

import java.util.Collection;

public class CollectionElementGetter {

	public static < E > E getFromCollection(Collection<? extends E> c, E o){
		for(E e: c){
			if(e.equals(o)) return e;
		}
		return null;
	}
}
