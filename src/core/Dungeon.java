/**
 * 
 */
package core;

import java.awt.Point;
import java.util.ArrayList;

import entities.Arrow;
import entities.Bomb;
import entities.Boulder;
import entities.Character;
import entities.Coward;
import entities.Door;
import entities.Enemy;
import entities.Entity;
import entities.EntityState;
import entities.Exit;
import entities.Hound;
import entities.HoverPotion;
import entities.Hunter;
import entities.InvincibilityPotion;
import entities.Key;
import entities.Pit;
import entities.Player;
import entities.Strategist;
import entities.Switch;
import entities.Sword;
import entities.Treasure;
import entities.Wall;
import entities.Weapon;

/**
 *
 *
 */
public class Dungeon {

	public static final int dungeonSize = 20;
	private static final int EXIT  = 0;
	private static final int TREASURE = 1;
	private static final int SWITCH = 2;
	private static final int ENEMIES = 3;
	
	private String name;
	private Entity[][] Map;
	private ArrayList<Character> Enemies;
	private Point playerLocation;
	private Point playerStart;
	private Player player;
	private boolean isComplete;
	private boolean isDead;
	private ArrayList<Bomb> bombs;
	private int method;
	private int rowNo;
	private int colNo;
	private ArrayList<Door> doors;
	private ArrayList<Key> keys;
	
	/**
	 * Dungeon constructor that creates a blank 20x20 Dungeon grid
	 */
	public Dungeon() {
		this.Enemies = new ArrayList<Character>();
		this.player = new Player();
		setEmptyDungeon();
		this.isComplete = false;
		this.bombs = new ArrayList<Bomb>();
		this.method = EXIT;
		rowNo = 20;
		colNo = 20;
		doors = new ArrayList<Door>();
		keys = new ArrayList<Key>();
	}
	
	/**
	 * Dungeon constructor that creates a blank Dungeon grid to the size specified
	 * @param rows
	 * @param columns
	 */
	public Dungeon(int rows, int columns) {
		this.Enemies = new ArrayList<Character>();
		this.player = new Player();
		setEmptyDungeon();
		this.isComplete = false;
		this.bombs = new ArrayList<Bomb>();
		this.method = EXIT;
		rowNo = rows;
		colNo = columns;
		doors = new ArrayList<Door>();
		keys = new ArrayList<Key>();
	}
	
//	 for testing purposes creates a set dungeon
	public Dungeon(String testDungeon) {
		this.playerStart = new Point(1,1);
		this.playerLocation = new Point(1,1);
		this.bombs = new ArrayList<Bomb>();

		Point hunter_position = new Point(10,10);
		Point hound_position = new Point(2,10);
		Point strategist_position = new Point(9,14);
		this.Enemies = new ArrayList<Character>();
		this.player = new Player();
		Hound hound = new Hound();
		Hunter hunter = new Hunter();
		Strategist strategist = new Strategist();
		
		hunter.setPosition(hunter_position);
		hound.setPosition(hound_position);
		strategist.setPosition(strategist_position);
		addEnemy(hunter);
		addEnemy(hound);
		addEnemy(strategist);
		
		player.getInventory().addArrow();
		setEmptyDungeon();
		this.Map[1][1] = this.player;
		Boulder boulder = new Boulder();
		boulder.setPosition(new Point(10,5));

		player.getInventory().addSword(new Sword());
		player.setEquiped(player.getInventory().getSword());
		this.isComplete = false;
		this.isDead = false;
		this.Map[1][5] = new Bomb();
		this.Map[6][3] = new Wall();
		this.Map[6][4] = new Wall();
		this.Map[6][5] = new Boulder();
		this.Map[7][5] = new Treasure();

		this.Map[2][1] = new Sword();
		this.Map[3][1] = new Sword();
		this.Map[1][2] = new Treasure();
		this.Map[2][1] = new Sword();
		this.Map[2][1] = new InvincibilityPotion();
		this.Map[5][1] = new Exit();
		Key key = new Key();
		Key key2 = new Key();
		Door door = new Door();
		Door door2 = new Door();
		Door door3 = new Door();
		this.Map[3][1] = key;
		this.Map[4][1] = door;
		this.Map[3][2] = door2;
		
		key.setKeyNo(11);
		door.setDoorNo(11);
		door2.setDoorNo(15);
		this.Map[4][1] = key2;
		this.Map[6][1] = door;
		this.Map[5][2] = door3;
		
		this.Map[8][2] = new HoverPotion();
		this.Map[9][1] = new Pit();
		
		
		boulder.setPosition(new Point(2,2));
		this.Map[10][10] = hunter;
		this.Map[2][10] = hound;
		this.Map[2][2] = boulder;
		this.Map[9][14] = strategist;
		Pit pit = new Pit();
		this.Map[3][3] = pit;
		this.Map[9][1] = new Sword();
		
		this.Map[2][3] = new Switch();
		this.method = ENEMIES;
		rowNo = 20;
		colNo = 20;
		doors = new ArrayList<Door>();
		keys = new ArrayList<Key>();
		addDoorandKey(door);
		addDoorandKey(door2);
		addDoorandKey(key);
		addDoorandKey(key2);
		addDoorandKey(door3);
	}
	
