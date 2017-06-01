package nl.tue.cs.patterndiscovery.view.util.player;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/*
 * Class for playng a single note, terminates either when done playing or when interrupted.
 */
public class NoteRunnable implements Runnable {
	
	private int midiNumber;
	private int velocity;
	private int duration;

	public MidiChannel mChannel;
	
	public NoteRunnable(int midiNumber, int velocity, int duration, MidiChannel mChannel){
		this.midiNumber = midiNumber;
		this.velocity = velocity;
		this.duration = duration;
		this.mChannel = mChannel;
	}
	
	public void run(){
	    mChannel.noteOn(midiNumber, velocity);//On channel 0, play note number 60 with velocity 100 
		try { Thread.sleep(duration); // wait time in milliseconds to control duration
		} catch( InterruptedException e ) { }
		mChannel.noteOff(midiNumber);//turn of the note
	      
	}
}
