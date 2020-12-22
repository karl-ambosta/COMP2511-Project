package entities;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;
import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

public class Hound extends Enemy implements Character {

	private boolean hasHunter = false;
	private int houndID;
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/hound.png");
		i.setId("hound");
		return i;
	}
	
	/**
	 * Takes in a distance HashMap and the distance of the hunter to the player, 
	 * and gets the closest distance in order to match the hunter
	 * @pre HashMap has values, distance > 0
	 * @param Map<Point, Double> distMap
	 * @param distance of hunter to player
	 * @post 
	 */
	public Point findClosestDistance(Map<Point, Double> distMap, double distance) {
		
		Point point = new Point(0,0);
		double minimum_distance = 99999;
		
		for(double value : distMap.values()) {
			if(distance - value < minimum_distance) {
				minimum_distance = value;
			}
		}
		
		for(Point key : distMap.keySet()) {
		    if(distMap.get(key).equals(minimum_distance)) {
			    point = key;
		    }
		}
		 
		return point;
	}

	/**
	 * Evaluates the closet distance to the player in each of the adjacent squares and then move to that spot, if empty
	 * @pre 
	 * @param Entity[][] Map -> Dungeon
	 * @param Point player
	 * @post Moves the Hound to an avaliable tile
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
	public Point findPoint(Dungeon dungeon, Map<Point, Double> distMap) {
		Entity[][] Map = dungeon.getMap();
		Player p = dungeon.getPlayer();
		Point point = new Point(0,0);
		
		if(!hasHunter) {
			// If the Hound does not have a hunter to mirror...
			if(!p.hasInvincPotion()) {
				point = getMinPoint(Map, distMap);
			} else {
				point = getMaxPoint(Map, distMap);
			}
		} else {
			
			//If the Hound does have a Hunter to mirror
			Hunter h = getAssignedHunter(dungeon);
			
			if(h == null) {
				changeHasHunter();
				if(!p.hasInvincPotion()) {
					return getMinPoint(Map, distMap);
				} else {
					return getMaxPoint(Map, distMap);
				}
			}
			
			double dist = h.getPosition().distance(p.getPosition());
			double closest_dist = 9999999;
			//Find the associated point with the closest distance
			for(Entry<Point,Double> key: distMap.entrySet()) {
				Point k = key.getKey();
				if((Map[k.x][k.y] == null || Map[k.x][k.y].canMoveThrough()) && this.isValidPoint(closest_dist) && !(Map[k.x][k.y] instanceof Pit)) {
					if(Math.abs(distMap.get(k) - dist) <= Math.abs(closest_dist - dist)) {
				    	closest_dist = key.getValue();
				    	point = k;
					}
		    		
		    	} else {
		    		continue;
		    	}
			}
		}
		return point;
	}
	
	/**
	 * Function to set the Hound's ID
	 * @param id -> int
	 */
	public void setHoundID(int id) {
		this.houndID = id;
	}
	
	/**
	 * Function to get the Hound's ID
	 * @return int -> houndID
	 */
	public int getID() {
		return this.houndID;
	}
	
	/**
	 * Change the hasHunter variable
	 */
	public void changeHasHunter() {
		if(this.hasHunter) {
			this.hasHunter = false;
		} else {
			this.hasHunter = true;
		}
	}
	
	/**
	 * Gets the assigned Hunter to this Hound
	 * @param d -> Dungeon
	 * @return h -> Hunter
	 */
	public Hunter getAssignedHunter(Dungeon d) {
		for(Character c : d.getEnemies()) {
			if(c instanceof Hunter) {
				Hunter h = (Hunter) c;
				if(h.getID() == this.houndID) {
					return h;
				}
			}
		}
		return null;
	}
}
