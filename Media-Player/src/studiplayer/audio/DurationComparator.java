package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
	
	public int compare (AudioFile o1, AudioFile o2) {
		long duration1, duration2;
		
		if (o1 instanceof SampledFile) {
			duration1 = ((SampledFile) o1).getDuration();
		} else {
			duration1 = 0;
		}
		
		if (o2 instanceof SampledFile) {
			duration2 = ((SampledFile) o2).getDuration();
		} else {
			duration2 = 0;
		}
		
		
		
		if (duration1 < duration2) {
			return -1;
		} else if (duration1 > duration2) {
			return 1;
		} else {
			return 0;
		}
	}
}
