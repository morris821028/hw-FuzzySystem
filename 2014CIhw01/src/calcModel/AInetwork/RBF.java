package calcModel.AInetwork;

public class RBF {

	public double theta;
	public double[] sigma;
	public double[] W;
	public double[][] M;

	public int J, xDim;

	public RBF(int J, int xDim) {
		W = new double[J];
		M = new double[J][xDim];
		sigma = new double[J];
		this.J = J;
		this.xDim = xDim;
	}

	public double calcuateOutput(double x[]) {
		double ret = theta, val;
		for (int i = 0; i < this.J; i++) {
			val = W[i] * Math.exp(-dist(x, M[i]) / (2 * sigma[i] * sigma[i]));
			ret += val;
		}
		return ret;
	}

	private double dist(double x[], double y[]) {
		double ret = 0;
		for (int i = 0; i < x.length; i++) {
			ret += (x[i] - y[i]) * (x[i] - y[i]);
		}
		return ret;
	}
}
