package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 *
 */
public class Pit implements Entity {
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/pit.png");
		i.setId("pit");
		return i;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		if (inv.hasHoverPotion()) return EntityState.Alive;
		return EntityState.Dead;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		if (inv.hasHoverPotion()) {
			return false;
		}
		return true;
	}
}
