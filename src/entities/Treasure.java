package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 *
 */
public class Treasure implements Entity{

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/treasure.png");
		i.setId("treasure");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		inv.addTreasure();
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
}
