package nl.tue.cs.patterndiscovery.view.util.player;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class NotePlayer {

	private final int msPerMinute = 60*1000;
	
	private int numNotes = 10;
	private int currentNote = 0;
	protected Thread[] notes = new Thread[numNotes];
	
	//Milliseconds per count in music, defaults to 120bpm.
	private int msPerCount = 500;
	
	private int velocity = 100;
	
	private Synthesizer midiSynth;
	private Instrument[] instr;
	private MidiChannel[] mChannels;
	
	public NotePlayer(){
        try {
            /* Create a new Sythesizer and open it. Most of 
             * the methods you will want to use to expand on this 
             * example can be found in the Java documentation here: 
             * https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/Synthesizer.html
             */
			midiSynth = MidiSystem.getSynthesizer();
	        midiSynth.open();

	        //get and load default instrument and channel lists
	        instr = midiSynth.getDefaultSoundbank().getInstruments();
	        mChannels = midiSynth.getChannels();

	        midiSynth.loadInstrument(instr[0]);//load an instrument
	        
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/*
	 * Play a note with the given midi number and duration in counts.
	 */
	public void PlayNote(int midiNumber, double duration){
		NoteRunnable nr = new NoteRunnable(midiNumber, velocity, (int) (duration * msPerCount), mChannels[0]);
        notes[currentNote] = new Thread(nr);
        notes[currentNote].start();
        currentNote = (currentNote + 1)%numNotes;
	}
	
	/*
	 * Stops all notes from playing, fails if too many notes are played at a time.
	 */
	public void stopPlaying(){
		for(int i = 0; i < numNotes; i++){
			if(notes[i] != null) notes[i].interrupt();
			notes[i] = null;
		}
	}
	
	public void setMsPerCount(int ms){
		msPerCount = ms;
	}
	
	public void setBPM(int bpm){
		msPerCount = msPerMinute / bpm;
	}
	
	public int getMsPerCount(){
		return msPerCount;
	}
	
	public int getBPM(){
		return msPerMinute / msPerCount;
	}
}
