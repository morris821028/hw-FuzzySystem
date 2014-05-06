package CarSimulator.component;

import java.awt.Polygon;

public class LimitedBullet extends Bullet {
	int limitTime;
	public LimitedBullet(double x, double y, double phi, Polygon road, Polygon obstacle[]) {
		super(x, y, phi, road, obstacle);
		limitTime = 50;
		boomRange = 50;
	}
	public boolean run() {
		limitTime--;
		if(limitTime < 0 && this.deadCounter < 0) {
			this.deadCounter = boomRange;
		}
		return super.run();
	}
}
