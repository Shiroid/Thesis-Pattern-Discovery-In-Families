package nl.tue.cs.patterndiscovery.view.util.painters;

import java.awt.Graphics;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.colors.ColorChooser;

public class SimpleSongDecorator extends SongViewDecorator{

	private NoteBlockPainter notePainter = new NoteBlockPainter();
	
	public SimpleSongDecorator(SongViewDecorator child){
		super(child);
	}
	
	public void paintDecoration(Graphics g, ColorChooser cs, ModelSubTree mst,
			double scaleH, double scaleV, int offsetH, int offsetV, boolean ignorePitch){
		
		super.paintDecoration(g, cs, mst, scaleH, scaleV, offsetH, offsetV, ignorePitch);
		
		if(mst.song != null){
			ModelSubTree base = SubTreeFactory.createDSetToSong(mst.dataSet, mst.tuneFamily, mst.song);
			for(Note n: mst.song){
				notePainter.paintNote(g, SubTreeFactory.createFromMST(base, n), cs, scaleH, scaleV, offsetH, offsetV, 0, ignorePitch);
			}
		}
	}
}
