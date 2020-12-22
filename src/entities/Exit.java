package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * Class for level Exit, passing onto this tile in dungeon completes level
 */
public class Exit implements Entity {
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/exit.png");
		i.setId("exit");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		return EntityState.Complete;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
}
