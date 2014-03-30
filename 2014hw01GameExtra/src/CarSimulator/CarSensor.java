package CarSimulator;

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class CarSensor {
	public static Point2D.Double getNearestPoint(Polygon poly[], double x,
			double y, double theta, Polygon bounds) {
		Point2D.Double ret = new Point2D.Double();
		ret.x = x;
		ret.y = y;
		double mn = 1e+5;
		for (int k = 0; k < poly.length; k++)
			for (int i = 0, j = poly[k].npoints - 1; i < poly[k].npoints; j = i++) {
				Line2D.Double l1 = new Line2D.Double(poly[k].xpoints[i],
						poly[k].ypoints[i], poly[k].xpoints[j],
						poly[k].ypoints[j]);
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
		for (int i = 0, j = bounds.npoints - 1; i < bounds.npoints; j = i++) {
			Line2D.Double l1 = new Line2D.Double(bounds.xpoints[i],
					bounds.ypoints[i], bounds.xpoints[j], bounds.ypoints[j]);
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
}
