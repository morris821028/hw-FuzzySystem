package MarathonRound2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 Read integer N. Read N integers A[0], A[1], ..., A[N-1]. Read N integers
 * B[0], B[1], ..., B[N-1]. Call place(A, B). Let ret be the return value. Print
 * integers ret[0], ret[1], ..., ret[3*N-1]. Flush standard output stream.
 */
public class RectanglesAndHolesVis {
	public static String execCommand = null;
	public static long seed = 1;
	public boolean vis = true;
	public static int size = 700;

	public static Process solution;

	public static final int MIN_N = 100;
	public static final int MAX_N = 1000;

	public static final int MAX_DIM = 1000;

	public static final int MAX_COORD = 1000000;

	public static final int UNKNOWN = -1;
	public static final int OBSTACLE = -2;

	Drawer drawer;
	int N;
	int[] A, B;
	int[] LX, LY, RX, RY;
	int[] XS, YS;
	int[][] map;

	class Drawer extends JFrame {
		public static final int PADDING = 50;
		double minX, maxX, minY, maxY, scale;
		int drawOrder = 1;

		class DrawerKeyListener extends KeyAdapter {
			Drawer parent;

			public DrawerKeyListener(Drawer parent) {
				this.parent = parent;
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					drawOrder = 3 - drawOrder;
					parent.repaint();
				}
			}
		}

		class DrawerPanel extends JPanel {
			int getX(double x) {
				return (int) Math.round((x - (maxX + minX) / 2) * scale + size
						/ 2.0);
			}

			int getY(double y) {
				return (int) Math.round(((maxY + minY) / 2 - y) * scale + size
						/ 2.0);
			}

			int getL(double len) {
				return (int) Math.round(len * scale);
			}

			void drawRectangles(Graphics g) {
				for (int i = 0; i < N; i++) {
					g.setColor(new Color(127, 127, 127));
					g.fillRect(getX(LX[i]), getY(RY[i]), getX(RX[i])
							- getX(LX[i]) + 1, getY(LY[i]) - getY(RY[i]) + 1);

					g.setColor(Color.BLACK);
					g.drawRect(getX(LX[i]), getY(RY[i]), getX(RX[i])
							- getX(LX[i]), getY(LY[i]) - getY(RY[i]));
				}
			}

