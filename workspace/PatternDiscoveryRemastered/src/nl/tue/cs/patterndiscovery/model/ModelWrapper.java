package nl.tue.cs.patterndiscovery.model;

import java.util.ArrayList;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

public class ModelWrapper {

	protected static ArrayList<DataSet> dataSets;
	
	public static void initWrapper(){
		dataSets = new ArrayList<DataSet>();
	}
	
	public static boolean isInitialized(){
		return dataSets != null;
	}
	
	public static void addDataSet(DataSet ds){
		dataSets.add(ds);
	}
	
	public static DataSet getDataSet(DataSet ds){
		return CollectionElementGetter.getFromCollection(dataSets, ds);
	}
	
}
