package controllers;

import java.util.ArrayList;

import core.Dungeon;
import core.LevelList;
import entities.Entity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import screens.DungeonScreen;
import screens.MainMenuScreen;

public class LevelScreenController {

	private Stage currStage;
	static final int maxDungeonSize = 20;
	private LevelList levels;

	@FXML
	Button playButton;

	@FXML
	Button exitButton;

	@FXML
	ListView<String> dungeonList;

	@FXML
	TilePane preview;

	@FXML
	GridPane levelMap;
	
	@FXML
	ImageView background;

	public LevelScreenController(Stage s, LevelList l) {
		currStage = s;
		this.levels = l;
	}

	/**
	 * Function to initialise the LevelScreen
	 */
	@FXML
	public void initialize() {
		dungeonList.setItems(levels.addDungeons());
		background.setVisible(false);
	}

	/**
	 * Function to get the Level selected by the User to play and changes the screen
	 * to the dungeon
	 */
	@FXML
	public void playLevel() {
		String s = dungeonList.getSelectionModel().getSelectedItem();
		ArrayList<Integer> dimensions = levels.getMapDimensions(s);
		Dungeon d = new Dungeon(dimensions.get(0), dimensions.get(1));
		Entity[][] map = levels.getMapwithName(s);
		d.setMap(map);
		d.setName(s);
		d.setMethod(levels.getMethodWithName(s));
		
		DungeonScreen ds = new DungeonScreen(currStage, d, levels, false);
		ds.start();
	}

	/**
	 * Function to go back to the Main Menu Screen
	 */
	@FXML
	public void backtoMenu() {
		MainMenuScreen ms = new MainMenuScreen(currStage, levels);
		ms.start();
	}

	/**
	 * Function to show a preview of the dungeon layout selected by the User from
	 * the list of Levels(Maps)
	 */
	@FXML
	public void getDungeonPreview() {
		// Get the dungeon name selected and the corresponding dimensions of the map.
		String s = dungeonList.getSelectionModel().getSelectedItem();
		Entity[][] map = levels.getMapwithName(s);
		ArrayList<Integer> dimensions = levels.getMapDimensions(s);
		levelMap.getChildren().clear();
		getDungeonLayout(map, dimensions);
	}

	/**
	 * Function to get the Layout of the Dungeon and display it on the GridPane
	 * @param map -> Entity[][]
	 * @param dimensions -> ArrayList<Integer>
	 */
	public void getDungeonLayout(Entity[][] map, ArrayList<Integer> dimensions) {
		for (int row = 0; row < maxDungeonSize; row++) {
			for (int col = 0; col < maxDungeonSize; col++) {
				if (row > dimensions.get(0) - 1 || col > dimensions.get(1) - 1) {
					continue;
				} else if (row == dimensions.get(0) - 1 || col == dimensions.get(1) - 1) {
					ImageView img = new ImageView("/images/wall.png");
					img.setId("floor");
					img.setFitWidth(22);
					img.setFitHeight(20);
					levelMap.add(img, col, row);
				} else if (map[row][col] != null) {
					Entity entity = map[row][col];
					ImageView img = new ImageView(map[row][col].getPicture().getImage());
					img.setId(entity.getPicture().getId());
					img.setImage(entity.getPicture().getImage());
					img.setId(entity.getPicture().getId());
					img.setFitWidth(22);
					img.setFitHeight(20);
					levelMap.add(img, col, row);
				}
			}
		}
		
		background.setVisible(true);
		background.setPreserveRatio(false);
		background.setFitHeight(20*(dimensions.get(0)-2));
		background.setFitWidth(22*(dimensions.get(1)-2));
	}
}
