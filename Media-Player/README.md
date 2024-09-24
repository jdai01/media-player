# Media Player

## Overview

This project is a practical exercise focused on developing a fully functional media player for playing audio files. 
The application provides an intuitive interface with a searchable and sortable playlist, along with essential audio controls (play, pause, stop). 
The media player supports various audio file formats and includes features for reading and handling metadata such as tags (ID3, Vorbis comments) and file properties (e.g., RIFF WAVE).

![Media Player](display.png)

## Features

### Core Functionality:
- **Audio Playback**: Plays various audio formats like MP3, OggVorbis, and WAVE.
- **Playlist Management**: Create, load, and manage playlists in M3U format.
- **Metadata Handling**: Display and manage metadata from audio files, including ID3 tags and Vorbis comments.
- **Search and Sort**: Filter and sort the playlist by album, author, title, and duration.

### User Interface:
A graphical user interface (GUI) will be implemented to provide seamless control over all the developed functionalities, including playlist management, playback control, sorting, and search filtering.


## Project Set Up
1. Clone the repository:

```bash
git clone https://github.com/jdai01/CAI_Prog2
```

2. Setup Java Project in Eclipse IDE, i.e. create Java project "Media-Player"

3. [Adding JAR Libraries to the Classpath in Eclipse](../config/add-jar-lib.md)

4. [Configure JavaFX Setup on project](../config/javafx-setup.md)

5. Go to `src/studiplayer/ui/` and run `Player.java`