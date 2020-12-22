
package entities;

import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * Superclass for all objects that can be placed in map
 */
public interface Entity {

	/**
	 * Function to get the picture associated with an Entity
	 * @return
	 */
	public ImageView getPicture();
	/**
	 * Checks if the player can move into a square containing this item
	 * @return whether the player can move there
	 */
	public boolean canMoveThrough();
	/**
	 * Action that is performed when the player steps onto a square containing this
	 * @param inv players inventory
	 * @return state specifying if the player is still alive
	 */
	public EntityState interact(Inventory inv);
	/**
	 * Checks if player can interact with the item
	 * @param inv players inventory
	 * @return if player can interact return true
	 */
	public boolean canInteract(Inventory inv);
}
