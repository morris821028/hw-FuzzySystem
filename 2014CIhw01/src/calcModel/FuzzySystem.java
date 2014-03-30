package calcModel;

import java.awt.Graphics;
import java.awt.geom.Point2D;

public class FuzzySystem {
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;

	public void paint(Graphics g, int dx, int dy) {

	}

	/**
	 * 
	 * @param d1
	 *            head distance.
	 * @param d2
	 *            right distance.
	 * @param d3
	 *            left distance.
	 * @return delta rotate phi.
	 */
	public double program(double d1, double d2, double d3) {
		double[] funcVal = new double[7];
		double[] a = new double[7]; // alpha
		
		a[0] = mf_d3(d3, LARGE);
		a[1] = mf_d2(d2, LARGE);
		a[2] = mf_d2(d2, MEDIUM);
		a[3] = mf_d3(d3, MEDIUM);
		a[4] = mf_d1(d1, LARGE) * mf_d2(d2, SMALL);
		a[5] = mf_d1(d1, LARGE) * mf_d3(d3, SMALL);
		a[6] = mf_d1(d1, SMALL);
		
		funcVal[0] = 55;
		funcVal[1] = -55;
		funcVal[2] = -40;
		funcVal[3] = 40;
		funcVal[4] = 30;
		funcVal[5] = -30;
		funcVal[6] = 60;
		return Math.max(Math.min(40, this.functionalWeightedAverage(a, funcVal)), -40);
	}

	public double functionalWeightedAverage(double a[], double funcVal[]) {
		assert (a.length == funcVal.length);
		double A = 0, B = 0;
		for(int i = 0; i < a.length; i++) {
			A += a[i] * funcVal[i];
			B += a[i];
		}
		if(Math.abs(B) < 1e-6)
			return 0;
		return A / B;
	}

	/**
	 * membership function of D1
	 * 
	 * @param d1
	 * @return
	 */
	public double mf_d1(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 3)
				return 1;
			if (d1 < 10)
				return -d1 / 7.0 + 10.0 / 7.0;
			return 0;
		case MEDIUM:
		case LARGE:
			if (d1 < 30)
				return 0;
			return 1;
		}
		return 0;
	}

	/**
	 * membership function of D2
	 * 
	 * @param d2
	 * @return
	 */
	public double mf_d2(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 4)
				return 1;
			if (d1 < 5)
				return -d1 + 5;
			return 0;
		case MEDIUM:
			if (d1 < 4)
				return 0;
			if (d1 < 10)
				return d1 / 6 - 4 / 6.0;
			if (d1 < 16)
				return -d1 / 6 + 16 / 6.0;
			return 0;
		case LARGE:
			if (d1 < 8)
				return 0;
			if (d1 < 16)
				return d1 / 8 - 1;
			return 1;
		}
		return 0;
	}

	/**
	 * membership function of D3
	 * 
	 * @param d1
	 * @return
	 */
	public double mf_d3(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 4)
				return 1;
			if (d1 < 5)
				return -d1 + 5;
			return 0;
		case MEDIUM:
			if (d1 < 4)
				return 0;
			if (d1 < 10)
				return d1 / 6 - 4 / 6.0;
			if (d1 < 16)
				return -d1 / 6 + 16 / 6.0;
			return 0;
		case LARGE:
			if (d1 < 10)
				return 0;
			if (d1 < 16)
				return d1 / 6 - 10 / 6.0;
			return 1;
		}
		return 0;
	}
}
