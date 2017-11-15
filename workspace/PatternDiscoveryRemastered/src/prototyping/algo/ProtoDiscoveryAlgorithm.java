package prototyping.algo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tue.cs.patterndiscovery.config.Config;
import nl.tue.cs.patterndiscovery.model.*;

public class ProtoDiscoveryAlgorithm {
	
	protected TuneFamily tf;
	
	protected double gs; 
	protected double rr;
	protected double ss;
	protected double mr; 

	protected int patsPerEnd = 3;
	protected int maxEmbellish = 1;
	protected int minDiscPatSize = 3;
	protected boolean includeTranspositions = true;

	protected int maxRepsPerSong = 3;
	protected double subCoverageLeeway = 0.9;
	
	protected double pwsBias = 0.05;
	protected double isBias = 0.5;
	protected int numIterations = 10;
	protected double skipCost = 1;
	
	protected PatternSet clustered;
	protected PatternSet selectedInclusive;
	protected PatternSet selected;
	
	public ProtoDiscoveryAlgorithm(TuneFamily tf, double gs, double rr, double ss, double mr){
		this.tf = tf;
		this.gs = gs;
		this.rr = rr;
		this.ss = ss;
		this.mr = mr;
	}
	
	public ProtoDiscoveryAlgorithm(TuneFamily tf, double gs, double rr, double ss, double mr,
			int maxEmbellish, int patsPerEnd, int  minDiscPatSize, boolean includeTranspositions){
		this(tf, gs, rr, ss, mr);
		this.patsPerEnd = patsPerEnd;
		this.maxEmbellish = maxEmbellish;
		this.minDiscPatSize = minDiscPatSize;
		this.includeTranspositions = includeTranspositions;
	}
	
	public ProtoDiscoveryAlgorithm(TuneFamily tf, double gs, double rr, double ss, double mr,
			int maxEmbellish, int patsPerEnd, int  minDiscPatSize, boolean includeTranspositions, 
			int maxRepsPerSong, double subCoverageLeeway){
		this(tf, gs, rr, ss, mr, maxEmbellish, patsPerEnd, minDiscPatSize, includeTranspositions);
		this.maxRepsPerSong = maxRepsPerSong;
		this.subCoverageLeeway = subCoverageLeeway;
	}
	
	public ProtoDiscoveryAlgorithm(TuneFamily tf, double gs, double rr, double ss, double mr,
			int maxEmbellish, int patsPerEnd, int  minDiscPatSize, boolean includeTranspositions, 
			int maxRepsPerSong, double subCoverageLeeway,
			double pwsBias, double isBias, int numIterations, double skipCost){
		this(tf, gs, rr, ss, mr, maxEmbellish, patsPerEnd, minDiscPatSize, includeTranspositions, 
				maxRepsPerSong, subCoverageLeeway);
		this.pwsBias = pwsBias;
		this.isBias = isBias;
		this.numIterations = numIterations;
		this.skipCost = skipCost;
	}
	
	public Collection<PatternSet> run(){
		System.out.println("Starting");
		prepNotes();
		PatternSet result;
		ProtoNormalizationTask normalization = new ProtoNormalizationTask(tf, gs, ss);
		System.out.println("Normalizing");
		normalization.computeNormalization();
		normalization.apply();
		ProtoDiscoveryTask discovery = new ProtoDiscoveryTask(tf, gs, rr, maxEmbellish, patsPerEnd, minDiscPatSize, includeTranspositions);
		System.out.println("Discovering");
		result = discovery.run();
		ProtoClusteringTask clustering = new ProtoClusteringTask(tf, result, gs, mr,
				maxRepsPerSong, subCoverageLeeway);
		System.out.println("Clustering");
		clustered = clustering.run();
		ProtoSelectionTask selection = new ProtoSelectionTask(tf, clustered, mr,
				pwsBias, isBias, numIterations, skipCost);
		System.out.println("Selecting from " + clustered.getPatterns().size());
		selected = selection.run();
		selectedInclusive = selection.getFilledResult();
		normalization.revert();
		System.out.println("Finished");
		return getSelected();
	}
	
	public Collection<PatternSet> getSelectedInclusive(){
		selectedInclusive = truncateIDs(selectedInclusive);
		selectedInclusive = includeTimeRange(selectedInclusive);
		return selectedInclusive.splitBySongs();
	}
	
	public Collection<PatternSet> getSelected(){
		selected = truncateIDs(selected);
		selected = includeTimeRange(selected);
		return selected.splitBySongs();
	}
	
	public Collection<PatternSet> getClustered(){
		clustered = truncateIDs(clustered);
		clustered = includeTimeRange(clustered);
		return clustered.splitBySongs();
	}
	
	public void prepNotes(){
		for(Song s: tf.getSongs()){
			List<Note> notes = s.getNotes();
			Collections.sort(notes);
			for(int i = notes.size()-1; i >= 0; i--){
				Note n = notes.get(i);
				n.setSong(s);
				n.setNoteID(i);
			}
		}
	}
	
	public PatternSet includeTimeRange(PatternSet ps){
		for(Pattern p: ps.getPatterns()){
			for(PatternOccurrence o: p.getOccurrences()){
				List<Note> notes = o.getSong().getNotes();
				for(int i = o.getStartNote().getNoteID()+1; i < o.getEndNote().getNoteID(); i++){
					if(!o.getNotes().contains(notes.get(i))) o.addNote(notes.get(i));
				}
			}
		}
		return ps;
	}
	
	public PatternSet truncateIDs(PatternSet ps){
		int pi = 0;
		for(Pattern p: ps.getPatterns()){
			p.forcePatternID(pi++);
			int oi = 0;
			for(PatternOccurrence o: p.getOccurrences()){
				o.forceOccurrenceID(oi++);
			}
		}
		return ps;
	}
	
	public String getParams(){
		return gs + "_" + rr + "_" + ss + "_" + mr + "_" + 
				maxEmbellish + "_" + patsPerEnd + "_" + minDiscPatSize + "_" + includeTranspositions + "_" + 
				maxRepsPerSong + "_" + subCoverageLeeway + "_" + 
				pwsBias + "_" + isBias + "_" + numIterations + "_" + skipCost;
	}
}