			void drawHoles(Graphics g) {
				g.setColor(Color.RED);
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						if (map[i][j] > 0) {
							g.setColor(new Color(255, 127, 127));
							g.fillRect(getX(XS[i]), getY(YS[j + 1]),
									getX(XS[i + 1]) - getX(XS[i]) + 1,
									getY(YS[j]) - getY(YS[j + 1]) + 1);
						}
					}
				}
			}

			public void paint(Graphics g) {
				if (drawOrder == 1) {
					drawHoles(g);
					drawRectangles(g);
				} else {
					drawRectangles(g);
					drawHoles(g);
				}
			}
		}

		DrawerPanel panel;

		public Drawer() {
			super();

			panel = new DrawerPanel();
			getContentPane().add(panel);

			setSize(size, size);
			setTitle("Visualizer tool for problem RectanglesAndHoles");

			addKeyListener(new DrawerKeyListener(this));
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			setResizable(false);
			setVisible(true);

			final double INF = 10000000.0;

			minX = INF;
			maxX = -INF;
			minY = INF;
			maxY = -INF;

			for (int elm : XS) {
				minX = Math.min(minX, elm);
				maxX = Math.max(maxX, elm);
			}

			for (int elm : YS) {
				minY = Math.min(minY, elm);
				maxY = Math.max(maxY, elm);
			}

			scale = (size - 2 * PADDING) / Math.max(maxX - minX, maxY - minY);
		}

		public void adjustView() {
			final double INF = 10000000.0;
			minX = INF;
			maxX = -INF;
			minY = INF;
			maxY = -INF;

			for (int elm : XS) {
				minX = Math.min(minX, elm);
				maxX = Math.max(maxX, elm);
			}

			for (int elm : YS) {
				minY = Math.min(minY, elm);
				maxY = Math.max(maxY, elm);
			}

			scale = (size - 2 * PADDING) / Math.max(maxX - minX, maxY - minY);

		}
	}

	boolean overlap(int A, int B, int C, int D) {
		return Math.max(A, C) < Math.min(B, D);
	}

	int[] enumerateCoordinates(int[] A, int[] B) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		Set<Integer> coords = new HashSet<Integer>();

		for (int elm : A) {
			min = Math.min(min, elm);
			max = Math.max(max, elm);
			coords.add(elm);
		}

		for (int elm : B) {
			min = Math.min(min, elm);
			max = Math.max(max, elm);
			coords.add(elm);
		}

		coords.add(min - 1);
		coords.add(max + 1);

		int cnt = coords.size();

		int[] res = new int[cnt];

		int pos = 0;
		for (int elm : coords) {
			res[pos++] = elm;
		}

		Arrays.sort(res);

		return res;
	}

	public long runTest() {
		solution = null;

		try {
			solution = Runtime.getRuntime().exec(execCommand);
		} catch (Exception e) {
			System.err
					.println("ERROR: Unable to execute your solution using the provided command: "
							+ execCommand + ".");
			return -1;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				solution.getInputStream()));
		PrintWriter writer = new PrintWriter(solution.getOutputStream());
		new ErrorStreamRedirector(solution.getErrorStream()).start();

		Random rnd = null;
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {
			System.err.println("ERROR: unable to generate test case.");
			System.exit(1);
		}

		rnd.setSeed(seed);

		N = MIN_N + rnd.nextInt(MAX_N - MIN_N + 1);

		A = new int[N];
		B = new int[N];

		for (int i = 0; i < N; i++) {
			A[i] = rnd.nextInt(MAX_DIM) + 1;
			B[i] = rnd.nextInt(MAX_DIM) + 1;
		}

		writer.println(N);
		for (int i = 0; i < N; i++) {
			writer.println(A[i]);
		}
		for (int i = 0; i < N; i++) {
			writer.println(B[i]);
		}
		writer.flush();

		LX = new int[N];
		LY = new int[N];
		RX = new int[N];
		RY = new int[N];

		int[] kind = new int[N];

		try {
			for (int i = 0; i < N; i++) {
				LX[i] = Integer.parseInt(reader.readLine());
				LY[i] = Integer.parseInt(reader.readLine());
				kind[i] = Integer.parseInt(reader.readLine());
			}
		} catch (Exception e) {
			System.err.println("ERROR: unable to parse your return value.");
			e.printStackTrace();
			return -1;
		}

		stopSolution();

		for (int i = 0; i < N; i++) {
			if (LX[i] < -MAX_COORD || LX[i] > MAX_COORD) {
				System.err
						.println("ERROR: the left X coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LX[i] + ".");
				return -1;
			}
			if (LY[i] < -MAX_COORD || LY[i] > MAX_COORD) {
				System.err
						.println("ERROR: the bottom Y coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LY[i] + ".");
				return -1;
			}
			if (kind[i] != 0 && kind[i] != 1) {
				System.err
						.println("ERROR: element "
								+ (3 * i + 2)
								+ " of your return value must be 0 or 1, but it is equal to "
								+ kind[i] + ".");
				return -1;
			}
			RX[i] = LX[i] + (kind[i] == 0 ? A[i] : B[i]);
			RY[i] = LY[i] + (kind[i] == 0 ? B[i] : A[i]);
		}

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (overlap(LX[i], RX[i], LX[j], RX[j])
						&& overlap(LY[i], RY[i], LY[j], RY[j])) {
					System.err.println("ERROR: rectangles " + i
							+ " (0-based) and " + j
							+ " (0-based) in your solution overlap. Rectangle "
							+ i + " is (" + LX[i] + ", " + LY[i] + ") - ("
							+ RX[i] + ", " + RY[i] + "). Rectangle " + j
							+ " is (" + LX[j] + ", " + LY[j] + ") - (" + RX[j]
							+ ", " + RY[j] + ").");
					return -1;
				}
			}
		}

		XS = enumerateCoordinates(LX, RX);
		YS = enumerateCoordinates(LY, RY);

		map = new int[XS.length - 1][YS.length - 1];

		for (int[] elm : map) {
			Arrays.fill(elm, UNKNOWN);
		}

		for (int i = 0; i < N; i++) {
			int fromX = Arrays.binarySearch(XS, LX[i]);
			int toX = Arrays.binarySearch(XS, RX[i]);
			int fromY = Arrays.binarySearch(YS, LY[i]);
			int toY = Arrays.binarySearch(YS, RY[i]);

			for (int x = fromX; x < toX; x++) {
				for (int y = fromY; y < toY; y++) {
					map[x][y] = OBSTACLE;
				}
			}
		}

		int cells = map.length * map[0].length;

		int compCnt = 0;

		int[] QX = new int[cells];
		int[] QY = new int[cells];

		int qBeg = 0, qEnd = 0;

		final int[] DX = new int[] { -1, 1, 0, 0 };
		final int[] DY = new int[] { 0, 0, -1, 1 };

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] == UNKNOWN) {
					QX[qBeg] = x;
					QY[qBeg++] = y;
					map[x][y] = compCnt;

					while (qEnd < qBeg) {
						int curX = QX[qEnd];
						int curY = QY[qEnd++];

						for (int d = 0; d < DX.length; d++) {
							int nextX = curX + DX[d];
							int nextY = curY + DY[d];

							if (nextX >= 0 && nextX < map.length && nextY >= 0
									&& nextY < map[0].length
									&& map[nextX][nextY] == UNKNOWN) {
								QX[qBeg] = nextX;
								QY[qBeg++] = nextY;
								map[nextX][nextY] = compCnt;
							}
						}
					}

					compCnt++;
				}
			}
		}

		long totArea = 0;

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] > 0) {
					totArea += (long) (XS[x + 1] - XS[x]) * (YS[y + 1] - YS[y]);
				}
			}
		}

		compCnt--;

		System.out.println("Holes count (Cnt) = " + compCnt);
		System.out.println("Holes area (Area) = " + totArea);
		/*
		 * if (vis) { drawer = new Drawer(); }
		 */

		return totArea * compCnt * compCnt;
	}

	public long runTest(int N, int A[], int B[], int LX[], int LY[], int kind[]) {
		solution = null;

		Random rnd = null;
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {
			System.err.println("ERROR: unable to generate test case.");
			System.exit(1);
		}

		rnd.setSeed(seed);

		// N = MIN_N + rnd.nextInt(MAX_N - MIN_N + 1);

		this.A = A;
		this.B = B;
		this.N = N;

		this.LX = LX;
		this.LY = LY;
		this.RX = new int[N];
		this.RY = new int[N];

		stopSolution();

		for (int i = 0; i < N; i++) {
			if (LX[i] < -MAX_COORD || LX[i] > MAX_COORD) {
				System.err
						.println("ERROR: the left X coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LX[i] + ".");
				return -1;
			}
			if (LY[i] < -MAX_COORD || LY[i] > MAX_COORD) {
				System.err
						.println("ERROR: the bottom Y coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LY[i] + ".");
				return -1;
			}
			if (kind[i] != 0 && kind[i] != 1) {
				System.err
						.println("ERROR: element "
								+ (3 * i + 2)
								+ " of your return value must be 0 or 1, but it is equal to "
								+ kind[i] + ".");
				return -1;
			}
			RX[i] = LX[i] + (kind[i] == 0 ? A[i] : B[i]);
			RY[i] = LY[i] + (kind[i] == 0 ? B[i] : A[i]);
		}

//		for (int i = 0; i < N; i++) {
//			for (int j = i + 1; j < N; j++) {
//				if (overlap(LX[i], RX[i], LX[j], RX[j])
//						&& overlap(LY[i], RY[i], LY[j], RY[j])) {
//					System.err.println("ERROR: rectangles " + i
//							+ " (0-based) and " + j
//							+ " (0-based) in your solution overlap. Rectangle "
//							+ i + " is (" + LX[i] + ", " + LY[i] + ") - ("
//							+ RX[i] + ", " + RY[i] + "). Rectangle " + j
//							+ " is (" + LX[j] + ", " + LY[j] + ") - (" + RX[j]
//							+ ", " + RY[j] + ").");
//					return -1;
//				}
//			}
//		}

		XS = enumerateCoordinates(LX, RX);
		YS = enumerateCoordinates(LY, RY);

		map = new int[XS.length - 1][YS.length - 1];

		for (int[] elm : map) {
			Arrays.fill(elm, UNKNOWN);
		}

		for (int i = 0; i < N; i++) {
			int fromX = Arrays.binarySearch(XS, LX[i]);
			int toX = Arrays.binarySearch(XS, RX[i]);
			int fromY = Arrays.binarySearch(YS, LY[i]);
			int toY = Arrays.binarySearch(YS, RY[i]);

			for (int x = fromX; x < toX; x++) {
				for (int y = fromY; y < toY; y++) {
					map[x][y] = OBSTACLE;
				}
			}
		}

		int cells = map.length * map[0].length;

		int compCnt = 0;

		int[] QX = new int[cells];
		int[] QY = new int[cells];

		int qBeg = 0, qEnd = 0;

		final int[] DX = new int[] { -1, 1, 0, 0 };
		final int[] DY = new int[] { 0, 0, -1, 1 };

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] == UNKNOWN) {
					QX[qBeg] = x;
					QY[qBeg++] = y;
					map[x][y] = compCnt;

					while (qEnd < qBeg) {
						int curX = QX[qEnd];
						int curY = QY[qEnd++];

						for (int d = 0; d < DX.length; d++) {
							int nextX = curX + DX[d];
							int nextY = curY + DY[d];

							if (nextX >= 0 && nextX < map.length && nextY >= 0
									&& nextY < map[0].length
									&& map[nextX][nextY] == UNKNOWN) {
								QX[qBeg] = nextX;
								QY[qBeg++] = nextY;
								map[nextX][nextY] = compCnt;
							}
						}
					}

					compCnt++;
				}
			}
		}

		long totArea = 0;

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] > 0) {
					totArea += (long) (XS[x + 1] - XS[x]) * (YS[y + 1] - YS[y]);
				}
			}
		}

		compCnt--;

