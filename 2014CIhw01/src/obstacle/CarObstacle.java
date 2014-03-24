package obstacle;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;

import CarSimulator.*;

public class CarObstacle extends Obstacle {
	public Car car;

	public CarObstacle(Polygon poly) {
		super(poly);
	}

	public CarObstacle(Car car) {
		this.car = car;
	}

	public boolean inObstacle(double x, double y) {
		double cx, cy;
		cx = car.getX();
		cy = car.getY();
		return Math.hypot(x - cx, y - cy) < car.getRadius();
	}

	public double getDistToBounds(double x, double y) {
		double cx, cy;
		cx = car.getX();
		cy = car.getY();
		return Math.hypot(x - cx, y - cy) - car.getRadius();
	}

	public Point2D.Double getNearestPoint(double x, double y, double theta) {
		this.poly = new Polygon();
		double cx, cy, R;
		cx = car.getX();
		cy = car.getY();
		R = car.getRadius() + 3;
		this.poly.addPoint((int) (cx+R), (int) (cy+R));
		this.poly.addPoint((int) (cx+R), (int) (cy-R));
		this.poly.addPoint((int) (cx-R), (int) (cy-R));
		this.poly.addPoint((int) (cx-R), (int) (cy+R));
		return super.getNearestPoint(x, y, theta);
	}

	public void paint(Graphics g, CarMap map) {
		return;
	}
}
