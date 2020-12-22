/**
 * 
 */
package core;

import entities.Arrow;
import entities.Bomb;
import entities.HoverPotion;
import entities.InvincibilityPotion;
import entities.Key;
import entities.Potion;
import entities.Sword;
import entities.Weapon;

/**
 * 
 *
 */
public class Inventory {

	private Sword sword;
	private int arrows;
	private int treasure;
	private Key key;
	private int bombs;
	private boolean hoverPotion;
	private InvincibilityPotion invincPotion;
	private Weapon equiped;
	
	/**
	 * 
	 */
	public Inventory() {
		this.arrows = 0;
		this.treasure = 0;
		this.bombs = 0;
		this.key = null;
		this.sword = null;
		this.hoverPotion = false;
		this.invincPotion = null;
		this.equiped = null;
	}
	
	public void addBomb() {
		this.bombs++;
	}
	
	public void addArrow() {
		this.arrows++;
	}
	
	public void addKey(Key k) {
		if(this.key == null) {
			this.key = k;
		}
	}
	
	public void addTreasure() {
		this.treasure++;
	}
	
	public void dropBomb() {
		this.bombs--;
	}
	
	public void dropArrow() {
		this.arrows--;
	}
	
	public void dropSword() {
		this.sword = null;
	}
	
	public void dropKey() {
		this.key = null;
	}
	
	/**
	 * Add potion to inventory
	 * @param Potion p
	 */
	public void addPotion(Potion p) {
		if (p instanceof HoverPotion) {
			this.hoverPotion = true;
		} else {
			this.invincPotion = new InvincibilityPotion();
		}
	}
	
	/**
	 * Function to check if the player has an Invincibility Potion
	 * @return boolean
	 */
	public boolean hasInvincPotion() {
		return (invincPotion != null);
	}

	/**
	 * Function to stop the effects of an Invincibility Potion
	 */
	public void stopInvincPotion() {
		this.invincPotion = null;
	}
	
	/**
	 * Adds a sword to inventory if one is not already there
	 * @Pre sword must be non-null and have at least one use left
	 * @param sword Sword to add to inventory
	 * @return if add was successful
	 */
	public boolean addSword(Sword sword) {
		if (this.sword != null) {
			return false;
		}
		this.sword = sword;
		this.equiped = sword;
		return true;
	}
	
	/**
	 * Function to return the Key in the Inventory
	 * @return Key
	 */
	public Key getKey() {
		return key;
	}
	
	/**
	 * Function to return the Sword in the Inventory
	 * @return Sword
	 */
	public Sword getSword() {
		return sword;
	}
	
	/**
	 * Function to return the number of Arrows in the Inventory
	 * @return int -> number of arrows
	 */
	public int getArrows() {
		return arrows;
	}
	
	/**
	 * Function to return the number of Treasure in the Inventory
	 * @return int -> number of treasure
	 */
	public int getTreasure() {
		return treasure;
	}
	
	/**
	 * Function to return the number of Bombs in the Inventory
	 * @return int -> number of bombs
	 */
	public int getBombs() {
		return bombs;
	}
	
	/**
	 * Function to check if the player has a Hover Potion
	 * @return boolean
	 */
	public boolean hasHoverPotion() {
		return hoverPotion;
	}
	
	/**
	 * Checks if character has the weapon in their inventory
	 * @param w weapon to check is in the inventory
	 * @return Whether weapon was found
	 */
	public boolean containsWeapon(Weapon w) {
		if (w instanceof Sword && sword != null) {
			return true;
		} else if (w instanceof Arrow && arrows > 0) {
			return true;
		} else if (w instanceof Bomb && bombs > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Destroys sword that has no uses left
	 */
	public void discardSword() {
		if (sword != null && sword.getUsesLeft() == 0) {
			sword = null;
			equiped = null;
		}
	}
	
	/**
	 * Gets the Player's current equipped Weapon
	 * @return equipped Weapon
	 */
	public Weapon getEquiped() {
		return equiped;
	}
	
	/**
	 * Sets the Player's equipped Weapon
	 * @param equiped -> Weapon
	 */
	public void setEquiped(Weapon equiped) {
		this.equiped = equiped;
	}
	
	/**
	 * Cycles the Player's equipped weapon
	 */
	public void cycleWeapon() {
		if (equiped == null || equiped instanceof Sword) {
			equiped = new Arrow();
		} else if (equiped instanceof Arrow) {
			equiped = new Bomb();
		} else if (equiped instanceof Bomb) {
			equiped = this.sword;
		}
	}

	/**
	 * Function to check the status of the Player's Invincibility Potion
	 */
	public void checkInvinc() {
		if (invincPotion != null) {
			if (invincPotion.getTurnsLeft() > 0) {
				invincPotion.loseTurn();
			} else if (invincPotion.getTurnsLeft() == 0) {
				invincPotion = null;
			}
		}
	}
	
	/**
	 * Checks if the Player has a Sword in the Inventory
	 * @return
	 */
	public boolean hasSword() {
		if(this.sword != null) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Function to return the Player's Invincibility Potion
	 * @return
	 */
	public InvincibilityPotion getInvinciPotion() {
		return this.invincPotion;
	}

	/**
	 * Function to reset the Inventory
	 */
	public void reset() {
		this.arrows = 0;
		this.bombs = 0;
		this.key = null;
		this.equiped = null;
		this.sword = null;
	}
}
