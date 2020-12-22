package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * Class for potions with methods to use position
 *
 */
public class HoverPotion implements Potion{
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/hover_potion.png");
		i.setId("hover");
		return i;
	}

	@Override
	public void usePotion() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		inv.addPotion(this);
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		if(inv.hasHoverPotion()) {
			return false;
		}
		return true;
	}
}
