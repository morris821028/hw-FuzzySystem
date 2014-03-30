package CarSimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class BowBullet extends Bullet {
	int lifeTime;
	Color bulletColor = new Color(77, 57, 0);
	public BowBullet(double x, double y, double phi, Polygon road,
			Polygon obstacle[]) {
		super(x, y, phi, road, obstacle);
		lifeTime = 1;
	}
	@Override
	public boolean run() {
		if (deadCounter == 0)
			return false;
		if (deadCounter == -1)
			lifeTime++;
		if (deadCounter > 0) {
			deadCounter--;
			return true;
		}
		double tx, ty;
		tx = x + (lifeTime) * Math.cos(phi);
		ty = y + (lifeTime) * Math.sin(phi);
		if (!road.contains(tx, ty)) {
			if (deadCounter < 0)
				deadCounter = boomRange;
			return true;
		}
		for (int i = 0; i < obstacle.length; i++) {
			if (obstacle[i].contains(tx, ty)) {
				if (deadCounter < 0)
					deadCounter = boomRange;
				return true;
			}
		}
		return true;
	}

	public void paint(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g.setColor(bulletColor);
		if (deadCounter < 0) {
			g.drawArc(x - (lifeTime * 4), y- (lifeTime * 4), 2*(lifeTime * 4), 2*(lifeTime * 4),
					(int) ((phi - Math.PI * 0.22) * 180 / Math.PI),
					(int) ((Math.PI * 0.44) * 180 / Math.PI));
		} else {
		}
	}
}
