package tests;

import org.junit.jupiter.api.Test;

import core.Dungeon;
import core.Inventory;
import entities.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

public class DungeonTesting {

	//Test for User Story 1A: Create character movements
	@Test
	public void movePlayer() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Player player = dungeon.getPlayer();
		
		dungeon.movePlayer('d');
		assertEquals(new Point(2,1), player.getPosition());
		
		dungeon.movePlayer('r');
		assertEquals(new Point(2,2), player.getPosition());
		
		dungeon.movePlayer('l');
		assertEquals(new Point(2,1), player.getPosition());
		
		dungeon.movePlayer('u');
		assertEquals(new Point(1,1), player.getPosition());	
		
		//Trying to move in a square that is a Wall
		dungeon.movePlayer('u');
		assertEquals(new Point(1,1), player.getPosition());	
	}
	
	//Test for User Story 1B: Pick Up Items
	@Test
	public void pickUpItems() {
		Dungeon dungeon = new Dungeon();
		Inventory inv = dungeon.getPlayer().getInventory();
		assertEquals(0, inv.getArrows());
		inv.addArrow();
		assertEquals(1, inv.getArrows());
	}
	
	//Test for User Story 1B: Pick Up (ONE SWORD AT A TIME)
	@Test
	public void OneSwordAtATime() {
		Dungeon dungeon = new Dungeon();
		Player player = dungeon.getPlayer();
		Sword sword = new Sword();
		assertTrue(player.getInventory().getSword() == null);
		//Already has 1 Sword
		player.getInventory().addSword(sword);
		assertTrue(player.getInventory().getSword() != null);
		assertFalse(player.getInventory().addSword(sword));
		
	}
	
	//Test for User Story 1C: Using Weapons
	
	@Test
	// checks arrow usage
	public void useWeaponsArrow() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Player player = dungeon.getPlayer();

		player.getInventory().addArrow();
		assertEquals(1, player.getInventory().getArrows());
		dungeon.cycleWeapon();
		dungeon.attack('u', dungeon.getWeapon());
		assertEquals(0, player.getInventory().getArrows());
	}
	
	@Test
	// checks arrow usage when at range and when blocked by wall
	public void useWeaponsArrowRanged() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addHunter(1, 7)
				.Build();
		
		Player player = dungeon.getPlayer();
		player.getInventory().addArrow();
		assertEquals(1, player.getInventory().getArrows());
		
		dungeon.cycleWeapon();
		dungeon.attack('r', dungeon.getWeapon());
		Entity[][] map = dungeon.getMap();
		assertTrue(map[1][7] == null);
		assertEquals(0, player.getInventory().getArrows());
		
		dungeon = new DungeonBuilder(dungeon)
				.addHunter(1, 7)
				.addWall(1, 4)
				.Build();
				
		player.getInventory().addArrow();
		dungeon.attack('r', dungeon.getWeapon());
		assertTrue(map[1][4] != null);
	}
	
	@Test
	//checks bomb usage
	public void useWeaponsBomb() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Player player = dungeon.getPlayer();
		player.getInventory().addBomb();
		assertEquals(1, player.getInventory().getBombs());
		dungeon.cycleWeapon();
		dungeon.cycleWeapon();
		
		//use bomb when blocked by wall
		dungeon.attack('u', dungeon.getWeapon());
		assertEquals(1, player.getInventory().getBombs());
		
		dungeon.attack('r', dungeon.getWeapon());
		assertEquals(0, player.getInventory().getBombs());
	}
	
	@Test
	//checks sword usage
	public void useWeaponSword() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addHunter(1, 2)
				.Build();
		
		Player player = dungeon.getPlayer();
		player.getInventory().addSword(new Sword());
		assertTrue(player.getInventory().getSword() != null);
		dungeon.cycleWeapon();
		dungeon.cycleWeapon();
		dungeon.cycleWeapon();
		assertTrue(player.getEquiped() instanceof Sword);
		
		dungeon.attack('r', dungeon.getWeapon());
		assertTrue(dungeon.getPlayer().getInventory().getSword().getUsesLeft() == 4);
	}
	
	@Test
	//checks sword usage when only one use left
	public void useWeaponSwordBreak() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addHunter(1, 2)
				.Build();
		
		Player player = dungeon.getPlayer();
		player.getInventory().addSword(new Sword(1));
		assertTrue(player.getInventory().getSword() != null);
		dungeon.cycleWeapon();
		dungeon.cycleWeapon();
		dungeon.cycleWeapon();
		assertTrue(player.getEquiped() instanceof Sword);
		
		dungeon.attack('r', dungeon.getWeapon());
		assertTrue(dungeon.getPlayer().getInventory().getSword() == null);
	}
	
	
	//Test for User Story 1D: Battle Different Enemies
	@Test
	public void battleEnemies() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addHunter(2, 1)
				.Build();
		
		Player player = dungeon.getPlayer();
		Entity[][] map = dungeon.getMap();
		assertTrue(map[2][1] instanceof Hunter);
		player.getInventory().addArrow();
		dungeon.attack('d', new Arrow());
		assertTrue(map[2][1] == null);
	}
	
	//Test for User Story 1E: Collecting Treasure
	@Test
	public void collectTreasure() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Player player = dungeon.getPlayer();
		assertTrue(player.getInventory().getTreasure() == 0);
		player.getInventory().addTreasure();
		assertTrue(player.getInventory().getTreasure() == 1);
	}
	
	//Test for User Story 1F: Moving onto Pits
	@Test
	public void moveonPit() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addPit(2, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[2][1] instanceof Pit);
		dungeon.movePlayer('d');
		
		assertEquals(true, dungeon.get_isDead());
	}
	
	//Test for User Story 1G: Lighting and Exploding Bombs
	@Test
	public void LightingBombs() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		Player p = dungeon.getPlayer();
		
		Bomb b = new Bomb();
		p.getInventory().addBomb();
		
		assertEquals(1, p.getInventory().getBombs());
		p.setEquiped(b);
		
		assertTrue(map[2][1] == null);
		dungeon.attack('d', dungeon.getWeapon());
		
		assertTrue(map[2][1] instanceof Bomb);
		Bomb b2 = (Bomb) map[2][1];
		assertTrue(b2.isLit());
	}

	@Test
	public void ExplodingBombs() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();

		dungeon.getPlayer().getInventory().addBomb();
		
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		
		dungeon.attack('r', new Bomb());
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		//one turn till explosion
		assertTrue(map[1][9] != null);
		
		dungeon = new DungeonBuilder(dungeon)
				.addHunter(1, 10)
				.Build();
		
		assertFalse(dungeon.getEnemies().isEmpty());
		
		dungeon.movePlayer('l');
		dungeon.move();
		assertTrue(dungeon.getEnemies().isEmpty());
		
		dungeon.getPlayer().getInventory().addBomb();
		dungeon.attack('r', new Bomb());
		
		dungeon = new DungeonBuilder(dungeon)
				.addWall(1, 9)
				.addBoulder(2, 9)
				.Build();
		
		dungeon.movePlayer('l');
		
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();

		assertTrue(map[1][9] == null);
		assertTrue(map[2][9] == null);
		
		dungeon.getPlayer().getInventory().addBomb();
		dungeon.attack('r', new Bomb());
		
		assertFalse(dungeon.get_isDead());
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		dungeon.move();
		
		assertTrue(dungeon.get_isDead());
	}
	
	//Test for User Story 1H: Using Potions
	@Test
	public void usePotionsHover() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addHoverPotion(1, 2)
				.addPit(1, 3)
				.Build();
		
		dungeon.movePlayer('r');
		assertTrue(dungeon.getPlayer().getInventory().hasHoverPotion());
				
		//move onto pit
		EntityState state = dungeon.movePlayer('r');
		assertEquals(EntityState.Alive, state);	
	}
	
	@Test
	public void usePotionsInvinc() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addInvincibilityPotion(1, 2)
				.addHunter(1, 4)
				.Build();
		
		dungeon.movePlayer('r');
		assertTrue(dungeon.getPlayer().getInventory().hasInvincPotion());
		Entity[][] map = dungeon.getMap();
		
		//move into enemy
		assertTrue(map[1][4] instanceof Hunter);
		EntityState state = dungeon.movePlayer('r');
		assertEquals(EntityState.Alive, state);	
		assertTrue(map[1][3] instanceof Player);
		
		// enemy moves into you
		dungeon.move();
		assertTrue(map[1][3] instanceof Player);
		assertFalse(dungeon.get_isDead());
		
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		dungeon.movePlayer('r');
		
		assertFalse(dungeon.getPlayer().hasInvincPotion());	
	}
	
	@Test
	public void useKillEnemyPotion() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addKillEnemyPotion(1, 2)
				.addKillEnemyPotion(1, 3)
				.addHunter(6, 6)
				.addCoward(7, 7)
				.Build();
		
		assertEquals(2, dungeon.getEnemies().size());
		
		// pick up kill enemy potion
		dungeon.movePlayer('r');
		assertEquals(1, dungeon.getEnemies().size());
		
		// pick up another kill enemy potion
		dungeon.movePlayer('r');
		assertEquals(0, dungeon.getEnemies().size());
	}
	
	//Test for User Story 1I: Triggering Floor Switches
	@Test
	public void triggerSwitches() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addBoulder(1, 2)
				.addSwitch(1, 3)
				.Build();
		
		Entity[][] map = dungeon.getMap();

		Boulder b = (Boulder) map[1][2];
		assertFalse(b.getIsOnSwitch());
		dungeon.movePlayer('r');
		b = (Boulder) map[1][3];
		assertTrue(b.getIsOnSwitch());
	}
	
	//Test for User Story 1J: Unlocking Doors with Keys
	@Test
	public void unlockingDoors() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addKey(2, 1)
				.addDoor(3, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		Door door = (Door) map[3][1];
		assertFalse(door.canMoveThrough());
		
		dungeon.movePlayer('d');
		dungeon.movePlayer('d');

		assertTrue(door.canMoveThrough());
	}
	
	//Test for User Story 1K: Exiting a Level
	@Test
	public void exitLevel() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addExit(3, 1)
				.Build();
		
		assertFalse(dungeon.get_isComplete());
		dungeon.movePlayer('d');
		dungeon.movePlayer('d');
		assertTrue(dungeon.get_isComplete());
	}
	
	//Test for User Story 1L: Push and Destroy Boulders
	@Test
	public void interactWithBoulders() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addBoulder(2, 1)
				.Build();

		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[2][1] instanceof Boulder);
		assertTrue(map[3][1] == null);
		
		dungeon.movePlayer('d');
		
		assertTrue(map[2][1] instanceof Player);
		assertTrue(map[3][1] instanceof Boulder);
	}
	
	//Test for User Story 2A: Place Items in Dungeon
	@Test
	public void addItems_toDungeon() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[5][6] == null);
		
		dungeon = new DungeonBuilder(dungeon)
				.addSword(5, 6)
				.Build();
		
		assertTrue(map[5][6] instanceof Sword);
	}
	
	//Test for User Story 2B: Place Obstacles in Dungeon
	@Test
	public void addObstacles_toDungeon() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[5][6] == null);
		
		dungeon = new DungeonBuilder(dungeon)
				.addBoulder(5, 6)
				.Build();
		
		assertTrue(map[5][6] instanceof Boulder);
	}
	
	//Test for User Story 2B: Place OBSTACLES (NO MORE THAN THREE DOORS IN A DUNGEON)
	@Test
	public void noMoreThanThreeDoors() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addDoor(2, 2)
				.addDoor(3, 3)
				.addDoor(4, 4)
				.addDoor(5, 5)
				.Build();
		
		Entity[][] map = dungeon.getMap();

		assertTrue(map[2][2] instanceof Door);
		assertTrue(map[3][3] instanceof Door);
		assertTrue(map[4][4] instanceof Door);
		assertTrue(map[5][5] == null);
	}
	
	//Test for User Story 2C: Place Characters in Dungeon
	@Test
	public void addCharacters_toDungeon() {		
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[5][6] == null);
		
		dungeon = new DungeonBuilder(dungeon)
				.addStrategist(5, 6)
				.Build();
		
		assertTrue(map[5][6] instanceof Strategist);
	}
	
	//Test for User Story 2C: Place Characters (NO MORE THAN ONE PLAYER IN A DUNGEON)
	@Test
	public void noMoreThanOnePlayer() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[5][6] == null);
		assertTrue(map[1][1] instanceof Player);

		dungeon = new DungeonBuilder(dungeon)
				.addPlayer(5, 6)
				.Build();
				
		assertTrue(map[5][6] == null);
		assertTrue(map[1][1] instanceof Player);
	}
	
	
	//Test for User Story 2G: Deleting an Entity
	@Test
	public void deletingEntities() {
		Dungeon dungeon = new DungeonBuilder(new Dungeon())
				.addPlayer(1, 1)
				.addDoor(2, 2)
				.Build();
		
		Entity[][] map = dungeon.getMap();
		
		assertTrue(map[2][2] instanceof Door);

		dungeon = new DungeonBuilder(dungeon)
				.removeEntity(2, 2)
				.Build(); 
		
		assertTrue(map[2][2] == null);
	}
}
