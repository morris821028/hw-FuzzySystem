package MarathonRound2;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class FloorPlanTool {
	static int[] LX;
	static int[] LY;
	static int[] used;
	static int[] A;
	static int[] B;
	static Random rand = new Random();
	static Rectangle[] rect;

	public static boolean overlap(int A, int B, int C, int D) {
		return Math.max(A, C) < Math.min(B, D);
	}

	public static void placeUpSingle(int place, int base) {
		int lx, ly;
		lx = LX[base] + A[base];
		ly = LY[base];

		Rectangle rr = new Rectangle(lx, ly, A[place], B[place]);
		boolean update = false;
		do {
			update = false;
			for (int i = 0; i < used.length; i++) {
				if (used[i] == 1) {
					if (rect[i].intersects(rr)) {
						Rectangle in = rect[i].intersection(rr);
						rr.x = Math.max(in.x + in.width, rr.x);
						rr.y = Math.max(in.y + in.height, rr.y);
						update = true;
					}
				}
			}
		} while (update);
		LX[place] = rr.x;
		LY[place] = rr.y;
		rect[place] = new Rectangle(LX[place], LY[place], A[place], B[place]);
		used[place] = 1;
	}

	public static void placeRightSingle(int place, int base) {
		int lx, ly;
		lx = LX[base];
		ly = LY[base] + B[base];
		
		Rectangle rr = new Rectangle(lx, ly, A[place], B[place]);
		boolean update = false;
		do {
			update = false;
			for (int i = 0; i < used.length; i++) {
				if (used[i] == 1) {
					if (rect[i].intersects(rr)) {
						Rectangle in = rect[i].intersection(rr);
						rr.x = Math.max(in.x + in.width, rr.x);
						rr.y = Math.max(in.y + in.height, rr.y);
						update = true;
					}
				}
			}
		} while (update);
		
		LX[place] = rr.x;
		LY[place] = rr.y;
		rect[place] = new Rectangle(LX[place], LY[place], A[place], B[place]);
		used[place] = 1;
	}

	private static void dfs(int baseNode, int[] T) {
		if (T.length == 0)
			return;
		int up = rand.nextInt(T.length);
		int right = rand.nextInt(T.length);
		int type = 0; // 0 up, 1 right, 2 up and right
		if (up == right) {
			type = rand.nextInt(2);
		} else {
			type = rand.nextInt(3);
		}
		if (type == 0) {
			int[] nextT = new int[T.length - 1];
			for (int i = 0, j = 0; i < T.length; i++) {
				if (i == up)
					continue;
				nextT[j++] = T[i];
			}
			placeUpSingle(baseNode, T[up]);
			dfs(T[up], nextT);
		} else if (type == 1) {
			int[] nextT = new int[T.length - 1];
			for (int i = 0, j = 0; i < T.length; i++) {
				if (i == right)
					continue;
				nextT[j++] = T[i];
			}
			placeUpSingle(baseNode, T[right]);
			dfs(T[right], nextT);
		} else if (type == 2) {
			int[] nextU;
			int[] nextR;
			ArrayList<Integer> UP, RIGHT;
			UP = new ArrayList<Integer>();
			RIGHT = new ArrayList<Integer>();
			for (int i = 0; i < T.length; i++) {
				if (i == up || i == right)
					continue;
				if (Math.random() < 0.8)
					UP.add(T[i]);
				else
					RIGHT.add(T[i]);
			}
			Integer[] tmp;
			tmp = UP.toArray(new Integer[UP.size()]);
			nextU = new int[tmp.length];
			for (int i = 0; i < tmp.length; i++)
				nextU[i] = tmp[i];
			tmp = RIGHT.toArray(new Integer[RIGHT.size()]);
			nextR = new int[tmp.length];
			for (int i = 0; i < tmp.length; i++)
				nextR[i] = tmp[i];
			
			placeUpSingle(baseNode, T[up]);
			
			placeUpSingle(baseNode, T[right]);
			
			dfs(T[up], nextU);
			dfs(T[right], nextR);
		}
	}

	public static int[] getRandomPlace(int A[], int B[]) {
		int N = A.length;
		int[] T = new int[N - 1];

		LX = new int[N];
		LY = new int[N];
		used = new int[N];
		rect = new Rectangle[N];
		FloorPlanTool.A = A;
		FloorPlanTool.B = B;

		for (int i = 1, j = 0; i < N; i++, j++) {
			T[j] = i;
		}

		LX[0] = 0;
		LY[0] = 0;
		used[0] = 1;
		rect[0] = new Rectangle(LX[0], LY[0], A[0], B[0]);
		dfs(0, T);
		return null;
	}
}
