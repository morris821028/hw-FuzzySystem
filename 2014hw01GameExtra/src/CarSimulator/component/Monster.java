package CarSimulator.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import CarSimulator.CarMap;
import CarSimulator.component.circleAlgorithm.Circle;
import CarSimulator.component.circleAlgorithm.CircleUtility;

public class Monster {
	protected double x, y, phi;
	private MonsterEngine engine;
	public int deadCounter, boomRange = 10;
	public Polygon road;
	public int R;
	public Polygon obstacle[];
	public int born;

	public Monster(double x, double y, Polygon road, Polygon obstacle[]) {
		this.x = x;
		this.y = y;
		this.road = road;
		this.obstacle = obstacle;
		deadCounter = -1;
		R = 4;
		born = 0;
		engine = new MonsterEngine();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getPhi() {
		return phi;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public boolean run() {
		if (Math.random() < 0.3 && born >= 30) {
			R++;
			R = Math.min(R, 20);
		}
		if (born < 30) {
			if (born % 10 == 0)
				R++;
			born++;
			return true;
		}
		if (deadCounter == 0)
			return false;
		if (deadCounter == -1)
			engine.runDeltaT(this, phi, 0);
		if (deadCounter > 0) {
			deadCounter--;
			return true;
		}
		if (!road.contains(x, y)) {
			if (deadCounter < 0)
				deadCounter = boomRange;
			return true;
		}
		for (int i = 0; i < obstacle.length; i++) {
			if (obstacle[i].contains(x, y)) {
				if (deadCounter < 0)
					deadCounter = boomRange;
				return true;
			}
		}
		return true;
	}

	public void paintSpecialCircle(Graphics g, int x, int y,
			Map<Monster, TimerTask> monsterMap) {
		Object[] mm = monsterMap.keySet().toArray();
		Circle[] circle = new Circle[mm.length];
		Circle I = null;
		for (int i = 0; i < circle.length; i++) {
			Point p = CarMap.getInstance().transOnSwing(
					((Monster) mm[i]).getX(), ((Monster) mm[i]).getY());
			circle[i] = new Circle();
			circle[i].x = p.x;
			circle[i].y = - p.y;
			circle[i].r = ((Monster) mm[i]).R + 1;
			if (mm[i] == this)
				I = circle[i];
		}
		Point2D.Double[] interval = CircleUtility.circle(I, circle);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, new float[] { 5, 5 }, 5));
		g.setColor(Color.BLACK);
		if (deadCounter < 0) {
			g.fillOval(x - R, y - R, 2 * R, 2 * R);
		} else {
			R += boomRange - deadCounter;
			g.drawOval(x - R, y - R, 2 * R, 2 * R);
		}
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g.setColor(Color.RED);
		if (interval != null) {
			for (int i = 0; i < interval.length; i++) {
				int l, r;
				l = (int) (interval[i].y / Math.PI * 180);
				r = (int) (interval[i].x / Math.PI * 180);
				g.drawArc(x - R, y - R, 2 * R, 2 * R, l, r - l);
			}
		}
	}

	public void paintNormalCircle(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, new float[] { 5, 5 }, 5));
		g.setColor(Color.BLACK);
		if (deadCounter < 0) {
			g.fillOval(x - R, y - R, 2 * R, 2 * R);
			g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 10));
			g.setColor(Color.RED);
			g.drawOval(x - R, y - R, 2 * R, 2 * R);
		} else {
			R += boomRange - deadCounter;
			g.drawOval(x - R, y - R, 2 * R, 2 * R);
		}
	}

	public void paint(Graphics g, int x, int y,
			Map<Monster, TimerTask> monsterMap) {
		// paintNormalCircle(g, x, y);
		paintSpecialCircle(g, x, y, monsterMap);
	}
}
