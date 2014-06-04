package MarathonRound2;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import calcModel.geneAlgorithm.Gene;
import calcModel.geneAlgorithm.ui.GeneControl;

public class RectanglesAndHoles {
	public static RectanglesAndHolesVis vis = new RectanglesAndHolesVis();
	public static int N = 1000;
	public static int[] A = new int[N];
	public static int[] B = new int[N];
	public static int[] LX = new int[N];
	public static int[] LY = new int[N];
	public static int[] kind = new int[N];

	public static ArrayList<ArrayList<Integer>> getEdgeSet(int R, int N,
			int[] A, int[] B) {
		ArrayList<ArrayList<Integer>> edgeSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> edge = new ArrayList<Integer>();
		int height = 0;
		for (int i = 0; i < N; i++) {
			height += A[i];
			if (height >= R) {
				edgeSet.add(edge);
				height = 0;
				edge = new ArrayList<Integer>();
			}
			edge.add(i);
		}
		if (edge.size() > 0)
			edgeSet.add(edge);
		return edgeSet;
	}

	public static void buildBee(ArrayList<ArrayList<Integer>> edgeSet, int N,
			int[] A, int[] B) {
		int[] dx = { 0, 1, -1, -1, -1, 1 };
		int[] dy = { 1, 1, 1, 0, -1, -1 };
		int startDir = 1;
		int cellCount = 0;
		int nextRound = 1, nextRoundDiv = 6, round = 1, roundCell = 0, prev = 4;
		int[] used = new int[N];
		ArrayList<Integer> e = edgeSet.get(0);
		int x = 0, y = 0, p1x = 0, p1y = 0, p2x = 0, p2y = 0, p3x = 0, p3y = 0;
		for (int i = 0; i < e.size(); i++) {
			int rectIdx = e.get(i);
			LX[rectIdx] = x;
			LY[rectIdx] = y;
			used[rectIdx] = 1;
			x += A[rectIdx];
		}
		for (int i = 1, j = 0; i < edgeSet.size(); i++) {
			e = edgeSet.get(i);
			System.out.printf("%d %d %d %d %d %d\n", startDir, j, cellCount,
					round, roundCell, prev);
			switch (startDir) {
			case 0:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x += A[rectIdx];
				}
				break;
			case 1:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x += A[rectIdx] / 2;
					y += B[rectIdx];
				}
				break;
			case 2:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x -= A[rectIdx] / 2;
					y += B[rectIdx];
				}
				if (roundCell == 0) {
					p3x = x;
					p3y = y;
				}
				break;
			case 3:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x -= A[rectIdx];
				}
				break;
			case 4:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x -= A[rectIdx] / 2;
					y -= B[rectIdx];
				}
				break;
			case 5:
				for (int k = 0; k < e.size(); k++) {
					int rectIdx = e.get(k);
					LX[rectIdx] = x;
					LY[rectIdx] = y;
					used[rectIdx] = 1;
					x += A[rectIdx] / 2;
					y -= B[rectIdx];
				}
				break;
			}
			p2x = p1x;
			p2y = p1y;
			p1x = x;
			p1y = y;
			j++;
			startDir++;
			if (cellCount <= 1) {
				if (j % 5 == 0) {
					System.out.println("in");
					startDir -= 3;
					x = p2x;
					y = p2y;
					j = 0;
					cellCount++;
					roundCell++;
					if (cellCount % nextRound == 0) {
						round++;
						startDir = 1;
						x = p3x;
						y = p3y;
						System.out.println("in2");
						nextRound += nextRoundDiv;
						nextRoundDiv += 6;
						roundCell = 0;
					}
				}
			} else if (cellCount <= 6) {
				if ((roundCell == 0 && j % 5 == 0)
						|| (roundCell > 0 && j % 3 == 0 && cellCount + 1 == nextRound)
						|| (roundCell > 0 && j % 4 == 0 && cellCount + 1 != nextRound)) {
					System.out.println("in");
					startDir -= 3;
					x = p2x;
					y = p2y;
					j = 0;
					cellCount++;
					roundCell++;
					if (cellCount % nextRound == 0) {
						round++;
						startDir = 1;
						x = p3x;
						y = p3y;
						System.out.println("in2");
						nextRound += nextRoundDiv;
						nextRoundDiv += 6;
						roundCell = 0;
					}
				}
			} else {
				if ((roundCell == 0 && j % 5 == 0)
						|| (roundCell > 0 && j % 3 == 0 && roundCell
								% (round - 1) != 0)
						|| (roundCell > 0 && j % 4 == 0 && roundCell
								% (round - 1) == 0)
						|| (roundCell > 0 && j % 2 == 0 && cellCount + 1 == nextRound)) {
					System.out.println("in");
					startDir -= 3;
					x = p2x;
					y = p2y;
					j = 0;
					cellCount++;
					roundCell++;
					if (cellCount % nextRound == 0) {
						round++;
						startDir = 1;
						x = p3x;
						y = p3y;
						System.out.println("in2");
						nextRound += nextRoundDiv;
						nextRoundDiv += 6;
						roundCell = 0;
					}
				}
			}
			startDir = (startDir % 6 + 6) % 6;

			vis.runTest2(N, A, B, LX, LY, kind);
			
			try {
				Thread.sleep(30);
			} catch(Exception ee) {
				
			}
		}
	}

	public static int[] place(int N, int[] A, int[] B) {
		vis.runTest(N, A, B, LX, LY, kind);
		int totLength = 0;
		for (int i = 0; i < N; i++)
			totLength += A[i];
		for (int i = 0, r = totLength / 500; i < 50; i++, r += totLength / 500) {
			ArrayList<ArrayList<Integer>> edgeSet;
			edgeSet = getEdgeSet(r, N, A, B);
			buildBee(edgeSet, N, A, B);
		}
		return null;
	}

	public static void main(String[] args) {
		try {

			Random generator = new Random();

			for (int i = 0; i < N; i++) {
				A[i] = 10;
				B[i] = 10;
				if (A[i] < B[i]) {
					int t = A[i];
					A[i] = B[i];
					B[i] = t;
				}
				kind[i] = 0;
			}
			place(N, A, B);

		} catch (RuntimeException e) {
			System.err
					.println("ERROR: Unexpected error while running your test case.");
			e.printStackTrace();
			RectanglesAndHolesVis.stopSolution();
		}
	}
}
