package studiplayer.audio;

import java.util.Map;
import studiplayer.basic.TagReader;


public class TaggedFile extends SampledFile {
    private String album;
	
	public TaggedFile() throws NotPlayableException {
		super();
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		readAndStoreTags();
	}
	
	
	public void readAndStoreTags() throws NotPlayableException {		
		try {
			Map<String, Object> tagMap = TagReader.readTags(getPathname());
			
			if (tagMap.containsKey("author")) {
				setAuthor(((String) tagMap.get("author")).trim());
			}
			
			if (tagMap.containsKey("title")) {
				setTitle(((String) tagMap.get("title")).trim());
			}
			
			if (tagMap.containsKey("album")) {
				setAlbum(((String) tagMap.get("album")).trim());
			}
			
			if (tagMap.containsKey("duration")) {
				setDuration(((long) tagMap.get("duration")));
			}
			
		} catch (Exception e) {
			throw new NotPlayableException();
		}
	}
	
	public String getAlbum() {
		return this.album;
	}
	
    public void setAlbum(String album) {
		this.album = album;
	}
	
	
	
	
	

	public String toString() {
		if (getAlbum() != null) { 
			return super.toString() + " - " + getAlbum() + " - " + formatDuration();
		} else {
			return super.toString() + " - " + formatDuration();
		}
	}

}
