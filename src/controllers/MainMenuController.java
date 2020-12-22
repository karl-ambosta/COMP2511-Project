package controllers;

import core.LevelList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import screens.CreatorScreen;
import screens.LevelScreen;

public class MainMenuController {

	private Stage currStage;
	private LevelList levels = new LevelList();

	@FXML
	Button toGame;

	@FXML
	Button toCreator;

	public MainMenuController(Stage s, LevelList l) {
		currStage = s;
		this.levels = l;
	}

	@FXML
	public void initialize() {
		return;
	}

	@FXML
	public void goToLevels() {
		LevelScreen ls = new LevelScreen(currStage, levels);
		ls.start();

	}

	@FXML
	public void goToCreator() {
		CreatorScreen cs = new CreatorScreen(currStage, levels, null);
		cs.start();
	}

}
