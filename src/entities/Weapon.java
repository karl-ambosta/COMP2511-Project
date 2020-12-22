/**
 * 
 */
package entities;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 *
 */
public interface Weapon extends Entity {
	
	/**
	 * method runs behavior of weapon in a specified direction
	 * @Pre dir must be a valid direction (u, d, r, l), map, position and e must be valid and non-null
	 * @param map contains map
	 * @param dir char specifying direction
	 * @param position position of player
	 * @param e contains list of enemies to ensure they are known
	 * @Post map border walls must not be removed and still contain non-destroyable entities
	 */
	public void useWeapon(Entity[][] map, char dir, Point position, ArrayList<Character> e);
	
}
