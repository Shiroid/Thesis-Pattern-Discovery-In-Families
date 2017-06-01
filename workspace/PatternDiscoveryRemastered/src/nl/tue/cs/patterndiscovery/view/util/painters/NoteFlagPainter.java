package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Color;
import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class NoteFlagPainter extends NotePainter{

	@Override
	public void paintNote(Graphics g, ModelSubTree mst, ColorChooser cs, double scaleH, double scaleV,
			int offsetH, int offsetV, int timesPainted, boolean ignorePitch) {
		if(mst.note != null && mst.song != null){
			g.setColor(cs.getColor(mst, timesPainted));
			
			switch(timesPainted){
			case 0:
			case 1: //Draw full flag
				g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
						offsetV + (int) (scaleV * (ignorePitch?0:(mst.song.getMaxPitch() - mst.note.getChromaticPitch()))), 
						(int) (scaleH * mst.note.getDuration()), 
						(int) scaleV);
				g.setColor(g.getColor().darker());
				g.drawRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
						offsetV + (int) (scaleV * (ignorePitch?0:(mst.song.getMaxPitch() - mst.note.getChromaticPitch()))), 
						(int) (scaleH * mst.note.getDuration()), 
						(int) scaleV);
				break;
			case 2: //Draw lower half
				g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
						offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)), 
						(int) (scaleH * mst.note.getDuration()), 
						(int) (scaleV*0.5));
				g.setColor(g.getColor().darker());
				g.drawRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
						offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)), 
						(int) (scaleH * mst.note.getDuration()), 
						(int) (scaleV*0.5));
				break;
			case 3: //Draw left triangle
				g.fillPolygon(new int[]{
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.5 * mst.note.getDuration())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime()))
						},
						new int[]{
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()))),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 1))
						}, 3);
				g.setColor(g.getColor().darker());
				g.drawPolygon(new int[]{
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.5 * mst.note.getDuration())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime()))
						},
						new int[]{
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()))),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 1))
						}, 3);
				break;
			case 4: //Draw right triangle
				g.fillPolygon(new int[]{
							offsetV + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + mst.note.getDuration())),
							offsetV + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.5 * mst.note.getDuration())),
							offsetV + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + mst.note.getDuration()))
						},
						new int[]{
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()))),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 1))
						}, 3);
				g.setColor(g.getColor().darker());
				g.fillPolygon(new int[]{
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + mst.note.getDuration())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.5 * mst.note.getDuration())),
							offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + mst.note.getDuration()))
						},
						new int[]{
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()))),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.5)),
							offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 1))
						}, 3);
				break;
			case 5: //Draw middle rectangle
				g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.3 * mst.note.getDuration())), 
						offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.3)), 
						(int) (scaleH * mst.note.getDuration() * 0.4), 
						(int) (scaleV*0.4));
				g.setColor(g.getColor().darker());
				g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime() + 0.3 * mst.note.getDuration())), 
						offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.3)), 
						(int) (scaleH * mst.note.getDuration() * 0.4), 
						(int) (scaleV*0.4));
				break;
			default: //Mark as excess
				g.setColor(Color.WHITE);
				g.fillRect(offsetH + (int) (scaleH * (mst.note.getOnset() - mst.song.getStartTime())), 
						offsetV + (int) (scaleV * ((ignorePitch?0:mst.song.getMaxPitch() - mst.note.getChromaticPitch()) + 0.45)), 
						(int) (scaleH * mst.note.getDuration()), 
						(int) (scaleV*0.1));
				break;
			}
		}
	}

}
