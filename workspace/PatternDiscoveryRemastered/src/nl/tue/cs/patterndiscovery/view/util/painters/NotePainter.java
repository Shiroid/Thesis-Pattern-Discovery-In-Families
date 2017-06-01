package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;

public abstract class NotePainter {

	public void paintNote(Graphics g, ModelSubTree mst, ColorChooser cs, double scaleH, double scaleV,
			int offsetH, int offsetV, int timesPainted){
		this.paintNote(g, mst, cs, scaleH, scaleV, offsetH, offsetV, timesPainted, false);
	}
	
	public abstract void paintNote(Graphics g, ModelSubTree mst, ColorChooser cs, double scaleH, double scaleV,
			int offsetH, int offsetV, int timesPainted, boolean ignorePitch);
	
}
