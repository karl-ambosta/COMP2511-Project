package entities;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import core.Dungeon;

public abstract class Enemy  {
	
	Point position;
	Entity onTile;

	/**
	 * Function to create a Map of Points surrounding the 
	 * Enemy and their corresponding distance to the PLayer
	 * @param player -> Point
	 * @param enemy -> Point
	 * @return mapOfValues -> Map<Point, Double>
	 */
	public Map<Point, Double> getDistances(Point player, Point enemy) {
		
		Map<Point, Double> mapOfValues = new HashMap<Point, Double>();
		
		Point next_point = new Point(this.position.x-1, this.position.y);
		double dist_left = next_point.distance(player);
		mapOfValues.put(next_point, dist_left);
		
		next_point = new Point(this.position.x+1, this.position.y);
		double dist_right = next_point.distance(player);
		mapOfValues.put(next_point, dist_right);
		
		next_point = new Point(this.position.x, this.position.y+1);
		double dist_up = next_point.distance(player);
		mapOfValues.put(next_point, dist_up);

		next_point = new Point(this.position.x, this.position.y-1);
		double dist_down = next_point.distance(player);
		mapOfValues.put(next_point, dist_down);
		
		return mapOfValues;
	}
	
	/**
	 * Function to get the POint with the minimum distance in the Map
	 * @param Map -> Entity[][]
	 * @param map -> Map<Point,Double>
	 * @return p -> Point
	 */
	public Point getMinPoint(Entity[][] Map, Map<Point,Double> map) {
		Point p = this.position;
		Double min = Collections.min(map.values());
		
		for(Point key: map.keySet()) {
		    if(map.get(key).equals(min)) {
		    	if((Map[key.x][key.y] == null || Map[key.x][key.y].canMoveThrough()) && this.isValidPoint(min) && !(Map[key.x][key.y] instanceof Pit)) {
		    		
			    	p = key;
			    	break;

		    	} else {
		    		map.remove(key);
		    		break;
		    	}
			}
		}
		return p;
	}
	
	/**
	 * Function to get the POint with the minimum distance in the Map
	 * @param Map -> Entity[][]
	 * @param map -> Map<Point,Double>
	 * @return p -> Point
	 */
	public Point getMaxPoint(Entity[][] Map, Map<Point,Double> map) {
		Point p = this.position;
		Double max = Collections.max(map.values());
		
		for(Point key: map.keySet()) {
		    if(map.get(key).equals(max)) {
		    	if((Map[key.x][key.y] == null || Map[key.x][key.y].canMoveThrough()) && !(Map[key.x][key.y] instanceof Pit)) {
			    	p = key;
			    	break;

		    	} else {
		    		map.remove(key);
		    		break;
		    	}
			}
		}
		return p;
	}
	
	/**
	 * Function to get the Enemy's position
	 * @return position -> Point
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Function to set the Enemy's position
	 */
	public void setPosition(Point position) {
		this.position = position;
	}
	
	/**
	 * Function to check if the specfic distance (corresponding to a Point) is valid
	 * @param distance -> doubl
	 * @return boolean
	 */
	public abstract boolean isValidPoint(double distance);
	
	/**
	 * Function to find a Point for the Enemy to move to
	 * @param dungeon -> Dungeon
	 * @param distMap -> Map<Point, Double>
	 * @return point
	 */
	public abstract Point findPoint(Dungeon dungeon, Map<Point, Double> distMap);

	/**
	 * Function to get the Entity this is on
	 * @return
	 */
	public Entity getOnTile() {
		return onTile;
	}
	
	/**
	 * Function to set the onTile function
	 * @param e -> Entity
	 */
	public void setOnTile(Entity e) {
		this.onTile = e;
	}
}
