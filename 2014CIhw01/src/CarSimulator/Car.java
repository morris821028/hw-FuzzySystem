package CarSimulator;

import javax.swing.*;

import calcModel.Engine;
import calcModel.FuzzySystem;
import calcModel.FuzzySystemII;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Car {
	private double x, y, phi, r;
	private Engine engine;
	public double theta;
	public double d1, d2, d3;
	public Vector<Point2D.Double> carPath = new Vector<Point2D.Double>();
	public FuzzySystem fuzzySystem = new FuzzySystemII();
	public CarSensor sensor;

	Car(Engine engine) {
		x = 0;
		y = 0;
		phi = Math.PI / 2;
		theta = phi;
		r = 3;
		this.sensor = new CarSensor(this);
		this.engine = engine;
	}

	public synchronized double getX() {
		return x;
	}

	public synchronized double getY() {
		return y;
	}

	public synchronized double getPhi() {
		return phi;
	}

	public synchronized double getRadius() {
		return r;
	}

	public synchronized void setX(double x) {
		this.x = x;
	}

	public synchronized void setY(double y) {
		this.y = y;
	}

	public synchronized void setPhi(double phi) {
		this.phi = phi;
	}

	public synchronized void run() {
		theta = phi + fuzzySystem.program(d1, d2, d3) / 180.0 * Math.PI;
		engine.runDeltaT(this, theta - phi);
	}

	public boolean hasCollision(CarMap cmap) {
		double bb = sensor.getDistToBounds(cmap, this.getX(), this.getY());
		return bb < this.getRadius();
	}

	public void paint(Graphics g, CarMap map) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
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
		g2d.setColor(Color.GREEN);
		x1 = (int) (x + Math.cos(this.theta) * R / 2.0);
		y1 = (int) (y + -Math.sin(this.theta) * R / 2.0);
		x2 = (int) (x1 + Math.cos(this.theta) * R);
		y2 = (int) (y1 + -Math.sin(this.theta) * R);
		g2d.drawLine(x1, y1, x2, y2);
		// Sensor.
		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, new float[] { 5, 5 }, 5));
		Point2D.Double npt;
		npt = sensor.getNearestPoint(map, this.getX(), this.getY(), this.theta);
		double v1, v2, v3;
		p = map.transOnSwing(npt.x, npt.y);
		g2d.drawLine(x, y, p.x, p.y);
		v1 = Math.hypot(npt.x - this.getX(), npt.y - this.getY());

		npt = sensor.getNearestPoint(map, this.getX(), this.getY(), this.theta
				- Math.PI / 180.0 * 50);
		p = map.transOnSwing(npt.x, npt.y);
		g2d.drawLine(x, y, p.x, p.y);
		v2 = Math.hypot(npt.x - this.getX(), npt.y - this.getY());

		npt = sensor.getNearestPoint(map, this.getX(), this.getY(), this.theta
				+ Math.PI / 180.0 * 50);
		p = map.transOnSwing(npt.x, npt.y);
		g2d.drawLine(x, y, p.x, p.y);
		v3 = Math.hypot(npt.x - this.getX(), npt.y - this.getY());
		this.d1 = v1;
		this.d2 = v2;
		this.d3 = v3;
		if (this.hasCollision(map)) {
			map.eventFlag = 1;
			String msg = String
					.format("[Collision] x %.3f y %.3f phi %.3f d1 %.3f d2 %.3f d3 %.3f\n",
							this.getX(), this.getY(), this.getPhi(), v1, v2, v3);
			CarControlPanel.getInstance().consoleArea.append("\r\n" + msg);
			System.out.printf(msg);
		}
		CarControlPanel.getInstance().updateInfo(this, v1, v2, v3,
				sensor.getDistToBounds(map, this.getX(), this.getY()));
	}
}
