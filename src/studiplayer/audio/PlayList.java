package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayList implements Iterable<AudioFile> {
	private LinkedList<AudioFile> list;
	private SortCriterion sortCriterion;
	private String search;
	private ControllablePlayListIterator iterator;
	
	// Default CTOR
	public PlayList() {
		this.list = new LinkedList<>();
		this.sortCriterion = SortCriterion.DEFAULT;
		newIterator();
	}
	
	public PlayList(String m3uPathname) {
		this();
		loadFromM3U(m3uPathname);
		newIterator();
	}
	
	
	public LinkedList<AudioFile> getList() {
		return this.list;
	}
	
	public void add(AudioFile file) {
		this.list.add(file);
		newIterator();
	}
	
	public void remove(AudioFile file) {
		this.list.remove(file);
		newIterator();
	}
	
	public int size() {
		return this.list.size();
	}
	
	
	public AudioFile currentAudioFile() {
		if (this.size() == 0) {
			return null;
		}
		
		// if hasNext() is false, reset the counter.
		if (!iterator.hasNext()) {
			iterator.reset();
		} 

		return iterator.getCurrentSong();
	}
	
	public void nextSong() {
		if (this.size() > 0) {
			iterator.next();
			
			// reset if iterator has reached the end of list
			if (!iterator.hasNext()) {
                iterator.reset();
            }
		}
	}
	
	
	
	
	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		
		try {
			// create the file if it does not exist, otherwise reset the file and open it for writing
			writer = new FileWriter(pathname);
			
			// To iterate and write all pathnames
			for (AudioFile audiofile : this.list) {
				writer.write(audiofile.getPathname() + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		} finally {
			try {
				// close the file writing back all buffers
				writer.close();
			} catch (Exception e) {
				// ignore exception
			}
		}
	}
	

	public void loadFromM3U(String pathname) {
		// Reset count and list
		this.list = new LinkedList<>();
		this.iterator.reset();
		
		Scanner scanner = null;
		
		try {
			// open the file for reading
			scanner = new Scanner(new File(pathname));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			while (scanner.hasNextLine()) { // check if there is next line
				String line = scanner.nextLine();
				if (line.startsWith("#") || line.trim().equals("")) {
					continue; // pass; nothing to be done
				} else {
					// Try adding valid music files to playlist, else, none is added
					try {
						add(AudioFileFactory.createAudioFile(line)); 
					} catch (Exception e) {
						// ignore if failed
					}
				}
				
			}
			
			// close scanner
			scanner.close();
		}
	}
	
	
	
	
	public void jumpToAudioFile(AudioFile file) {
		this.iterator.jumpToAudioFile(file);
	}
	
	
	public SortCriterion getSortCriterion() {
		return this.sortCriterion;
	}
	
	public String getSearch() {
		return this.search;
	}
	
	public void setSortCriterion(SortCriterion sort) {
	    this.sortCriterion = sort;
	    newIterator();
	}
	
	public void setSearch(String value) {
	    this.search = value;
	    newIterator();
	}

	
	
	
	
	@Override
    public ControllablePlayListIterator iterator() {
        return new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
    }
	
	public void newIterator() {
		this.iterator = new ControllablePlayListIterator(this.list, this.search, this.sortCriterion);
//		this.iterator.reset();
	}
	
	
	
	@Override
	public String toString() {
		if (this.list == null || this.list.isEmpty()) {
            return "[]";
        }
		
		StringBuilder playListString = new StringBuilder();
		
		for (AudioFile af : this.list) {
			playListString.append(af).append(", ");
		}
		
		// remove last ", "
		int lastIndex = playListString.lastIndexOf(", ");
		
		
		return "[" + playListString.substring(0, lastIndex) + "]";
	}

	
    
}



