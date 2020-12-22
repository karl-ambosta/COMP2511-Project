package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

public class Key implements Entity {

	private int keyNo;
	private boolean hasDoor;
	
	/**
	 * Function to set the Key's number
	 * @param keyNo
	 */
	public void setKeyNo(int keyNo) {
		this.keyNo = keyNo;
	}
	
	/**
	 * Function to get the Key's number
	 * @return
	 */
	public int getKeyNo() {
		return this.keyNo;
	}

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/key.png");
		i.setId("key");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		inv.addKey(this);
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		if(inv.getKey() != null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets whether a Key has an associated Door
	 * @return boolean
	 */
	public boolean getHasDoor() {
		return this.hasDoor;
	}
	
	/**
	 * Sets hasDoor to true;
	 * @return boolean
	 */
	public void setHasDoor() {
		this.hasDoor = true;
	}
	
}
