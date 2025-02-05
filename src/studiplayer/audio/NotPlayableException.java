package studiplayer.audio;

public class NotPlayableException extends Exception {
	private String pathname, msg;
	private Throwable t;
	
	public NotPlayableException() {
	}
	
	// Constructor with msg
	public NotPlayableException(String pathname, String msg) {
		super(msg);
		this.pathname = pathname;
		this.msg = msg;
	}
	
	// Constructor with a cause (throwable)
	public NotPlayableException(String pathname, Throwable t) {
		super(t);
		this.pathname = pathname;
		this.t = t;
	}
	
	
	// Constructor with both cause and msg
	public NotPlayableException(String pathname, String msg, Throwable t) {
		super(msg, t);
		this.pathname = pathname;
		this.msg = msg;
		this.t = t;
	}
	
	@Override
	public String toString() {
		return "studiplayer.audio.NotPlayableException: " + getMessage() + " on " + this.pathname;
	}
}