package calcModel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.geom.Point2D;

public class FunctionCurve {
	public Point transOnScreen(double x, double y, int dx, int dy) {
		y = y * 80;
		int vx, vy;
		vx = (int) (x * 1.5);
		vy = (int) (60 - y);
		return new Point(dx + vx, dy + vy);
	}

	Color lineColor = new Color(0xbb, 0xff, 0xee, 150);
	Color filledColor = new Color(0x77, 0xdd, 0xff, 150);
	Color backgroundColor = new Color(0x44, 0x44, 0x44, 150);

	public void paint(Graphics g, int dx, int dy, Function f[], String title) {
		if (f == null)
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 3));
		Point p1 = transOnScreen(-70, -0.3, dx, dy);
		Point p2 = transOnScreen(70, 1.2, dx, dy);
		g2d.setColor(backgroundColor);
		g2d.fillRoundRect(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y),
				Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y), 15, 15);
		g2d.setColor(Color.BLACK);
		g2d.drawString(title, dx + 5, dy);
		for (int i = 0; i < f.length; i++) {
			for (int j = 1; j < f[i].pass.length; j++) {
				p1 = transOnScreen(f[i].pass[j].x, f[i].pass[j].y, dx, dy);
				p2 = transOnScreen(f[i].pass[j - 1].x, f[i].pass[j - 1].y, dx,
						dy);
				g2d.setColor(filledColor);
				Polygon polygon = new Polygon();
				polygon.addPoint(p1.x, p1.y);
				polygon.addPoint(p2.x, p2.y);
				p2 = transOnScreen(f[i].pass[j - 1].x, 0, dx, dy);
				p1 = transOnScreen(f[i].pass[j].x, 0, dx, dy);
				polygon.addPoint(p2.x, p2.y);
				polygon.addPoint(p1.x, p1.y);
				g2d.fillPolygon(polygon);
			}
		}
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 3));
		for (int i = -60; i < 60; i += 15) {
			p1 = transOnScreen(i, 0, dx, dy);
			p2 = transOnScreen(i + 15, 0, dx, dy);
			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			g2d.drawLine(p2.x, p2.y - 3, p2.x, p2.y + 3);
			g2d.drawString("" + (i + 15), p2.x, p2.y + 15);
		}
		for (double i = 0; i < 1; i += 0.5) {
			p1 = transOnScreen(0, i, dx, dy);
			p2 = transOnScreen(0, i + 0.5, dx, dy);
				g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			g2d.drawLine(p2.x - 3, p2.y, p2.x + 3, p2.y);
			g2d.drawString("" + (i + 0.5), p2.x + 10, p2.y);
		}
	}
}
