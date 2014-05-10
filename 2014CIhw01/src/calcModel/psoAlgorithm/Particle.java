package calcModel.psoAlgorithm;

import calcModel.AInetwork.RBF;
import calcModel.geneAlgorithm.Gene;

public class Particle {
	public RBF rbf;
	private double[] X;
	private double[] V;
	/*
	 * double theta; double[] W; double[][] M; double[] sigma;
	 */
	/*
	 * (theta, w1, w2, ..., wj, m11, m12, ..., mjp, sigma1, sigma2....sigmaj)
	 */
	public static final int J = 3, xDim = 3;
	public static final int XLength = 1 + J + J * xDim + J;

	public Particle() {
		rbf = new RBF(J, xDim);
		setX(new double[XLength]);
		setV(new double[XLength]);
	}

	public static int getXLength() {
		return XLength;
	}

	public static int getVLength() {
		return XLength;
	}

	public static String[] getXName() {
		String[] ret = new String[XLength];
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

	public Particle clone() {
		Particle g = new Particle();
		for (int i = 0; i < getX().length; i++)
			g.getX()[i] = getX()[i];
		for (int i = 0; i < getV().length; i++)
			g.getV()[i] = getV()[i];
		g.on();
		return g;
	}
	
	public void move() {
		for(int i = 0; i < XLength; i++)
			X[i] += V[i];
	}

	public void on() {
		rbf.theta = getX()[0];
		for (int i = 0; i < J; i++) {
			rbf.W[i] = getX()[i + 1];
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			rbf.M[j / xDim][j % xDim] = getX()[i];
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			rbf.sigma[j] = getX()[i];
		}

		rbf.theta = Math.min(Math.max(getX()[0], 0), 1);
		getX()[0] = rbf.theta;
		for (int i = 0; i < J; i++) {
			rbf.W[i] = Math.min(Math.max(getX()[i + 1], 0), 80);
			getX()[i + 1] = rbf.W[i];
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			rbf.M[j / xDim][j % xDim] = Math.min(Math.max(getX()[i], 0), 30);
			getX()[i] = rbf.M[j / xDim][j % xDim];
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			rbf.sigma[j] = Math.min(Math.max(getX()[i], 1e-6), 10);
			getX()[i] = rbf.sigma[j];
		}
	}

	public void randomBuild() {
		for (int i = 0; i < 1; i++) {
			getX()[i] = Math.random();
		}
		for (int i = 1; i < 1 + J; i++) {
			getX()[i] = Math.random();
		}
		for (int i = 1 + J; i < 1 + J + J * xDim; i++) {
			getX()[i] = Math.random() * 30;
		}
		for (int i = 1 + J + J * xDim; i < 1 + J + J * xDim + J; i++) {
			getX()[i] = Math.random() * 10;
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

	public double[] getX() {
		return X;
	}

	public double[] getV() {
		return V;
	}

	public void setX(double[] x) {
		X = x;
	}

	public void setV(double[] v) {
		V = v;
	}

	public double distTo(Particle o) {
		double ret = 0;
		for (int i = 0; i < X.length; i++)
			ret += (X[i] - o.X[i]) * (X[i] - o.X[i]);
		return ret;
	}
}
