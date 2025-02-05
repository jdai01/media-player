package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	public int compare (AudioFile o1, AudioFile o2) {
		if (!(o1 instanceof TaggedFile) && !(o2 instanceof TaggedFile)) {
            return 0;
        }
		
        String album1 = (o1 instanceof TaggedFile) ? ((TaggedFile) o1).getAlbum() : "";
        String album2 = (o2 instanceof TaggedFile) ? ((TaggedFile) o2).getAlbum() : "";

        if (album1 == null && album2 == null) {
            return 0;
        } else if (album1 == null || album1.isEmpty()) {
            return -1;
        } else if (album2 == null || album2.isEmpty()) {
            return 1;
        } else {
            return album1.compareTo(album2);
        }
		
	}
	
}
