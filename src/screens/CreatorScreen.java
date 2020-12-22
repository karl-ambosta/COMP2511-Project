package screens;

import java.io.IOException;

import controllers.CreatorScreenController;
import core.Dungeon;
import core.LevelList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreatorScreen {

	private Stage s;
	private String title;
	private FXMLLoader fxmlLoader;
	private LevelList levels;
	private Dungeon dung;
	    
	public CreatorScreen(Stage s, LevelList l, Dungeon dung) {
        this.s = s;
        this.title = "Dungeon Creator";
        this.fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("screens/fxmlFiles/creatorScreen.fxml"));
        this.levels = l;
        this.dung = dung;
    }
	
	public void start()  {
        s.setTitle(title);
        // set controller for start.fxml
        fxmlLoader.setController(new CreatorScreenController(s, dung, levels));
        try {
            // load into a Parent node called root
            Parent root = fxmlLoader.load();
            Scene sc = new Scene(root, 2000, 2000);
            s.setScene(sc);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	    
}