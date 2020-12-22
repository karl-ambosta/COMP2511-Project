package entities;

import java.awt.Point;
import java.util.Map;

import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

public class Hunter extends Enemy implements Character {
	
	private boolean hasHound = false;
	private int hunterID;
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/hunter.png");
		i.setId("hunter");
		return i;
	}

	/**
	 * Evaluates the closet distance to the player in each of the adjacent squares and then move to that spot, if empty
	 * @pre 
	 * @param Entity[][] Map -> Dungeon
	 * @param Point player
	 * @post Moves the Hunter to an avaliable tile
	 */
	@Override
	public EntityState move(Dungeon d, Point player) {
		Entity[][] Map = d.getMap();
		Map<Point, Double> distMap = getDistances(player.getLocation(), this.getPosition());

		Point next = this.findPoint(d, distMap);
		
		if (next.x == player.x && next.y == player.y) {
			 Player p = (Player) Map[player.x][player.y];
			 if(p.hasInvincPotion()) {
				 return EntityState.EnemyDead;
			 }
		}

		Map[this.getPosition().x][this.getPosition().y] = onTile;
		this.getPosition().move(next.x, next.y);
		onTile = Map[next.x][next.y];
		
		Map[next.x][next.y] = this;
		if(onTile instanceof Player) {
			return EntityState.Dead;
		}
		return EntityState.Alive;
	}

	@Override
	public boolean canMoveThrough() {
		return false;
	}

	@Override
	public EntityState interact(Inventory inv) {
		if (inv.hasInvincPotion()) {
			return EntityState.EnemyDead;
		}
		return EntityState.Dead;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}

	@Override
	public boolean isValidPoint(double distance) {
		return true;
	}
	
	@Override
	public Point findPoint(Dungeon dungeon, Map<Point,Double> distMap) {
		
		Entity[][] Map = dungeon.getMap();
		Player p = dungeon.getPlayer();
		Point toMove;
		if(!p.hasInvincPotion()) {
			toMove = getMinPoint(Map, distMap);
		} else {
			toMove = getMaxPoint(Map, distMap);
		}
		return toMove;
	}
	
	/**
	 * Function to set the Hunter's ID
	 * @param id -> int
	 */
	public void setHunterID(int id) {
		this.hunterID = id;
	}
	
	/**
	 * Function to get the Hunter's ID
	 * @return
	 */
	public int getID() {
		return this.hunterID;
	}
	
	/**
	 * Change the hasHound variable
	 */
	public void changeHasHound() {
		if(this.hasHound) {
			this.hasHound = false;
		} else {
			this.hasHound = true;
		}
	}
}
