package frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import convexhull.Algorithm;

public class ConvexHullCanvas extends JPanel implements KeyEventDispatcher,
		MouseWheelListener, MouseListener, MouseMotionListener {
	private static ConvexHullCanvas singleton = null;

	public static ConvexHullCanvas getInstance() {
		if (singleton == null)
			singleton = new ConvexHullCanvas();
		return singleton;
	}

	public int xLarge = 7, pointRadius = 5;
	public Algorithm algorithm;
	// mouse input.
	private int map_vx, map_vy;
	private Point mouseFirstPressPoint;

	//
	public Point2D.Double[] plane2DPoint;

	private ConvexHullCanvas() {
		this.setBackground(Color.WHITE);
		int x[] = { 0, 6, 6, 30, 30, 18, 18, -6, -6 };
		int y[] = { -100, -100, 10, 10, 100, 100, 22, 22, -100 };
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		// loadMapFile("map0");
		setPreferredSize(new Dimension(500, 500));
	}

	public void loadMapFile(String file) {
		try {
			InputStream fin = getClass().getResourceAsStream(
					"text/" + file + ".txt");
			Scanner cin = new Scanner(fin);
			int n = cin.nextInt();
			Polygon r = new Polygon();
			for (int i = 0; i < n; i++) {
				double x, y;
				x = cin.nextDouble();
				y = cin.nextDouble();
				r.addPoint((int) x, (int) y);
			}
			int hasFinalLine = cin.nextInt();
			if (hasFinalLine > 0) {
				double sx, sy, ex, ey;
				sx = cin.nextDouble();
				sy = cin.nextDouble();
				ex = cin.nextDouble();
				ey = cin.nextDouble();
			} else {
			}
			while (cin.hasNext()) {
				n = cin.nextInt();
				r = new Polygon();
				for (int i = 0; i < n; i++) {
					double x, y;
					x = cin.nextDouble();
					y = cin.nextDouble();
					r.addPoint((int) x, (int) y);
				}
			}
			this.repaint();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED)
			return false;
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			// restart();
		}
		return false;
	}

	public Point transOnSwing(double x, double y) {
		int Ox = this.getWidth() / 2 + map_vx, Oy = this.getHeight() - 100
				+ map_vy;
		return new Point((int) (x * xLarge + Ox), (int) (-y * xLarge + Oy));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Stroke g2dOrigin = g2d.getStroke();
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		paintGridCoordinate(g);
		paintAxisCoordinate(g);
		paintPlane2DPoint(g);
		
		if (algorithm != null) {
			algorithm.paint(g);
		}
		
		g2d.setStroke(g2dOrigin);
	}

	public void paintPlane2DPoint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		if (plane2DPoint != null) {
			for (int i = 0; i < plane2DPoint.length; i++) {
				Point p = this.transOnSwing(plane2DPoint[i].x,
						plane2DPoint[i].y);
				g2d.setColor(Color.CYAN);
				g.fillOval(p.x - pointRadius, p.y - pointRadius,
						2 * pointRadius, 2 * pointRadius);
				g2d.setColor(Color.BLACK);
				g.drawOval(p.x - pointRadius, p.y - pointRadius,
						2 * pointRadius, 2 * pointRadius);
			}
		}
	}

	public void paintAxisCoordinate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 10, new float[] { 2, 2 }, 5));
		Point prev = this.transOnSwing(0, 0), origin = prev;
		int unit = (35 - 3 * xLarge) / 5 * 5;
		if (unit == 0)
			unit = 1;
		for (int x = 0;; x++) {
			Point p = this.transOnSwing(x, 0);
			g.drawLine(prev.x, prev.y, p.x, p.y);
			if (x % (unit) == 0) {
				g.drawLine(p.x, p.y - 5, p.x, p.y + 5);
				g.drawString("" + x, p.x, p.y + 20);
			}
			if (p.x > this.getWidth())
				break;
			prev = p;
		}
		prev = origin;
		for (int x = 0;; x--) {
			Point p = this.transOnSwing(x, 0);
			g.drawLine(prev.x, prev.y, p.x, p.y);
			if (x % (unit) == 0) {
				g.drawLine(p.x, p.y - 5, p.x, p.y + 5);
				g.drawString("" + x, p.x, p.y + 20);
			}
			if (p.x < 0)
				break;
			prev = p;
		}
		prev = origin;
		for (int y = 0;; y++) {
			Point p = this.transOnSwing(0, y);
			g.drawLine(prev.x, prev.y, p.x, p.y);
			if (y % (unit) == 0) {
				g.drawLine(p.x - 5, p.y, p.x + 5, p.y);
				g.drawString("" + y, p.x + 10, p.y + 10);
			}
			if (p.y < 0)
				break;
			prev = p;
		}
		for (int y = 0;; y--) {
			Point p = this.transOnSwing(0, y);
			g.drawLine(prev.x, prev.y, p.x, p.y);
			if (y % (unit) == 0) {
				g.drawLine(p.x - 5, p.y, p.x + 5, p.y);
				g.drawString("" + y, p.x + 10, p.y + 10);
			}
			if (p.y > this.getHeight())
				break;
			prev = p;
		}
	}

	public void paintGridCoordinate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		// g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 10, new float[] { 2, 2 }, 5));
		Point prev = this.transOnSwing(0, 0), origin = prev;
		int unit = (35 - 3 * xLarge) / 5 * 5;
		if (unit == 0)
			unit = 1;
		for (int x = 0;; x++) {
			Point p = this.transOnSwing(x, 0);
			if (x % (unit) == 0)
				g.drawLine(p.x, 0, p.x, this.getHeight());
			if (p.x > this.getWidth())
				break;
			prev = p;
		}
		prev = origin;
		for (int x = 0;; x--) {
			Point p = this.transOnSwing(x, 0);
			if (x % (unit) == 0)
				g.drawLine(p.x, 0, p.x, this.getHeight());
			if (p.x < 0)
				break;
			prev = p;
		}
		prev = origin;
		for (int y = 0;; y++) {
			Point p = this.transOnSwing(0, y);
			if (y % (unit) == 0)
				g.drawLine(0, p.y, this.getWidth(), p.y);
			if (p.y < 0)
				break;
			prev = p;
		}
		for (int y = 0;; y--) {
			Point p = this.transOnSwing(0, y);
			if (y % (unit) == 0)
				g.drawLine(0, p.y, this.getWidth(), p.y);
			if (p.y > this.getHeight())
				break;
			prev = p;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.getWheelRotation() < 0) {
			xLarge = Math.max(1, xLarge - 1);
		} else {
			xLarge = xLarge + 1;
		}

		this.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point p = arg0.getPoint();
		this.map_vx += p.x - this.mouseFirstPressPoint.x;
		this.map_vy += p.y - this.mouseFirstPressPoint.y;
		this.mouseFirstPressPoint = p;
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.mouseFirstPressPoint = arg0.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setAlgorithm(Algorithm al) {
		// TODO Auto-generated method stub
		this.algorithm = al;
	}
}
