package prototyping.algo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.TuneFamily;

public class ProtoNormalizationTask {
	
	protected TuneFamily tf;
	protected double gs;
	protected double ss;

	protected Map<Song, Double> timeMults;
	protected Map<Song, Integer> diatonicAdds;
	
	public ProtoNormalizationTask(TuneFamily tf, double gs, double ss){
		this.tf = tf;
		this.gs = gs;
		this.ss = ss;
	}
	
	public void computeNormalization(){
		ProtoDiscoveryTask discovery = new ProtoDiscoveryTask(tf, gs, ss);
		PatternSet samples = discovery.run();

		//Compute normalization weights
		Map<Song, Map<Song, Map<Integer, Integer>>> pitchWeights = new HashMap<Song, Map<Song, Map<Integer, Integer>>>();
		Map<Song, Map<Song, Map<Double, Integer>>> tempoWeights = new HashMap<Song, Map<Song, Map<Double, Integer>>>();
		for(Pattern p: samples){
			List<PatternOccurrence> occurrences = p.getOccurrences();
			PatternOccurrence o1 = occurrences.get(0);
			PatternOccurrence o2 = occurrences.get(1);
			Song s1 = o1.getSong();
			Song s2 = o2.getSong();
			List<Note> no1 = o1.getNotes();
			List<Note> no2 = o2.getNotes();
			if(!pitchWeights.containsKey(s1)){
				pitchWeights.put(s1, new HashMap<Song, Map<Integer, Integer>>());
				tempoWeights.put(s1, new HashMap<Song, Map<Double, Integer>>());
			}
			if(!pitchWeights.containsKey(s2)){
				pitchWeights.put(s2, new HashMap<Song, Map<Integer, Integer>>());
				tempoWeights.put(s2, new HashMap<Song, Map<Double, Integer>>());
			}
			if(!pitchWeights.get(s2).containsKey(s1)){
				pitchWeights.get(s2).put(s1, new HashMap<Integer, Integer>());
				tempoWeights.get(s2).put(s1, new HashMap<Double, Integer>());
			}
			if(!pitchWeights.get(s1).containsKey(s2)){
				pitchWeights.get(s1).put(s2, new HashMap<Integer, Integer>());
				tempoWeights.get(s1).put(s2, new HashMap<Double, Integer>());
			}
			Map<Integer, Integer> diaMap1 = pitchWeights.get(s1).get(s2);
			Map<Integer, Integer> diaMap2 = pitchWeights.get(s1).get(s2);
			Map<Double, Integer> temMap1 = tempoWeights.get(s1).get(s2);
			Map<Double, Integer> temMap2 = tempoWeights.get(s1).get(s2);
			int pitchDiff = no1.get(0).getDiatonicPitch() - no2.get(0).getDiatonicPitch();
			for(int i = no1.size()-1; i > 0; i--){
				double tempoDiff = (no1.get(i).getOnset() - no1.get(i-1).getOnset())/(no2.get(i).getOnset() - no2.get(i-1).getOnset());
				if(diaMap1.containsKey(pitchDiff))
					diaMap1.put(pitchDiff, diaMap1.get(pitchDiff) + 1); 
				else diaMap1.put(pitchDiff, 1); 
				if(diaMap2.containsKey(-pitchDiff))
					diaMap2.put(-pitchDiff, diaMap2.get(pitchDiff) + 1); 
				else diaMap2.put(-pitchDiff, 1); 
				if(temMap1.containsKey(tempoDiff))
					temMap1.put(tempoDiff, temMap1.get(tempoDiff) + 1); 
				else temMap1.put(tempoDiff, 1); 
				if(temMap2.containsKey(1/tempoDiff))
					temMap2.put(1/tempoDiff, temMap2.get(1/tempoDiff) + 1); 
				else temMap2.put(1/tempoDiff, 1); 
			}
		}

		//Determine pairwise normalizations
		Map<Song, Map<Song, Integer>> pitchNorms = new HashMap<Song, Map<Song, Integer>>();
		Map<Song, Map<Song, Double>> tempoNorms = new HashMap<Song, Map<Song, Double>>();
		for(Song s1: pitchWeights.keySet()){
			HashMap<Song, Integer> s1pn = new HashMap<Song, Integer>();
			pitchNorms.put(s1, s1pn);
			HashMap<Song, Double> s1tn = new HashMap<Song, Double>();
			tempoNorms.put(s1, s1tn);
			for(Song s2: pitchWeights.get(s1).keySet()){
				Map<Integer, Integer> pitchWeightsPair = pitchWeights.get(s1).get(s2);
				Map<Double, Integer> tempoWeightsPair = tempoWeights.get(s1).get(s2);
				int maxPWeight = 0;
				int maxTWeight = 0;
				for(Integer i: pitchWeightsPair.keySet()){
					if(pitchWeightsPair.get(i) > maxPWeight){
						maxPWeight = pitchWeightsPair.get(i);
						s1pn.put(s2, i);
					}
				}
				for(Double i: tempoWeightsPair.keySet()){
					if(tempoWeightsPair.get(i) > maxTWeight){
						maxTWeight = tempoWeightsPair.get(i);
						s1tn.put(s2, i);
					}
				}
			}
		}
		//Fill up missing pairs
		for(Song s: tf.getSongs()){
			pitchNorms.put(s, new HashMap<Song, Integer>());
			for(Song s2: tf.getSongs()){
				pitchNorms.get(s).put(s2, 0);
				tempoNorms.get(s).put(s2, 1.0);
			}
		}

		//Determine anchor
		Set<Song> commonSongs = new HashSet<Song>();
		int maxConsistency = 0;
		int consistency = 0;
		for(Song s1: pitchNorms.keySet()){
			consistency = 0;
			Map<Song, Double> s1tm = tempoNorms.get(s1);
			for(Song s2: s1tm.keySet()){
				if(s1tm.get(s2) == 1) consistency++;
			}
			if(consistency > maxConsistency){
				commonSongs = new HashSet<Song>();
				maxConsistency = consistency;
			}
			if(consistency == maxConsistency) commonSongs.add(s1);
		}
		maxConsistency = 0;
		Song anchor = null;
		for(Song s1: pitchNorms.keySet()){
			consistency = 0;
			for(Song s2: pitchNorms.keySet()){
				for(Song s3: pitchNorms.keySet()){
					if(pitchNorms.containsKey(s1) && pitchNorms.containsKey(s2) && pitchNorms.containsKey(s3)){
						if(pitchNorms.get(s1).get(s2) + pitchNorms.get(s2).get(s3) + pitchNorms.get(s3).get(s1) == 0 &&
								tempoNorms.get(s1).get(s2) * tempoNorms.get(s2).get(s3) * tempoNorms.get(s3).get(s1) == 1)
							consistency++;
					}
				}
			}
			if(consistency >= maxConsistency){
				maxConsistency = consistency;
				anchor = s1;
			}
		}

		//Finalize the normalization mapping
		timeMults = tempoNorms.get(anchor);
		diatonicAdds = pitchNorms.get(anchor);
		if(timeMults == null) timeMults = new HashMap<Song, Double>();
		if(diatonicAdds == null) diatonicAdds = new HashMap<Song, Integer>();
		for(Song s: tf.getSongs()){
			if(!timeMults.containsKey(s)){
				timeMults.put(s, 1.0);
				diatonicAdds.put(s, 0);
			}
		}

	}
	
	public void normalize(){
		for(Song s: tf.getSongs()){
			double curTM = timeMults.get(s);
			int curDA = diatonicAdds.get(s);
			for(Note n: s.getNotes()){
				n.setNormalization(curTM, curDA, 0);
			}
		}
	}
	
	public void revert(){
		for(Song s: tf.getSongs()){
			for(Note n: s.getNotes()){
				n.revertNormalization();
			}
		}
	}

}
