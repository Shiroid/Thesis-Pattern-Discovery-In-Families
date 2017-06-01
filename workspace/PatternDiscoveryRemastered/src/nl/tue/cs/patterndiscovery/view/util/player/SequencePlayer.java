package nl.tue.cs.patterndiscovery.view.util.player;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import nl.tue.cs.patterndiscovery.model.Note;
import nl.tue.cs.patterndiscovery.model.NoteSequence;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.RepaintListener;

public class SequencePlayer implements Runnable {
	
	protected NoteSequence seq;
	protected ArrayList<RepaintListener> listeners;
	
	//The position of the player cursor, in song time.
	protected double cursorPos = 0.0;
	//Thread playing the song.
	protected Thread playThread;
	protected boolean isPlaying;
	protected ArrayList<Note> playingNotes;
	protected NotePlayer notePlayer;
	
	protected boolean isRunning = false;
	
	public SequencePlayer(){
		this.playingNotes = new ArrayList<Note>();
		this.notePlayer = new NotePlayer();
		this.listeners = new ArrayList<RepaintListener>();
	}
	
	public SequencePlayer(NoteSequence seq){
		setSequence(seq);
		this.playingNotes = new ArrayList<Note>();
		this.notePlayer = new NotePlayer();
		this.listeners = new ArrayList<RepaintListener>();
	}

	public void setSequence(NoteSequence seq){
		this.seq = seq;
		this.cursorPos = seq.getStartTime();
	}

	public NoteSequence getSequence(){
		return seq;
	}
	
	
	
	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public void setBPM(int bpm){
		notePlayer.setBPM(bpm);
	}
	
	public void addListener(RepaintListener l){
		listeners.add(l);
	}
	
	public void removeListener(RepaintListener l){
		listeners.remove(l);
	}
	
	protected void notifyListeners(){
		for(RepaintListener l: listeners){
			l.repaintRequest();
		}
	}
	
	public void setCursorPos(double cursorPos) {
		this.cursorPos = cursorPos;
	}
	
	public double getCursorPos() {
		return cursorPos;
	}
	
	public void Play(){
		isPlaying = true;
		playThread = new Thread(this);
		playThread.setPriority(Thread.MAX_PRIORITY);
		playThread.start();
	}
	
	public void Pause(){
		isPlaying = false;
		playingNotes.clear();
		notePlayer.stopPlaying();
	}
	
	public void Stop(){
		isPlaying = false;
		if(seq != null)
			setCursorPos(seq.getStartTime());
		playingNotes.clear();
		notePlayer.stopPlaying();
		notifyListeners();
		
		//Wait for run to finish, find more elegant method!
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void MoveCursor(double newPos){
		notePlayer.stopPlaying();
		playingNotes.clear();
		setCursorPos(newPos);
		notifyListeners();
	}
	
	@Override
	public void run() {
		isRunning = true;
		Instant last = Instant.now();
		
		while (isPlaying){
			cursorPos += ((double) Duration.between(last, Instant.now()).toMillis())/notePlayer.getMsPerCount();
	        last = Instant.now();
			notifyListeners();
			
			//Play notes
			for(Note n: seq){
				if(cursorPos < n.getOnset() + n.getDuration() &&
						cursorPos > n.getOnset()){
					if(!playingNotes.contains(n)){// add if it should be playing and is not
						playingNotes.add(n);
						notePlayer.PlayNote(n.getChromaticPitch(), n.getOnset() - cursorPos + n.getDuration());
					}
				} else playingNotes.remove(n); // remove if it should not be playing
			}
			
	        try { Thread.sleep(10); // wait 10 ms before continuing, giving other processes time
	        } catch( InterruptedException e ) { }
	        
	        if(cursorPos > seq.getEndTime()){
	        	Stop();
	        }
		}
		notePlayer.stopPlaying();
		notifyListeners();
		isRunning = false;
	}
	
	public boolean isRunning(){
		return isRunning;
	}

}
