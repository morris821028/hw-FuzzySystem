package CarSimulator;

import java.awt.*;

public class Bullet {
	protected double x, y, phi;
	private BulletEngine engine;
	public int deadCounter, boomRange = 10;
	public Polygon road;
	public Polygon obstacle[];
	public Bullet(double x, double y, double phi, Polygon road, Polygon obstacle[]) {
		this.x = x;
		this.y = y;
		this.phi = phi;
		this.road = road;
		this.obstacle = obstacle;
		deadCounter = -1;
		engine = new BulletEngine();
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
		if(deadCounter == 0)
			return false;
		if(deadCounter == -1)
			engine.runDeltaT(this, phi, 0);
		if(deadCounter > 0) {
			deadCounter--;
			return true;
		}
		if(!road.contains(x, y)) {
			if(deadCounter < 0)
				deadCounter = boomRange;
			return true;
		}
		for(int i = 0; i < obstacle.length; i++) {
			if(obstacle[i].contains(x, y)) {
				if(deadCounter < 0)
					deadCounter = boomRange;
				return true;
			}
		}
		return true;
	}
	
	public void paint(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, new float[] { 5, 5 }, 5));
		int R = 5;
		g.setColor(Color.BLACK);
		if(deadCounter < 0) {
			g.fillOval(x - R, y - R, 2*R, 2*R);
		} else {
			R += boomRange - deadCounter;
			g.drawOval(x - R, y - R, 2*R, 2*R);
		}
	}
}
