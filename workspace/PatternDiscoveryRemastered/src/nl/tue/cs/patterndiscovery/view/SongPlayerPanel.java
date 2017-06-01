package nl.tue.cs.patterndiscovery.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.RepaintListener;
import nl.tue.cs.patterndiscovery.view.util.player.NotePlayer;
import nl.tue.cs.patterndiscovery.view.util.player.SequencePlayer;

public class SongPlayerPanel extends PatternViewerPanel implements MouseListener, RepaintListener{
	
	protected ModelSubTree mst;
	protected double scaleH = 1;
	protected int sizeV = 20;
	
	
	protected SequencePlayer seqPlayer;
	
	public SongPlayerPanel(ModelSubTree mst, double scaleH){
		this.seqPlayer = new SequencePlayer();
		this.seqPlayer.addListener(this);
		
		setSong(mst);
		setScaleH(scaleH);
		this.addMouseListener(this);
	}
	
	public void setBPM(int bpm){
		seqPlayer.setBPM(bpm);
	}

	public ModelSubTree getSong() {
		return mst;
	}

	public void setSong(ModelSubTree mst) throws IllegalArgumentException{
		if(mst.song != null) {
			this.mst = mst;
			seqPlayer.setSequence(mst.song);
			correctSize();
		} else throw new IllegalArgumentException("ModelSubTree for SongPlayerPanel must contain a Song.");
	}

	public double getScaleH() {
		return scaleH;
	}

	public void setScaleH(double scaleH) {
		this.scaleH = scaleH;
		correctSize();
	}
	
	public void setWidth(double width){
		scaleH *= width/this.getPreferredSize().getWidth();
		correctSize();
	}
	
	protected void correctSize(){
		this.setPreferredSize(new Dimension(
				(int) (scaleH * (this.mst.song.getEndTime() - this.mst.song.getStartTime())),
				sizeV));
		repaint();
	}

	public double getCursorPos() {
		return seqPlayer.getCursorPos();
	}

	public void setCursorPos(double cursorPos) {
		seqPlayer.setCursorPos(cursorPos);;
	}
	
	public void setCursorFromMouse(int x){
		setCursorPos((x/scaleH) + mst.song.getStartTime());
	}
	
	public double getCursorFromMouse(int x){
		return (x/scaleH) + mst.song.getStartTime();
	}
	
	public void Play(){
		seqPlayer.Play();
	}
	
	public void Pause(){
		seqPlayer.Pause();
	}
	
	public void Stop(){
		seqPlayer.Stop();
	}
	
	public void MoveCursor(double newPos){
		seqPlayer.MoveCursor(newPos);
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
		if(SwingUtilities.isLeftMouseButton(arg0)){
			//Move cursor
			if(arg0.getX() > sizeV * 2){
				MoveCursor(getCursorFromMouse(arg0.getX()));
			}
			//Press Stop
			else if(arg0.getX() > sizeV)
				Stop();
			//Toggle Play/Pause
			else if(seqPlayer.isPlaying())
				Pause();
			else Play();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//Draw the cursor
		g.setColor(Color.CYAN);
		g.fillRect((int) ((seqPlayer.getCursorPos()-0.5 - mst.song.getStartTime()) * scaleH), 0,
				(int) scaleH, (int) this.getPreferredSize().getHeight());
		g.setColor(Color.RED);
		g.drawLine((int) ((seqPlayer.getCursorPos() - mst.song.getStartTime()) * scaleH), 0,
				(int) ((seqPlayer.getCursorPos() - mst.song.getStartTime()) * scaleH), (int) this.getPreferredSize().getHeight());
		
		//Draw the control buttons
		g.setColor(Color.BLACK);
		g.drawOval(0, 0, sizeV, sizeV);
		g.drawOval(sizeV, 0, sizeV, sizeV);
		
		//Draw the button details
		if(seqPlayer.isPlaying()){//Pause
			g.fillRect((sizeV)/4, sizeV/4, sizeV/8, sizeV/2);
			g.fillRect((sizeV*5)/8, sizeV/4, sizeV/8, sizeV/2);
		} else {//Play
			g.fillPolygon(new int[]{sizeV/4, (sizeV*3)/4, sizeV/4}, new int[]{sizeV/4, sizeV/2, (sizeV*3)/4}, 3);
		}
		//Stop
		g.fillRect((sizeV)*5/4, sizeV/4, sizeV/2, sizeV/2);
	}

	@Override
	public void repaintRequest() {
		this.repaint();
	}

	@Override
	public List<ModelSubTree> getViewedMSTs() {
		List<ModelSubTree> result = new ArrayList<ModelSubTree>();
		result.add(mst);
		return result;
	}

	@Override
	public ModelSubTree getHighlightedMST() {
		return null;
	}
	
}
