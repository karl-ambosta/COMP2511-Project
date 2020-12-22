package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 *
 *
 */
public class InvincibilityPotion implements Potion{
	
	private int turnsLeft;
	
	public InvincibilityPotion() {
		this.turnsLeft = 5;
	}
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/invincibility_potion.png");
		i.setId("invinc");
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
		if(inv.hasInvincPotion()) {
			return false;
		}
		return true;
	}

	/**
	 * Function to get the number of turns left for this Potion
	 * @return int -> turnsLeft
	 */
	public int getTurnsLeft() {
		return turnsLeft;
	}
	
	/**
	 * Function to decrease the number of turns left for this Potion by 1
	 */
	public void loseTurn() {
		this.turnsLeft--;
	}
}
