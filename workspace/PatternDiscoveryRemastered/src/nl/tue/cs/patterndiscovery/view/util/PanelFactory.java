package nl.tue.cs.patterndiscovery.view.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;

import nl.tue.cs.patterndiscovery.filemanager.PatternSetReader;
import nl.tue.cs.patterndiscovery.filemanager.SongReader;
import nl.tue.cs.patterndiscovery.model.DataSet;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.TuneFamily;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternIDHasher;
import nl.tue.cs.patterndiscovery.view.FamilyPatternPanel;
import nl.tue.cs.patterndiscovery.view.PanelContainer;
import nl.tue.cs.patterndiscovery.view.PatternViewerPanel;
import nl.tue.cs.patterndiscovery.view.PianoRollPanel;
import nl.tue.cs.patterndiscovery.view.SongPlayerPanel;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;
import nl.tue.cs.patterndiscovery.view.util.colors.DistinctColorChooser;
import nl.tue.cs.patterndiscovery.view.util.colors.HeatColorChooser;
import nl.tue.cs.patterndiscovery.view.util.highlights.PlayingPatternTracker;
import nl.tue.cs.patterndiscovery.view.util.painters.NoteBlockPainter;
import nl.tue.cs.patterndiscovery.view.util.painters.PatternNoteDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.PatternOccurrenceDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.SimpleSongDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.SongNameDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.SongViewDecorator;

public class PanelFactory {

	public static PanelContainer putPatternHeatMap(ModelSubTree mst, PanelContainer pc){
		SongViewDecorator svd = new SongNameDecorator(new PatternNoteDecorator(
				new SimpleSongDecorator(null), new NoteBlockPainter()), true);
		ColorChooser cc = new HeatColorChooser(new PlayingPatternTracker(true), new PatternIDHasher());
		return putPianoRollPanel(mst, pc, svd, cc);
	}

	public static PanelContainer putPatternHighlightRoll(ModelSubTree mst, PanelContainer pc){
		SongViewDecorator svd = new SongNameDecorator(new PatternNoteDecorator(new SimpleSongDecorator(
				new PatternOccurrenceDecorator(null, true))), true);
		ColorChooser cc = new DistinctColorChooser(new PlayingPatternTracker(true), new PatternIDHasher());
		return putPianoRollPanel(mst, pc, svd, cc);
	}
	
	public static PanelContainer putPianoRollPanel(ModelSubTree mst, PanelContainer pc, 
			SongViewDecorator svd, ColorChooser cc){
		pc.removeAll();
		pc.setLayout(new BorderLayout());
		
		PatternViewerPanel pvp = new PianoRollPanel(mst, cc, svd, pc.getScale(), pc.getScale());
		SongPlayerPanel spp = new SongPlayerPanel(mst, pc.getScale());
		
		pc.add(spp, BorderLayout.PAGE_START);
		pc.add(pvp, BorderLayout.CENTER);
		Dimension pvpSize = pvp.getPreferredSize();
		Dimension sppSize = spp.getPreferredSize();
		pc.setPreferredSize(new Dimension(Math.max((int) pvpSize.getWidth(), (int) sppSize.getWidth()), 
				(int) (pvpSize.getHeight() + sppSize.getHeight())));
		
		pc.revalidate();
		pc.repaint();
		return pc;
	}

	public static PanelContainer putPatternHighlightFamily(Collection<ModelSubTree> msts, PanelContainer pc){
		SongViewDecorator svd = new SongNameDecorator(new PatternOccurrenceDecorator(null, true), true);
		ColorChooser cc = new DistinctColorChooser(new PlayingPatternTracker(true), new PatternIDHasher());
		return putFamilyPatternPanel(msts, pc, svd, cc);
	}

	public static PanelContainer putFamilyHeatMap(Collection<ModelSubTree> msts, PanelContainer pc){
		SongViewDecorator svd = new SongNameDecorator(new PatternNoteDecorator(
				new SimpleSongDecorator(null), new NoteBlockPainter()), true);
		ColorChooser cc = new HeatColorChooser(new PlayingPatternTracker(true), new PatternIDHasher());
		return putFamilyPatternPanel(msts, pc, svd, cc);
	}
	
	public static PanelContainer putFamilyPatternPanel(Collection<ModelSubTree> msts, PanelContainer pc, 
			SongViewDecorator svd, ColorChooser cc){
		pc.removeAll();
		pc.setLayout(new BorderLayout());

		PatternViewerPanel pvp = new FamilyPatternPanel(msts, cc, svd, pc.getScale(), pc.getScale());

		pc.add(pvp, BorderLayout.CENTER);
		pc.setPreferredSize(pvp.getPreferredSize());
		
		pc.revalidate();
		pc.repaint();
		return pc;
	}
	
	public static PanelContainer copyPanel(PanelContainer from, PanelContainer to){
		for(Component comp: from.getComponents()){
			if(comp instanceof PianoRollPanel){
				if(!from.getViewedMSTs().isEmpty()){
					return putPianoRollPanel(from.getViewedMSTs().get(0), to, 
							((PianoRollPanel) comp).getSvd(), ((PianoRollPanel) comp).getCs());
				} else return to;
			}
			if(comp instanceof FamilyPatternPanel){
				return putFamilyPatternPanel(from.getViewedMSTs(), to, 
						((FamilyPatternPanel) comp).getSvd(), ((FamilyPatternPanel) comp).getCs());
			}
		}
		return to;
	}
	
	public static PanelContainer swapPanel(PanelContainer p1, PanelContainer p2){
		PanelContainer temp = new PanelContainer();
		copyPanel(p2, temp);
		copyPanel(p1, p2);
		copyPanel(temp, p1);
		return p2;
	}
}
