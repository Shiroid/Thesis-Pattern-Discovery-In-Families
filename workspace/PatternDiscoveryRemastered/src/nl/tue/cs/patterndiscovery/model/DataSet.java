package nl.tue.cs.patterndiscovery.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

public class DataSet implements Iterable<TuneFamily> {

	//ID of the data set this song belongs to, used for matching songs
	protected String setName;
	
	private ArrayList<TuneFamily> families;
	
	public DataSet(){
		this.families = new ArrayList<TuneFamily>();
	}
	
	public DataSet(String name){
		this.families = new ArrayList<TuneFamily>();
		this.setName(name);
	}
	
	public DataSet(Collection<TuneFamily> families){
		this.families = new ArrayList<TuneFamily>(families);
	}
	
	public DataSet(Collection<TuneFamily> families, String name){
		this.families = new ArrayList<TuneFamily>(families);
		this.setName(name);
	}

	public String getName() {
		return setName;
	}

	public void setName(String name) {
		if(this.setName == null)
			this.setName = name;
	}

	@Override
	public Iterator<TuneFamily> iterator() {
		return families.iterator();
	}
	
	public List<TuneFamily> getFamilies(){
		return this.families;
	}
	
	public void addFamily(TuneFamily family){
		this.families.add(family);
	}
	
	public TuneFamily getFamily(TuneFamily family){
		return CollectionElementGetter.getFromCollection(families, family);
	}
	
	@Override
	public boolean equals(Object o){
		if(this.setName == null) return false;
		if(o instanceof DataSet){
			return (setName.equals(((DataSet) o).getName()));
		}
		return false;
	}
	
	@Override
	public String toString(){
		return getName();
	}
}
