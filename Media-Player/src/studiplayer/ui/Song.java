package studiplayer.ui;

import studiplayer.audio.AudioFile;

public class Song {
	private AudioFile af;
	private String interpret, titel, album, laenge;

	public Song(AudioFile af, String interpret, String titel, String album, String laenge) {
		this.af = af; 				// AudioFile
		this.interpret = interpret; // author
		this.titel = titel; 		// title
		this.album = album; 		// album
		this.laenge = laenge; 		// duration
	}

	public AudioFile getAudioFile() {
		return af;
	}

	public String getInterpret() {
		return interpret;
	}

	public String getTitel() {
		return titel;
	}

	public String getAlbum() {
		return album;
	}

	public String getLaenge() {
		return laenge;
	}
	
}	
