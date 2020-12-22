package screens;

import java.io.IOException;

import controllers.DungeonScreenController;
import core.Dungeon;
import core.LevelList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonScreen {

	private Stage s;
	private String title;
	private FXMLLoader fxmlLoader;
	
	private Dungeon dungeon;
	private LevelList l;
	private boolean creator;
	    
	public DungeonScreen(Stage s, Dungeon d, LevelList l, boolean creator) {
        this.s = s;
        this.title = "Dungeon Player";
        this.fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("screens/fxmlFiles/dungeonScreen.fxml"));
        this.dungeon = d;
        this.l = l;
        this.creator = creator;
    }
	
	public void start()  {
        s.setTitle(title);
        // set controller for start.fxml
        fxmlLoader.setController(new DungeonScreenController(s, dungeon, l, creator));
        try {
            // load into a Parent node called root
            Parent root = fxmlLoader.load();
            Scene sc = new Scene(root, 1000, 1000);
            s.setScene(sc);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	    
}