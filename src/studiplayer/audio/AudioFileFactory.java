package studiplayer.audio;

public class AudioFileFactory {
	
	public static AudioFile createAudioFile(String path) throws NotPlayableException {
		int extIndex = path.lastIndexOf("."); // check for extension
		
		// If invalid extension
		if (extIndex < 0) {
			throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
		}
		
		// Get valid extension
		String ext = path.substring(extIndex+1).toLowerCase();
		
		if (ext.equals("wav")) {
			return new WavFile(path);
	    } else if (ext.equals("ogg") || ext.equals("mp3")) {
	    	return new TaggedFile(path);
	    } else {
	    	throw new NotPlayableException();
	    }
	}
		
}
