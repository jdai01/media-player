package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	
	public WavFile() throws NotPlayableException {
		super();
	}
	
	public WavFile(String path) throws NotPlayableException {
		super(path);
		readAndSetDurationFromFile();
	}
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
		try {
			WavParamReader reader = new WavParamReader();
			reader.readParams(getPathname());
			
			// Get and set frameRate
			setFrameRate(reader.getFrameRate());
			
			// Get and set frameRate
			setNumberOfFrames(reader.getNumberOfFrames());
			
			// Compute and set duration
			long durationFromFrames = computeDuration(getNumberOfFrames(), getFrameRate());
			setDuration(durationFromFrames);
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), "Parameters cannot be read.");
		}
		
	}

	public String toString() {
		return super.toString() + " - " + formatDuration();
	}

	public static long computeDuration(long numberOfFrames, float frameRate) {
	    return (long) (((float) numberOfFrames) / frameRate * 1000000);
	}
	


}