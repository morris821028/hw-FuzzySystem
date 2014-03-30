package CarSimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ActivityPanel extends JPanel {
	public java.util.Timer testTimer = new java.util.Timer("Record Timer");
	public TimerTask testTask;
	public double[] f;
	public double timeStamp;

	public ActivityPanel() {
		super();
		this.setBorder(new TitledBorder("Fuzzy System"));
		f = new double[50];
		testTask = new TimerTask() {
			public void run() {
				for (int i = 0; i < f.length - 1; i++) {
					f[i] = f[i + 1];
				}
				f[f.length - 1] = timeStamp;
				timeStamp = 0;
				ActivityPanel.this.repaint();
				Thread.yield();
			}
		};
		testTimer.scheduleAtFixedRate(testTask, 1000, 150);
	}

	public void addTheta(double theta) {
		timeStamp = theta;
	}

	public Point transOnScreen(double x, double y) {
		int vx, vy;
		y = y * this.getHeight() / 2;
		vx = (int) (x * this.getWidth() / f.length);
		vy = (int) (this.getHeight() / 2 - y);
		return new Point(vx, vy);
	}

	Color lineColor = new Color(0x00, 0xaa, 0x00);
	Color filledColor = new Color(0x77, 0xdd, 0xff, 150);
	Color backgroundColor = new Color(0x44, 0x44, 0x44, 150);

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 3));
		g2d.setColor(lineColor);
		for (int i = 0; i < f.length - 1; i++) {
			Point p1 = transOnScreen(i, f[i]);
			Point p2 = transOnScreen(i + 1, f[i + 1]);
			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		g2d.setFont(new Font("Consolas", Font.BOLD, 24));
		g2d.drawString(String.format("%-5.1f", f[f.length - 1] / Math.PI * 180),
			30, 50);
	}
}
