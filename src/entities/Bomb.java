/**
 * 
 */
package entities;

import java.awt.Point;
import java.util.ArrayList;

import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * Bomb class with methods for explosion and placement of bombs
 */
public class Bomb implements Weapon {

	static final int notLit = -1;
	private int countDown;
	private Point position;
	
	public Bomb(Point p) {
		this.countDown = notLit;
		this.position = p;
	}
	
	public Bomb() {
		this.countDown = notLit;
		this.position = new Point(0,0);
	}
	
	@Override
	public ImageView getPicture() {
		ImageView i;
		switch(this.countDown) {
		case 5:
			i = new ImageView("/images/bomb_unlit.png");
			break;
		case 4:
			i = new ImageView("/images/bomb_lit_1.png");
			 break;
		case 3:
			i = new ImageView("/images/bomb_lit_2.png");
			break;
		case 2:
			i = new ImageView("/images/bomb_lit_3.png");
			break;
		case 1:
			i = new ImageView("/images/bomb_lit_4.png");
			break;
		case 0:
			i = new ImageView();
			i.setImage(null);
			i.setId("empty");
			return i;
		default:
			i = new ImageView("/images/bomb_unlit.png");
			break;
		}
		i.setId("Bomb");
		return i;
	}

	/**
	 * Explodes the bomb, running destroySquare on each square surrounding the bomb, if it is an enemy, wall or boulder the object is destroyed
	 * @Pre map and e are valid and non-null
	 * @param map map containing entities and their location
	 * @param e list containing enemies and there location
	 * @return number signifying whether player was killed by bomb
	 * @Post map still contains border walls and a player
	 */
	public EntityState explode(Entity[][] map, ArrayList<Character> e) {
		int x = this.position.x;
		int y = this.position.y;
		EntityState isAlive = EntityState.Alive;
		x--;
		y--;
		//check upper left
		EntityState state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		
		x++;
		//check upper
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		
		x++;
		//check upper right
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		
		x -= 2;
		y++;
		//check left
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		
		x += 2;
		//check right
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;

		x -= 2;
		y++;
		//check lower left
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;

		x++;
		//check lower
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		
		x++;
		//check lower right
		state = destroySquare(e, map, x, y);
		if (state == EntityState.Dead) isAlive = EntityState.Dead;
		map[this.position.x][this.position.y] = null;
		return isAlive;
	}

	/**
	 * looks at a specific square in the map and removes the entity in the square if it is destroyable or killable
	 * @Pre map and e must be valid and non-null; x and y must be valid coordinate values between 1 and dungeonSize static variable
	 * @param e list of enemies in map and there locations
	 * @param map the map containing the entities and there locations
	 * @param x position value x coordinate
	 * @param y position value y coordinate
	 * @return a number specifying if the player is still alive or not
	 * @Post map still contains border walls and a player
	 */
	private EntityState destroySquare(ArrayList<Character> e, Entity[][] map, int x, int y) {
		if (map[x][y] != null) {
			if (x != 0 && x != (Dungeon.dungeonSize -1) && y != 0 && y != (Dungeon.dungeonSize-1)) {
				if (e.contains(map[x][y])) {
					e.remove(map[x][y]);
					Enemy en = (Enemy) map[x][y];
					map[x][y] = en.getOnTile();
				} else if ((map[x][y] instanceof Wall) || (map[x][y] instanceof Boulder)) {
					map[x][y] = null;
				} else if (map[x][y] instanceof Player) {
					return EntityState.Dead;
				}
			}
		}
		return EntityState.Alive;
	}
	
	@Override
	public boolean canMoveThrough() {
		if (this.countDown != notLit) return false;
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		if (this.countDown == notLit) {
			inv.addBomb();
		}
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
	
	@Override
	public String toString() {
		return "Bomb ";
	}

	/**
	 * Function to get the Bomb's countdown
	 * @return countDown -> int
	 */
	public int getCountDown() {
		return countDown;
	}
	
	/**
	 * Function to start the Bomb's countdown
	 */
	public void lightBomb() {
		this.countDown = 5;
	}
	
	/**
	 * Checks if a bomb is ready to explode, otherwise if lit it reduces the countdown
	 * @return if bomb ready to explode
	 */
	public boolean checkBomb() {
		if (this.countDown == 0) {
			this.countDown = notLit;
			return true;
		} else if (this.countDown != notLit) {
			this.countDown--;
		}
		return false;
	}

	/**
	 * Fucntion to get the Bomb's position
	 * @return position -> Point
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Function to set the Bomb's position
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void setPosition(int x, int y) {
		this.position.setLocation(x, y);;
	}

	/**
	 * Checks whether the Bomb is Lit or Not
	 * @return boolean
	 */
	public boolean isLit() {
		return (this.countDown != notLit);
	}
	
	/**
	 * Places Bomb in specified direction and lights it, it will explode in 5 turns 
	 */
	@Override
	public void useWeapon(Entity[][] map, char dir, Point position, ArrayList<Character> e) {
		int x = position.x;
		int y = position.y;
		switch(dir) {
		case 'u': 
			if(map[x-1][y] == null) {
				setPosition(x-1, y);
				map[x-1][y]= this; 
				lightBomb();
			}
			break;
		case 'l': 
			if(map[x][y-1] == null) {
				setPosition(x, y-1);
				map[x][y-1]= this;
				lightBomb();
			}
			break;
		case 'd': 
			if(map[x+1][y] == null) {
				setPosition(x+1, y);
				map[x+1][y]= this;
				lightBomb();
			}
			break;
		case 'r': 
			if(map[x][y+1] == null) {
				setPosition(x, y+1);
				map[x][y+1]= this;
				lightBomb();
			}
			break;
		}
		
	}
	
	/**
	 * Stops the Bomb's countdown and sets it to NotLit
	 */
	public void resetBomb() {
		this.countDown = notLit;
	}
}
