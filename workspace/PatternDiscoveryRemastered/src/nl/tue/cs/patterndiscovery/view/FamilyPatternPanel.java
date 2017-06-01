package nl.tue.cs.patterndiscovery.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;
import nl.tue.cs.patterndiscovery.view.util.highlights.HighlightTracker;
import nl.tue.cs.patterndiscovery.view.util.painters.SongViewDecorator;

public class FamilyPatternPanel extends PatternViewerPanel implements MouseListener{

	protected List<ModelSubTree> msts;
	protected ColorChooser cs;
	protected SongViewDecorator svd;
	
	protected double globalStartTime, globalEndTime;
	
	public FamilyPatternPanel(Collection<ModelSubTree> msts, ColorChooser cs, SongViewDecorator svd, double scaleH, double scaleV){
		this.msts = new ArrayList<ModelSubTree>(msts);
		this.cs = cs;
		this.svd = svd;
		this.scaleH = scaleH;
		this.scaleV = scaleV;
		this.correctSize();
		this.addMouseListener(this);
	}
	
	public void correctSize(){
		for(ModelSubTree mst: msts){
			globalStartTime = Math.min(mst.song.getStartTime(), globalStartTime);
			globalEndTime = Math.max(mst.song.getEndTime(), globalEndTime);
		}
		this.setPreferredSize(new Dimension((int) (scaleH*(globalEndTime - globalStartTime)),(int) (scaleV*msts.size())));
	}
	
	public ColorChooser getCs() {
		return cs;
	}

	public SongViewDecorator getSvd() {
		return svd;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int songsDrawn = 0;
		for(ModelSubTree mst: msts){
			this.svd.paintDecoration(g, cs, mst, scaleH, scaleV, (int) (scaleH*(mst.song.getStartTime() - globalStartTime)), (int) (scaleV*songsDrawn), true);
			songsDrawn++;
		}
	}
	
	public Note getNoteFromPos(int x, int y){
		ModelSubTree mst = yToMST(y);
		double time = xToTime(x);
		for(Note n: mst.song){
			if(n.getOnset() <= time && n.getOnset() + n.getDuration() >= time)
					return n;
		}
		return null;
	}
	
	public ModelSubTree getNoteMSTFromPos(int x, int y){
		ModelSubTree mst = yToMST(y);
		double time = xToTime(x);
		if(mst != null){
			for(Note n: mst.song){
				if(n.getOnset() <= time && n.getOnset() + n.getDuration() >= time)
						return SubTreeFactory.createFromMST(mst, n);
			}
		}
		return null;
	}
	
	public List<ModelSubTree> getOcurrencesFromPos(int x, int y){
		ArrayList<ModelSubTree> result = new ArrayList<ModelSubTree>();
		ModelSubTree mst = yToMST(y);
		double time = xToTime(x);
		if(mst != null){
			if(mst.pattern != null){
				for(PatternOccurrence o: mst.pattern){
					if(o.getStartTime() <= time && o.getEndTime() >= time)
						result.add(SubTreeFactory.createFromMST(mst, o, mst.pattern));
				}
			}
			else {
				for(Pattern p: mst.patternSet){
					for(PatternOccurrence o: p){
						if(o.getStartTime() <= time && o.getEndTime() >= time)
							result.add(SubTreeFactory.createFromMST(mst, o, p));
					}
				}
			}
		}
		return result;
	}
	
	public double xToTime(int x){
		return ((double) x) / scaleH + globalStartTime;
	}
	
	public ModelSubTree yToMST(int y){
		int songsDrawn = 0;
		for(ModelSubTree mst: msts){
			if(songsDrawn * scaleV < y && (songsDrawn + 1) * scaleV > y)
				return mst;
			songsDrawn++;
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
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
			
			ModelSubTree mst = getNoteMSTFromPos(e.getX(), e.getY());
			ht.toggleHighlight(mst);
			
			List<ModelSubTree> occurrences = getOcurrencesFromPos(e.getX(), e.getY());
			ModelSubTree smallest = occurrences.isEmpty()?null:occurrences.get(0);
			for(ModelSubTree m: occurrences){
				if(m.patternOccurrence.getEndTime() - m.patternOccurrence.getStartTime() < 
						smallest.patternOccurrence.getEndTime() - smallest.patternOccurrence.getStartTime())
					smallest = m;
			}
			ht.toggleHighlight(smallest);
			
			if(mst == null && occurrences.isEmpty()) ht.unhighlightAll();
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ModelSubTree> getViewedMSTs() {
		return new ArrayList<ModelSubTree>(msts);
	}

	@Override
	public ModelSubTree getHighlightedMST() {
		return cs.getHighlightTracker().getHighlightedTree();
	}
}
