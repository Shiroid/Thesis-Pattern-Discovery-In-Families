package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class PatternNoteDecorator extends SongViewDecorator{

	protected NotePainter notePainter;
	
	public PatternNoteDecorator(SongViewDecorator child){
		super(child);
		this.notePainter = new NoteFlagPainter(); //Default to simple note block painter
	}

	public PatternNoteDecorator(SongViewDecorator child, NotePainter notePainter){
		super(child);
		this.notePainter = notePainter;
	}
	
	public void paintDecoration(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch){
		
		super.paintDecoration(g, cs, mst, scaleH, scaleV, offsetH, offsetV, ignorePitch);

		boolean maxChanged = true;

		for(int runs = 0; runs < 2 && maxChanged; runs++){
			Map<Note, Integer> countMap = new HashMap<Note, Integer>();
			Map<Note, ArrayList<Integer>> patIDMap = new HashMap<Note, ArrayList<Integer>>();
			if(mst.patternSet != null){
				if(mst.pattern != null){
					for(PatternOccurrence o: mst.pattern){
						for(Note n: o){
							if(countMap.containsKey(n)){
								if(!patIDMap.get(n).contains(mst.pattern.getPatternID())) {
									countMap.put(n, countMap.get(n) + 1);
									patIDMap.get(n).add(mst.pattern.getPatternID());
								}
							} else {
								countMap.put(n, 1);
								patIDMap.put(n, new ArrayList<Integer>());
								patIDMap.get(n).add(mst.pattern.getPatternID());
							}
							notePainter.paintNote(g, SubTreeFactory.createFromMST(mst, n, o, mst.pattern),
									cs, scaleH, scaleV, offsetH, offsetV, countMap.get(n), ignorePitch);
						}
					}
				}
				else {
					for(Pattern p: mst.patternSet){
						for(PatternOccurrence o: p){
							for(Note n: o){
								if(countMap.containsKey(n)){
									if(!patIDMap.get(n).contains(p.getPatternID())) {
										countMap.put(n, countMap.get(n) + 1);
										patIDMap.get(n).add(p.getPatternID());
									}
								} else {
									countMap.put(n, 1);
									patIDMap.put(n, new ArrayList<Integer>());
									patIDMap.get(n).add(p.getPatternID());
								}
								notePainter.paintNote(g, SubTreeFactory.createFromMST(mst, n, o, p),
										cs, scaleH, scaleV, offsetH, offsetV, countMap.get(n), ignorePitch);
							}
						}
					}
				}
			}
			
			int maxCount = 0;
			for(Integer i: countMap.values()){
				if(i > maxCount) maxCount = i;
			}
			maxChanged = cs.getNumColors() != maxCount;
			cs.increaseNumColors(maxCount);
		}
	}
}
