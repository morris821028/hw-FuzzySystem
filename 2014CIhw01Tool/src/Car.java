

import javax.swing.*;

import java.awt.*;
import java.awt.geom.Point2D;

public class Car {
	private double x, y, phi, r;
	private Engine engine;
	public double theta;
	public double d1, d2, d3;

	Car(Engine engine) {
		x = 0;
		y = -10;
		phi = Math.PI / 2;
		theta = phi;
		r = 3;
		this.engine = engine;
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

	public double getRadius() {
		return r;
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

	public void run() {
		engine.runDeltaT(this, theta - phi);
	}


	public void paint(Graphics g, CarMap map) {
		Graphics2D g2d = (Graphics2D) g;
		// paint car.
		g2d.setColor(Color.RED);
		int x, y, R;
		Point p = map.transOnSwing(this.getX(), this.getY());
		x = p.x;
		y = p.y;
		R = (int) (this.getRadius() * map.xLarge);
		g2d.drawOval(x - R, y - R, 2 * R, 2 * R);
		//
		int x1, y1, x2, y2;
		x1 = (int) (x + Math.cos(this.getPhi()) * R / 2.0);
		y1 = (int) (y + -Math.sin(this.getPhi()) * R / 2.0);
		x2 = (int) (x1 + Math.cos(this.getPhi()) * R);
		y2 = (int) (y1 + -Math.sin(this.getPhi()) * R);
		g2d.drawLine(x1, y1, x2, y2);
		// Next direction.
		g2d.setColor(Color.BLUE);
		x1 = (int) (x + Math.cos(this.theta) * R / 2.0);
		y1 = (int) (y + -Math.sin(this.theta) * R / 2.0);
		x2 = (int) (x1 + Math.cos(this.theta) * R);
		y2 = (int) (y1 + -Math.sin(this.theta) * R);
		g2d.drawLine(x1, y1, x2, y2);
	}
}
