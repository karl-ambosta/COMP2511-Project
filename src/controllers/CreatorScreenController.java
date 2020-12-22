package controllers;

import java.awt.Point;
import java.util.ArrayList;
import core.Dungeon;
import core.LevelList;
import entities.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import screens.DungeonScreen;
import screens.MainMenuScreen;

public class CreatorScreenController {
	private Stage currStage;
	private Dungeon dung;
	private boolean newDung;
	private LevelList levels;
	private String method;

	@FXML
	Button exitButton;

	@FXML
	GridPane levelMap;

	@FXML
	TextField dungeonNameField;

	@FXML
	ImageView selectedPane;

	@FXML
	ImageView background;

	@FXML
	TextField rowField;

	@FXML
	TextField colField;

	@FXML
	Button setMethodButton;

	@FXML
	ChoiceBox<String> methodChoices;

	@FXML
	Label saveMessage;

	@FXML
	TableView<Dungeon> dungeonList;

	@FXML
	TableColumn<Dungeon, String> dungeonName;

	@FXML
	TableColumn<Dungeon, Integer> dungeonMethod;

	@FXML
	TableColumn<Dungeon, String> dungeonSize;

	@FXML
	TableColumn<Dungeon, Integer> dungeonRow;

	@FXML
	TableColumn<Dungeon, Integer> dungeonColumn;

	@FXML
	Label methodDisplay;

	public CreatorScreenController(Stage s, Dungeon d, LevelList l) {
		currStage = s;
		selectedPane = new Wall().getPicture();
		this.levels = l;
		if (d == null) {
			dung = new Dungeon();
			newDung = true;
		} else {
			dung = d;
			newDung = false;
		}
	}

	@FXML
	public void initialize() {
		setEmptyDungeon();
		if (!newDung) {
			openDungeon(dung.getMap(), dung.getDimensions());
		}

		ObservableList<String> choices = methodChoices.getItems();
		choices.add("Reach Exit");
		choices.add("Kill Enemies");
		choices.add("Activate Switches");
		choices.add("Collect Treasure");

		methodChoices.setItems(choices);

		dungeonName.setCellValueFactory(new PropertyValueFactory<Dungeon, String>("name"));
		dungeonMethod.setCellValueFactory(new PropertyValueFactory<Dungeon, Integer>("method"));
		dungeonRow.setCellValueFactory(new PropertyValueFactory<Dungeon, Integer>("rowNo"));
		dungeonColumn.setCellValueFactory(new PropertyValueFactory<Dungeon, Integer>("colNo"));

		dungeonList.setItems(levels.getAllMaps());

		methodDisplay.setText("Nothing");
		ArrayList<Integer> d = dung.getDimensions();
		background.setFitHeight(22*(d.get(0)-1));
		background.setFitWidth(22*(d.get(1)));
	}

	/**
	 * Opens a dungeon and load the map on screen for editing
	 * 
	 * @param map -> Entity[][] map to load
	 * @param dimensions -> ArrayList<Integer> dimensions of the dungeon
	 */
	public void openDungeon(Entity[][] map, ArrayList<Integer> dimensions) {
		saveMessage.setText("");
		int x = dimensions.get(0);
		int y = dimensions.get(1);
		this.dung = new Dungeon(x, y);
		this.dung.setMap(map);
		setDimensions(x, y);

		for (int i = 0; i < dimensions.get(0); i++) {
			for (int j = 0; j < dimensions.get(1); j++) {
				if (dung.getMap()[i][j] != null) {
					ImageView im = getNodeFromCoordinates(i, j);
					if(dung.getMap()[i][j] instanceof Door) {
						Door d = (Door) dung.getMap()[i][j];
						d.closeDoor();
					}
					im.setImage(dung.getMap()[i][j].getPicture().getImage());
					im.setId(dung.getMap()[i][j].getPicture().getId());
				}
			}
		}
	}

	/**
	 * Gets the Gridpane node from a set of coordinates
	 * @param i -> column index
	 * @param j -> row index
	 * @return node -> Node that is in coordinates (i,j)
	 */
	private ImageView getNodeFromCoordinates(int i, int j) {
		for (Node n : levelMap.getChildren()) {
			if (GridPane.getColumnIndex(n) == j && GridPane.getRowIndex(n) == i) {
				return (ImageView) n;
			}
		}
		return null;
	}

