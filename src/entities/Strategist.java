package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

public class Strategist extends Enemy implements Character {

	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/strategist.png");
		i.setId("strategist");
		return i;
	}

	/**
	 * Evaluates the closet distance to the player in each of the adjacent squares and then move to that spot, if empty
	 * @pre 
	 * @param Entity[][] Map -> Dungeon
	 * @param Point player
	 * @post Moves the Strategist to an avaliable tile
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
		Point player = dungeon.getPlayer().getPosition();
		int method = dungeon.getMethod(); // Completion Methods: EXIT = 0, TREASURE = 1, SWITCH = 2, ENEMIES = 3;
		ArrayList<Point> points;
		Map<Point, Double> distances = new HashMap<Point,Double>();
		Map<Point, Double> distances2 = new HashMap<Point,Double>();
		
		if(this.position.distance(player) < 2 && !(dungeon.getPlayer().hasInvincPotion())) {
			return getMinPoint(Map, distMap);
		} else if(method == 0) {
			points = findEntity(dungeon, new Exit());
			if(points.size() == 1) {
				return MinOrMaxPoint(dungeon, distMap);
			}
			Double dist = this.position.distance(player);
			distances = getDistancestoEntity(dungeon, points);
			Point closest = getPoint(distances);
			distances2 = getDistances(closest, this.position);	
			distances2.put(player, dist);
			return MinOrMaxPoint(dungeon, distances2);
		} else if(method == 1) {
			points = findEntity(dungeon, new Treasure());
			if(points.size() == 1) {
				return MinOrMaxPoint(dungeon, distMap);
			}
			Double dist = this.position.distance(player);
			distances = getDistancestoEntity(dungeon, points);
			Point closest = getPoint(distances);
			distances2 = getDistances(closest, this.position);	
			distances2.put(player, dist);
			return MinOrMaxPoint(dungeon, distances2);
		} else if(method == 2) {
			points = findEntity(dungeon, new Switch());
			if(points.size() == 1) {
				return MinOrMaxPoint(dungeon, distMap);
			}
			Double dist = this.position.distance(player);
			distances = getDistancestoEntity(dungeon, points);
			Point closest = getPoint(distances);
			distances2 = getDistances(closest, this.position);	
			distances2.put(player, dist);
			return MinOrMaxPoint(dungeon, distances2);
		} else if(method == 3) {
			points = findEntity(dungeon, this);
			if(points.size() == 1) {
				return MinOrMaxPoint(dungeon, distMap);
			}
			Double dist = this.position.distance(player);
			distances = getDistancestoEntity(dungeon, points);
			Point closest = getPoint(distances);
			distances2 = getDistances(closest, this.position);	
			distances2.put(player, dist);
			return MinOrMaxPoint(dungeon, distances2);
		} else {
			return this.position;
		}
	}
	
	/**
	 * Function to find all Entity in the Dungeon of the type specified
	 * @param dungeon -> Dungeon
	 * @param e -> Entity
	 * @return points -> ArrayList<Point>
	 */
	public ArrayList<Point> findEntity(Dungeon dungeon, Entity e) {
		Entity[][] map = dungeon.getMap();
		ArrayList<Point> points = new ArrayList<Point>();
		if(e instanceof Strategist) {
			for(int i = 0; i < 20; i++) {
				for(int j = 0; j < 20; j++) {
					if(map[i][j] != null) {
						if(map[i][j] instanceof Enemy) {
							Point p = new Point(i,j);
							points.add(p);
						}
					}
				}
			}
		} else {
			for(int i = 0; i < 20; i++) {
				for(int j = 0; j < 20; j++) {
					if(map[i][j] != null) {
						if(map[i][j].getClass().equals(e.getClass())) {
							Point p = new Point(i,j);
							points.add(p);
						}
					}
				}
			}
		}	
		return points;
	}
	
	/**
	 * Function to calculate the distances of all entities of 
	 * the type specified by the player
	 * @param d -> Dungeon
	 * @param points -> ArrayList<Point>
	 * @return map -> Map<Point, Double>
	 */
	public Map<Point,Double> getDistancestoEntity(Dungeon d, ArrayList<Point> points) {
		Map<Point,Double> dist = new HashMap<Point,Double>();
		
		for(Point p : points) {
			dist.put(p, d.getPlayer().getPosition().distance(p));
		}
		
		return dist;
	}
	
	/**
	 * Get the closest point within the smallest distance
	 * @param map -> Map<Point, Double>
	 * @return p -> Point
	 */
	public Point getPoint(Map<Point, Double> map) {
		Double min = Collections.min(map.values());
		
		for(Entry<Point,Double> e : map.entrySet()) {
			if(e.getValue() == min) {
				return e.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * Function to decide to find the Minpoint or Max Point from a distMap
	 * @param d -> Dungeon
	 * @param distances -> Map<Point, Double>
	 * @return
	 */
	public Point MinOrMaxPoint(Dungeon d, Map<Point, Double> distances) {
		Player p = d.getPlayer();
		Entity[][] Map = d.getMap();
		Point toMove;
		if(!p.hasInvincPotion()) {
			toMove = getMinPoint(Map, distances);
		} else {
			toMove = getMaxPoint(Map, distances);
		}
		return toMove;
	}
}
