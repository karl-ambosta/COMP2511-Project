/**
 * 
 */
package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 * Class for door with methods for door
 */
public class Door implements Entity {

	private boolean open;
	private int DoorNo;
	private boolean hasKey;
	
	public Door() {
		this.open = false;
	}
	
	@Override
	public ImageView getPicture() {
		ImageView i;
		if(open) {
			i = new ImageView("/images/open_door.png");
		} else {
			i = new ImageView("/images/closed_door.png");
		}
		
		i.setId("door");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return this.open;
	}
	
	public void setDoorNo(int doorNo) {
		this.DoorNo = doorNo;
	}
	
	public int getDoorNo() {
		return this.DoorNo;
	}
	
	public void openDoor(int keyNo) {
		if(keyNo == DoorNo) {
			this.open = true;
		}
	}

	/**
	 * Used to open door if player has correct key
	 * @param Inventory inv
	 */
	@Override
	public EntityState interact(Inventory inv) {
		int keyNo = inv.getKey().getKeyNo();
		if(keyNo == DoorNo) {
			this.open = true;
			inv.dropKey();
		}
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return false;
	}
	
	/**
	 * Returns the hasKey variable
	 * @return
	 */
	public boolean getHasKey() {
		return this.hasKey;
	}
	
	/**
	 * Set the hasKey variable to true
	 */
	public void setHasKey() {
		this.hasKey = true;
	}
	
	/**
	 * Function to close the door after the Dungeon is completed
	 */
	public void closeDoor() {
		this.open = false;
	}
}
