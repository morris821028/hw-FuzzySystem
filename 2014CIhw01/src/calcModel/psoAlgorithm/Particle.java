package calcModel.psoAlgorithm;

import calcModel.AInetwork.RBF;
import calcModel.geneAlgorithm.Gene;

public class Particle {
	public RBF rbf;
	private double[] DNA;
	/*
	 * double theta; double[] W; double[][] M; double[] sigma;
	 */
	/*
	 * (theta, w1, w2, ..., wj, m11, m12, ..., mjp, sigma1, sigma2....sigmaj)
	 */
	public static final int J = 3, xDim = 3;
	public static final int DNALength = 1 + J + J * xDim + J;

	public Particle() {
		rbf = new RBF(J, xDim);
		setDNA(new double[DNALength]);
	}

	public static int getDNALength() {
		return DNALength;
	}

	public static String[] getDNAName() {
		String[] ret = new String[DNALength];
		ret[0] = "£c";
		for (int i = 1; i <= J; i++) {
			ret[i] = "w" + i;
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			ret[i] = "m" + (j / xDim + 1) + "," + (j % xDim + 1);
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			ret[i] = "£m" + (j + 1);
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
		rbf.theta = getDNA()[0];
		for (int i = 0; i < J; i++) {
			rbf.W[i] = getDNA()[i + 1];
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			rbf.M[j / xDim][j % xDim] = getDNA()[i];
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			rbf.sigma[j] = getDNA()[i];
		}

		rbf.theta = Math.min(Math.max(getDNA()[0], 0), 1);
		getDNA()[0] = rbf.theta;
		for (int i = 0; i < J; i++) {
			rbf.W[i] = Math.min(Math.max(getDNA()[i + 1], 0), 80);
			getDNA()[i + 1] = rbf.W[i];
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			rbf.M[j / xDim][j % xDim] = Math.min(Math.max(getDNA()[i], 0), 30);
			getDNA()[i] = rbf.M[j / xDim][j % xDim];
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			rbf.sigma[j] = Math.min(Math.max(getDNA()[i], 1e-6), 10);
			getDNA()[i] = rbf.sigma[j];
		}
	}

	public void randomBuild() {
		for (int i = 0; i < 1; i++) {
			getDNA()[i] = Math.random();
		}
		for (int i = 1; i < 1 + J; i++) {
			getDNA()[i] = Math.random();
		}
		for (int i = 1 + J; i < 1 + J + J * xDim; i++) {
			getDNA()[i] = Math.random() * 30;
		}
		for (int i = 1 + J + J * xDim; i < 1 + J + J * xDim + J; i++) {
			getDNA()[i] = Math.random() * 10;
		}
		on();
	}

	/**
	 * 
	 * @param correct
	 * @param x
	 * @return small better.
	 */
	public double calcuateFitness(double correct[], double x[][]) {
		double ret = 0;
		for (int i = 0; i < correct.length; i++) {
			double f = rbf.calcuateOutput(x[i]);
			ret += Math.pow(correct[i] - f, 2);
		}
		ret = ret / 2.0;
		return ret;
	}

	public double[] getDNA() {
		return DNA;
	}

	public void setDNA(double[] dNA) {
		DNA = dNA;
	}
}
