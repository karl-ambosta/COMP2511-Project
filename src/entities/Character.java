package entities;

import java.awt.Point;

import core.Dungeon;

public interface Character extends Entity {

	/**
	 * method to make characters move
	 * @param map map of dungeon
	 * @param player players position
	 * @return state specifying whether player is alive, dead of the character has died
	 */
	public EntityState move(Dungeon d, Point player);
}
