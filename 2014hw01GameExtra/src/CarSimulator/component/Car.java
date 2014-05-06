package CarSimulator.component;

import javax.swing.*;

import CarSimulator.CarControlPanel;
import CarSimulator.CarMap;
import CarSimulator.CarSensor;
import CarSimulator.CarWeaponry;

import java.awt.*;
import java.awt.geom.Point2D;

public class Car {
	private double x, y, phi, r;
	private Engine engine;
	public double theta;
	// Weaponry state
	public String[] weaponry = { "NORMAL", "LIMIT", "BOW" };
	public int weaponryIdx;
	public Color[] weaponShowColor = { Color.RED, Color.GREEN,
			new Color(77, 57, 0) };

	public Car(Engine engine) {
		x = 0;
		y = 0;
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

	public void changeWeaponry(int command) {
		weaponryIdx = (weaponryIdx + 1) % weaponry.length;
	}

	public Bullet shootWeaponry(int command, CarMap map) {
		return CarWeaponry.shoot(weaponry[weaponryIdx], x, y, theta, map.road,
				map.obstacle);
	}

	public void run() {
		engine.runDeltaT(this, theta - phi);
	}

	public void paint(Graphics g, CarMap map) {
		Graphics2D g2d = (Graphics2D) g;
		// paint car.
		g2d.setColor(weaponShowColor[weaponryIdx]);
		// g2d.setColor(Color.RED);
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
		Point2D.Double npt = CarSensor.getNearestPoint(map.obstacle,
				this.getX(), this.getY(), this.theta, map.road);
		int x3, y3;
		double v1, v2 = 0, v3 = 0;
		p = map.transOnSwing(npt.x, npt.y);
		x3 = p.x;
		y3 = p.y;
		g2d.drawLine(x, y, x3, y3);
		v1 = Math.hypot(x3 - x, y3 - x);
		npt = CarSensor.getNearestPoint(map.obstacle, this.getX(), this.getY(),
				this.theta + Math.PI / 2, map.road);
		p = map.transOnSwing(npt.x, npt.y);/*
		x3 = p.x;
		y3 = p.y;
		g2d.drawLine(x, y, x3, y3);
		v2 = Math.hypot(x3 - x, y3 - x);
		npt = CarSensor.getNearestPoint(map.obstacle, this.getX(), this.getY(),
				this.theta - Math.PI / 2, map.road);
		p = map.transOnSwing(npt.x, npt.y);
		x3 = p.x;
		y3 = p.y;
		g2d.drawLine(x, y, x3, y3);
		v3 = Math.hypot(x3 - x, y3 - x);*/
		CarControlPanel.getInstance().updateInfo(this, v1, v2, v3);
	}
}
