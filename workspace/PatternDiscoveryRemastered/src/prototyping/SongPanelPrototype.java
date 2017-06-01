package prototyping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JPanel;

import nl.tue.cs.patterndiscovery.filemanager.NoteReader;
import nl.tue.cs.patterndiscovery.model.*;

public class SongPanelPrototype extends JPanel {

	private Color[] colors = new Color[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, 
			Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK};
	
	private Song song;
	
	private double scale = 30;

	public void setSong(File f){
		song = new SongNoteSaving();
		String[] lines = new String[0];
		try {
			lines = new String(Files.readAllBytes(Paths.get(f.getCanonicalPath()))).split("[\n\r]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < lines.length; i++){
			song.addNote(NoteReader.readLispNoteLossless(lines[i]));
		}
	}

	public void addPatternSet(File f){
		if(song != null){
				
			String[] lines = new String[0];
			try {
				lines = new String(Files.readAllBytes(Paths.get(f.getCanonicalPath()))).split("[\n\r]");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PatternSet s = new PatternSet();
			song.addPatternSet(s);
			
			Pattern p = new Pattern();
			PatternOccurrence o = new PatternOccurrence();
			for(int i = 0; i < lines.length; i++){
				if(!lines[i].isEmpty()){
					switch(lines[i].charAt(0)){
					case 'p':
						p = new Pattern(Integer.parseInt(lines[i].substring(lines[i].length() - 1)));
						s.addPattern(p);
						break;
					case 'o':
						o = new PatternOccurrence(Integer.parseInt(lines[i].substring(lines[i].length() - 1)));
						p.addOccurrence(o);
						break;
					default:
						o.addNote(NoteReader.readPlainNote(lines[i]));
					}
				}
			}
		}
	}
	
	@Override
	 public void paintComponent(Graphics g) {
        super.paintComponent(g);   
        
        g.setColor(Color.BLACK);
        if(song == null) return;
        for(Note n: song){
	        g.fillRect(
	        		(int) ((n.getOnset() - song.getStartTime())*scale), 
	        		(int) ((song.getMaxPitch() - n.getChromaticPitch())*scale),
	        		(int) (scale*n.getDuration() -1),  
	        		(int) (scale -1) 
	        		);
        }
        if(song.getPatternSets().isEmpty()) return;
        for(Pattern p: song.getPatternSets().get(song.getPatternSets().size()-1).getPatterns()){
        	g.setColor(colors[p.getPatternID() % colors.length]);
        	for(PatternOccurrence o: p.getOccurrences()){
    	        g.drawRect(
    	        		(int) ((o.getStartTime() - song.getStartTime())*scale + p.getPatternID()), 
    	        		(int) ((song.getMaxPitch() - o.getMaxPitch())*scale + p.getPatternID()),
    	        		(int) ((o.getEndTime() - o.getStartTime())*scale),  
    	        		(int) ((o.getMaxPitch()+1 - o.getMinPitch())*scale)
    	        		);
        		}
        	}
        
        for(Pattern p: song.getPatternSets().get(song.getPatternSets().size()-1).getPatterns()){
        	g.setColor(colors[p.getPatternID() % colors.length]);
        	for(PatternOccurrence o: p.getOccurrences()){
    	        for(Note n: o.getNotes()){
    		        g.fillRect(
    		        		(int) ((n.getOnset() - song.getStartTime())*scale + p.getPatternID()), 
    		        		(int) ((song.getMaxPitch() - n.getChromaticPitch())*scale + p.getPatternID()),
    		        		(int) (scale*n.getDuration() -1),  
    		        		(int) (scale -1) 
    		        		);
    	        }
        	}
        }
        setPreferredSize(new Dimension((int) ((song.getEndTime() - song.getStartTime())*scale), (int) ((song.getMaxPitch()+1 - song.getMinPitch())*scale)));
		
	}
}
