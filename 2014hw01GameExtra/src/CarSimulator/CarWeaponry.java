package CarSimulator;

import java.awt.Polygon;

public class CarWeaponry {
	public static Bullet shoot(String kind, double x, double y, double phi, Polygon road, Polygon obstacle[]) {
		if(kind.equals("NORMAL"))
			return new Bullet(x, y, phi, road, obstacle);
		if(kind.equals("LIMIT"))
			return new LimitedBullet(x, y, phi, road, obstacle);
		if(kind.equals("BOW"))
			return new BowBullet(x, y, phi, road, obstacle);
		return new Bullet(x, y, phi, road, obstacle);
	}
}
