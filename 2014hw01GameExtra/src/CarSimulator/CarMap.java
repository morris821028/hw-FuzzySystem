package CarSimulator;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;

import CarSimulator.component.Bullet;
import CarSimulator.component.Car;
import CarSimulator.component.Monster;

import java.util.*;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class CarMap extends JPanel implements KeyEventDispatcher,
		MouseListener, MouseMotionListener, MouseWheelListener {
	private static CarMap singleton = null;

	public static CarMap getInstance() {
		if (singleton == null)
			singleton = new CarMap();
		return singleton;
	}

	public Polygon road;
	public Polygon obstacle[];
	public Color roadColor;
	public Color roadColor2;
	public int xLarge = 4;
	public Car car;
	// game keyboard input.
	java.util.Timer keyRepeatTimer = new java.util.Timer("Key Repeat Timer");
	Map<Integer, TimerTask> repeatingTasks = new HashMap<Integer, TimerTask>();
	// game element.
	java.util.Timer bulletRepeatTimer = new java.util.Timer("BulletRepeatTimer");
	Map<Bullet, TimerTask> bulletMap = new HashMap<Bullet, TimerTask>();
	java.util.Timer monsterRepeatTimer = new java.util.Timer(
			"BulletRepeatTimer");
	Map<Monster, TimerTask> monsterMap = new HashMap<Monster, TimerTask>();

	public CarMap() {
		this.setBackground(Color.BLACK);
		// this.setBackground(new Color(112, 180, 215));
		int x[] = { -80, 80, 80, -80 };
		int y[] = { 60, 60, -60, -60 };
		this.road = new Polygon(x, y, x.length);
		this.roadColor = Color.GRAY;
		this.roadColor2 = new Color(232, 119, 89);
		this.car = car;
		setObstacle();
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						CarMap.this.repaint();
						Thread.sleep(30);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		};
		t.start();
	}

	private void setObstacle() {
		obstacle = new Polygon[1];
		int x[] = { -20, -20, 20, 20, 10, 10, -10, -10 };
		int y[] = { 10, -20, -20, 10, 10, -10, -10, 10 };
		obstacle[0] = new Polygon(x, y, x.length);
	}

	public void actionByPressed(int keycode) {
		boolean moveFlag = false;
		double x = car.getX(), y = car.getY();
		switch (keycode) {
		case KeyEvent.VK_RIGHT:
			break;
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_SPACE:
			addBullet();
			break;
		case KeyEvent.VK_W:
			y += 2;
			moveFlag = true;
			break;
		case KeyEvent.VK_A:
			x -= 2;
			moveFlag = true;
			break;
		case KeyEvent.VK_S:
			y -= 2;
			moveFlag = true;
			break;
		case KeyEvent.VK_D:
			x += 2;
			moveFlag = true;
			break;
		}

		if (moveFlag == true) {
			double ox = car.getX(), oy = car.getY();
			car.run();
			car.setPhi(car.theta);
			car.setX(x);
			car.setY(y);
			if (!road.contains(car.getX(), car.getY())) {
				car.setX(ox);
				car.setY(oy);
			}
			for (int i = 0; i < obstacle.length; i++) {
				if (obstacle[i].contains(car.getX(), car.getY())) {
					car.setX(ox);
					car.setY(oy);
					break;
				}
			}
		}
	}

	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED) {
			stopRepeating(e.getKeyCode());
			return false;
		}
		if (e.getKeyCode() == KeyEvent.VK_TAB) {
			car.changeWeaponry(e.getKeyCode());
			return false;
		}
		startRepeating(e.getKeyCode());
		return false;
	}

	public Point transOnSwing(double x, double y) {
		int Ox = this.getWidth() / 2, Oy = this.getHeight() / 2;
		return new Point((int) (x * xLarge + Ox), (int) (-y * xLarge + Oy));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Polygon proad = new Polygon(road.xpoints, road.ypoints, road.npoints);
		for (int i = 0; i < proad.npoints; i++) {
			Point p = transOnSwing(proad.xpoints[i], proad.ypoints[i]);
			proad.xpoints[i] = p.x;
			proad.ypoints[i] = p.y;
		}
		g.setColor(roadColor);
		g.fillPolygon(proad);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 10));
		g2d.setColor(roadColor2);
		g2d.drawPolygon(proad);

		for (int i = 0; i < obstacle.length; i++) {
			Polygon o = new Polygon(obstacle[i].xpoints, obstacle[i].ypoints,
					obstacle[i].npoints);
			for (int j = 0; j < obstacle[i].npoints; j++) {
				Point p = transOnSwing(obstacle[i].xpoints[j],
						obstacle[i].ypoints[j]);
				o.xpoints[j] = p.x;
				o.ypoints[j] = p.y;
			}
			g2d.setColor(new Color(112, 180, 215));
			g2d.fillPolygon(o);
			g2d.setColor(roadColor2);
			g2d.drawPolygon(o);
		}
		if (car != null) {
			car.paint(g, this);
		}
		paintBulletOnMap(g);
		paintMonsterOnMap(g);
	}

	public void paintBulletOnMap(Graphics g) {
		Iterator iter = bulletMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Bullet b = ((Bullet) key);
			Point p = transOnSwing(b.getX(), b.getY());
			b.paint(g, p.x, p.y);
		}
		iter = bulletMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Bullet b = ((Bullet) key);
			if (!b.run()) {
				iter.remove();
			}
		}
	}

	public void paintMonsterOnMap(Graphics g) {
		Iterator iter = monsterMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Monster b = ((Monster) key);
			Point p = transOnSwing(b.getX(), b.getY());
			b.paint(g, p.x, p.y, monsterMap);
		}
		iter = monsterMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Monster b = ((Monster) key);
			if (!b.run())
				iter.remove();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		Point p = arg0.getPoint();
		Point c = transOnSwing(car.getX(), car.getY());
		car.theta = Math.atan2(p.x - c.x, p.y - c.y) - Math.PI / 2;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (SwingUtilities.isMiddleMouseButton(arg0)) {
			System.out.println("wheel");
		}
		Point p = arg0.getPoint();
		Point c = transOnSwing(car.getX(), car.getY());
		car.theta = Math.atan2(p.x - c.x, p.y - c.y) - Math.PI / 2;
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
		startRepeating(KeyEvent.VK_SPACE);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		stopRepeating(KeyEvent.VK_SPACE);
	}

	private synchronized void stopRepeating(int keycode) {
		if (!isRepeating(keycode))
			return;
		repeatingTasks.get(keycode).cancel();
		repeatingTasks.remove(keycode);
	}

	private synchronized boolean isRepeating(int keycode) {
		return repeatingTasks.get(keycode) != null;
	}

	private synchronized void startRepeating(int keycode) {
		assert EventQueue.isDispatchThread();

		if (isRepeating(keycode))
			return;

		int rate = 60;
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

	private synchronized void addBullet() {
		assert EventQueue.isDispatchThread();
		addMonster();
		int rate = 50;
		long period = (long) (1000.0 / rate), delay = 60;

		Bullet b = car.shootWeaponry(0, this);
		TimerTask tt = new TimerTask() {
			Bullet b;

			public TimerTask init(Bullet b) {
				this.b = b;
				return this;
			}

			public void run() {
				/*if (!b.run()) {
					bulletMap.get(b).cancel();
					bulletMap.remove(b);
				}*/
				// Attempt to make it more responsive to key-releases.
				// Even if there are multiple this-tasks piled up (due to
				// "scheduleAtFixedRate") we don't want this thread to take
				// precedence over AWT thread.
				Thread.yield();
			}
		}.init(b);
		bulletMap.put(b, tt);
		//bulletRepeatTimer.scheduleAtFixedRate(tt, delay, period);
	}

	private synchronized void addMonster() {
		assert EventQueue.isDispatchThread();

		int rate = 50;
		long period = (long) (1000.0 / rate), delay = 60;

		Monster b = new Monster(Math.random() * 100, Math.random() * 100,
				this.road, this.obstacle);
		TimerTask tt = new TimerTask() {
			Monster b;

			public TimerTask init(Monster b) {
				this.b = b;
				return this;
			}

			public void run() {
				/*
				 * if (!b.run()) { monsterMap.get(b).cancel();
				 * monsterMap.remove(b); }
				 */
				// Attempt to make it more responsive to key-releases.
				// Even if there are multiple this-tasks piled up (due to
				// "scheduleAtFixedRate") we don't want this thread to take
				// precedence over AWT thread.
				Thread.yield();
			}
		}.init(b);
		monsterMap.put(b, tt);
		// monsterRepeatTimer.scheduleAtFixedRate(tt, delay, period);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		car.changeWeaponry(0);
		/*
		 * if (e.getWheelRotation() < 0) { System.out.println("Rotated Up... " +
		 * e.getWheelRotation()); } else { System.out.println("Rotated Down... "
		 * + e.getWheelRotation()); }
		 * 
		 * System.out.println("ScrollAmount: " + e.getScrollAmount());
		 * 
		 * 
		 * if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
		 * System.out.println("MouseWheelEvent.WHEEL_UNIT_SCROLL"); }
		 * 
		 * if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
		 * System.out.println("MouseWheelEvent.WHEEL_BLOCK_SCROLL"); }
		 */
	}
}
