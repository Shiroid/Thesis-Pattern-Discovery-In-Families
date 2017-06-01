package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Color;
import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class NoteBlockPainter extends NotePainter{
	
	@Override
	public void paintNote(Graphics g, ModelSubTree mst, ColorChooser cs, double scaleH, double scaleV,
			int offsetH, int offsetV, int timesPainted, boolean ignorePitch) {	
		if(mst.note != null && mst.song != null){
			
			g.setColor(cs.getColor(mst, timesPainted));
			g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
					offsetV + (ignorePitch?0:(int) (scaleV * (mst.song.getMaxPitch() - mst.note.getChromaticPitch()))), 
					(int) (scaleH * mst.note.getDuration()), 
					(int) scaleV);
			//Add outline
			g.setColor(g.getColor().darker());
			g.drawRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
					offsetV + (ignorePitch?0:(int) (scaleV * (mst.song.getMaxPitch() - mst.note.getChromaticPitch()))), 
					(int) (scaleH * mst.note.getDuration()), 
					(int) scaleV);
		}
	}

}
