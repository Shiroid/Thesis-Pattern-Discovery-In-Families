package nl.tue.cs.patterndiscovery.view;

import java.awt.Component;
import java.util.List;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.JPanel;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;

public abstract class PatternViewerPanel extends JPanel{

	protected double scaleH, scaleV;
	
	@Override
	public Component add(Component comp){
		for(MouseListener l: this.getMouseListeners()){
			comp.addMouseListener(l);
		}
		return super.add(comp);
	}
	
	public abstract List<ModelSubTree> getViewedMSTs();
	
	public abstract ModelSubTree getHighlightedMST();
}
