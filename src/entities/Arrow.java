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
 * Arrow class with methods for using arrows
 *
 */
public class Arrow implements Weapon{

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/arrow.png");
		i.setId("Arrow");
		return i;
		
	}

	/**
	 * Arrow continues in specified direction until it impacts an entity, killing it if an enemy or stopping it otherwise
	 * @pre Map, dir, position, e are valid and non-null, dir is set to the designated input
	 * @param Entity[][] map
	 * @param char dir
	 * @param Point position
	 * @param ArrayList<Character> e
	 * @post map still contains border walls and a player
	 */
	@Override
	public void useWeapon(Entity[][] map, char dir, Point position, ArrayList<Character> e) {
		int x = position.x;
		int y = position.y;
		switch(dir) {
		case 'l':
			y--;
			while (y > 0) {
				if (e.contains(map[x][y])) {
					e.remove(map[x][y]);
					Enemy en = (Enemy) map[x][y];
					map[x][y] = en.getOnTile();
					break;
				} else if (map[x][y] instanceof Wall || map[x][y] instanceof Boulder || map[x][y] instanceof Door || map[x][y] instanceof Exit) {
					break;
				}
				y--;
			}
			break;
		case 'u':
			x--;
			while (x > 0) {
				if (e.contains(map[x][y])) {
					e.remove(map[x][y]);
					Enemy en = (Enemy) map[x][y];
					map[x][y] = en.getOnTile();
					break;
				} else if (map[x][y] instanceof Wall || map[x][y] instanceof Boulder || map[x][y] instanceof Door || map[x][y] instanceof Exit) {
					break;
				}
				x--;
			}
			break;
		case 'r':
			y++;
			while (y < (Dungeon.dungeonSize - 1)) {
				if (e.contains(map[x][y])) {
					e.remove(map[x][y]);
					Enemy en = (Enemy) map[x][y];
					map[x][y] = en.getOnTile();
					break;
				} else if (map[x][y] instanceof Wall || map[x][y] instanceof Boulder || map[x][y] instanceof Door || map[x][y] instanceof Exit) {
					break;
				}
				y++;
			}
			break;
		case 'd':
			x++;
			while (x < (Dungeon.dungeonSize - 1)) {
				if (e.contains(map[x][y])) {
					e.remove(map[x][y]);
					Enemy en = (Enemy) map[x][y];
					map[x][y] = en.getOnTile();
					break;
				} else if (map[x][y] instanceof Wall || map[x][y] instanceof Boulder || map[x][y] instanceof Door || map[x][y] instanceof Exit) {
					break;
				}
				x++;
			}
			break;
		}
		
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		inv.addArrow();	
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
	
	@Override
	public String toString() {
		return "Arrow ";
	}
}
