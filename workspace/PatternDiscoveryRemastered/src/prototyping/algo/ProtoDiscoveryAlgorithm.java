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
	
	public ProtoDiscoveryAlgorithm(TuneFamily tf, double gs, double rr, double ss, double mr){
		this.tf = tf;
		this.gs = gs;
		this.rr = rr;
		this.ss = ss;
		this.mr = mr;
	}
	
	public Collection<PatternSet> run(){
		System.out.println("Starting");
		prepNotes();
		PatternSet result;
		ProtoNormalizationTask normalization = new ProtoNormalizationTask(tf, gs, ss);
		System.out.println("Normalizing");
		normalization.computeNormalization();
		normalization.normalize();
		ProtoDiscoveryTask discovery = new ProtoDiscoveryTask(tf, gs, Math.sqrt(rr));
		System.out.println("Discovering");
		result = discovery.run();
		ProtoClusteringTask clustering = new ProtoClusteringTask(tf, result, gs, mr);
		System.out.println("Clustering");
		result = clustering.run();
		ProtoSelectionTask selection = new ProtoSelectionTask(tf, result, mr);
		System.out.println("Selecting from " + result.getPatterns().size());
		result = selection.run();
		normalization.revert();
		result = truncateIDs(result);
		result = includeTimeRange(result);
		System.out.println("Finished");
		return result.splitBySongs();
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
		return gs + "_" + rr + "_" + ss + "_" + mr;
	}
}
