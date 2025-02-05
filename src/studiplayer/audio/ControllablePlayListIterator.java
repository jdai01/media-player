package studiplayer.audio;

import java.util.Iterator;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> audioFileList;
	private int currentIndex = 0;

	private int nextCalled = 0; // next method called? default: false (0)
	

	
	public ControllablePlayListIterator(List<AudioFile> list) {
		this.audioFileList = list;
		this.reset();
	}
	

	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
		this.audioFileList = new ArrayList<>(list);
	    
	    // Apply filtering based on search string
		if (search == null || search.isEmpty()) {
			// ignore
		} else {
//			ArrayList<AudioFile> filterList = new ArrayList<AudioFile>();
//			
//			for (AudioFile af : list) {
//				boolean match = false; // default
//				
//				// Check for Author
//				if (af.getAuthor().contains(search)) {
//					match = true;
//				}
//				
//				// Check for Title
//				if (af.getTitle().contains(search)) {
//					match = true;
//				}
//				
//				// Check for Album, only if af is TaggedFile
//				if (af instanceof TaggedFile) {
//					String albumSearch = ((TaggedFile) af).getAlbum();
//					if (albumSearch != null && albumSearch.contains(search)) {
//						match = true;
//					}
//				}
//				
//				// If match, add to filter list, else ignore
//				if (match) {
//					filterList.add(af);
//				}
//				
//			}
//			
//			// Cast filterList to audioFileList
//			this.audioFileList = filterList;
			
			final String searchString = search.toLowerCase(); 
			
			this.audioFileList = this.audioFileList.stream()
	                .filter(af -> af.getAuthor() != null && af.getAuthor().toLowerCase().contains(searchString) ||	 // Search for Author
	                			af.getTitle() != null && af.getTitle().toLowerCase().contains(searchString) ||			 // Search for Title
	                			(af instanceof TaggedFile && ((TaggedFile) af).getAlbum() != null && ((TaggedFile) af).getAlbum().toLowerCase().contains(searchString)) && af.getTitle() != null)  // Search for Album
	                .collect(Collectors.toList());
			
			
		}

		// Apply sorting based on sortCriterion
        Comparator<AudioFile> comparator = null;
        switch (sort) {
            case AUTHOR:
                comparator = new AuthorComparator();
                break;
            case TITLE:
                comparator = new TitleComparator();
                break;
            case ALBUM:
                comparator = new AlbumComparator();
                break;
            case DURATION:
                comparator = new DurationComparator();
                break;
            default:
                break;
        }
        if (comparator != null) {
            this.audioFileList.sort(comparator);
        }
        
        this.reset();
	    
	}

	
	public AudioFile jumpToAudioFile(AudioFile file) {
		int audioFileIndex = audioFileList.indexOf(file);
		
		if (audioFileIndex == -1) { // if audiofile is not found
			return null;
		}
		
		this.currentIndex = audioFileIndex + nextCalled;
		return file;
	}

	
	@Override
	public boolean hasNext() {
		return currentIndex < audioFileList.size();
	}
	
	@Override
	public AudioFile next() {
		if (!hasNext()) {
			this.reset();
		}
		
		AudioFile nextSong = this.getCurrentSong();
		currentIndex++;
		nextCalled = 1; // next is called
		return nextSong;
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}

	public AudioFile getCurrentSong() {
		return this.audioFileList.get(currentIndex);
	}
	
	public void reset() {
		this.currentIndex = 0;
	}
	
	public List<AudioFile> getAudioFileList() {
	    return this.audioFileList;
	}
	

}
