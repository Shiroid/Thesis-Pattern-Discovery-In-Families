package nl.tue.cs.patterndiscovery.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.ContainerTracker;
import nl.tue.cs.patterndiscovery.view.util.PanelFactory;

public class PanelContainer extends PatternViewerPanel implements MouseListener{
	
	private ContainerFiller filler;
	private double scale = 20;

	public PanelContainer(){
		super();
		this.addMouseListener(this);
		this.filler = null;
	}
	
	public PanelContainer(double scale){
		super();
		this.addMouseListener(this);
		this.filler = null;
		this.scale = scale;
	}
	
	public double getScale() {
		return scale;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(SwingUtilities.isRightMouseButton(arg0)){
			PanelContainer clicked = ContainerTracker.getLastClicked();
			boolean canSwap = clicked != this && clicked != null;
			switch(ContainerTracker.getClickMode()){
			
			case FILL:
				ModelSubTree fillerMST = this.getHighlightedMST();
				if(fillerMST == null){
					List<ModelSubTree> vmsts = this.getViewedMSTs();
					if(!vmsts.isEmpty())
						fillerMST = vmsts.get(0);
				}
				if(filler == null){
			        filler = new ContainerFiller(this, fillerMST);
				} else if(filler.isVisible()){
					filler.toFront();
				} else{
			        filler = new ContainerFiller(this, fillerMST);
				}
				break;
				
			case COPYTO:
				if(canSwap){
					PanelFactory.copyPanel(this, clicked);
				}
				break;
				
			case COPYFROM:
				if(canSwap){
					PanelFactory.copyPanel(clicked, this);
				}
				break;
				
			case SWAP:
				if(canSwap){
					PanelFactory.swapPanel(this, clicked);
				}
				break;
				
			default:
				break;
			}
			ContainerTracker.setClickMode(ContainerTracker.ClickMode.FILL);
			ContainerTracker.setLastClicked(this);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Component add(Component comp){
		comp.addMouseListener(this);
		return super.add(comp);
	}
	
	@Override
	public Component add(String name, Component comp){
		comp.addMouseListener(this);
		return super.add(name, comp);
	}
	
	@Override
	public Component add(Component comp, int index){
		comp.addMouseListener(this);
		return super.add(comp, index);
	}
	
	@Override
	public void add(Component comp, Object constraints){
		comp.addMouseListener(this);
		super.add(comp, constraints);
	}
	
	@Override
	public void add(Component comp, Object constraints, int index){
		comp.addMouseListener(this);
		super.add(comp, constraints, index);
	}

	@Override
	public List<ModelSubTree> getViewedMSTs() {
		for(Component comp: this.getComponents()){
			if(comp instanceof PatternViewerPanel)
				return ((PatternViewerPanel) comp).getViewedMSTs();
		}
		return new ArrayList<ModelSubTree>();
	}

	@Override
	public ModelSubTree getHighlightedMST() {
		for(Component comp: this.getComponents()){
			if(comp instanceof PatternViewerPanel){
				ModelSubTree hmst = ((PatternViewerPanel) comp).getHighlightedMST();
				if(hmst != null)
					return hmst;
			}
		}
		return null;
	}
	
}
