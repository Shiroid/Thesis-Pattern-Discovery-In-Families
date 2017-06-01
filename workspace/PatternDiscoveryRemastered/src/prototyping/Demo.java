package prototyping;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.patterngrouping.PatternIDHasher;
import nl.tue.cs.patterndiscovery.config.Config;
import nl.tue.cs.patterndiscovery.config.ConfigSetter;
import nl.tue.cs.patterndiscovery.filemanager.PatternSetReader;
import nl.tue.cs.patterndiscovery.filemanager.SongReader;
import nl.tue.cs.patterndiscovery.model.*;
import nl.tue.cs.patterndiscovery.view.ContainerFiller;
import nl.tue.cs.patterndiscovery.view.FamilyPatternPanel;
import nl.tue.cs.patterndiscovery.view.PanelContainer;
import nl.tue.cs.patterndiscovery.view.PatternViewerFrame;
import nl.tue.cs.patterndiscovery.view.PatternViewerPanel;
import nl.tue.cs.patterndiscovery.view.PianoRollPanel;
import nl.tue.cs.patterndiscovery.view.SongPlayerPanel;
import nl.tue.cs.patterndiscovery.view.util.ContainerTracker;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;
import nl.tue.cs.patterndiscovery.view.util.colors.DistinctColorChooser;
import nl.tue.cs.patterndiscovery.view.util.colors.HeatColorChooser;
import nl.tue.cs.patterndiscovery.view.util.highlights.NoteTracker;
import nl.tue.cs.patterndiscovery.view.util.highlights.PatternTracker;
import nl.tue.cs.patterndiscovery.view.util.highlights.PlayingPatternTracker;
import nl.tue.cs.patterndiscovery.view.util.painters.NoteBlockPainter;
import nl.tue.cs.patterndiscovery.view.util.painters.PatternNoteDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.PatternOccurrenceDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.SimpleSongDecorator;
import nl.tue.cs.patterndiscovery.view.util.painters.SongViewDecorator;

public class Demo {
	
	public static void main(String args[]){
		JFrame frame = new JFrame("Demo");
		Config.initConfig();
		while(!Config.isInitialized()){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Integer> intArgs = new ArrayList<Integer>();

		for(int i = 0; i < args.length; i++){
			try{
				int arg = Integer.parseInt(args[i]);
				intArgs.add(arg);
			} catch(Exception e){
				
			}
		}
		
		int[] viewDistribution = new int[]{2, 3, 5};
		if(!intArgs.isEmpty()){
			viewDistribution = new int[intArgs.size()];
			for(int i = 0; i < viewDistribution.length; i++){
				viewDistribution[i] = intArgs.get(i);
			}
		}
        new PatternViewerFrame(1280, 720, 30, viewDistribution, true);
	}
}
