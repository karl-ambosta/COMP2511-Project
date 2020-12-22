package entities;

import java.awt.Point;
import java.util.ArrayList;
import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

import java.util.Random;


/**
 * Class for potions with methods to use position
 *
 */
public class KillEnemyPotion implements Potion{
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/killenemy_potion.png");
		i.setId("killEnemy");
		return i;
	}

	@Override
	public void usePotion() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canMoveThrough() {
		return true;
	}

	@Override
	public EntityState interact(Inventory inv) {
		return EntityState.Alive;
	}
	
	@Override
	public boolean canInteract(Inventory inv) {
		return true;
	}
	
	/**
	 * Function to use the Kill Enemy Potion (kills a random Enemy)
	 * @param dungeon -> Dungeon
	 */
	public void findEnemy(Dungeon dungeon) {
		ArrayList <Character> enemies = dungeon.getEnemies();
		if (!enemies.isEmpty()) {
			Random rand = new Random();
			int n = rand.nextInt(enemies.size());
			Point point = ((Enemy) enemies.get(n)).getPosition();
			Entity[][] Map = dungeon.getMap();
			Map[point.x][point.y] = null;
			enemies.remove(n);
			dungeon.getMap();
		}
	}

}
