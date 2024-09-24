package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	private boolean isPaused = false; 

	private long duration;
	private float frameRate;
	private long numberOfFrames;
	
	public SampledFile() throws NotPlayableException  {
		super();
	}
	
	public SampledFile(String path) throws NotPlayableException {
		super(path);
	}
	
	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(getPathname());
			isPaused = false;
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), "File is unable to play:");
		}
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
		if (isPaused) { // paused, to be resumed
			isPaused = false;
		} else { // playing, to be paused
			isPaused = true;
		}
	}
	

	public void stop() {
		BasicPlayer.stop();
	}
	
	public String formatDuration() {
		return timeFormatter(getDuration());
	}
	
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
	}
//	
	public static String timeFormatter(long timeInMicroSeconds) {
		if (timeInMicroSeconds < 0L) {
			throw new RuntimeException("Time value underflows format.");
		} 
		if (timeInMicroSeconds >= 6000000000L) {
			throw new RuntimeException("Time value overflows format.");
		} 
		
		long totalSeconds = timeInMicroSeconds / 1000000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d", minutes, seconds);
	}

	

	public long getDuration() {
		return this.duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public float getFrameRate() {
		return this.frameRate;
	}
	
	public long getNumberOfFrames() {
		return this.numberOfFrames;
	}
	
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}
	
	public void setNumberOfFrames(long numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}
}
