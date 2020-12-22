package entities;

import java.awt.Point;

import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

public class Player implements Character {
	
	private Inventory inv;
	private char dir;
	private Point position;
	private Entity onTile;	
	
	public Player() {
		this.inv = new Inventory();
		this.position = null;
		this.onTile = null;
	}
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/player.png");
		i.setId("player");
		return i;
	}
	
	/**
	 * Function to get the Payer's position
	 * @return Point
	 */
	public Point getPosition() {
		return this.position;
	}
	
	/**
	 * Function to set the Payer's position
	 * @param p -> Point
	 */
	public void setPosition(Point p) {
		this.position = p;
	}
	
	/**
	 * Function to set the Player's direction to move
	 * @param dir -> char
	 */
	public void setDir(char dir) {
		this.dir = dir;
	}
	
	/**
	 * Moves the player based on the designated input
	 * @Pre this.dir is set to the designated input
	 * @param Entity[][] Map -> Dungeon
	 * @param Point player
	 * @post Moves the Player in the specified direction (if available)
	 */
	@Override
	public EntityState move(Dungeon d, Point player) {
		Entity[][] Map = d.getMap();
		int x = this.position.x;
		int y = this.position.y;
		Boulder boulder = new Boulder();
		EntityState isAlive = EntityState.Alive;
		boolean boulderMove = false;
		switch (dir) {
		case 'u':
			if (Map[x-1][y] instanceof KillEnemyPotion) {
				KillEnemyPotion p = (KillEnemyPotion) Map[x-1][y];
				p.findEnemy(d);
			}
			if (Map[x-1][y] instanceof Boulder) {
				boulderMove = boulder.move(Map, new Point(x-2,y));
				boulder = (Boulder) Map[x-1][y];
			}
			if (Map[x-1][y] == null || (Map[x-1][y].canMoveThrough()  || isInvinc(Map[x-1][y])|| boulderMove)) {
				if(Map[x-1][y] != null && Map[x-1][y].canInteract(inv)) {
					isAlive = Map[x-1][y].interact(inv);
					//if enemy died from invinc potion
					if (isAlive == EntityState.EnemyDead) {
						Map[x-1][y] = null;
						isAlive = EntityState.Alive;
					}
					Map[x][y] = onTile;
					onTile = null;
				} else if(Map[x-1][y] != null && !Map[x-1][y].canInteract(inv)) {
					Map[x][y] = onTile;
					onTile = Map[x-1][y];
				} else {
					Map[x][y] = onTile;
					onTile = null;
				}
				
				this.position.move(x-1, y);
				Map[x-1][y] = this;
			} else if(Map[x-1][y] instanceof Door) {
				Door door = (Door) Map[x-1][y];
				Key key = this.getInventory().getKey();
				if(key != null && door.getDoorNo() == key.getKeyNo()) {
					door.openDoor(key.getKeyNo());
					Map[x][y] = onTile;
					onTile = Map[x-1][y];
					this.position.move(x-1, y);
					Map[x-1][y] = this;
				}
			}
			if (boulderMove && boulder.getIsOnSwitch()) onTile = new Switch();
			break;
		case 'l':
			if (Map[x][y-1] instanceof KillEnemyPotion) {
				KillEnemyPotion p = (KillEnemyPotion) Map[x][y-1];
				p.findEnemy(d);
			}
			if (Map[x][y-1] instanceof Boulder) {
				boulderMove = boulder.move(Map, new Point(x,y-2));
				boulder = (Boulder) Map[x][y-1];
			}
			if (Map[x][y-1] == null || (Map[x][y-1].canMoveThrough()  || isInvinc(Map[x][y-1]) || boulderMove)) {
				if(Map[x][y-1] != null && Map[x][y-1].canInteract(inv)) {
					isAlive = Map[x][y-1].interact(inv);
					//if enemy died from invinc potion
					if (isAlive == EntityState.EnemyDead) {
						Map[x][y-1] = null;
						isAlive = EntityState.Alive;
					}
					Map[x][y] = onTile;
					onTile = null;
				} else if(Map[x][y-1] != null && !Map[x][y-1].canInteract(inv)) {
					Map[x][y] = onTile;
					onTile = Map[x][y-1];
					
				} else {
					Map[x][y] = onTile;
					onTile = null;
				}
				
				this.position.move(x, y-1);
				Map[x][y-1] = this;
			} else if(Map[x][y-1] instanceof Door) {
				Door door = (Door) Map[x][y-1];
				Key key = this.getInventory().getKey();
				if(key != null && door.getDoorNo() == key.getKeyNo()) {
					door.openDoor(key.getKeyNo());
					Map[x][y] = onTile;
					onTile = Map[x][y-1];
					this.position.move(x, y-1);
					Map[x][y-1] = this;
				}
			}	
			if (boulderMove && boulder.getIsOnSwitch()) onTile = new Switch();
			break;
		case 'd':
			if (Map[x+1][y] instanceof KillEnemyPotion) {
				KillEnemyPotion p = (KillEnemyPotion) Map[x+1][y];
				p.findEnemy(d);
			}
			if (Map[x+1][y] instanceof Boulder) {
				boulderMove = boulder.move(Map, new Point(x+2,y));
				boulder = (Boulder) Map[x+1][y];
			}
			if (Map[x+1][y] == null || (Map[x+1][y].canMoveThrough()  || isInvinc(Map[x+1][y]) || boulderMove)) {
				if(Map[x+1][y] != null && Map[x+1][y].canInteract(inv)) {
					isAlive = Map[x+1][y].interact(inv);
					//if enemy died from invinc potion
					if (isAlive == EntityState.EnemyDead) {
						Map[x+1][y] = null;
						isAlive = EntityState.Alive;
					}
					Map[x][y] = onTile;
					onTile = null;
				} else if(Map[x+1][y] != null && !Map[x+1][y].canInteract(inv)) {
					Map[x][y] = onTile;
					onTile = Map[x+1][y];
				} else {
					Map[x][y] = onTile;
					onTile = null;
				}

				this.position.move(x+1, y);
				Map[x+1][y] = this;
			} else if(Map[x+1][y] instanceof Door) {
				Door door = (Door) Map[x+1][y];
				Key key = this.getInventory().getKey();
				if(key != null && door.getDoorNo() == key.getKeyNo()) {
					door.openDoor(key.getKeyNo());
					Map[x][y] = onTile;
					onTile = Map[x+1][y];
					this.position.move(x+1, y);
					Map[x+1][y] = this;
				}
			}		
			if (boulderMove && boulder.getIsOnSwitch()) onTile = new Switch();
			break;
		case 'r':
			if (Map[x][y+1] instanceof KillEnemyPotion) {
				KillEnemyPotion p = (KillEnemyPotion) Map[x][y+1];
				p.findEnemy(d);
			}
			if (Map[x][y+1] instanceof Boulder) {
				boulderMove = boulder.move(Map, new Point(x,y+2));
				boulder = (Boulder) Map[x][y+1];
			}
			if (Map[x][y+1] == null || (Map[x][y+1].canMoveThrough() || isInvinc(Map[x][y+1]) || boulderMove)) {
				if(Map[x][y+1] != null && Map[x][y+1].canInteract(inv)) {
					isAlive = Map[x][y+1].interact(inv);
					//if enemy died from invinc potion
					if (isAlive == EntityState.EnemyDead) {
						Map[x][y+1] = null;
						isAlive = EntityState.Alive;
					}
					Map[x][y] = onTile;
					onTile = null;
				} else if(Map[x][y+1] != null && !Map[x][y+1].canInteract(inv)) {
					Map[x][y] = onTile;
					onTile = Map[x][y+1];
				} else {
					Map[x][y] = onTile;
					onTile = null;
				}
				
				this.position.move(x, y+1);
				Map[x][y+1] = this;
			} else if(Map[x][y+1] instanceof Door) {
				Door door = (Door) Map[x][y+1];
				Key key = this.getInventory().getKey();
				if(key != null && door.getDoorNo() == key.getKeyNo()) {
					door.openDoor(key.getKeyNo());
					Map[x][y] = onTile;
					onTile = Map[x][y+1];
					this.position.move(x, y+1);
					Map[x][y+1] = this;
				}
			}	
			if (boulderMove && boulder.getIsOnSwitch()) onTile = new Switch();
			break;
		default:
			return isAlive;
		}
		
		// invinc potion stuff
		inv.checkInvinc();
		
		return isAlive;
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}
	
	public Inventory getInventory(){
		return this.inv;
	}

	@Override
	public EntityState interact(Inventory inv) {
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
	
	public boolean hasHoverPotion() {
		return inv.hasHoverPotion();
	}
	
	public boolean hasInvincPotion() {
		return inv.hasInvincPotion();
	}
	
	public void stopInvinc() {
		inv.stopInvincPotion();
	}
	
	public void discardSword() {
		inv.discardSword();
	}

	public Weapon getEquiped() {
		return inv.getEquiped();
	}

	public void setEquiped(Weapon equiped) {
		inv.setEquiped(equiped);
	}
	
	public void cycleWeapon() {
		inv.cycleWeapon();
	}
	
	/**
	 * Check if Entity is Invincible
	 * @param e -> Entity
	 * @return boolean
	 */
	public boolean isInvinc(Entity e) {
		if (e instanceof Coward || e instanceof Hound || e instanceof Hunter || e instanceof Strategist) {
			if (hasInvincPotion()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Function to drop the Player's Sword
	 * @return boolean
	 */
	public boolean dropSword() {
		if (this.onTile == null) {
			this.onTile = inv.getSword();
			if (this.getEquiped() == inv.getSword()) {
				inv.cycleWeapon();
			}
			inv.dropSword();
			return true;
		}
		return false;
	}
	
	/**
	 * Function to drop one of the Player's Bombs
	 * @return boolean
	 */
	public boolean dropBomb() {
		if (this.onTile == null && inv.getBombs() > 0) {
			this.onTile = new Bomb();
			inv.dropBomb();
			return true;
		}
		return false;
	}
	
	/**
	 * Function to drop the Player's Key
	 * @return boolean
	 */
	public boolean dropKey() {
		if (this.onTile == null) {
			this.onTile = inv.getKey();
			inv.dropKey();
			return true;
		}
		return false;
	}

	/**
	 * Function to get the Entity that this Player is standing on
	 * @return onTile -> Entity
	 */
	public Entity getOnTile() {
		return onTile;
	}
	
	
}