	/**
	 * sets empty dungeon with walls
	 * 
	 */
	public void setEmptyDungeon() {
		this.Map = new Entity[dungeonSize][dungeonSize];
		for (int x = 0; x < dungeonSize; x++) {
			for (int y = 0; y < dungeonSize; y++) {
				//Set the edges of the Dungeon to be Walls
				if (x == 0 || x == dungeonSize-1 || y == 0 || y == dungeonSize-1) {
					this.Map[x][y] = new Wall();
				} else {
					this.Map[x][y] = null;
				}
			}
		}
	}

	/**
	 * Function that moves all the enemies with the Dungeon
	 */
	public void move() {
		ArrayList<EntityState> returns = new ArrayList<EntityState>();
		Bomb dest = null;
		//First check if any Enemies are killed by a bomb
		if(!checkCompletion() && !isDead) {
			if(!(this.bombs.isEmpty())) {
				for (Bomb b : this.bombs) {
					if (b.checkBomb()) {
						returns.add(b.explode(Map, Enemies));
						dest = b;
					}
				}
				bombs.remove(dest);
			}

			for (Character C : this.Enemies) {
				returns.add(C.move(this, player.getPosition()));
			}
		}
		
		// If any enemies have been killed, remove them from the Dungeon and the Enemy List
		while(returns.contains(EntityState.EnemyDead)) {
			int index = returns.indexOf(EntityState.EnemyDead);
			Enemy e = (Enemy)  this.Enemies.get(index);
			Map[e.getPosition().x][e.getPosition().y] = null;
			this.Enemies.remove((Character) e);
			returns.add(index, EntityState.Alive);
			returns.remove(index+1);
		}
		
		//If the Player has been killed, set the Dungeon to GameOver
		if(returns.contains(EntityState.Dead)) {
			this.isDead = true;
		}
	}
	
	/**
	 * Function that returns the player class inside the Dungeon
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Function to move the player in the required direction within the Dungeon
	 * @param dir -> Direction to move the player, 'u', 'd', 'l' or 'r'
	 * @return EntityState -> status of the player, Dead or Alive
	 */
	public EntityState movePlayer(char dir) {
		if(checkCompletion() || this.get_isDead()) {
			return EntityState.Dead;
		}
		
		player.setDir(dir);
		
		EntityState state = player.move(this, player.getPosition());
		checkEnemies();
		if(state == EntityState.Complete) {
			isComplete = true;
		} else if (state == EntityState.Dead) {
			isDead = true;
		}
		return state;
	}
	
