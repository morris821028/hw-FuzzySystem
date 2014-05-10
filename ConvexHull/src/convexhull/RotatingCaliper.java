package convexhull;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Comparator;

import frame.ConvexHullCanvas;

public class RotatingCaliper extends Algorithm {
	Algorithm convex;

	Point2D.Double processP1, processP2;
	double processTheta;
	boolean processFlag;

	public RotatingCaliper(Algorithm convex) {
		this.convex = convex;
	}

	@Override
	public void run(ConvexHullCanvas canvas) {
		Thread r = new Thread() {
			public void sleep() {
				try {
					Thread.sleep(40);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

			private void singleRun(int l, int r) {
				ConvexHullCanvas canvas = ConvexHullCanvas.getInstance();
				int N = convex.CH.length;

				for (int i = l, j = l; i % N != r; i = (i + 1) % N) {
					while (cross(D[(i + 1) % N], D[i], D[(j + 1) % N], D[j]) >= 0)
						j = (j + 1) % N;

					double stTheta = Math.atan2(-(D[(i + 1) % N].y - D[i].y),
							D[(i + 1) % N].x - D[i].x);
					double edTheta = Math.atan2(-(D[(i + 2) % N].y - D[(i + 1)
							% N].y), D[(i + 2) % N].x - D[(i + 1) % N].x);

					if (processFlag == false) {
						processFlag = true;
						processTheta = stTheta;
					} else {
						if (processTheta < -Math.PI)
							processTheta += Math.PI * 2;
					}

					if (edTheta > processTheta)
						edTheta -= Math.PI * 2;

					Point p1 = ConvexHullCanvas.getInstance().transOnSwing(
							D[i].x, D[i].y);
					Point p2 = ConvexHullCanvas.getInstance().transOnSwing(
							D[j].x, D[j].y);
					Point p3 = ConvexHullCanvas.getInstance().transOnSwing(
							D[(j + 1) % N].x, D[(j + 1) % N].y);
					Point p4 = ConvexHullCanvas.getInstance().transOnSwing(
							D[(i + 1) % N].x, D[(i + 1) % N].y);

					processP1 = D[i];
					processP2 = D[j];
					while (processTheta >= edTheta) {
						double a, b, c;

						a = Math.sin(processTheta);
						b = -Math.cos(processTheta);

						c = -(a * p2.x + b * p2.y);
						if ((a * p1.x + b * p1.y + c)
								* (a * p3.x + b * p3.y + c) <= 0)
							break;

						c = -(a * p1.x + b * p1.y);
						if ((a * p3.x + b * p3.y + c)
								* (a * p4.x + b * p4.y + c) <= 0)
							processP1 = D[(i + 1) % N];

						canvas.repaint();
						sleep();

						processTheta -= 0.02;
					}
				}
			}

			public void run() {
				ConvexHullCanvas canvas = ConvexHullCanvas.getInstance();
				int N = convex.CH.length;

				D = convex.CH;

				processFlag = false;

				for (int loop = 0, i = 0, j = 0, prevj = j; loop < 10; loop++) {
					for (j = 0, prevj = j;; i++, i = i % N) {
						prevj = j;
						int cnt = 0;
						while (cross(D[(i + 1) % N], D[i], D[(j + 1) % N], D[j]) >= 0) {
							j = (j + 1) % N;
							cnt++;
						}

						if (cnt >= 1) {
							singleRun(prevj, j);
							i = (i + 1) % N;
						}

						double stTheta = Math.atan2(
								-(D[(i + 1) % N].y - D[i].y), D[(i + 1) % N].x
										- D[i].x);
						double edTheta = Math.atan2(
								-(D[(i + 2) % N].y - D[(i + 1) % N].y),
								D[(i + 2) % N].x - D[(i + 1) % N].x);

						if (processFlag == false) {
							processFlag = true;
							processTheta = stTheta;
						} else {
							if (processTheta < -Math.PI)
								processTheta += Math.PI * 2;
						}

						if (edTheta > processTheta)
							edTheta -= Math.PI * 2;

						Point p1 = ConvexHullCanvas.getInstance().transOnSwing(
								D[i].x, D[i].y);
						Point p2 = ConvexHullCanvas.getInstance().transOnSwing(
								D[j].x, D[j].y);
						Point p3 = ConvexHullCanvas.getInstance().transOnSwing(
								D[(j + 1) % N].x, D[(j + 1) % N].y);
						Point p4 = ConvexHullCanvas.getInstance().transOnSwing(
								D[(i + 1) % N].x, D[(i + 1) % N].y);

						processP1 = D[i];
						processP2 = D[j];
						System.out.println("NORMAL");
						while (processTheta >= edTheta) {
							double a, b, c;

							a = Math.sin(processTheta);
							b = -Math.cos(processTheta);

							c = -(a * p2.x + b * p2.y);
							if ((a * p1.x + b * p1.y + c)
									* (a * p3.x + b * p3.y + c) <= 0)
								break;

							c = -(a * p1.x + b * p1.y);
							if ((a * p3.x + b * p3.y + c)
									* (a * p4.x + b * p4.y + c) <= 0) {
								System.out.println("DOING");
								processP1 = D[(i + 1) % N];
							}

							canvas.repaint();
							sleep();

							processTheta -= 0.02;
						}
					}
				}
			}
		};
		r.start();
	}

	public double cross(Point2D.Double a, Point2D.Double b, Point2D.Double p,
			Point2D.Double q) {
		double ax, ay, bx, by;
		ax = a.x - b.x;
		ay = a.y - b.y;
		bx = p.x - q.x;
		by = p.y - q.y;
		return ax * by - ay * bx;
	}

	public void paint(Graphics g) {
		if (processP1 != null) {
			Point p1 = ConvexHullCanvas.getInstance().transOnSwing(processP1.x,
					processP1.y);
			Point p2 = ConvexHullCanvas.getInstance().transOnSwing(processP2.x,
					processP2.y);

			Graphics2D g2d = (Graphics2D) g;

			g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_BEVEL, 10));

			g.setColor(Color.GREEN);
			int sx, sy, ex, ey;
			sx = (int) (p1.x - 1000 * Math.cos(processTheta));
			ex = (int) (p1.x + 1000 * Math.cos(processTheta));
			sy = (int) (p1.y - 1000 * Math.sin(processTheta));
			ey = (int) (p1.y + 1000 * Math.sin(processTheta));
			g.drawLine(sx, sy, ex, ey);

			int pointRadius = ConvexHullCanvas.getInstance().pointRadius;

			g.setColor(Color.RED);
			sx = (int) (p2.x - 1000 * Math.cos(processTheta));
			ex = (int) (p2.x + 1000 * Math.cos(processTheta));
			sy = (int) (p2.y - 1000 * Math.sin(processTheta));
			ey = (int) (p2.y + 1000 * Math.sin(processTheta));

			g.drawLine(sx, sy, ex, ey);
		}
	}
}
