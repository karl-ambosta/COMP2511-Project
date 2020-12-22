package controllers;

import java.util.ArrayList;

import core.Dungeon;
import core.Inventory;
import core.LevelList;
import entities.Entity;
import entities.Player;
import entities.Wall;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import screens.CreatorScreen;
import screens.LevelScreen;

public class DungeonScreenController {

	private static final int maxDungeonSize = 20;
	private Stage currStage;
	private Dungeon dungeon;
	private Dungeon copyDung;
	private Entity[][] originalMap;
	private LevelList levels;

	@FXML
	Button exitButton;

	@FXML
	GridPane levelMap;

	@FXML
	TilePane dungeonLayout;

	@FXML
	Label weaponText;

	@FXML
	Label treasureCount;

	@FXML
	Label swordText;

	@FXML
	Label arrowCount;

	@FXML
	Label bombCount;

	@FXML
	Label hoverPotion;

	@FXML
	Label invinciPotion;

	@FXML
	Label keyNumber;

	@FXML
	Label endMessage;

	@FXML
	Button returnCreator;
	
	@FXML
	ImageView background;
	
	@FXML
	Label methodMessage;

	boolean creator;

	public DungeonScreenController(Stage s, Dungeon d, LevelList l, boolean creator) {
		currStage = s;
		dungeon = d;
		this.levels = l;
		this.creator = creator;
		ArrayList<Integer> dim = d.getDimensions();
		this.copyDung = new Dungeon(dim.get(0), dim.get(1));
		copyDung.setMap(l.copyMap(d.getMap()));
	}

	@FXML
	public void initialize() {
		// Need to initialise the dungeon into the TilePane
		Entity[][] map = dungeon.getMap();
		ArrayList<Integer> dimensions = dungeon.getDimensions();

		originalMap = new Entity[dimensions.get(0)][dimensions.get(1)];

		for (int i = 0; i < dimensions.get(0); i++) {
			for (int j = 0; j < dimensions.get(1); j++) {
				originalMap[i][j] = map[i][j];
			}
		}
		
		dungeon.setMap(originalMap);

		getDungeonLayout(map, dimensions);
		setItemText();
		returnCreator.setVisible(creator);
		background.setPreserveRatio(false);
		background.setFitHeight(22*(dimensions.get(0)-2));
		background.setFitWidth(22*(dimensions.get(1)-2));
		
		setMethodMessage(dungeon.getMethod());
	}

	/**
	 * Function to get the key pressed and direct to the corresponding control
	 * @param key -> KeyEvent
	 */
	@FXML
	public void getKey(KeyEvent key) {
		ArrayList<Integer> dimensions = dungeon.getDimensions();
		KeyCode code = key.getCode();

		if (code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT) { // Move Player
			handlePlayerMovement(code);
		} else if (code == KeyCode.Q) { // Skip Turn
			handlePlayerMovement(code);
		} else if (code == KeyCode.J || code == KeyCode.K || code == KeyCode.L) { // Item Drop
			handleItemDrop(code);
		} else if (code == KeyCode.W || code == KeyCode.S || code == KeyCode.A || code == KeyCode.D) { // Use Weapon
			handleAttack(code);
		} else if (code == KeyCode.F) { // Cycle Weapon
			handleWeaponCycle();
		}

		getDungeonLayout(dungeon.getMap(), dimensions);
		setItemText();
		checkCompleteOrDead();
	}

	/** 
	 * Returns to the Levels Screen
	 */
	@FXML
	public void backtoLevels() {
		dungeon.reset();
		LevelScreen ls = new LevelScreen(currStage, levels);
		ls.start();
	}

	/**
	 * Function to handle the Player's movement
	 * @param code -> KeyCode
	 */
	public void handlePlayerMovement(KeyCode code) {
		if (code == KeyCode.UP) {
			dungeon.movePlayer('u');
		} else if (code == KeyCode.DOWN) {
			dungeon.movePlayer('d');
		} else if (code == KeyCode.LEFT) {
			dungeon.movePlayer('l');
		} else if (code == KeyCode.RIGHT) {
			dungeon.movePlayer('r');
		}

		dungeon.move();

		if (dungeon.checkCompletion()) {
			endMessage.setText("COMPLETED!");
		} else if (dungeon.get_isDead()) {
			endMessage.setText("GAME OVER!!!");
		} else {
			endMessage.setText("");
		}
	}

	/**
	 * Function to handle the Player's Weapon cycle
	 */
	public void handleWeaponCycle() {
		dungeon.cycleWeapon();
	}

	/**
	 * Function to handle the Player's Attack
	 * @param code -> KeyCode
	 */
	public void handleAttack(KeyCode code) {
		if (code == KeyCode.W) {
			dungeon.attack('u', dungeon.getWeapon());
		} else if (code == KeyCode.S) {
			dungeon.attack('d', dungeon.getWeapon());
		} else if (code == KeyCode.A) {
			dungeon.attack('l', dungeon.getWeapon());
		} else if (code == KeyCode.D) {
			dungeon.attack('r', dungeon.getWeapon());
		}
	}

