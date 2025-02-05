package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	public int compare (AudioFile o1, AudioFile o2) {
		String title1 = o1.getTitle();
		String title2 = o2.getTitle();
		
		// If there is no title tag
		if (title1 == null) {
			title1 = "";
		}
		
		if (title2 == null) {
			title2 = "";
		}
		
		return title1.compareTo(title2);
	}
}
