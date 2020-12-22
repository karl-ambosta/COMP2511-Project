package entities;

import java.awt.Point;
import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * Boulder class with methods to move boulder and to check whether boulder is placed on switch
 *
 */
public class Boulder implements Entity {

	private Point position;
	private boolean isOnSwitch = false;

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/boulder.png");
		i.setId("boulder");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return false;
	}

	@Override
	public EntityState interact(Inventory inv) {
		return EntityState.Alive;
	}

	/** 
	 * Function to get the boulder's position
	 * @return position -> Point
	 */
	public Point getPosition() {
		return position;
	}

	/** 
	 * Function to set the boulder's position
	 * @param position -> Point
	 */
	public void setPosition(Point position) {
		this.position = position;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
	
	/**
	 * Sets the boulder on a Switch
	 * @param isOnSwitch -> boolean
	 */
	public void setIsOnSwitch(boolean isOnSwitch) {
		this.isOnSwitch = isOnSwitch;
	}
	
	/**
	 * Get whether the Boulder is on a Switch
	 * @return
	 */
	public boolean getIsOnSwitch() {
		return this.isOnSwitch;
	}
	
	/**
	 * Moves the boulder if not obstructed based on the designated input of the player
	 * @param Entity[][] Map
	 * @param Point position -> Dungeon
	 * @return whether move is possible
	 */
	public boolean move(Entity[][] Map, Point position) {
		int x = position.x;
		int y = position.y;
		if (Map[x][y] instanceof Wall || Map[x][y] instanceof Enemy || Map[x][y] instanceof Door || Map[x][y] instanceof Treasure || Map[x][y] instanceof Key || Map[x][y] instanceof Exit || Map[x][y] instanceof Boulder) return false;
		else if (Map[x][y] instanceof Pit) Map[x][y] = new Pit();
		else {
			if (this.getIsOnSwitch()) Map[this.getPosition().x][this.getPosition().y] = new Switch();
			if (Map[x][y] instanceof Switch) this.setIsOnSwitch(true);
			this.setPosition(new Point(x,y));
			Map[x][y] = this;
		}
		return true;
	}
}