	/**
	 * Function to handle the Player's Item Drop
	 * @param code -> KeyCode
	 */
	public void handleItemDrop(KeyCode code) {
		if (code == KeyCode.J) {
			dungeon.getPlayer().dropSword();
		} else if (code == KeyCode.K) {
			dungeon.getPlayer().dropKey();
		} else if (code == KeyCode.L) {
			if (dungeon.getPlayer().getInventory().getBombs() > 0) {
				dungeon.getPlayer().dropBomb();
			}
		}
	}
	
	/**
	 * Function to handle the Dungeon's reset
	 * @param code -> KeyCode
	 */
	public void handleReset() {
		dungeon.setMap(this.originalMap);
		ArrayList<Integer> dimensions = dungeon.getDimensions();

		dungeon.reset();
		getDungeonLayout(this.originalMap, dimensions);
		setItemText();
		endMessage.setText("");
	}

	/**
	 * Displays the Inventory text on the screen
	 */
	public void setItemText() {
		Player p = dungeon.getPlayer();
		Inventory inv = p.getInventory();
		String b = null;

		treasureCount.setText("x " + inv.getTreasure());

		b = String.valueOf(inv.hasSword());
		if (inv.hasSword()) {
			swordText.setText("true (" + inv.getSword().getUsesLeft() + " uses left)");
		} else {
			swordText.setText("false");
		}

		arrowCount.setText("x " + inv.getArrows());
		bombCount.setText("x " + inv.getBombs());

		b = String.valueOf(inv.hasHoverPotion());
		hoverPotion.setText(b);

		b = String.valueOf(inv.hasInvincPotion());
		String turns = "";
		if (inv.hasInvincPotion()) {
			turns = "(" + (inv.getInvinciPotion().getTurnsLeft() + 1) + " turns left" + ")";
		}
		invinciPotion.setText(b + " " + turns);

		if (inv.getKey() != null) {
			keyNumber.setText("Key Num:" + inv.getKey().getKeyNo());
		} else {
			keyNumber.setText("No key");
		}

		weaponText.setText(dungeon.getWeaponString());
	}

	/**
	 * Function to display the Dungeon's layout on the GridPane
	 * @param map
	 * @param dimensions
	 */
	public void getDungeonLayout(Entity[][] map, ArrayList<Integer> dimensions) {
		for (int row = 0; row < maxDungeonSize; row++) {
			for (int col = 0; col < maxDungeonSize; col++) {
				if(row > dimensions.get(0)-1 || col > dimensions.get(1)-1) {
					continue;
				} else if(row == dimensions.get(0)-1 || col == dimensions.get(1)-1) {
					Node n = getNode(row, col);
					removeNode(n);
					map[row][col] = new Wall();
					ImageView img = new ImageView("/images/wall.png");
					img.setId("wall");
			        img.setFitWidth(22);
					img.setFitHeight(22);
					levelMap.add(img, col, row);
			    } else if(map[row][col] != null) {
			    	Node n = getNode(row, col);
					removeNode(n);
					Entity entity = map[row][col];
					ImageView img = new ImageView(map[row][col].getPicture().getImage());
					img.setId(entity.getPicture().getId());
					img.setImage(entity.getPicture().getImage());
			        img.setId(entity.getPicture().getId());
					img.setFitWidth(22);
					img.setFitHeight(22);
					levelMap.add(img, col, row);
				} else {
					Node n = getNode(row, col);
					removeNode(n);
				}
			}
		}
	}
	
	/**
	 * Function to get the GridPane's node at the corresponding position
	 * @param row -> int
	 * @param column -> int
	 * @return node -> Node
	 */
	public Node getNode(int row, int column) {
	    Node result = null;
	    ObservableList<Node> childrens = levelMap.getChildren();

	    for (Node node : childrens) {
	        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
	            result = node;
	            break;
	        }
	    }

	    return result;
	}
	
	/**
	 * Function to remove the Node form the GridPane
	 * @param node -> Node
	 */
	public void removeNode(Node node) {
		levelMap.getChildren().remove(node);
	}

	/**
	 * Function to return to the Creator
	 */
	public void returnCreator() {
		dungeon.reset();
		CreatorScreen cs = new CreatorScreen(currStage, levels, copyDung);
		cs.start();
	}
	
	/**
	 * Function to check if the Dungeon is Complete or the Player is Dead
	 */
	public void checkCompleteOrDead() {
		if(dungeon.checkCompletion()) {
			endMessage.setText("COMPLETED!");
		} else if(dungeon.get_isDead()) {
			endMessage.setText("GAME OVER!!!");
		} else {
			endMessage.setText("");
		}
	}
	
	/**
	 * Function to set the method Message
	 * @param method -> int
	 */
	public void setMethodMessage(int method) {
		switch(method) {
		case 0:
			methodMessage.setText("Reach the Exit!");
			break;
		case 1:
			methodMessage.setText("Collect all the Treasure!");
			break;
		case 2:
			methodMessage.setText("Push Boulders onto all Switches!");
			break;
		case 3:
			methodMessage.setText("Eliminate all Enemies");
			break;
		}
	}
}