//		System.out.println("Holes count (Cnt) = " + compCnt);
//		System.out.println("Holes area (Area) = " + totArea);

		if (vis) {
			drawer = new Drawer();
		}

		return totArea * compCnt * compCnt;
	}

	public long runTest2(int N, int A[], int B[], int LX[], int LY[],
			int kind[]) {
		solution = null;

		Random rnd = null;
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch (Exception e) {
			System.err.println("ERROR: unable to generate test case.");
			System.exit(1);
		}

		rnd.setSeed(seed);

		// N = MIN_N + rnd.nextInt(MAX_N - MIN_N + 1);

		this.A = A;
		this.B = B;
		this.N = N;

		this.LX = LX;
		this.LY = LY;
		this.RX = new int[N];
		this.RY = new int[N];

		stopSolution();

		for (int i = 0; i < N; i++) {
			if (LX[i] < -MAX_COORD || LX[i] > MAX_COORD) {
				System.err
						.println("ERROR: the left X coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LX[i] + ".");
				return -1;
			}
			if (LY[i] < -MAX_COORD || LY[i] > MAX_COORD) {
				System.err
						.println("ERROR: the bottom Y coordinate of "
								+ i
								+ "-th rectangle (0-based) must be in -1,000,000 .. 1,000,000. Your return value = "
								+ LY[i] + ".");
				return -1;
			}
			if (kind[i] != 0 && kind[i] != 1) {
				System.err
						.println("ERROR: element "
								+ (3 * i + 2)
								+ " of your return value must be 0 or 1, but it is equal to "
								+ kind[i] + ".");
				return -1;
			}
			RX[i] = LX[i] + (kind[i] == 0 ? A[i] : B[i]);
			RY[i] = LY[i] + (kind[i] == 0 ? B[i] : A[i]);
		}

		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				if (overlap(LX[i], RX[i], LX[j], RX[j])
						&& overlap(LY[i], RY[i], LY[j], RY[j])) {
					// System.err.println("ERROR: rectangles " + i
					// + " (0-based) and " + j
					// + " (0-based) in your solution overlap. Rectangle "
					// + i + " is (" + LX[i] + ", " + LY[i] + ") - ("
					// + RX[i] + ", " + RY[i] + "). Rectangle " + j
					// + " is (" + LX[j] + ", " + LY[j] + ") - (" + RX[j]
					// + ", " + RY[j] + ").");
					return -1;
				}
			}
		}

		XS = enumerateCoordinates(LX, RX);
		YS = enumerateCoordinates(LY, RY);

		map = new int[XS.length - 1][YS.length - 1];

		for (int[] elm : map) {
			Arrays.fill(elm, UNKNOWN);
		}

		for (int i = 0; i < N; i++) {
			int fromX = Arrays.binarySearch(XS, LX[i]);
			int toX = Arrays.binarySearch(XS, RX[i]);
			int fromY = Arrays.binarySearch(YS, LY[i]);
			int toY = Arrays.binarySearch(YS, RY[i]);

			for (int x = fromX; x < toX; x++) {
				for (int y = fromY; y < toY; y++) {
					map[x][y] = OBSTACLE;
				}
			}
		}

		int cells = map.length * map[0].length;

		int compCnt = 0;

		int[] QX = new int[cells];
		int[] QY = new int[cells];

		int qBeg = 0, qEnd = 0;

		final int[] DX = new int[] { -1, 1, 0, 0 };
		final int[] DY = new int[] { 0, 0, -1, 1 };

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] == UNKNOWN) {
					QX[qBeg] = x;
					QY[qBeg++] = y;
					map[x][y] = compCnt;

					while (qEnd < qBeg) {
						int curX = QX[qEnd];
						int curY = QY[qEnd++];

						for (int d = 0; d < DX.length; d++) {
							int nextX = curX + DX[d];
							int nextY = curY + DY[d];

							if (nextX >= 0 && nextX < map.length && nextY >= 0
									&& nextY < map[0].length
									&& map[nextX][nextY] == UNKNOWN) {
								QX[qBeg] = nextX;
								QY[qBeg++] = nextY;
								map[nextX][nextY] = compCnt;
							}
						}
					}

					compCnt++;
				}
			}
		}

		long totArea = 0;

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] > 0) {
					totArea += (long) (XS[x + 1] - XS[x]) * (YS[y + 1] - YS[y]);
				}
			}
		}

		compCnt--;

		if (vis && drawer != null) {
			drawer.adjustView();
			drawer.repaint();
		}
		return totArea * compCnt * compCnt;
	}

	public static void stopSolution() {
		if (solution != null) {
			try {
				solution.destroy();
			} catch (Exception e) {
				// do nothing
			}
		}
	}
}

class ErrorStreamRedirector extends Thread {
	public BufferedReader reader;

	public ErrorStreamRedirector(InputStream is) {
		reader = new BufferedReader(new InputStreamReader(is));
	}

	public void run() {
		while (true) {
			String s;
			try {
				s = reader.readLine();
			} catch (Exception e) {
				// e.printStackTrace();
				return;
			}
			if (s == null) {
				break;
			}
			System.out.println(s);
		}
	}
}