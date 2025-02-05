package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	public int compare (AudioFile o1, AudioFile o2) {
		String author1 = o1.getAuthor();
		String author2 = o2.getAuthor();
		
		// If there is no author tag
		if (author1 == null) {
			author1 = "";
		}
		
		if (author2 == null) {
			author2 = "";
		}
		
		return author1.compareTo(author2);
	}
}
