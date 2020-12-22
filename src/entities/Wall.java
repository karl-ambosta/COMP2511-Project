package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * 
 *
 */
public class Wall implements Entity {

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/wall.png");
		i.setId("wall");
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
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
}
