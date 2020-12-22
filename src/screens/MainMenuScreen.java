package screens;

import java.io.IOException;

import controllers.MainMenuController;
import core.LevelList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuScreen {

	private Stage s;
	private String title;
	private FXMLLoader fxmlLoader;
	private LevelList l;
	    
	public MainMenuScreen(Stage s, LevelList l) {
        this.s = s;
        this.title = "Main Menu";
        this.fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("screens/fxmlFiles/mainMenu.fxml"));
        this.l = l;
    }
	
	public void start()  {
        s.setTitle(title);
        // set controller for start.fxml
        fxmlLoader.setController(new MainMenuController(s, l));
        try {
            // load into a Parent node called root
            Parent root = fxmlLoader.load();
            Scene sc = new Scene(root, 500, 500);
            s.setScene(sc);
            s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }	    
}
