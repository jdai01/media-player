package studiplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SampledFile;
import studiplayer.audio.TaggedFile;

public class SongTable extends TableView<Song> {
	private ObservableList<Song> tableData;
	private PlayList playList;
	
	/**
	 * Intialisiert die Tabelle mit den Daten aus der PlayList und setzt Tabellen-Header
	 * @param playList
	 * 
	 * Initializes the table with the data from the PlayList and sets table headers
	 */
	public SongTable(PlayList playList) {
		this.playList = playList;
		this.tableData = FXCollections.observableArrayList();
		setItems(tableData);
		
		// For Artist
        TableColumn<Song, String> interpretColumn = new TableColumn<>("Artist");
        interpretColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("interpret"));
        interpretColumn.setSortable(false);
        
        // For Title
		TableColumn<Song, String> titelColumn = new TableColumn<>("Title");
		titelColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("titel"));
        titelColumn.setSortable(false);
        
        // For Album
		TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
		albumColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("album"));
		albumColumn.setSortable(false);
		
		// For Duration
		TableColumn<Song, String> laengeColumn = new TableColumn<>("Duration");
		laengeColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("laenge"));
		laengeColumn.setSortable(false);
		
		// Get Columns
		getColumns().add(interpretColumn);
		getColumns().add(titelColumn);
		getColumns().add(albumColumn);
		getColumns().add(laengeColumn);
        setEditable(false);
        refreshSongs();
	}

	/**
	 * Registert eine Ereignisbehandlung für den Fall, dass eine Zeile mit der
	 * Maus angeklickt wird.
	 * @param handler
	 * 
	 * Registers an event handler for when a row is clicked with the mouse.
	 */
	public void setRowSelectionHandler(EventHandler<? super MouseEvent> handler) {
        setOnMouseClicked(handler);		
	}
	
	/**
	 * Zeigt die Tabelle neu an nach Änderungen (Einträge, Konfiguration) an der PlayListe 
	 * 
	 * Redisplays the table after changes (entries, configuration) to the playlist
	 */
	public void refreshSongs() {
		tableData.clear();
		for (AudioFile af : playList) {
			String album = "";
			String laenge = "";
			
			if (af instanceof TaggedFile) {
				album = ((TaggedFile) af).getAlbum();
			}
			if (af instanceof SampledFile) {
				SampledFile sf = (SampledFile) af;
				laenge = sf.formatDuration();
			}
			
			tableData.add(new Song(af, af.getAuthor(), af.getTitle(), album, laenge));
		}		
	}

	/**
	 * Selektiert den Song "song" in der Tabelle
	 * @param song
	 * 
	 * Select the song "song" in the table
	 */
	public void selectSong(AudioFile song) {
		AudioFile currentAudioFile = playList.currentAudioFile();
		int index = 0;
		
		for (Song s : tableData) {
			if (s.getAudioFile() == currentAudioFile) {
				getSelectionModel().select(index);
			}
			++index;
		}
	}
	
	/**
	 * - Added -
     * Get the AudioFile at the specified index
     */
    public AudioFile getAudioFileAtIndex(int index) {
        return tableData.get(index).getAudioFile();
    }
}
