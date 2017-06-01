package nl.tue.cs.patterndiscovery.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tue.cs.patterndiscovery.config.Config;
import nl.tue.cs.patterndiscovery.model.DataSet;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.SongNoteSaving;
import nl.tue.cs.patterndiscovery.model.TuneFamily;
import nl.tue.cs.patterndiscovery.model.util.FamilyExtractorSingleton;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;

public class DirectoryNavigator {

	public static List<DataSet> findDataSets(){
		ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
		for(File f: Config.getDataSetsRoot().listFiles()){
			if(f.isDirectory()){
				dataSets.add(new DataSet(f.getName()));
			}
		}
		return dataSets;
	}

	public static List<TuneFamily> findTuneFamilies(ModelSubTree mst){
		ArrayList<TuneFamily> families = new ArrayList<TuneFamily>();
		if(mst.dataSet != null){
			File dataSetDir = findDataSetDirectory(Config.getDataSetsRoot(), mst.dataSet);
			
			if(dataSetDir != null){
				dataSetDir = climbDataSetDirectory(dataSetDir);
				
				//Extract all the families
				Set<String> names = extractFamilyNames(dataSetDir);
				for(String s: names){
					families.add(new TuneFamily(s));
				}
			}
		} else{
			for(DataSet ds: findDataSets()){
				families.addAll(findTuneFamilies(SubTreeFactory.createDSet(ds)));
			}
		}
		return families;
	}

	public static List<Song> findSongs(ModelSubTree mst){
		ArrayList<Song> songs = new ArrayList<Song>();
		if(mst.dataSet != null){
			File dataSetDir = findDataSetDirectory(Config.getDataSetsRoot(), mst.dataSet);
			
			if(dataSetDir != null){
				dataSetDir = climbDataSetDirectory(dataSetDir);
				
				//Extract all the songs
				List<File> files = extractSongFiles(dataSetDir, mst);
				for(File f: files){
					Song song = new SongNoteSaving();
					SongReader.setSongNameAndPath(song, f);
					songs.add(song);
				}
			}
		} else {
			for(DataSet ds: findDataSets()){
				songs.addAll(findSongs(SubTreeFactory.createDSet(ds)));
			}
		}
		return songs;
	}

	public static List<File> findScopes(ModelSubTree mst){
		ArrayList<File> scopes = new ArrayList<File>();
		if(mst.dataSet != null){
			File dataSetDir = findDataSetDirectory(Config.getDiscoveryRoot(), mst.dataSet);
			
			if(dataSetDir != null){
				for(File f: dataSetDir.listFiles()){
					if(f.getName().equals("AnnotatedMotifs")) scopes.add(f);
					if(f.getName().equals("InterOpusDiscoveryClassTask")) scopes.add(f);
					if(f.getName().equals("IntraOpusDiscoveryTask")) scopes.add(f);
				}
			}
		} else{
			for(DataSet ds: findDataSets()){
				scopes.addAll(findScopes(SubTreeFactory.createDSet(ds)));
			}
		}
		return scopes;
	}

	public static List<File> findAlgorithms(File scope){
		ArrayList<File> algorithms = new ArrayList<File>();
		if(scope != null){
			if(scope.getName().equals("AnnotatedMotifs")){
				algorithms.add(scope);
			} else{
				for(File f: scope.listFiles()){
					if(f.getName().matches(".*Algorithm") && f.isDirectory())
						algorithms.add(f);
				}
			}
		}
		return algorithms;
	}

	public static List<File> findParameters(File algorithm){		
		ArrayList<File> parameters = new ArrayList<File>();
		if(algorithm != null){
			if(algorithm.getName().equals("AnnotatedMotifs")){
				parameters.add(algorithm);
			} else {
				for(File f: algorithm.listFiles()){
					if(f.isDirectory())
						parameters.add(f);
				}
			}
		}
		return parameters;
	}

	public static List<PatternSet> findPatternSets(ModelSubTree mst, File parameters){		
		ArrayList<PatternSet> patternSets = new ArrayList<PatternSet>();
		if(parameters != null){
			File psRoot = parameters;
			for(File f: parameters.listFiles()){
				if(f.getName().equals("discovery"))
					psRoot = f;
			}
			//Extract all the songs
			List<File> files = extractSongFiles(psRoot, mst);
			for(File f: files){
				PatternSet patternSet = new PatternSet();
				PatternSetReader.setInfoFromFile(patternSet, f);
				patternSets.add(patternSet);
			}
		}
		return patternSets;
	}

	public static List<File> findPatternSetFiles(ModelSubTree mst, File parameters){		
		List<File> patternSets = new ArrayList<File>();
		if(parameters != null){
			File psRoot = parameters;
			for(File f: parameters.listFiles()){
				if(f.getName().equals("discovery"))
					psRoot = f;
			}
			//Extract all the songs
			patternSets = extractSongFiles(psRoot, mst);
		}
		return patternSets;
	}
	
	
	
	public static File findDataSetDirectory(File baseDir, DataSet dataSet){
		File dataSetDir = null;
		//Get the directory of the dataset
		for(File f: baseDir.listFiles()){
			if(f.isDirectory() && dataSet.getName().equals(f.getName())){
				dataSetDir = f;
				break;
			}
		}
		return dataSetDir;
	}
	
	public static File climbDataSetDirectory(File dataSetDir){
		//Dig into the dataset directory to the actual files
		File result = dataSetDir;
		for(File f: result.listFiles()){
			if(f.getName().equals("intra-opus")){
				result = f;
				break;
			}
		}
		for(File f: result.listFiles()){
			if(f.getName().equals("lisp")){
				result = f;
				break;
			}
		}
		return result;
	}
	
	public static Set<String> extractFamilyNames(File dataSetDir){
		//Extract all the families
		HashSet<String> names = new HashSet<String>();
		for(File f: dataSetDir.listFiles()){
			if(FileValidityChecker.isValid(f)){
				names.add(
						FamilyExtractorSingleton.familyNameFromName(
								FileExtensionStripper.getNameWithoutExt(
										f.getName())));
			}
		}
		return names;
	}
	
	public static Set<String> extractSongNames(File dataSetDir, TuneFamily tuneFamily){
		HashSet<String> names = new HashSet<String>();
		for(File f: dataSetDir.listFiles()){
			if(FileValidityChecker.isValid(f)){
				if(tuneFamily != null){
					if(FamilyExtractorSingleton.familyNameFromName(
									FileExtensionStripper.getNameWithoutExt(
											f.getName())).equals(tuneFamily.getFamilyName())){
						names.add(FileExtensionStripper.getNameWithoutExt(
								f.getName()));
					}
				} else {
					names.add(FileExtensionStripper.getNameWithoutExt(
							f.getName()));
				}
			}
		}
		return names;
	}
	
	public static List<File> extractSongFiles(File dataSetDir, ModelSubTree mst){
		//Extract all the families
		ArrayList<File> files = new ArrayList<File>();
		for(File f: dataSetDir.listFiles()){
			if(FileValidityChecker.isValid(f)){
				if(mst.song != null) {
					if(SongReader.songNameFromFile(f).equals(mst.song.getSongName())){
						files.add(f);
					}
				} else 
					if(mst.tuneFamily != null){
						if(familyNameFromFile(f).equals(mst.tuneFamily.getFamilyName())){
							files.add(f);
						}
				} else {
					files.add(f);
				}
			}
		}
		return files;
	}
	
	public static String familyNameFromFile(File f){
		return FamilyExtractorSingleton.familyNameFromName(SongReader.songNameFromFile(f));
	}
}