	/**
	 * Initialises an empty dungeon for creation
	 */
	public void setEmptyDungeon() {
		int col = 0;
		int row = 0;
		ArrayList<Integer> dims = dung.getDimensions();
		for (Node n : levelMap.getChildren()) {
			GridPane.setColumnIndex(n, col);
			GridPane.setRowIndex(n, row);
			if (col == dims.get(1) - 1 || row == dims.get(0) - 1 || col == 0 || row == 0) {
				ImageView im = (ImageView) n;
				Wall wall = new Wall();
				im.setImage(wall.getPicture().getImage());
				n.setId("edge");
			} else {
				n.setId("empty");
			}
			col++;
			if (col % (dims.get(1)) == 0) {
				col = 0;
				row++;
			}
			if (row == dims.get(0)) {
				break;
			}
		}
	}

	/**
	 * Returns to the Main Menu Screen
	 */
	@FXML
	public void backtoMenu() {
		MainMenuScreen ms = new MainMenuScreen(currStage, levels);
		ms.start();
	}

	/**
	 * Saves the created Dungeon
	 */
	@FXML
	public void saveDungeon() {
		if (!isValidDungeon()) {
			return;
		}
		for (Node n : levelMap.getChildren()) {
			Integer col = GridPane.getColumnIndex(n);
			Integer row = GridPane.getRowIndex(n);
			if (row != null && col != null) {
				Entity e = getEntity(n.getId());
				if (e != null) {
					dung.addEntity(e, new Point(row.intValue(), col.intValue()));
				}
			}
		}
		String name = this.dungeonNameField.getText();
		if (name.equals("")) {
			saveMessage.setText("Enter Dungeon Name");
			return;
		}
		levels.saveMap(dung, name);
		saveMessage.setText("Dungeon Saved!!!");
		reloadList();
	}

	/**
	 * Opens the Created Dungeon in the Dungeon Player Screen
	 */
	@FXML
	public void openCreatedDungeon() {
		String name = this.dungeonNameField.getText();
		if (name.equals("")) {
			saveMessage.setText("Dungeon Must be Saved Before Testing");
			return;
		}
		saveDungeon();
		DungeonScreen ds = new DungeonScreen(currStage, dung, levels, true);
		ds.start();
	}

	// Swapping selected item section
	@FXML
	public void swapSelectedPlayer() {
		selectedPane = (new Player()).getPicture();
	}

	@FXML
	public void swapSelectedHunter() {
		selectedPane = (new Hunter()).getPicture();
	}

	@FXML
	public void swapSelectedHound() {
		selectedPane = (new Hound()).getPicture();
	}

	@FXML
	public void swapSelectedCoward() {
		selectedPane = (new Coward()).getPicture();
	}

	@FXML
	public void swapSelectedStrategist() {
		selectedPane = (new Strategist()).getPicture();
	}

	@FXML
	public void swapSelectedBoulder() {
		selectedPane = (new Boulder()).getPicture();
	}

	@FXML
	public void swapSelectedSwitch() {
		selectedPane = (new Switch()).getPicture();
	}

	@FXML
	public void swapSelectedWall() {
		selectedPane = (new Wall()).getPicture();
	}

	@FXML
	public void swapSelectedExit() {
		selectedPane = (new Exit()).getPicture();
	}

	@FXML
	public void swapSelectedTreasure() {
		selectedPane = (new Treasure()).getPicture();
	}

	@FXML
	public void swapSelectedPit() {
		selectedPane = (new Pit()).getPicture();
	}

	@FXML
	public void swapSelectedKey() {
		selectedPane = (new Key()).getPicture();
	}

	@FXML
	public void swapSelectedDoor() {
		selectedPane = (new Door()).getPicture();
	}

	@FXML
	public void swapSelectedInvinc() {
		selectedPane = (new InvincibilityPotion()).getPicture();
	}

