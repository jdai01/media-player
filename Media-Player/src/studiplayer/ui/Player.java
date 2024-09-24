package studiplayer.ui;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.SampledFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application {
	private Boolean windows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0; // Get OS of running system
	
	private static final String PLAYLIST_DIRECTORY = "playlists/";
    public static final String DEFAULT_PLAYLIST = PLAYLIST_DIRECTORY + "DefaultPlayList.m3u";
    public static final String CERT_PLAYLIST = PLAYLIST_DIRECTORY + "playList.cert.m3u";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = "-";
	
	private PlayList playList;
	private boolean useCertPlayList = false;
	
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Button filterButton;
	
	private Label playListLabel;
	private Label playTimeLabel;
	private Label currentSongLabel;
	
	private ChoiceBox<SortCriterion> sortChoiceBox;
	
	private TextField searchTextField;
	
	private SongTable songTable;
	
	// Initialise for thread controls
	private boolean isPaused = false;
	private PlayerThread playerThread;
	private TimerThread timerThread;
	
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String playListLabelString = null;
		
		BorderPane mainPane = new BorderPane();
		primaryStage.setTitle("APA Player");
		

		// Load playlist
		if (this.useCertPlayList) {
			this.playList = new PlayList(CERT_PLAYLIST);
		} else {
			// Interface to select file
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose Playlist");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("M3U Files", "*.m3u"));
			
			// Load selected file
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile == null) {
				this.loadPlayList(null);
				playListLabelString = DEFAULT_PLAYLIST;
			} else {
				String selectedFilePath = selectedFile.getAbsolutePath(); // getPathname of selected file
				this.loadPlayList(selectedFilePath);
				playListLabelString = selectedFilePath;
			}
		}
		
		
		System.out.println("Songs in Playlist:");
		for (AudioFile af : this.playList) {
			System.out.println(af);
		}
		System.out.println("---");
		
		// GUI
		// Top area ----------------------------
		TitledPane topPane = new TitledPane();
		topPane.setText("Filter");
		
		double topWidth = 150; // common width for textfield & choicebox
		
		// - Row1 instance + formatting
		searchTextField = new TextField();
		searchTextField.setPrefWidth(topWidth);
		
		Label searchNameLabel = new Label("Search text \t");
		
		HBox topRow1 = new HBox();
		topRow1.getChildren().addAll(searchNameLabel, searchTextField);
		
		// - Row2 instance + formatting
		sortChoiceBox = new ChoiceBox<>();
		sortChoiceBox.getItems().addAll(SortCriterion.values());
		sortChoiceBox.setValue(SortCriterion.DEFAULT);
		sortChoiceBox.setPrefWidth(topWidth);
		
		filterButton = createButton("display"); 
		
		Label sortNameLabel = new Label("Sort by \t\t\t");
		
		HBox topRow2 = new HBox();
		topRow2.getChildren().addAll(sortNameLabel, sortChoiceBox, filterButton);
		
		
		// - Combined formatting
		VBox rowsCombined = new VBox();
		rowsCombined.getChildren().addAll(topRow1, topRow2);
		
		topPane.setContent(rowsCombined);
		// -------------------------------------
		mainPane.setTop(topPane);
		
		
		
		// Middle area -------------------------
		BorderPane middlePane = new BorderPane();
		
		// - Display Playlist via TableView
		songTable = new SongTable(this.playList);
		middlePane.setCenter(songTable);
		// -------------------------------------
		mainPane.setCenter(middlePane);
		
		
		// Bottom area -------------------------
		VBox bottomPane = new VBox();
		
		// - Top VBox
		playListLabel = new Label(playListLabelString);
		currentSongLabel = new Label(NO_CURRENT_SONG);
		playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
		
		//   set width of front labels
		double bottomWidth = 100;
		Label playListNameLabel = new Label("Playlist");
		Label currentSongNameLabel = new Label("Current Song");
		Label playTimeNameLabel = new Label("Playtime");
		
		playListNameLabel.setPrefWidth(bottomWidth);
		currentSongNameLabel.setPrefWidth(bottomWidth);
		playTimeNameLabel.setPrefWidth(bottomWidth);
		
		HBox bottomRow1 = new HBox();
		bottomRow1.getChildren().addAll(playListNameLabel, playListLabel);
		HBox bottomRow2 = new HBox();
		bottomRow2.getChildren().addAll(currentSongNameLabel, currentSongLabel);
		HBox bottomRow3 = new HBox();
		bottomRow3.getChildren().addAll(playTimeNameLabel, playTimeLabel);
		
		VBox songInfoBox = new VBox();
		songInfoBox.getChildren().addAll(bottomRow1, bottomRow2, bottomRow3);
		
		// - Bottom VBox
		playButton = createButton("play.jpg");
		pauseButton = createButton("pause.jpg");
		stopButton = createButton("stop.jpg");
		nextButton = createButton("next.jpg");
		HBox buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
		buttonsBox.setAlignment(Pos.CENTER);; // center alignment
		
		bottomPane.getChildren().addAll(songInfoBox, buttonsBox);
		// -------------------------------------
		mainPane.setBottom(bottomPane);
		
		
		// Set button states if playList is null
		if (playList.size() == 0) {
			setButtonStates(true, true, true, true);
		} else {
			setButtonStates(false, true, true, false);
		}
		
		
		
		// Handle actions
		initialiseEventHandlers(); // - create actions for buttons
		filterButton.setOnAction(this::handleFilterAction);
		songTable.setRowSelectionHandler(arg0 -> {
			try {
				handleRowSelection(arg0);
			} catch (NotPlayableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// Final Setting
		Scene scene = new Scene(mainPane, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	
	
	public void setUseCertPlayList(boolean value) {
		this.useCertPlayList = value;
	}
	
	public void loadPlayList(String pathname) {
        if (pathname == null || pathname.isEmpty()) {
        	this.playList = new PlayList(DEFAULT_PLAYLIST);
        } else {
            this.playList = new PlayList(pathname);
        }
 
    }
	
	
	
	public Button createButton(String iconfile) {
		Button button = null;
		
		if (iconfile.contains(".jpg")) {
			// Button with icon
			String sep = (windows == true) ? "\\" : "/"; // create the separator for image path (OS dependent)
			String iconPath = "icons/" + iconfile;
			
			
			try {
				Image icon = new Image(iconPath);
				ImageView imageView = new ImageView(icon); 
				imageView.setFitHeight(20);
				imageView.setFitWidth(20);
				button = new Button("", imageView);  
			} catch (Exception e) { 
				System.out.println("Image " + "icons/" + iconfile + " not found!"); System.exit(-1);
			}
		} else {
			// Normal Button
			button = new Button(iconfile);  
		}		
		
		return button;
	}
	
	
	// Event Handles
	private void handleFilterAction(ActionEvent event) {
		// Get filter & search values
		String search = searchTextField.getText();
		SortCriterion sort = sortChoiceBox.getValue();
		
		// Set search & sort in Playlist
		this.playList.setSearch(search);
		this.playList.setSortCriterion(sort);
		
		// Update SongTable
		this.songTable.refreshSongs();
		
		// Console Output
		System.out.printf("Search: %s, Sort: %s, filterButton clicked\n", search, sort);
	}
	
	
	private void initialiseEventHandlers() throws NotPlayableException {
		playButton.setOnAction(e -> {
			try {
				playCurrentSong();
			} catch (NotPlayableException e1) {
				e1.printStackTrace();
			}
		});
		
		pauseButton.setOnAction(e -> {
			pauseOrResume();
		});
		
		stopButton.setOnAction(e -> {
			stopPlayback();
		});
		

		nextButton.setOnAction(e -> {			
			try {
				playNextSong();
			} catch (NotPlayableException e1) {
				e1.printStackTrace();
			}
		});
	}
	
	private void playCurrentSong() throws NotPlayableException {
		playerThread = new PlayerThread(); // new PlayerThread is called whenever the song is played
		timerThread = new TimerThread();
		isPaused = false;
		
		SampledFile currentAF = (SampledFile) playList.currentAudioFile();
		updateSongInfo(currentAF);
		setButtonStates(true, false, false, false);
		playerThread.start(); // - play music
		timerThread.start(); // - start formatting currentSongLabel
		songTable.selectSong(currentAF); // select song from table
		
		// Console output
		System.out.println("Playing " + playList.currentAudioFile());
		System.out.println("Filename is " + playList.currentAudioFile().getFilename());
		System.out.println("-----");
	}
	
	private void pauseOrResume() {
		
		playerThread.togglePause();
		setButtonStates(true, false, false, false);
		
		// Console
		if (isPaused == true) {
			// make it false, play music
			isPaused = false;
			System.out.println("Playing " + playList.currentAudioFile());
		} else {
			// make it true, pause music
			isPaused = true;
			System.out.println("Pausing " + playList.currentAudioFile());
		}
		System.out.println("Filename is " + playList.currentAudioFile().getFilename());
		System.out.println("-----");
	}
	
	private void stopPlayback() {
		
		playerThread.terminate(); // - stop music
		timerThread.terminate(); // - stop formatting currentSongLabel
//		updateSongInfo(currentAF);
		setButtonStates(false, true, true, false);
		
		// Console output
		System.out.println("Stopping " + playList.currentAudioFile());
		System.out.println("Filename is " + playList.currentAudioFile().getFilename());
		System.out.println("-----");
	}
	
	private void playNextSong() throws NotPlayableException{
		// Console output
		System.out.println("Switching to next audio file: stopped = false, paused = true");
		stopPlayback();
		playList.nextSong();  // - select next song
		playCurrentSong();
		System.out.println("Switched to next audio file: stopped = false, paused = true");
	}
	
    
    private void handleRowSelection(MouseEvent event) throws NotPlayableException {
        if (event.getClickCount() == 2) {
            int rowIndex = songTable.getSelectionModel().getSelectedIndex();
            if (rowIndex >= 0) {
                if (playerThread != null) {
                	stopPlayback(); // Stop currently playing song
                }
                playSelectedSong(rowIndex); // Play the selected song
            }
        }
    }

    private void playSelectedSong(int rowIndex) throws NotPlayableException {
        AudioFile selectedSong = songTable.getAudioFileAtIndex(rowIndex);
        playList.jumpToAudioFile(selectedSong); // Update the current song in the playlist
        updateSongInfo(selectedSong);
        setButtonStates(false, true, true, false);
        System.out.println("Song selected: " + selectedSong.getTitle());
        playCurrentSong();
    }
	
	
	private void setButtonStates(boolean playButtonState, 
								 boolean pauseButtonState, 
								 boolean stopButtonState, 
								 boolean nextButtonState) {
		playButton.setDisable(playButtonState);
		pauseButton.setDisable(pauseButtonState);
		stopButton.setDisable(stopButtonState);
		nextButton.setDisable(nextButtonState);
	}
	
	private void updateSongInfo(AudioFile af) { 
		Platform.runLater(() -> {
			String songTitle = (af == null) ? NO_CURRENT_SONG : af.toString();
			
			currentSongLabel.setText(songTitle);
		});
	}
	
	private void updatePlayTimeInfo(SampledFile af) {
		Platform.runLater(() -> {
			String timePos = af.formatPosition();
			
			System.out.println("Playtime: " + timePos);
			
			playTimeLabel.setText(timePos);
		});
	}
	
	
	
	
	// Threads ----------------------------------
	
	private class PlayerThread extends Thread {
		private volatile boolean stopped = false;
		private SampledFile currentAF; 
		
		public void terminate() {
			currentAF = (SampledFile) playList.currentAudioFile();
			
			System.out.println("PlayerThread is terminated");
			stopped = true;
			currentAF.stop();
		}
		
		public void togglePause() {
			currentAF = (SampledFile) playList.currentAudioFile();
			
			System.out.println("PlayerThread is toggled Pause");
			currentAF.togglePause();
		}
		
		@Override
		public void run() {
			currentAF = (SampledFile) playList.currentAudioFile();
			
			System.out.println("PlayerThread is running");
			stopped = false;
			
			try {
				currentAF.play();
			} catch (NotPlayableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (!stopped) {
				continue;
			}
		}
	}

	private class TimerThread extends Thread {
		private volatile boolean stopped = false;
		private SampledFile currentAF = (SampledFile) playList.currentAudioFile(); 
		
		public void terminate() {
			System.out.println("TimerThread is terminated");
			stopped = true;
//			playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL); // update PlayTime Info
//			System.out.println("Playtime: " + playTimeLabel.getText());
		}
		
		@Override
		public void run() {
//			currentAF = (SampledFile) playList.currentAudioFile();
			System.out.println("TimerThread is running");
			
			while (!stopped) {
				try {
					updatePlayTimeInfo(currentAF);; // update PlayTime Info
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Platform.runLater(() -> {
	            playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL); // update to initial playtime
	            System.out.println("Playtime: " + playTimeLabel.getText());
	        });
		}
	}
	
}


