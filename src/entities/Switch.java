package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 *
 */
public class Switch implements Entity {

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/floor_switch.png");
		i.setId("switch");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return false;
	}
	
}