	@FXML
	public void swapSelectedHover() {
		selectedPane = (new HoverPotion()).getPicture();
	}

	@FXML
	public void swapSelectedSword() {
		selectedPane = (new Sword()).getPicture();
	}

	@FXML
	public void swapSelectedArrow() {
		selectedPane = (new Arrow()).getPicture();
	}

	@FXML
	public void swapSelectedBomb() {
		selectedPane = (new Bomb()).getPicture();
	}
	
	@FXML
	public void swapSelectedKillEnemy() {
		selectedPane = (new KillEnemyPotion()).getPicture();
	}

	@FXML
	public void swapSelectedRemove() {
		selectedPane = new ImageView("/images/floor2.png");
		selectedPane.setId("empty");
	}

	@FXML
	public void gridClicked(MouseEvent event) {
		saveMessage.setText("");
		ImageView source = (ImageView) event.getSource();
		ArrayList<Integer> dimens = dung.getDimensions();
		Point coords = findGridCoordinates(source);
		if (coords.x > dimens.get(0) - 2 || coords.y > dimens.get(1) - 2) {
			return;
		} else if (!source.getId().equals("edge")) {

			if (selectedPane.getId().equals("player") && numPlayers() >= 1
					|| selectedPane.getId().equals("door") && numDoors() >= 3) {
				return;
			}

			source.setImage(selectedPane.getImage());
			source.setId(selectedPane.getId());
		}
	}

	/**
	 * Function to get an Entity from it's ID
	 * @param id -> String
	 * @return Entity
	 */
	public Entity getEntity(String id) {
		switch (id) {
		case "Arrow":
			return new Arrow();
		case "Bomb":
			return new Bomb();
		case "boulder":
			return new Boulder();
		case "coward":
			return new Coward();
		case "door":
			return new Door();
		case "exit":
			return new Exit();
		case "hound":
			return new Hound();
		case "hover":
			return new HoverPotion();
		case "hunter":
			return new Hunter();
		case "invinc":
			return new InvincibilityPotion();
		case "key":
			return new Key();
		case "killEnemy":
			return new KillEnemyPotion();
		case "player":
			return new Player();
		case "pit":
			return new Pit();
		case "strategist":
			return new Strategist();
		case "switch":
			return new Switch();
		case "sword":
			return new Sword();
		case "treasure":
			return new Treasure();
		case "wall":
			return new Wall();
		}
		return null;
	}

	/**
	 * Function to reset the Dungeon
	 */
	@FXML
	public void reset() {
		saveMessage.setText("");
		for (Node n : this.levelMap.getChildren()) {
			if (n instanceof ImageView) {
				ImageView im = (ImageView) n;
				im.setImage(new Image("/images/floor2.png"));
				im.setId("empty");
			}
		}
		this.setEmptyDungeon();
		methodDisplay.setText("Nothing");
		dungeonNameField.setText("");
		rowField.setText("");
		colField.setText("");
	}
	
	@FXML
	public void resetButton() {
		reset();
		this.dung.setEmptyDungeon();
	}

	/**
	 * Funciton to Load a Chosen Dungeon
	 */
	@FXML
	public void loadDungeon() {
		saveMessage.setText("");
		String name = dungeonList.getSelectionModel().getSelectedItem().getName();
		Entity[][] map = levels.getMapwithName(name);
		if (map != null) {
			dung.reloadDungeon(dung, map);
			openDungeon(map, levels.getMapDimensions(name));
		}
	}

	/**
	 * Function to get the Dimensions requested by the User
	 */
	@FXML
	public void getDimensions() {
		int rows = 20;
		int columns = 20;
		try {
			rows = Integer.parseInt(rowField.getText());
			columns = Integer.parseInt(colField.getText());
		} finally {
			if (rows > 20 || rows < 3) {
				rows = 20;
			}
			if (columns > 20 || columns < 3) {
				columns = 20;
			}
			this.dung = new Dungeon(rows, columns);
			setDimensions(rows, columns);
		}
	}

