package obstacle;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import CarSimulator.*;

public class Obstacle {
	protected Polygon poly;

	public Obstacle() {

	}

	public Obstacle(Polygon poly) {
		this.poly = poly;
	}

	public boolean inObstacle(double x, double y) {
		return poly.contains(x, y);
	}

	public double getDistToBounds(double x, double y) {
		double ret = 1e+5;
		for (int i = 0, j = poly.npoints - 1; i < poly.npoints; j = i++) {
			Line2D.Double l1 = new Line2D.Double(poly.xpoints[i],
					poly.ypoints[i], poly.xpoints[j], poly.ypoints[j]);
			ret = Math.min(ret, l1.ptSegDist(x, y));
		}
		return ret;
	}

	public Point2D.Double getNearestPoint(double x, double y, double theta) {
		Point2D.Double ret = new Point2D.Double();
		ret.x = x;
		ret.y = y;
		double mn = 1e+5;
		for (int i = 0, j = poly.npoints - 1; i < poly.npoints; j = i++) {
			Line2D.Double l1 = new Line2D.Double(poly.xpoints[i],
					poly.ypoints[i], poly.xpoints[j], poly.ypoints[j]);
			Line2D.Double l2 = new Line2D.Double(x, y, x + Math.cos(theta)
					* 1e+5, y + Math.sin(theta) * 1e+5);
			if (l1.intersectsLine(l2)) {
				double l = 0, r = 1e+5, mid;
				int iteratorCnt = 0;
				while (iteratorCnt++ < 20) {
					mid = (l + r) / 2;
					l2.setLine(x, y, x + Math.cos(theta) * mid,
							y + Math.sin(theta) * mid);
					if (l1.intersectsLine(l2))
						r = mid;
					else
						l = mid;
				}
				mn = Math.min(mn, l);
			}
		}
		ret.x = ret.x + Math.cos(theta) * mn;
		ret.y = ret.y + Math.sin(theta) * mn;
		return ret;
	}

	public void paint(Graphics g, CarMap map) {
		Polygon proad = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
		for (int i = 0; i < proad.npoints; i++) {
			Point p = map.transOnSwing(proad.xpoints[i], proad.ypoints[i]);
			proad.xpoints[i] = p.x;
			proad.ypoints[i] = p.y;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g2d.setColor(map.obstacleColor);
		g2d.fillPolygon(proad);
		g2d.setColor(map.borderColor);
		g2d.drawPolygon(proad);
	}
}
