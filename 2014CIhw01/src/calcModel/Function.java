package calcModel;

import java.awt.geom.*;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.Arrays;

public class Function {
	public Point2D.Double[] pass;

	public Function(Point2D.Double[] pass) {
		this.pass = pass;
	}

	/**
	 * 
	 * @param xVal
	 * @param alpha
	 * @return minimum(f(xVal), alpha)
	 */
	public double getVal(double xVal, double alpha) {
		for (int i = 0, j = 1; j < pass.length; i++, j++) {
			if (pass[i].getX() <= xVal && xVal <= pass[j].getX()) {
				double vx = pass[j].getX() - pass[i].getX();
				double vy = pass[j].getY() - pass[i].getY();
				double y;
				if (Math.abs(vx) < 1e-6)
					return Math.min(pass[j].getY(), alpha);
				y = pass[i].getY() + (xVal - pass[i].getX()) / vx * vy;
				return Math.min(y, alpha);
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param alpha
	 *            yVal
	 * @return the function by intersection with y = alpha.
	 */
	public Function getIntersection(double alpha) {
		ArrayList<Point2D.Double> arr = new ArrayList<Point2D.Double>();
		double px = -100, py = -100;
		for (int i = 0, j = 1; j < pass.length; i++, j++) {
			double vx = pass[j].getX() - pass[i].getX();
			double vy = pass[j].getY() - pass[i].getY();
			double x;
			if (Math.abs(px - pass[i].getX()) > 1e-6
					|| Math.abs(py - pass[i].getY()) > 1e-6) {
				arr.add(new Point2D.Double(pass[i].getX(), Math.min(
						pass[i].getY(), alpha)));
				px = pass[i].getX();
				py = Math.min(pass[i].getY(), alpha);
			}
			if (Math.abs(vy) < 1e-6) {
			} else {
				x = pass[i].getX() + (alpha - pass[i].getY()) / vy * vx;
				if (pass[i].getX() <= x && x <= pass[j].getX()) {
					if (Math.abs(px - x) > 1e-6
							|| Math.abs(py - alpha) > 1e-6) {
						arr.add(new Point2D.Double(x, alpha));
						px = x;
						py = alpha;
					}
				} else {
				}
			}
			if (Math.abs(px - pass[j].getX()) > 1e-6
					|| Math.abs(py - pass[j].getY()) > 1e-6) {
				arr.add(new Point2D.Double(pass[j].getX(), Math.min(
						pass[j].getY(), alpha)));
				px = pass[j].getX();
				py = Math.min(pass[j].getY(), alpha);
			}
		}
		Point2D.Double[] r = new Point2D.Double[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			r[i] = arr.get(i);
		}
		Function ret = new Function(r);
		return ret;
	}
}
