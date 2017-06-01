package nl.tue.cs.patterndiscovery.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tue.cs.patterndiscovery.model.*;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;
import nl.tue.cs.patterndiscovery.view.util.painters.SongViewDecorator;

public class PianoRollPanel extends PatternViewerPanel implements MouseListener{

	protected ModelSubTree mst;
	protected ColorChooser cs;
	protected SongViewDecorator svd;
	
	protected MouseEvent lastMouseEvent;
	
	public PianoRollPanel(ModelSubTree mst, ColorChooser cs, SongViewDecorator svd, double scaleH, double scaleV){
		this.mst = mst;
		this.cs = cs;
		this.svd = svd;
		this.scaleH = scaleH;
		this.scaleV = scaleV;
		this.setPreferredSize(new Dimension((int) (scaleH * (mst.song.getEndTime() - mst.song.getStartTime())), 
				(int) (scaleV * (mst.song.getMaxPitch() - mst.song.getMinPitch() + 1))));
		this.addMouseListener(this);
	}
	
	public ColorChooser getCs() {
		return cs;
	}

	public SongViewDecorator getSvd() {
		return svd;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.svd.paintDecoration(g, cs, mst, scaleH, scaleV, 0, 0, false);
	}
	
	public Note getNoteFromPos(int x, int y){
		int pitch = yToPitch(y);
		double time = xToTime(x);
		for(Note n: mst.song){
			if(n.getChromaticPitch() == pitch &&
					n.getOnset() <= time &&
					n.getOnset() + n.getDuration() >= time)
					return n;
		}
		return null;
	}
	
	public List<ModelSubTree> getOcurrencesFromPos(int x, int y){
		ArrayList<ModelSubTree> result = new ArrayList<ModelSubTree>();
		int pitch = yToPitch(y);
		double time = xToTime(x);
		if(mst.pattern != null){
			for(PatternOccurrence o: mst.pattern){
				if(o.getStartTime() <= time && o.getEndTime() >= time &&
						o.getMaxPitch() >= pitch && o.getMinPitch() <= pitch)
					result.add(SubTreeFactory.createFromMST(mst, o, mst.pattern));
			}
		}
		else{
			for(Pattern p: mst.patternSet){
				for(PatternOccurrence o: p){
					if(o.getStartTime() <= time && o.getEndTime() >= time &&
							o.getMaxPitch() >= pitch && o.getMinPitch() <= pitch)
						result.add(SubTreeFactory.createFromMST(mst, o, p));
				}
			}
		}
		return result;
	}
	
	public double xToTime(int x){
		return ((double) x) / scaleH + mst.song.getStartTime();
	}
	
	public int yToPitch(int y){
		return (int) (mst.song.getMaxPitch() - Math.floor(((double) y) / scaleV));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)){
			HighlightTracker ht = cs.getHighlightTracker();
			
			Note note = getNoteFromPos(e.getX(), e.getY());
			if(note != null)
				ht.toggleHighlight(
					SubTreeFactory.createFromMST(mst, note));
			
			List<ModelSubTree> occurrences = getOcurrencesFromPos(e.getX(), e.getY());
			ModelSubTree smallest = occurrences.isEmpty()?null:occurrences.get(0);
			for(ModelSubTree m: occurrences){
				if(m.patternOccurrence.getEndTime() - m.patternOccurrence.getStartTime() < 
						smallest.patternOccurrence.getEndTime() - smallest.patternOccurrence.getStartTime())
					smallest = m;
			}
			ht.toggleHighlight(smallest);
			
			if(note == null && occurrences.isEmpty()) ht.unhighlightAll();
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ModelSubTree> getViewedMSTs() {
		List<ModelSubTree> result = new ArrayList<ModelSubTree>();
		result.add(mst);
		return result;
	}

	@Override
	public ModelSubTree getHighlightedMST() {
		return cs.getHighlightTracker().getHighlightedTree();
	}
}
