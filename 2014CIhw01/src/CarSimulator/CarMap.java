package CarSimulator;

import obstacle.CarObstacle;
import obstacle.Obstacle;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.io.InputStream;

public class CarMap extends JPanel implements KeyEventDispatcher,
		MouseWheelListener, MouseListener, MouseMotionListener {

	public Polygon road;
	public Vector<Obstacle> obstacles = new Vector<Obstacle>();
	public Color roadColor;
	public Color borderColor;
	public Color obstacleColor;
	public int xLarge = 7;
	public Vector<Car> cars = new Vector<Car>();
	// public Car car;

	// mouse input.
	private int map_vx, map_vy;
	private Point mouseFirstPressPoint;

	// by fuzzy stream driver.
	public int eventFlag;

	public CarMap(Car car) {
		this.setBackground(new Color(0x66, 0x88, 0x00));
		int x[] = { 0, 6, 6, 30, 30, 18, 18, -6, -6 };
		int y[] = { -100, -100, 10, 10, 100, 100, 22, 22, -100 };
		this.road = new Polygon(x, y, x.length);
		this.roadColor = Color.GRAY;
		this.borderColor = new Color(232, 119, 89);
		this.obstacleColor = new Color(112, 180, 215);
		this.cars.add(car);
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		loadImage();
		CarControlPanel.getInstance().carMap = this;
	}

	public void loadMapFile(String file) {
		try {
			Car car = cars.get(0);
			cars.removeAllElements();
			cars.add(car);
			this.obstacles.removeAllElements();
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
			this.road = r;
			while (cin.hasNext()) {
				n = cin.nextInt();
				r = new Polygon();
				for (int i = 0; i < n; i++) {
					double x, y;
					x = cin.nextDouble();
					y = cin.nextDouble();
					r.addPoint((int) x, (int) y);
				}
				obstacles.add(new Obstacle(r));
			}
			this.repaint();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private Hashtable imgTable = new Hashtable();

	private void loadImage() {
		String fileIdx[] = { "FAIL" };
		String fileName[] = { "FAIL.png" };
		Image img;
		try {
			for (int i = 0; i < fileName.length; i++) {
				img = ImageIO.read(this.getClass().getResource(
						"image/" + fileName[i]));
				imgTable.put(fileIdx[i], new ImageIcon(img));
			}
		} catch (Exception e) {
			System.out.println("Err !!!!!! " + e.getMessage());
		}
	}

	public void restart() {
		eventFlag = 0;
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			do {
				car.setX(Math.random() * 20 - 10);
				car.setY(Math.random() * 20 - 10);
			} while (!isCarPositionVaild(car) || car.hasCollision(this));
			car.setPhi(Math.random());
			if (!CarControlPanel.getInstance().pathRetain.isSelected())
				car.carPath.removeAllElements();
		}
		this.repaint();
	}

	public boolean isCarPositionVaild(Car car) {
		double x = car.getX(), y = car.getY();
		if (!road.contains(x, y))
			return false;
		for (int i = 0; i < obstacles.size(); i++) {
			if (obstacles.get(i) instanceof CarObstacle) {
				CarObstacle co = (CarObstacle) obstacles.get(i);
				if (co.car == car)
					continue;
			}
			if (obstacles.get(i).inObstacle(x, y))
				return false;
		}
		return true;
	}

	private int runCarCount = 0;

	public synchronized boolean runCar() {
		for (int k = 0; k < cars.size(); k++) {
			Car car = cars.get(k);
			double x = car.getX(), y = car.getY();
			car.run();
			car.setPhi(car.theta);
			if (!isCarPositionVaild(car)) {
				car.setX(x);
				car.setY(y);
				continue;
			}
			if ((runCarCount++) % 3 == 0) {
				car.carPath.add(new Point2D.Double(car.getX(), car.getY()));
				if (car.carPath.size() > 1000) {
					Vector<Point2D.Double> temp = new Vector<Point2D.Double>();
					for (int i = car.carPath.size() / 2; i < car.carPath.size(); i++)
						temp.add(car.carPath.get(i));
					car.carPath = temp;
				}
			}
		}
		this.repaint();
		return true;
	}

	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED)
			return false;
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_RIGHT) { if (Math.abs(car.theta -
		 * Math.PI / 180.0 * 3 - car.getPhi()) < Math.PI / 180.0 * 40) car.theta
		 * -= Math.PI / 180.0 * 3; } if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		 * if (Math.abs(car.theta + Math.PI / 180.0 * 3 - car.getPhi()) <
		 * Math.PI / 180.0 * 40) car.theta += Math.PI / 180.0 * 3; }
		 */
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			restart();
		}
		return false;
	}

	public Point transOnSwing(double x, double y) {
		int Ox = this.getWidth() / 2 + map_vx, Oy = this.getHeight() - 100
				+ map_vy;
		return new Point((int) (x * xLarge + Ox), (int) (-y * xLarge + Oy));
	}

	public void autoTrackCarAdjust() {
		if (!CarControlPanel.getInstance().autoTrack.isSelected())
			return;
		Point p = transOnSwing(this.cars.get(0).getX(), this.cars.get(0).getY());
		int cx = this.getWidth() / 2, cy = this.getHeight() / 2;
		map_vx += cx - p.x;
		map_vy += cy - p.y;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (eventFlag > 0) {
			if (CarControlPanel.getInstance().errIgnore.isSelected())
				eventFlag = 0;
			else
				restart();
		}
		autoTrackCarAdjust();
		Graphics2D g2d = (Graphics2D) g;
		Stroke g2dOrigin = g2d.getStroke();
		paintRoadMap(g);
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			car.paint(g2d, this);
		}
		if (eventFlag == 1) {
			ImageIcon image = (ImageIcon) imgTable.get("FAIL");
			int Ox = this.getWidth() / 2 - image.getIconWidth() / 2, Oy = this
					.getHeight() / 2 - image.getIconHeight() / 2;
			g2d.drawImage(image.getImage(), Ox, Oy, this);
		}
		paintAxisCoordinate(g);
		paintGridCoordinate(g);
		paintCarRunPath(g);

		for (int i = 0; i < cars.size() && i < 1; i++) {
			Car car = cars.get(i);
			car.fuzzySystem.paint(g2d, this.getWidth() * 4 / 5,
					this.getHeight() * 4 / 5);
		}
		g2d.setStroke(g2dOrigin);
	}

	public void paintRoadMap(Graphics g) {
		Polygon proad = new Polygon(road.xpoints, road.ypoints, road.npoints);
		for (int i = 0; i < proad.npoints; i++) {
			Point p = transOnSwing(proad.xpoints[i], proad.ypoints[i]);
			proad.xpoints[i] = p.x;
			proad.ypoints[i] = p.y;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g2d.setColor(roadColor);
		g2d.fillPolygon(proad);
		g2d.setColor(borderColor);
		g2d.drawPolygon(proad);

		for (int k = 0; k < obstacles.size(); k++) {
			obstacles.get(k).paint(g, this);
		}
	}

	public void paintAxisCoordinate(Graphics g) {
		if (!CarControlPanel.getInstance().paintAxis.isSelected())
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		// g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 10, new float[] { 2, 2 }, 5));
		Point prev = this.transOnSwing(0, 0), origin = prev;
		int unit = (30 - 3 * xLarge) / 5 * 5;
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
		if (!CarControlPanel.getInstance().paintGrid.isSelected())
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10, new float[] { 10, 10 }, 10));
		// g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT,
		// BasicStroke.JOIN_BEVEL, 10, new float[] { 2, 2 }, 5));
		Point prev = this.transOnSwing(0, 0), origin = prev;
		int unit = (30 - 3 * xLarge) / 5 * 5;
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

	public void paintCarRunPath(Graphics g) {
		if (!CarControlPanel.getInstance().pathDraw.isSelected())
			return;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(4f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 2.0f, new float[] { 4f, 4f }, 4.0f));
		for (int k = 0; k < cars.size(); k++) {
			Car car = cars.get(k);
			for (int i = 1; i < car.carPath.size(); i++) {
				Point2D.Double st = car.carPath.get(i);
				Point2D.Double ed = car.carPath.get(i - 1);
				Point s = transOnSwing(st.x, st.y);
				Point e = transOnSwing(ed.x, ed.y);
				g2d.drawLine(s.x, s.y, e.x, e.y);
			}
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
}
