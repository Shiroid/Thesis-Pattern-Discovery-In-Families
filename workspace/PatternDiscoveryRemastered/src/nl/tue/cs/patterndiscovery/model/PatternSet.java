package nl.tue.cs.patterndiscovery.model;

import java.io.File;
import java.util.*;

import nl.tue.cs.patterndiscovery.filemanager.SongReader;
import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

/*
 * A pattern set belonging to a song, can be extended for use in more settings
 */
public class PatternSet implements Iterable<Pattern>{

	//The file path this set is stored at
	protected String filePath = "";
	
	//The algorithm used to find this set
	protected String algorithm = "";

	//What scope this set was found in, intra- or inter opus
	protected String scope = "";
	
	//What parameters were used to find this set
	protected String parameters = "";
	
	//What name this set is given
	protected String name = "";
	
	//List of patterns in this set
	protected List<Pattern> patterns;

	
	/*
	 * Constructs pattern set with empty set of patterns.
	 */
	public PatternSet(){
		patterns = new ArrayList<Pattern>();
	}
	
	/*
	 * Constructs pattern set from existing collection of patterns.
	 */
	public PatternSet(Collection<Pattern> patSet){
		patterns = new ArrayList<Pattern>(patSet);
	}
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if(this.filePath.isEmpty())
			this.filePath = filePath;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		if(this.algorithm.isEmpty())
			this.algorithm = algorithm;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		if(this.scope.isEmpty())
			this.scope = scope;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		if(this.parameters.isEmpty())
			this.parameters = parameters;
	}

	public void setInfo(String algorithm, String scope, String parameters) {
		setAlgorithm(algorithm);
		setScope(scope);
		setParameters(parameters);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	/*
	 * Returns the key to identify this pattern set within a song
	 */
	public String getIntraSongKey(){
		return algorithm + "/" + parameters + "/" + scope;
	}
	
	public List<Pattern> getPatterns(){
		return patterns;
	}
	
	public void addPattern(Pattern p){
		patterns.add(p);
	}
	
	public Pattern getPattern(Pattern p){
		return CollectionElementGetter.getFromCollection(patterns, p);
	}
	
	public boolean hasSameKey(PatternSet s){
		return getIntraSongKey() == s.getIntraSongKey();
	}
	
	/*
	 * Returns the string representation of this pattern set
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		if(this.name.isEmpty())
			return getIntraSongKey();
		return name;
	}

	@Override
	public Iterator<Pattern> iterator() {
		return patterns.iterator();
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof PatternSet){
			return this.getIntraSongKey().equals(((PatternSet) o).getIntraSongKey());
		}
		return false;
	}
}
