package CarSimulator.component.circleAlgorithm;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CircleUtility {
	public static double eps = 1e-8;

	public static int IntersectCir(Circle A, Circle B, Point2D.Double v[]) { // Pt
																				// v[]
																				// result
																				// //
																				// buffer
		double disAB = Math.sqrt((A.x - B.x) * (A.x - B.x) + (A.y - B.y)
				* (A.y - B.y));
		if (A.r + B.r + eps < disAB || A.r + disAB + eps < B.r
				|| B.r + disAB + eps < A.r) {
			if (disAB < B.r - A.r)
				return 3; // cover all special case
			return -1;
		}
		double vx, vy;
		vx = B.x - A.x;
		vy = B.y - A.y;
		if (Math.abs(disAB - A.r - B.r) < eps
				|| Math.abs(A.r - disAB - B.r) < eps
				|| Math.abs(B.r - A.r - disAB) < eps) {
			if (Math.abs(disAB - A.r - B.r) < eps) {// (A)(B)
			} else {
				if (A.r < B.r) { // ((A)B)
					v[0].x = A.x - vx * A.r / (B.r - A.r);
					v[0].y = A.y - vy * A.r / (B.r - A.r);
					return 3; // cover all special case
				}// ((B)A)
			}
			return 1;
		}
		double theta = Math.acos((A.r * A.r + disAB * disAB - B.r * B.r) / 2
				/ A.r / disAB);
		double rvx, rvy;
		rvx = vx * Math.cos(theta) - vy * Math.sin(theta);
		rvy = vx * Math.sin(theta) + vy * Math.cos(theta);
		v[0].x = A.x + rvx * A.r / disAB;
		v[0].y = A.y + rvy * A.r / disAB;
		rvx = vx * Math.cos(-theta) - vy * Math.sin(-theta);
		rvy = vx * Math.sin(-theta) + vy * Math.cos(-theta);
		v[1].x = A.x + rvx * A.r / disAB;
		v[1].y = A.y + rvy * A.r / disAB;
		return 2;
	}

	public static Point2D.Double[] circle(Circle a, Circle D[]) {
		int n = D.length;
		double pi = Math.PI;
		Point2D.Double[] p = new Point2D.Double[3];
		ArrayList<Point2D.Double> Interval = new ArrayList<Point2D.Double>();
		for (int i = 0; i < p.length; i++)
			p[i] = new Point2D.Double();
		double ans = 0, L, R, M, tx, ty;
		int cover = 0;
		for (int j = 0; j < n; j++) {
			if (D[j] == a)
				continue;
			int r = IntersectCir(a, D[j], p);
			if (r == 3) {
				cover = 1;
				break;
			}
			if (r < 2)
				continue;
			L = Math.atan2(p[0].y - a.y, p[0].x - a.x);
			R = Math.atan2(p[1].y - a.y, p[1].x - a.x);
			if (L > R) {
				M = L;
				L = R;
				R = M;
			}
			M = (L + R) / 2;
			tx = a.x + a.r * Math.cos(M);
			ty = a.y + a.r * Math.sin(M);
			if (Math.pow(tx - D[j].x, 2) + Math.pow(ty - D[j].y, 2) < Math.pow(
					D[j].r, 2)) {
				Interval.add(new Point2D.Double(L, R));
			} else {
				Interval.add(new Point2D.Double(-pi, L));
				Interval.add(new Point2D.Double(R, pi));
			}
		}
		if (cover == 1) { // all cover
			return null;
		}
		Collections.sort(Interval, new Comparator<Point2D.Double>() {
			@Override
			public int compare(Point2D.Double o1, Point2D.Double o2) {
				if (Math.abs(o1.x - o2.x) > 1e-6)
					return o1.x < o2.x ? -1 : 1;
				if (Math.abs(o1.y - o2.y) > 1e-6)
					return o1.y < o2.y ? -1 : 1;
				return 0;
			}
		});
		ArrayList<Point2D.Double> ret = new ArrayList<Point2D.Double>();
		double rr = -pi;
		for (int i = 0; i < Interval.size(); i++) {
			if (Interval.get(i).x < rr + eps)
				rr = Math.max(Interval.get(i).y, rr);
			else {
				if (Interval.get(i).x > rr + eps)
					ret.add(new Point2D.Double(Interval.get(i).x, rr));
				ans += D[i].r * (Interval.get(i).x - rr);
				rr = Interval.get(i).y;
			}
		}
		if (pi > rr + eps)
			ret.add(new Point2D.Double(pi, rr));
		return ret.toArray(new Point2D.Double[ret.size()]);
	}
}
