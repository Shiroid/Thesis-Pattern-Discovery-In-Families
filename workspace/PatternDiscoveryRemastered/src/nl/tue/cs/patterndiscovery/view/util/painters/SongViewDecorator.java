package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class SongViewDecorator {

	protected SongViewDecorator child;
	
	public SongViewDecorator(){
	}
	
	public SongViewDecorator(SongViewDecorator child){
		this.child = child;
	}
	
	public void setChild(SongViewDecorator child){
		this.child = child;
	}
	
	public void appendChild(SongViewDecorator child){
		if(this.child != null) this.child.appendChild(child);
		else this.child = child;
	}

	public void paintDecoration(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch){
		if(child != null) child.paintDecoration(g, cs, mst, scaleH, scaleV, offsetH, offsetV, ignorePitch);
	}
}
