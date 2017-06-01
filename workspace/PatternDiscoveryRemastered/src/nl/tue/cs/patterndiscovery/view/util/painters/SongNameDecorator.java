package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class SongNameDecorator extends SongViewDecorator{
	
	private boolean doPatternDetails = false;
	
	public SongNameDecorator(SongViewDecorator child){
		super(child);
	}
	
	public SongNameDecorator(SongViewDecorator child, boolean doPatternDetails){
		super(child);
		this.doPatternDetails = doPatternDetails;
	}
	
	public void paintDecoration(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch){
		
		super.paintDecoration(g, cs, mst, scaleH, scaleV, offsetH, offsetV, ignorePitch);
		
		if(mst.song != null){
			g.setColor(new Color(0,0,0,140));
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) scaleV));
			String name = mst.song.getSongName();
			if(mst.patternSet != null){
				name += "/" + mst.patternSet;
			}
			if(mst.pattern != null){
				name += "/" + mst.pattern;
			}
			g.drawString(name, offsetH, (int) scaleV + offsetV);
		}
	}

}
