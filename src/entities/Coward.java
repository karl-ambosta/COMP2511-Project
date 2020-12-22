package entities;

import java.awt.Point;
import java.util.Map;

import core.Dungeon;
import core.Inventory;
import javafx.scene.image.ImageView;

/**
 * 
 * Coward class with methods for coward
 *
 */
public class Coward extends Enemy implements Character {
	
	@Override
	public ImageView getPicture() {
		ImageView i = new ImageView("/images/coward.png");
		i.setId("coward");
		return i;
	}

	/**
	 * Moves the Coward toward the Player and then moves away once gets close
	 * @param Entity[][] Map -> Dungeon
	 * @param Point player
	 * @post Moves the Coward in the specified direction (if available)
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
		
		//Moves the Coward Entity
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
		if(distance > 2) {
			return true;
		}
		return false;
	}
	
	@Override
	public Point findPoint(Dungeon dungeon, Map<Point, Double> distMap) {
		Entity[][] Map = dungeon.getMap();
		Point p = new Point(0,0);
		Player player = dungeon.getPlayer();
		
		if(player.hasInvincPotion() || this.position.distance(player.getPosition()) <= 2) {
			p = getMaxPoint(Map, distMap);
		} else {
			p = getMinPoint(Map, distMap);
		}
		return p;
	}
}
