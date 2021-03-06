package screens;

import java.io.IOException;

import controllers.LevelScreenController;
import core.LevelList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LevelScreen {

	private Stage s;
	private String title;
	private FXMLLoader fxmlLoader;
	private LevelList l;
	    
	public LevelScreen(Stage s, LevelList l) {
        this.s = s;
        this.title = "Levels";
        this.fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("screens/fxmlFiles/levelMenu.fxml"));
        this.l = l;
    }
	
	public void start()  {
        s.setTitle(title);
        // set controller for start.fxml
        fxmlLoader.setController(new LevelScreenController(s, l));
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