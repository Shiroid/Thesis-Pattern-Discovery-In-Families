package nl.tue.cs.patterndiscovery.model.util;

import nl.tue.cs.patterndiscovery.model.*;

public class SubTreeFactory {

	public static ModelSubTree createFromMST(ModelSubTree mst){
		return new ModelSubTree(mst.dataSet, mst.tuneFamily, mst.song, mst.patternSet, mst.pattern, mst.patternOccurrence, mst.note);
	}
	
	public static ModelSubTree createFromMST(ModelSubTree mst, Note n){
		return new ModelSubTree(mst.dataSet, mst.tuneFamily, mst.song, mst.patternSet, mst.pattern, mst.patternOccurrence, n);
	}
	
	public static ModelSubTree createFromMST(ModelSubTree mst, Note n, PatternOccurrence o, Pattern p){
		return new ModelSubTree(mst.dataSet, mst.tuneFamily, mst.song, mst.patternSet, p, o, n);
	}

	public static ModelSubTree createFromMST(ModelSubTree mst, PatternOccurrence o, Pattern p){
		return new ModelSubTree(mst.dataSet, mst.tuneFamily, mst.song, mst.patternSet, p, o, mst.note);
	}

	public static ModelSubTree createDSet(DataSet ds){
		return new ModelSubTree(ds, null, null, null, null, null, null);
	}

	public static boolean isDSet(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.DATASET.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToFamily(DataSet ds, TuneFamily tf){
		return new ModelSubTree(ds, tf, null, null, null, null, null);
	}

	public static boolean isDSetToFamily(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.TUNEFAMILY.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToSong(DataSet ds, TuneFamily tf, Song s){
		return new ModelSubTree(ds, tf, s, null, null, null, null);
	}

	public static boolean isDSetToSong(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.SONG.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToPSet(DataSet ds, TuneFamily tf, Song s, PatternSet ps){
		return new ModelSubTree(ds, tf, s, ps, null, null, null);
	}

	public static boolean isDSetToPSet(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERNSET.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToPattern(DataSet ds, TuneFamily tf, Song s, PatternSet ps, Pattern p){
		return new ModelSubTree(ds, tf, s, ps, p, null, null);
	}

	public static boolean isDSetToPattern(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERN.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToOccurrence(DataSet ds, TuneFamily tf, Song s, PatternSet ps, Pattern p,
			PatternOccurrence o){
		return new ModelSubTree(ds, tf, s, ps, p, o, null);
	}

	public static boolean isDSetToOccurrence(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERNOCCURRENCE.greaterThanEqual(mst.getBottom());
	}

	public static ModelSubTree createDSetToNote(DataSet ds, TuneFamily tf, Song s, PatternSet ps, Pattern p,
			PatternOccurrence o, Note n){
		return new ModelSubTree(ds, tf, s, ps, p, o, n);
	}

	public static boolean isDSetToNote(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERNOCCURRENCE.greaterThanEqual(mst.getBottom()) &&
				mst.hasNote();
	}

	public static ModelSubTree createDSetToSongWN(DataSet ds, TuneFamily tf, Song s, Note n){
		return new ModelSubTree(ds, tf, s, null, null, null, n);
	}

	public static boolean isDSetToSongWN(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.SONG.greaterThanEqual(mst.getBottom()) &&
				mst.hasNote();
	}

	public static ModelSubTree createDSetToPSetWN(DataSet ds, TuneFamily tf, Song s, PatternSet ps, Note n){
		return new ModelSubTree(ds, tf, s, ps, null, null, null);
	}

	public static boolean isDSetToPSetWN(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERNSET.greaterThanEqual(mst.getBottom()) &&
				mst.hasNote();
	}

	public static ModelSubTree createDSetToPatternWN(DataSet ds, TuneFamily tf, Song s, PatternSet ps, Pattern p, Note n){
		return new ModelSubTree(ds, tf, s, ps, p, null, n);
	}

	public static boolean isDSetToPatternWN(ModelSubTree mst){
		return mst.getTop().greaterThanEqual(DataType.DATASET) && 
				DataType.PATTERN.greaterThanEqual(mst.getBottom()) &&
				mst.hasNote();
	}

	public static ModelSubTree createEmpty(){
		return new ModelSubTree(null, null, null, null, null, null, null);
	}
}
