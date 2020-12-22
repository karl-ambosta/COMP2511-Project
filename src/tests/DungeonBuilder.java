package tests;

import java.awt.Point;

import core.Dungeon;
import entities.*;

public class DungeonBuilder {
	
	private Dungeon dungeon;
	
	public DungeonBuilder(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public Dungeon Build() {
		return this.dungeon;
	}
	
    /**
     * Adds an arrow to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addArrow(int x, int y) {
		this.dungeon.addEntity(new Arrow(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a bomb to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addBomb(int x, int y) {
		this.dungeon.addEntity(new Bomb(), new Point(x, y));
		return this;
	}
	
	
    /**
     * Adds a boulder to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addBoulder(int x, int y) {
		this.dungeon.addEntity(new Boulder(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a coward (enemy) to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addCoward(int x, int y) {
		this.dungeon.addEntity(new Coward(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a door to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addDoor(int x, int y) {
		this.dungeon.addEntity(new Door(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds an exit to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addExit(int x, int y) {
		this.dungeon.addEntity(new Exit(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a hound (enemy) to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addHound(int x, int y) {
		this.dungeon.addEntity(new Hound(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a hover potion to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addHoverPotion(int x, int y) {
		this.dungeon.addEntity(new HoverPotion(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a hunter (enemy) to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addHunter(int x, int y) {
		this.dungeon.addEntity(new Hunter(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a invincibility potion to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addInvincibilityPotion(int x, int y) {
		this.dungeon.addEntity(new InvincibilityPotion(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a key to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addKey(int x, int y) {
		this.dungeon.addEntity(new Key(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a 'kill enemy potion' to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addKillEnemyPotion(int x, int y) {
		this.dungeon.addEntity(new KillEnemyPotion(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a pit to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addPit(int x, int y) {
		this.dungeon.addEntity(new Pit(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a player to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addPlayer(int x, int y) {
		this.dungeon.addEntity(new Player(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a strategist to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addStrategist(int x, int y) {
		this.dungeon.addEntity(new Strategist(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a switch to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addSwitch(int x, int y) {
		this.dungeon.addEntity(new Switch(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a sword to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addSword(int x, int y) {
		this.dungeon.addEntity(new Sword(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a treasure item to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addTreasure(int x, int y) {
		this.dungeon.addEntity(new Treasure(), new Point(x, y));
		return this;
	}
	
    /**
     * Adds a wall to the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be placed at
     * @param y -> y-coordinate on the map that the entity will be placed at
     */
	public DungeonBuilder addWall(int x, int y) {
		this.dungeon.addEntity(new Wall(), new Point(x, y));
		return this;
	}
	
    /**
     * Removes an entity from the dungeon in the position indicated
     * @param x -> x-coordinate on the map that the entity will be removed from
     * @param y -> y-coordinate on the map that the entity will be removed from
     */
	public DungeonBuilder removeEntity(int x, int y) {
		this.dungeon.removeEntity(new Point(x, y));
		return this;
	}
}
