package studiplayer.audio;

import java.io.File;

public abstract class AudioFile {
    private String pathname, filename, author, title;
    private Boolean windows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0; // Get OS of running system
    

    // Default constructor, initialised
    public AudioFile() {
    	
    }

    // Main constructor
    public AudioFile(String path) throws NotPlayableException {
        parsePathname(path);
        parseFilename(this.filename);

        // Check if file is readable
        File file = new File(this.pathname);
        if (!file.canRead()) {
            throw new NotPlayableException(this.pathname, "File is not readable: " + this.pathname);
        }
    }
	
    public void parsePathname(String path) {
		
		if (path.trim().isEmpty()) {
			// If path is empty
			pathname = "";
			filename = "";
		} else if (path.trim().equals("-")) {
			// If path is invalid
			pathname = "-";
			filename = "-";
		} else {
			// Path is not empty 
			
			// Normalise path separators
			path = path.replaceAll("/+", "/");
			path = path.replaceAll("\\\\+", "\\\\");
			
			// Handle drive letter and separator based on os type
			if (windows != true) {
				// For Linux
				if (Character.isAlphabetic(path.charAt(0))  // with driver
						&& path.charAt(1) == ':') {
					path = "/" + path.charAt(0) + path.substring(2);
				}
				path = path.replaceAll("\\\\", "/");
			} else {
				// For Windows
				if (path.charAt(0) == ':' // with driver
						&& Character.isAlphabetic(path.charAt(1)) 
						&& path.charAt(2) == ':') {
					path = path.charAt(1) + ":\\" + path.substring(3);
				}
				path = path.replaceAll("/", "\\\\");
			}
			
			pathname = path.trim(); // Save in attribute
			
			// Search file name
			// Determine position last separator in pathname
			int sepLastIndex;
			if (windows) {
				sepLastIndex = pathname.lastIndexOf("\\");
			} else {
				sepLastIndex = pathname.lastIndexOf("/");
			}

			if (sepLastIndex >= 0) {
				// If there is a separator, and obtain the last filename after the separator
				filename = pathname.substring(sepLastIndex+1).trim();
			} else if (sepLastIndex == pathname.length()) {
				// If there is no filename
				filename = "";
			} else {
				// If there is no separator
				filename = pathname;
			}
		}
	}
	

    public void parseFilename(String filename) {
		if (filename == "" || filename == " ") {
			// If filename is empty
			author = "";
			title = "";
		} else if (filename == "-"){
			// If filename is "-"
			author = "";
			title = "-";
			
		} else if (!filename.contains(".")) { 
			// If filename does not contain "." extension
			author = "";
			title = filename;
		} else if (!filename.contains(" - ")) { 
			// If filename does not contain " - "
			int extIndex = filename.lastIndexOf("."); // extension index
		
			author = "";
			title = filename.substring(0, extIndex).trim();
		} else {
			int dashIndex = filename.indexOf(" - "); // dash index
			int extIndex; // extension index
			
			if (filename.contains(".")) {
				extIndex = filename.lastIndexOf(".");
			} else {
				extIndex = filename.length();
			}
			
			author = filename.substring(0, dashIndex).trim();
			title = filename.substring(dashIndex+3, extIndex).trim();
		}
	}
	
    
    public String getPathname() {
		return pathname;
	}
	 
    public String getFilename() {
		return filename;
	}
    
    public String getAuthor() {
		return author;
	}
	
    public String getTitle() {
		return title;
	}
    
    
    public void setAuthor(String author) {
		this.author = author;
	}

    public void setTitle(String title) {
		this.title = title;
	}
    
    
    
    
    
	@Override
	public String toString() {
		if (author == null && title == null) { // for empty initialiser
			return "";
		} else if (author.isEmpty()) {
			return title;
		} else {
			return author + " - " + title;
		}
	}
	
	
	public abstract void play() throws NotPlayableException ;
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String formatDuration();
	
	public abstract String formatPosition();
}

