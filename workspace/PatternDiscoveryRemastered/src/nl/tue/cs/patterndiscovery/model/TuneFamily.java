package nl.tue.cs.patterndiscovery.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.tue.cs.patterndiscovery.model.util.CollectionElementGetter;

public class TuneFamily implements Iterable<Song>{
	
	//ID of this tune family
	protected String familyName;
	
	private ArrayList<Song> songs;
	
	public TuneFamily(){
		this.songs = new ArrayList<Song>();
	}
	
	public TuneFamily(String name){
		this.songs = new ArrayList<Song>();
		setFamilyName(name);
	}
	
	public TuneFamily(Collection<Song> songs){
		this.songs = new ArrayList<Song>(songs);
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		if(this.familyName == null)
			this.familyName = familyName;
	}

	/*
	 * Adds a song to this tune family.
	 */
	public void addSong(Song song){
		if(!songs.contains(song)) songs.add(song);
	}
	
	/*
	 * Gets the song described by the input, if present.
	 */
	public Song getSong(Song song){
		return CollectionElementGetter.getFromCollection(songs, song);
	}
	
	/*
	 * Returns the string representation in Collins Lisp format
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return familyName;
	}
	
	@Override
	public boolean equals(Object o){
		if(familyName == null) return false;
		if(o instanceof TuneFamily){
			return (familyName.equals(((TuneFamily) o).getFamilyName()));
		}
		return false;
	}

	@Override
	public Iterator<Song> iterator() {
		return songs.iterator();
	}
	
	public List<Song> getSongs(){
		return songs;
	}
}
