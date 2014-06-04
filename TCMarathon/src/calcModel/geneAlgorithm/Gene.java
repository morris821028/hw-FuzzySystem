package calcModel.geneAlgorithm;

import MarathonRound2.RectanglesAndHoles;
import MarathonRound2.RectanglesAndHolesVis;

public class Gene {
	public int[] DNA;
	public static int DNALength;

	public Gene() {
		setDNA(new int[DNALength]);
	}

	public static int getDNALength() {
		return DNALength;
	}

	public static String[] getDNAName() {
		String[] ret = new String[DNALength];
		for (int i = 0; i < DNALength; i++) {
			ret[i] = "w" + i;
		}
		return ret;
	}

	public Gene clone() {
		Gene g = new Gene();
		for (int i = 0; i < getDNA().length; i++)
			g.getDNA()[i] = getDNA()[i];
		g.on();
		return g;
	}

	public void on() {
		for (int i = 0; i < DNALength; i += 3) {
			DNA[i] = Math.min(DNA[i], 1);
		}
		for (int i = 0; i < DNALength; i++) {
			DNA[i] = Math.min(DNA[i], 500);
		}
	}

	public void randomBuild() {
		for (int i = 0; i < DNALength; i++)
			DNA[i] = (int) Math.round(Math.random() * 500);
		for (int i = 0; i < DNALength; i += 3)
			DNA[i] = (int) Math.round(Math.random());
		on();
	}

	/**
	 * 
	 * @param correct
	 * @param x
	 * @return small better.
	 */
	public static RectanglesAndHolesVis vis = new RectanglesAndHolesVis();

	public void showVis() {
		for (int i = 0, j = 0; i < DNA.length; i += 3, j++) {
			RectanglesAndHoles.kind[j] = DNA[i];
			RectanglesAndHoles.LX[j] = DNA[i + 1];
			RectanglesAndHoles.LY[j] = DNA[i + 2];
		}
		vis.vis = true;
		RectanglesAndHoles.vis.runTest2(RectanglesAndHoles.N,
				RectanglesAndHoles.A, RectanglesAndHoles.B,
				RectanglesAndHoles.LX, RectanglesAndHoles.LY,
				RectanglesAndHoles.kind);
	}

	public double calcuateFitness() {
		for (int i = 0, j = 0; i < DNA.length; i += 3, j++) {
			RectanglesAndHoles.kind[j] = DNA[i];
			RectanglesAndHoles.LX[j] = DNA[i + 1];
			RectanglesAndHoles.LY[j] = DNA[i + 2];
		}
		vis.vis = false;
		long score = vis.runTest(RectanglesAndHoles.N, RectanglesAndHoles.A,
				RectanglesAndHoles.B, RectanglesAndHoles.LX,
				RectanglesAndHoles.LY, RectanglesAndHoles.kind);
		score++;
		if (score > 1)
			System.out.println("Score  = " + score);
		return (double) score;
	}

	public int[] getDNA() {
		return DNA;
	}

	public void setDNA(int[] dNA) {
		DNA = dNA;
	}
}