	/**
	 * Funciton to set the Dungeon preview to the given size
	 * @param x
	 * @param y
	 */
	public void setDimensions(int x, int y) {
		saveMessage.setText("");
		setEmptyDungeon();
		reset();

		background.setPreserveRatio(false);
		background.setFitHeight(22 * (x-1));
		background.setFitWidth(22 * (y));

	}

	/**
	 * Get the coordinates of the specified ImageView
	 * @param src -> ImageView
	 * @return Point
	 */
	public Point findGridCoordinates(ImageView src) {
		for (Node n : levelMap.getChildren()) {
			if (n.equals(src)) {
				int x = GridPane.getRowIndex(n);
				int y = GridPane.getColumnIndex(n);
				Point p = new Point(x, y);
				return p;
			}
		}
		return null;
	}

	/**
	 * Function to set the Dungeon's method
	 */
	public void setMethod() {
		String s = (String) methodChoices.getSelectionModel().getSelectedItem();
		this.method = s;
		switch (s) {
		case "Reach Exit":
			dung.setMethod(0);
			methodDisplay.setText("Reach Exit");
			break;
		case "Kill Enemies":
			dung.setMethod(3);
			methodDisplay.setText("Kill Enemies");
			break;
		case "Activate Switches":
			dung.setMethod(2);
			methodDisplay.setText("Activate Switches");
			break;
		case "Collect Treasure":
			dung.setMethod(1);
			methodDisplay.setText("Collect Treasure");
			break;
		default:
			dung.setMethod(0);
			break;
		}
	}

	/**
	 * Function to reload the list of available levels
	 */
	public void reloadList() {
		dungeonList.setItems(levels.getAllMaps());
	}

	/**
	 * Funciton to check if the Dungeon created is valid
	 * @return
	 */
	public boolean isValidDungeon() {
		if (numPlayers() < 1) {
			saveMessage.setText("Must have 1 Player");
			return false;
		} else if (method == null) {
			saveMessage.setText("Must choose Completion");
			return false;
		} else if (method == "Reach Exit" && !hasExit()) {
			saveMessage.setText("Must have Exit");
			return false;
		} else if (method == "Kill Enemies" && !hasEnemies()) {
			saveMessage.setText("Must have at least 1 enemy");
			return false;
		} else if (method == "Collect Treasure" && !hasTreasure()) {
			saveMessage.setText("Must have at least 1 treasure");
			return false;
		} else if (method == "Activate Switches" && !hasSwitchandBoulder()) {
			saveMessage.setText("Must have at least 1 switch and boulder");
			return false;
		}

		return true;
	}

	/**
	 * Calculates the number of Players on the Map
	 * @return numPlayers -> int
	 */
	public int numPlayers() {
		int numPlayers = 0;
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Player) {
					numPlayers++;
				}
			}
		}

		return numPlayers;
	}

	/**
	 * Calculates the number of Doors on the Map
	 * @return numDoors -> int
	 */
	public int numDoors() {
		int numDoors = 0;
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Door) {
					numDoors++;
				}
			}

		}

		return numDoors;
	}
	
	/**
	 * Calculates the number of Keys on the Map
	 * @return numKeys -> int
	 */
	public int numKeys() {
		int numKeys = 0;
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Key) {
					numKeys++;
				}
			}

		}

		return numKeys;
	}
	
	/**
	 * Checks if there is at least one Exit on the Map
	 * @return boolean
	 */
	public boolean hasExit() {
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Exit) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if there is at least one Enemy on the Map
	 * @return boolean
	 */
	public boolean hasEnemies() {
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Enemy) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if there is at least one Treasure on the Map
	 * @return boolean
	 */
	public boolean hasTreasure() {
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Treasure) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if there is at least one Switch AND Boulder on the Map
	 * @return boolean
	 */
	public boolean hasSwitchandBoulder() {
		boolean hasSwitch = false;
		boolean hasBoulder = false;
		for (Node n : levelMap.getChildren()) {
			if (n.getId() != null) {
				Entity e = getEntity(n.getId());
				if (e != null && e instanceof Switch) {
					hasSwitch = true;
				} else if (e != null && e instanceof Boulder) {
					hasBoulder = true;
				}
			}
		}

		return (hasSwitch && hasBoulder);
	}

}