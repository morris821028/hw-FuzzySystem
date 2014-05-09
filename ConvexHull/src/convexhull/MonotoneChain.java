package convexhull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.SwingWorker;

import frame.ConvexHullCanvas;

public class MonotoneChain extends Algorithm {
	//
	Point2D.Double[] ch;
	int processCH;
	Point2D.Double processP1, processP2;

	@Override
	public void run(ConvexHullCanvas canvas) {
		canvas.setAlgorithm(this);
		Thread r = new Thread() {
			public void sleep() {
				try {
					Thread.sleep(500);
				} catch (Exception e) {

				}
			}

			public void run() {
				
				processCH = 0;
				CH = null;
				processP1 = null;
				processP2 = null;
				
				ConvexHullCanvas canvas = ConvexHullCanvas.getInstance();
				Arrays.sort(D, new Comparator<Point2D.Double>() {
					@Override
					public int compare(Point2D.Double o1, Point2D.Double o2) {
						double eps = 1e-6;
						if (Math.abs(o1.x - o2.x) > eps)
							return o1.x < o2.x ? -1 : 1;
						if (Math.abs(o1.y - o2.y) > eps)
							return o1.y < o2.y ? -1 : 1;
						return 0;
					}
				});
				int i, m = 0, t;
				int n = D.length;
				ch = new Point2D.Double[2 * n + 10];
				for (i = 0; i < n; i++) {
					while (m >= 2 && cross(ch[m - 2], ch[m - 1], D[i]) <= 0) {
						processP1 = ch[m - 2];
						processP2 = D[i];
						processCH = m - 1;
						
						canvas.repaint();
						sleep();
						m--;
					}
					ch[m++] = D[i];
				}
				for (i = n - 1, t = m + 1; i >= 0; i--) {
					while (m >= t && cross(ch[m - 2], ch[m - 1], D[i]) <= 0) {
						processP1 = ch[m - 2];
						processP2 = D[i];
						processCH = m - 1;
						canvas.repaint();
						sleep();
						
						m--;
					}
					ch[m++] = D[i];
				}
				processP1 = null;
				processP2 = null;
				CH = new Point2D.Double[m];
				for (i = 0; i < m; i++)
					CH[i] = ch[i];
				canvas.repaint();
			}
		};
		r.start();
	}

	public double cross(Point2D.Double o, Point2D.Double a, Point2D.Double b) {
		return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.ORANGE);
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		
		if (processCH > 0) {
			int pointRadius = ConvexHullCanvas.getInstance().pointRadius;
			Point prevPt = null;
			for (int i = 0; i < processCH; i++) {
				Point p = ConvexHullCanvas.getInstance().transOnSwing(ch[i].x,
						ch[i].y);
				g.drawOval(p.x - pointRadius, p.y - pointRadius,
						2 * pointRadius, 2 * pointRadius);
				if (prevPt != null) {
					g.drawLine(prevPt.x, prevPt.y, p.x, p.y);
				}
				prevPt = p;
			}
			if (CH != null) {
				Point p = ConvexHullCanvas.getInstance().transOnSwing(ch[0].x,
						ch[0].y);
				if (prevPt != null) {
					g.drawLine(prevPt.x, prevPt.y, p.x, p.y);
				}
			}
			if(processP1 != null && processP2 != null) {
				Point p1 = ConvexHullCanvas.getInstance().transOnSwing(processP1.x,
						processP1.y);
				Point p2 = ConvexHullCanvas.getInstance().transOnSwing(processP2.x,
						processP2.y);
				g.setColor(Color.darkGray);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}
}
