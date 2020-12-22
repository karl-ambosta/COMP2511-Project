package entities;

import java.awt.Point;
import java.util.ArrayList;
import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 *
 */
public class Sword implements Weapon{

	private int usesLeft;
	
	public Sword() {
		this.usesLeft = 5;
	}
	
	//Used for testing
	public Sword(int i) {
		this.usesLeft = i;
	}
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/sword.png");
		i.setId("sword");
		return i;
	}

	/**
	 * Use sword in a specified direction, killing an enemy if one exists there, using one of its uses unless the square is empty
	 */
	@Override
	public void useWeapon(Entity[][] map, char dir, Point position, ArrayList<Character> e) {
		int x = position.x;
		int y = position.y;
		switch(dir) {
		case 'l':
			if (map[x][y-1] == null) {
				return;
			} else if (e.contains(map[x][y-1])) {
				e.remove(map[x][y-1]);
				Enemy en = (Enemy) map[x][y-1];
				map[x][y-1] = en.getOnTile();
			}
			this.usesLeft--;
			break;
		case 'u':
			if (map[x-1][y] == null) {
				return;
			} else if (e.contains(map[x-1][y])) {
				e.remove(map[x-1][y]);
				Enemy en = (Enemy) map[x-1][y];
				map[x-1][y] = en.getOnTile();
			}
			this.usesLeft--;
			break;
		case 'r':
			if (map[x][y+1] == null) {
				return;
			} else if (e.contains(map[x][y+1])) {
				e.remove(map[x][y+1]);
				Enemy en = (Enemy) map[x][y+1];
				map[x][y+1] = en.getOnTile();
			}
			this.usesLeft--;
			break;
		case 'd':
			if (map[x+1][y] == null) {
				return;
			} else if (e.contains(map[x+1][y])) {
				e.remove(map[x+1][y]);
				Enemy en = (Enemy) map[x+1][y];
				map[x+1][y] = en.getOnTile();
			}
			this.usesLeft--;
			break;
		}
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		inv.addSword(this);
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		if(inv.getSword() == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Function to get the number of uses left for this Sword
	 * @return int -> usesLeft
	 */
	public int getUsesLeft() {
		return usesLeft;
	}
	
	@Override
	public String toString() {
		return "Sword with " + this.usesLeft + " uses left ";
	}
	
	/**
	 * Function to reset the uses of the Sword back to 5
	 */
	public void resetUses() {
		this.usesLeft = 5;
	}
	
	
}