	/**
	 * Function to set the starting position of the Player
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void setStartLocation(int x, int y) {
		this.playerStart.setLocation(x, y);
		this.playerLocation.setLocation(x, y);
	}
	
	/**
	 * Function to print the layout of the Dungeon
	 */
	public void printDungeon() {
		for(int i = 0; i < dungeonSize; i++) {
			for(int j = 0; j < dungeonSize; j++) {
				if(Map[i][j] == null) {
					System.out.print("__   ");
				} else {
					System.out.print(Map[i][j].getPicture() + "   ");
				}
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Function to get the Dungeon Layout as a String
	 * @return dungeon -> String layout of Dungeon
	 */
	public String DungeontoString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < dungeonSize; i++) {
			for(int j = 0; j < dungeonSize; j++) {
				if(Map[i][j] == null) {
					sb.append("__   ");
				} else {
					sb.append(Map[i][j].getPicture() + "   ");
				}
			}
			sb.append("\n");
		}
		
		String dungeon = sb.toString();
		//dungeon = "<html> <p>" + dungeon.replaceAll("\n", "<br/>").replaceAll("   ", "&emsp;") + "</p></html>";
		return dungeon;
	}

	/**
	 * Adds an Entity e to the location given in the Dungeon
	 * @param e -> entity to add
	 * @param p -> point to add entity to
	 */
	public void addEntity(Entity e, Point p) {
		if(this.isValid_placement(e)) {
			this.Map[p.x][p.y] = e;
			
		}
		if(e instanceof Enemy) {
			Enemy c = (Enemy) e;
			c.setPosition(p);
			addEnemy((Enemy) e);
		} else if (e instanceof Player) {
			this.player = (Player) e;
			this.player.setPosition(p);
		} else if(e instanceof Door) {
			addDoorandKey((Door) e);
		} else if(e instanceof Key) {
			addDoorandKey((Key) e);
		}
	}
	
	/**
	 * Removes the entity in the square unless it is a border square
	 * @param p point of entity to remove
	 */
	public void removeEntity(Point p) {
		if(p.x == 0 || p.y == 0 || p.x == dungeonSize-1 || p.y == dungeonSize-1) {
			return;
		} else {
			this.Map[p.x][p.y] = null;
		}
	}
	
	/**
	 * Takes in weapon to use and direction to use it in and runs the useWeapon function
	 * @Pre weapon is not null and direction is a char depicting direction (u = up, d = down, r = right, l = left)
	 * @param dir -> direction to attack in
	 * @param w -> weapon to be used
	 */
	public void attack(char dir, Weapon w) {
		if (player.getInventory().containsWeapon(w)) {
			if (w instanceof Bomb) {
				Bomb b = new Bomb();
				b.useWeapon(Map, dir, player.getPosition(), this.Enemies);
				if(b.isLit()) {
					player.getInventory().dropBomb();
					this.bombs.add(b);
				}
				
			} else {
				w.useWeapon(Map, dir, player.getPosition(), this.Enemies);
				if (w instanceof Arrow) {
					player.getInventory().dropArrow();
				}
			}
		}
		player.discardSword();
	}
	
	
	public Weapon getWeapon() {
		return player.getEquiped();
	}
	
	/**
	 * Gets information about the currently equipped Weapon
	 * @return String -> containing weapon information
	 */
	public String getWeaponString() {
		if (player.getEquiped() == null) {
			return "Player has no equiped weapon";
		}
		StringBuilder sb = new StringBuilder(player.getEquiped().toString() + "equiped");
		if (player.getEquiped() instanceof Bomb) {
			sb.append(" and has " + player.getInventory().getBombs() + " left");
		} else if (player.getEquiped() instanceof Arrow) {
			sb.append(" and has " + player.getInventory().getArrows() + " left");
		}
		return sb.toString();
	}

	/**
	 * Function to get whether a Dungeon is Complete
	 * @return boolean -> Completed or Not Completed
	 */
	public boolean get_isComplete() {
		return this.isComplete;
	}
	
	/**
	 * Function to check whether a Dungeon has satisfied it's completion method
	 * @return boolean -> Completed or Not Completed
	 */
	public boolean checkCompletion() {
		switch(this.method) {
		case EXIT:
			return get_isComplete();
		case TREASURE:
			// Checks if any Treasure is left in the Dungeon
			for(int i = 0; i < dungeonSize; i++) {
				for(int j = 0; j < dungeonSize; j++) {
					if (Map[i][j] instanceof Treasure) {
						return false;
					}
				}
			}
			for (Character c : this.Enemies) {
				Enemy en = (Enemy) c;
				if (en.getOnTile() instanceof Treasure) {
					return false;
				}
			}
			return true;
		case SWITCH:
			// Checks if any Switches are left in the Dungeon
			for(int i = 0; i < dungeonSize; i++) {
				for(int j = 0; j < dungeonSize; j++) {
					if (Map[i][j] instanceof Switch) {
						return false;
					}
				}
			}
			for (Character c : this.Enemies) {
				Enemy en = (Enemy) c;
				if (en.getOnTile() instanceof Switch) {
					return false;
				}
			}
			if (player.getOnTile() instanceof Switch) {
				return false;
			}
			return true;
		case ENEMIES:
			// Checks if any Enemies are left in the Dungeon
			return Enemies.isEmpty();
		}
		return false;
	}
	
	/**
	 * Checks if an Entity can be placed in the dungeon
	 * i.e. Only One Player, Max 3 Doors
	 * @param entity
	 * @return boolean
	 */
	public boolean isValid_placement(Entity entity) {
		int count = 0;
		
		for(int i = 0; i < dungeonSize; i++) {
			for(int j = 0; j < dungeonSize; j++) {
				if(this.Map[i][j] != null && entity.getClass().equals(Map[i][j].getClass())) {
					count++;
				}
			}
		}

		if(entity instanceof Player || entity instanceof Exit) {
			return (count == 0);
		} else if(entity instanceof Door) {
			return (count < 3);
		}		
		return true;
	}

	/**
	 * Function to return the Dungeon's Entity[][] map
	 * @return
	 */
	public Entity[][] getMap() {
		return this.Map;
	}

	/**
	 * Function to check whether the player has been kille
	 * @return boolean
	 */
	public boolean get_isDead() {
		return isDead;
	}
	
	/**
	 * Cycles between equipable Weapons
	 */
	public void cycleWeapon() {
		player.cycleWeapon();
	}
	
	/**
	 * Function to reload a dungeon to a specified Entity[][] map
	 * @param dungeon -> Dungeon to change
	 * @param newMap -> Entity[][] to change to
	 * @return dungeon -> altered Dungeon
	 */
	public Dungeon reloadDungeon(Dungeon dungeon, Entity[][] newMap) {
		Entity[][] map = dungeon.getMap();
		
		for(int i = 0; i < dungeonSize; i++) {
			for(int j = 0; j < dungeonSize; j++) {
				map[i][j] = newMap[i][j];
			}
		}
		dungeon.Map = map;
		return dungeon;
	}
	
	/**
	 * Function to reload this Dungeon to a specified Entity[][] map
	 * @param map -> Entity[][] map to change
	 * @return dungeon -> Dungeon with new map
	 */
	public void setMap(Entity[][] map) {
		setEmptyDungeon();
		this.Enemies.clear();
		ArrayList<Integer> d = this.getDimensions();
		for(int i = 0; i < d.get(0); i++) {
			for(int j = 0; j < d.get(1); j++) {
				if(map[i][j] instanceof Enemy) {
					Enemy c = (Enemy) map[i][j];
					c.setPosition(new Point(i,j));
					c.setOnTile(null);
					addEnemy(c);
				} else if(map[i][j] instanceof Player) {
					Player pl = (Player) map[i][j];
					pl.setPosition(new Point(i,j));
					this.player = pl;
					this.addEntity(pl, new Point(i,j));
				} else if(map[i][j] instanceof Door) {
					Door door = (Door) map[i][j];
					door.closeDoor();
					addDoorandKey(door);
				} else if(map[i][j] instanceof Key) {
					addDoorandKey((Key) map[i][j]);
				}
				
				this.Map[i][j] = map[i][j];
			}
		}
		

		this.isComplete = false;
		this.isDead = false;
	}
	
	/**
	 * Function to print the Layout of the Dungeon's Map
	 * @param map
	 */
	public void printMap(Entity[][] map) {
		for(int i = 0; i < dungeonSize; i++) {
			for(int j = 0; j < dungeonSize; j++) {
				if(map[i][j] == null) {
					System.out.print("__   ");
				} else {
					System.out.print(map[i][j].getPicture() + "   ");
				}
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Function to check if any Enemies are on the same square as the Player
	 * and removes them from the Enemy List
	 */
	public void checkEnemies() {
		Enemy En = null;
		for (Character e : this.Enemies) {
			En = (Enemy) e;
			if (Map[En.getPosition().x][En.getPosition().y] instanceof Player) {
				this.Enemies.remove( (Character) En);
				break;
			}
		}	
	}
	
	/**
	 * Function to get the List of Enemies in this Dungeon
	 * @return ArrayList<Character> -> List of Enemies
	 */
	public ArrayList<Character> getEnemies(){
		return this.Enemies;
	}

	/**
	 * Function to get the Dungeon's completion method
	 * @return int -> representing completion method
	 */
	public int getMethod() {
		return method;
	}

	/**
	 * Function to set the Dungeon's completion method
	 * @param int -> representing method
	 */
	public void setMethod(int method) {
		this.method = method;
	}
	
	/**
	 * Function to get the dimensions of the Dungeon
	 * @return dimensions -> ArrayList<Integer>
	 */
	public ArrayList<Integer> getDimensions() {
		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		dimensions.add(rowNo);
		dimensions.add(colNo);
		return dimensions;
	}
	
	/**
	 * Sets the name of the Dungeon
	 * @param title
	 */
	public void setName(String title) {
		this.name = title;
	}
	
	/**
	 * Gets the name of the Dungeon
	 * @return String -> name of Dungeon
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the number of rows in the Dungeon
	 * @return int -> rows
	 */
	public int getRowNo() {
		return this.rowNo;
	}
	
	/**
	 * Gets the number of columns in the Dungeon
	 * @return int -> columns
	 */
	public int getColNo() {
		return this.colNo;
	}
	
	/**
	 * Function to add Enemy into the Dungeon
	 * @param e
	 */
	public void addEnemy(Enemy e) {
		if(e instanceof Hunter) {
			// Set the Hunter's id and (if possible) assign a Hound to the Hunter
			Hunter h = (Hunter) e;
			int numOfHunters = 1;
			for(Character c : this.Enemies) {
				if(c instanceof Hunter) {
					numOfHunters++;
				}
			}
			
			h.setHunterID(numOfHunters);
			this.assignHuntertoHound(h);
			this.Enemies.add(0, h);
		} else if(e instanceof Hound) {
			// Set the Hound's id and (if possible) assign a Hound to the Hunter
			Hound h = (Hound) e;
			int numOfHounds = 1;
			for(Character c : this.Enemies) {
				if(c instanceof Hound) {
					numOfHounds++;
				}
			}
			
			h.setHoundID(numOfHounds);
			this.assignHoundtoHunter(h);
			this.Enemies.add(this.Enemies.size(), h);
		} else {
			// Add Enemy to the enemy List
			this.Enemies.add((Character) e);
		}
	}
	
	/**
	 * Function to assign a Hound to a Hunter
	 * @param h -> Hound
	 */
	public void assignHoundtoHunter(Hound h) {
		for(Character c : this.Enemies) {
			if(c instanceof Hunter) {
				Hunter hunter = (Hunter) c;
				if(hunter.getID() == h.getID()) {
					hunter.changeHasHound();
					h.changeHasHunter();
					return;
				}
			}
		}
	}
	
	/**
	 * Function to assign a Hunter to a Hound
	 * @param h -> Hunter
	 */
	public void assignHuntertoHound(Hunter h) { 
		for(Character c : this.Enemies) {
			if(c instanceof Hound) {
				Hound hound = (Hound) c;
				if(hound.getID() == h.getID()) {
					hound.changeHasHunter();
					h.changeHasHound();
					return;
				}
			}
		}
	}
	
	/**
	 * Function to add a Door/Key into the Dungeon
	 * @param e -> Entity
	 */
	public void addDoorandKey(Entity e) {
		if(e instanceof Door) {
			// Set the Door's Number and (if possible) assign a Key to open this specific Door
			Door d = (Door) e;
			d.setDoorNo(doors.size());
			
			Key k = getUnAssignedKey();
			if(k != null) {
				k.setKeyNo(d.getDoorNo());
				k.setHasDoor();
				d.setHasKey();
			}
			
			doors.add(d);
		} else if(e instanceof Key) {
			// Set the Key's Number and (if possible) assign a Door that opens to this specific Key
			Key k = (Key) e;
			k.setKeyNo(keys.size());
			
			Door d = getUnAssignedDoor();
			if(d != null) {
				d.setDoorNo(k.getKeyNo());
				k.setHasDoor();
				d.setHasKey();
			}
			
			keys.add(k);
		}
	}
	
	/**
	 * Function to get a Key that has not been assigned to a Door
	 * @return k -> Key
	 */
	public Key getUnAssignedKey() {
		for(Key k : keys) {
			if(!k.getHasDoor()) {
				return k;
			}
		}
		
		return null;
	}
	
	/**
	 * Function to get a Door that has not been assigned a Key
	 * @return d -> Door
	 */
	public Door getUnAssignedDoor() {
		for(Door d : doors) {
			if(!d.getHasKey()) {
				return d;
			}
		}
		
		return null;
	}
	
	/**
	 * Function to print all the Door/Key Numbers and whether they have a corresponding Key/Door
	 */
	public void printDoorKeys() {
		for(Door d : doors) {
			System.out.println("Door Number: " + d.getDoorNo() + " hasKey: " + d.getHasKey());
		}
		
		for(Key k : keys) {
			System.out.println("Key Number: " + k.getKeyNo() + " hasDoor: " + k.getHasDoor());
		}
	}

	/**
	 * Function to reset the weapon's usage counter
	 */
	public void reset() {
		for (Entity[] e : this.Map) {
			for (Entity en : e) {
				if (en instanceof Sword) {
					((Sword) en).resetUses();
				} else if (en instanceof Bomb) {
					((Bomb) en).resetBomb();
				}
			}
		}
		if (player.getInventory().getSword() != null) {
			player.getInventory().getSword().resetUses();
			player.discardSword();
		}
		player.getInventory().reset();
	}
	
}



