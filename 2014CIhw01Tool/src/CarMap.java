import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.swing.*;
import javax.imageio.ImageIO;

public class CarMap extends JPanel implements KeyEventDispatcher,
		MouseWheelListener, MouseListener, MouseMotionListener {
	public Polygon road;
	public Polygon buildRoad;
	public Color roadColor;
	public Color roadColor2;
	public int xLarge = 7;
	public Car car;
	public Vector<Point> recordL = new Vector<Point>();
	public Vector<Point> recordR = new Vector<Point>();
	// game keyboard input.
	java.util.Timer keyRepeatTimer = new java.util.Timer("Key Repeat Timer");
	Map<Integer, TimerTask> repeatingTasks = new HashMap<Integer, TimerTask>();

	// mouse input.
	private int map_vx, map_vy;
	private Point mouseFirstPressPoint;

	// by fuzzy stream driver.
	public int eventFlag;

	public CarMap(Car car) {
		this.setBackground(Color.GREEN);
		int x[] = { 0, 6, 6, 30, 30, 18, 18, -6, -6 };
		int y[] = { -100, -100, 10, 10, 100, 100, 22, 22, -100 };
		this.road = null;
		this.roadColor = Color.GRAY;
		this.roadColor2 = new Color(232, 119, 89);
		this.car = car;
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
	}

	public void restart() {
		eventFlag = 0;
		car.setPhi(Math.random());
	}

	public int runCarCnt = 0;

	public synchronized boolean runCar() {
		double x = car.getX(), y = car.getY();
		car.run();
		car.setPhi(car.theta);
		if (road != null && !road.contains(car.getX(), car.getY())) {
			car.setX(x);
			car.setY(y);
			this.repaint();
			return false;
		}
		if ((runCarCnt++) % 10 == 0) {
			double rtheta = car.getPhi() - Math.PI / 180.0 * 90;
			double ltheta = car.getPhi() + Math.PI / 180.0 * 90;
			double rx, ry, lx, ly;
			rx = car.getX() + (8 + Math.sin(runCarCnt/10.0) * 3) * Math.cos(rtheta);
			ry = car.getY() + (8 + Math.cos(runCarCnt/10.0) * 3) * Math.sin(rtheta);
			lx = car.getX() + (8 + Math.sin(runCarCnt/10.0) * 3) * Math.cos(ltheta);
			ly = car.getY() + (8 + Math.cos(runCarCnt/10.0) * 3) * Math.sin(ltheta);
			recordL.add(new Point((int) lx, (int) ly));
			recordR.add(new Point((int) rx, (int) ry));
			buildRoad = new Polygon();
			for (int i = 0; i < recordL.size(); i++)
				buildRoad.addPoint(recordL.get(i).x, recordL.get(i).y);
			for (int i = recordR.size() - 1; i >= 0; i--)
				buildRoad.addPoint(recordR.get(i).x, recordR.get(i).y);
		}
		this.repaint();
		return true;
	}

	public void writeBuildRoad() {
		try {
			PrintStream pout = new PrintStream(new BufferedOutputStream(
					new FileOutputStream("output.txt", true)));
			PrintWriter out = new PrintWriter(pout, true);
			out.printf("%d\r\n", buildRoad.npoints);
			for(int i = 0; i < buildRoad.npoints; i++)
				out.printf("%d %d\r\n", buildRoad.xpoints[i], buildRoad.ypoints[i]);
			System.out.println("write success");
		} catch(Exception e) {
			
		}
	}

	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			stopRepeating(e.getKeyCode());
			if(e.getKeyCode() == KeyEvent.VK_W) {
				this.writeBuildRoad();
			}
			return false;
		}
		this.repaint();
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		}
		startRepeating(e.getKeyCode());
		return false;
	}

	public Point transOnSwing(double x, double y) {
		int Ox = this.getWidth() / 2 + map_vx, Oy = this.getHeight() - 100
				+ map_vy;
		return new Point((int) (x * xLarge + Ox), (int) (-y * xLarge + Oy));
	}

	public void autoTrackCarAdjust() {
		Point p = transOnSwing(this.car.getX(), this.car.getY());
		int cx = this.getWidth() / 2, cy = this.getHeight() / 2;
		map_vx += cx - p.x;
		map_vy += cy - p.y;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (eventFlag > 0)
			restart();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		if (road != null) {
			Polygon proad = new Polygon(road.xpoints, road.ypoints,
					road.npoints);
			for (int i = 0; i < proad.npoints; i++) {
				Point p = transOnSwing(proad.xpoints[i], proad.ypoints[i]);
				proad.xpoints[i] = p.x;
				proad.ypoints[i] = p.y;
			}
			g.setColor(roadColor);
			g.fillPolygon(proad);

			g2d.setColor(roadColor2);
			g2d.drawPolygon(proad);
		}
		if (buildRoad != null) {
			Polygon proad = new Polygon(buildRoad.xpoints, buildRoad.ypoints,
					buildRoad.npoints);
			for (int i = 0; i < proad.npoints; i++) {
				Point p = transOnSwing(proad.xpoints[i], proad.ypoints[i]);
				proad.xpoints[i] = p.x;
				proad.ypoints[i] = p.y;
			}
			g.setColor(roadColor);
			g.fillPolygon(proad);

			g2d.setColor(roadColor2);
			g2d.drawPolygon(proad);
		}
		if (car != null)
			car.paint(g2d, this);
		paintAxisCoordinate(g);
		paintGridCoordinate(g);
	}

	public void paintAxisCoordinate(Graphics g) {
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
		Point c = transOnSwing(car.getX(), car.getY());
		car.theta = Math.atan2(p.x - c.x, p.y - c.y) - Math.PI / 2;
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point p = arg0.getPoint();
		Point c = transOnSwing(car.getX(), car.getY());
		car.theta = Math.atan2(p.x - c.x, p.y - c.y) - Math.PI / 2;
		this.repaint();
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

	private synchronized void stopRepeating(int keycode) {
		if (!isRepeating(keycode))
			return;
		repeatingTasks.get(keycode).cancel();
		repeatingTasks.remove(keycode);
	}

	public void actionByPressed(int keycode) {
		boolean moveFlag = false;
		double x = car.getX(), y = car.getY();
		switch (keycode) {
		case KeyEvent.VK_RIGHT:
			if (Math.abs(car.theta - Math.PI / 180.0 * 5 - car.getPhi()) < Math.PI / 180.0 * 40)
				car.theta -= Math.PI / 180.0 * 5;
			break;
		case KeyEvent.VK_LEFT:
			if (Math.abs(car.theta + Math.PI / 180.0 * 5 - car.getPhi()) < Math.PI / 180.0 * 40)
				car.theta += Math.PI / 180.0 * 5;
			break;
		case KeyEvent.VK_SPACE:
			runCar();
			break;
		case KeyEvent.VK_W:
		}
		if (moveFlag == true) {
			double ox = car.getX(), oy = car.getY();
			car.run();
			car.setPhi(car.theta);
			car.setX(x);
			car.setY(y);
			if (road != null && !road.contains(car.getX(), car.getY())) {
				car.setX(ox);
				car.setY(oy);
			}
		}
	}

	private synchronized boolean isRepeating(int keycode) {
		return repeatingTasks.get(keycode) != null;
	}

	private synchronized void startRepeating(int keycode) {
		assert EventQueue.isDispatchThread();

		if (isRepeating(keycode))
			return;

		int rate = 300;
		long period = (long) (1000.0 / rate), delay = 60;
		if (keycode == KeyEvent.VK_SPACE)
			period = (long) (1000.0 / 10);
		final int key = keycode;
		TimerTask tt = new TimerTask() {
			public void run() {
				actionByPressed(key);
				// Attempt to make it more responsive to key-releases.
				// Even if there are multiple this-tasks piled up (due to
				// "scheduleAtFixedRate") we don't want this thread to take
				// precedence over AWT thread.
				Thread.yield();
			}
		};
		repeatingTasks.put(keycode, tt);
		keyRepeatTimer.scheduleAtFixedRate(tt, delay, period);
	}
}
