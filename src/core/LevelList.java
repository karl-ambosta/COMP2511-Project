/**
 * 
 */
package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 *
 */
public class LevelList {
	
	Map<String, Entity[][]> maps;
	Map<String, ArrayList<Integer>> dimensions;
	Map<String, Integer> completion_methods;
	
	public LevelList() {
		this.maps = new HashMap<String, Entity[][]>();
		this.dimensions = new HashMap<String, ArrayList<Integer>>();
		this.completion_methods = new HashMap<String, Integer>();
		this.addDungeons();
	}
	
	/**
	 * Function to combine all the Maps separate Dungeon classes for viewing
	 * @return list -> ObservableList<Dungeon>
	 */
	public ObservableList<Dungeon> getAllMaps() {
		ObservableList<Dungeon> list = FXCollections.observableArrayList();
		for(Entry<String, Integer> e  : completion_methods.entrySet()) {
			int method = e.getValue();
			String name = e.getKey();
			ArrayList<Integer> dimens = dimensions.get(e.getKey());
			Dungeon d = new Dungeon(dimens.get(0), dimens.get(1));
			d.setMethod(method);
			d.setName(name);
			list.add(d);
		}
		
		return list;
	}
	
	/**
	 * Save the map created
	 * @param Dungeon d
	 */
	public void saveMap(Dungeon d, String s) {
		Entity[][] map = copyMap(d.getMap());
		maps.put(s, map);
		dimensions.put(s, d.getDimensions());
		completion_methods.put(s, d.getMethod());
		
	}
	
	/**
	 * Copies the current Map of a Dungeon
	 * @param toCopy -> Entity[][]
	 * @return newMap -> Entity[][]
	 */
	public Entity[][] copyMap(Entity[][] toCopy) {
		Entity[][] map = new Entity[20][20];
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				map[i][j] = toCopy[i][j];
			}
		}
		
		return map;
		
	}
	/**
	 * delete the map created referenced with title
	 * @param title -> String
	 */
	public void deleteMap(String title) {
		maps.remove(title);
	}

	/**
	 * Function to print out all the names of saved Dungeons
	 */
	public void printAllNames() {
		System.out.println("Map Names:");
		for(String name : maps.keySet()) {
			System.out.println(" >>> " + name);
		}
	}
	
	/**
	 * Function to print a map layout
	 * @param map -> Entity[][]
	 */
	public void printMap(Entity[][] map) { 
		String s = null;
		for(Map.Entry<String, Entity[][]> entry: maps.entrySet()){
            if(entry.getValue().equals(map)) {
                s = (String) entry.getKey();
                break;
            }
        }
		
		System.out.println("Map name is:" + s);
		
		ArrayList<Integer> map_dimensions = dimensions.get(s);
		
		for(int i = 0; i < map_dimensions.get(0); i++) {
			for(int j = 0; j < map_dimensions.get(1); j++) {
				if(i == 1 && j == 1) {
					System.out.print("PL   ");
				} else if(map[i][j] == null) {
					System.out.print("__   ");
				} else {
					System.out.print(map[i][j].getPicture() + "   ");
				}
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Function to get a map corresponding to a given title
	 * @param title -> String
	 * @return map -> Entity[][]
	 */
	public Entity[][] getMapwithName(String title) {
		return maps.get(title);	
	}
	
	/**
	 * Function to get a completion method corresponding to a given title
	 * @param title -> String
	 * @return method -> int
	 */
	public int getMethodWithName(String name) {
		return this.completion_methods.get(name);
		
	}
	
	/**
	 * Function to add Dungeons (used for the creation of inital levels)
	 * @return dungeons -> ObservableList<String>
	 */
	public ObservableList<String> addDungeons() {
		Dungeon d = new Dungeon("testing");
		maps.put("Testing Dungeon: Exit", d.getMap());
		maps.put("Testing Dungeon: Treasure", d.getMap());
		maps.put("Testing Dungeon: Switch", d.getMap());
		maps.put("Testing Dungeon: Enemy", d.getMap());
		ObservableList<String> levels = FXCollections.observableArrayList();
		ArrayList<Integer> di  = new ArrayList<Integer>();
		di.add(20);
		di.add(20);
		dimensions.put("Testing Dungeon: Exit", di);
		completion_methods.put("Testing Dungeon: Exit", 0);
		
		di  = new ArrayList<Integer>();
		di.add(20);
		di.add(20);
		dimensions.put("Testing Dungeon: Treasure", di);
		completion_methods.put("Testing Dungeon: Treasure", 1);
		
		di  = new ArrayList<Integer>();
		di.add(20);
		di.add(20);
		dimensions.put("Testing Dungeon: Switch", di);
		completion_methods.put("Testing Dungeon: Switch", 2);
		
		di  = new ArrayList<Integer>();
		di.add(20);
		di.add(20);
		dimensions.put("Testing Dungeon: Enemy", di);
		completion_methods.put("Testing Dungeon: Enemy", 3);
		
		d = new Dungeon(5, 13);
		d.getPlayer().setPosition(new Point(1,2));
		d.getMap()[1][2] = d.getPlayer();
		d.getMap()[2][5] = new Exit();
		maps.put("Empty Dungeon: 5x13", d.getMap());
		di  = new ArrayList<Integer>();
		di.add(5);
		di.add(13);
		dimensions.put("Empty Dungeon: 5x13", di);
		completion_methods.put("Empty Dungeon: 5x13", 0);
		
		
		for(String s : maps.keySet()) {
			levels.add(s);
		}
		
		return levels;
	}
	
	/**
	 * Function to get the Dimension of a map from a given title
	 * @param title -> String
	 * @return dimensions -> ArrayList<Integer>
	 */
	public ArrayList<Integer> getMapDimensions(String title) {
		return dimensions.get(title);
	}
	
	/**
	 * Function to print all Map data
	 */
	public void printMaps() {
		System.out.println("Name : Map");
		for(Entry<String, Entity[][]> e : maps.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
		
		System.out.println("Name : Dimensions");
		for(Entry<String, ArrayList<Integer>> entry : dimensions.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().get(0) + "x" + entry.getValue().get(1));
		}
	}
}
