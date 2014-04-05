package calcModel.geneAlgorithm;

public class Gene {
	public RBF rbf;
	double[] DNA;
	/*
	 * double theta; double[] W; double[][] M; double[] sigma;
	 */

	public static final int J = 3, xDim = 3;
	public static final int DNALength = 1 + J + J * xDim + J;
	public Gene() {
		rbf = new RBF(J, xDim);
		DNA = new double[DNALength];
	}

	public static int getDNALength() {
		return DNALength;
	}

	public Gene clone() {
		Gene g = new Gene();
		for (int i = 0; i < DNA.length; i++)
			g.DNA[i] = DNA[i];
		g.on();
		return g;
	}

	public void on() {
		// rbf.theta = Math.min(Math.max(DNA[0], 0), 1);
		rbf.theta = DNA[0];
		for (int i = 0; i < J; i++) {
			// rbf.W[i] = Math.min(Math.max(DNA[i + 1], 0), 80);
			rbf.W[i] = DNA[i + 1];
		}
		for (int i = 1 + J, j = 0; i < 1 + J + J * xDim; i++, j++) {
			// rbf.M[j / xDim][j % xDim] = Math.min(Math.max(DNA[i], 0), 30);
			rbf.M[j / xDim][j % xDim] = DNA[i];
		}
		for (int i = 1 + J + J * xDim, j = 0; i < 1 + J + J * xDim + J; i++, j++) {
			// rbf.sigma[j] = Math.min(Math.max(DNA[i], 1e-6), 10);
			rbf.sigma[j] = DNA[i];
		}
	}

	public void randomBuild() {
		for (int i = 0; i < 1; i++) {
			DNA[i] = Math.random();
		}
		for (int i = 1; i < 1 + J; i++) {
			DNA[i] = Math.random();
		}
		for (int i = 1 + J; i < 1 + J + J * xDim; i++) {
			DNA[i] = Math.random() * 30;
		}
		for (int i = 1 + J + J * xDim; i < 1 + J + J * xDim + J; i++) {
			DNA[i] = Math.random() * 10;
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
}
