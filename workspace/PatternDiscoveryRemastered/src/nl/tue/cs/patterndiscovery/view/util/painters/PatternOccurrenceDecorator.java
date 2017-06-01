package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class PatternOccurrenceDecorator extends SongViewDecorator {

	protected boolean doFill;
	protected int alpha = 100;
	
	public PatternOccurrenceDecorator(SongViewDecorator child, boolean doFill){
		super(child);
		this.doFill = doFill;
	}
	
	public PatternOccurrenceDecorator(SongViewDecorator child, boolean doFill, int alpha){
		super(child);
		this.doFill = doFill;
		this.alpha = alpha;
	}
	
	public void paintDecoration(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch){
		
		super.paintDecoration(g, cs, mst, scaleH, scaleV, offsetH, offsetV, ignorePitch);
		
		if(mst.patternSet != null){
			if(doFill){
				if(mst.pattern != null)
					paintPatternFill(g, cs, mst,
							scaleH, scaleV, offsetH, offsetV, ignorePitch, mst.pattern);
				else{
					for(Pattern p: mst.patternSet){
						paintPatternFill(g, cs, mst,
							scaleH, scaleV, offsetH, offsetV, ignorePitch, p);
					}
				}
			}
			if(mst.pattern != null)
				paintPatternOutline(g, cs, mst,
						scaleH, scaleV, offsetH, offsetV, ignorePitch, mst.pattern);
			else{
				for(Pattern p: mst.patternSet){
					paintPatternOutline(g, cs, mst,
						scaleH, scaleV, offsetH, offsetV, ignorePitch, p);
				}
			}
		}
	}
	
	protected void paintPatternFill(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch, Pattern p){
		for(PatternOccurrence o: p){
			Color c = cs.getColor(SubTreeFactory.createFromMST(mst, o, p), 0);
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
			g.setColor(c);
			g.fillRect(offsetH + (int) (scaleH * (o.getStartTime() - mst.song.getStartTime())), 
					offsetV + (int) (scaleV * (ignorePitch?0:(mst.song.getMaxPitch() - o.getMaxPitch()))), 
					(int) (scaleH * (o.getEndTime() - o.getStartTime())), 
					(int) (scaleV * (ignorePitch?1:(o.getMaxPitch() - o.getMinPitch() + 1))));
		}
	}
	
	protected void paintPatternOutline(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch, Pattern p){
		for(PatternOccurrence o: p){
			g.setColor(cs.getColor(SubTreeFactory.createFromMST(mst, o, p), 0).brighter());
			g.drawRect(offsetH + (int) (scaleH * (o.getStartTime() - mst.song.getStartTime())), 
					offsetV + (int) (scaleV * (ignorePitch?0:(mst.song.getMaxPitch() - o.getMaxPitch()))), 
					(int) (scaleH * (o.getEndTime() - o.getStartTime())), 
					(int) (scaleV * (ignorePitch?1:(o.getMaxPitch() - o.getMinPitch() + 1))));
		}
	}
}
