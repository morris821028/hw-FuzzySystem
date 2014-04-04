package calcModel.fuzzySystem;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.*;

import calcModel.Function;
import calcModel.FunctionCurve;

public class FuzzySystemII extends FuzzySystem {
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;
	public Function[] prevRecord;
	public FunctionCurve curve = new FunctionCurve();
	public void paint(Graphics g, int dx, int dy) {
		curve.paint(g, dx, dy, prevRecord, "Fuzzy System II");
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
		Point2D.Double[] arr0 = { new Point2D.Double(0, 0),
				new Point2D.Double(50, 1), new Point2D.Double(60, 1) };
		Point2D.Double[] arr1 = { new Point2D.Double(-60, 1),
				new Point2D.Double(-50, 1), new Point2D.Double(0, 0) };
		Point2D.Double[] arr2 = { new Point2D.Double(-45, 1),
				new Point2D.Double(-40, 0), new Point2D.Double(0, 0) };
		Point2D.Double[] arr3 = { new Point2D.Double(0, 0),
				new Point2D.Double(40, 0), new Point2D.Double(45, 1) };
		Point2D.Double[] arr4 = { new Point2D.Double(0, 1),
				new Point2D.Double(20, 1), new Point2D.Double(40, 0) };
		Point2D.Double[] arr5 = { new Point2D.Double(-40, 0),
				new Point2D.Double(-20, 1), new Point2D.Double(0, 1) };
		Point2D.Double[] arr6 = { new Point2D.Double(0, 0),
				new Point2D.Double(40, 0), new Point2D.Double(60, 1) };

		Function[] func = new Function[7];
		double[] a = new double[7]; // alpha
		a[0] = mf_d3(d3, LARGE);
		a[1] = mf_d2(d2, LARGE);
		a[2] = mf_d2(d2, MEDIUM);
		a[3] = mf_d3(d3, MEDIUM);
		a[4] = mf_d1(d1, LARGE) * mf_d2(d2, SMALL);
		a[5] = mf_d1(d1, LARGE) * mf_d3(d3, SMALL);
		a[6] = mf_d1(d1, SMALL);

		func[0] = new Function(arr0);
		func[1] = new Function(arr1);
		func[2] = new Function(arr2);
		func[3] = new Function(arr3);
		func[4] = new Function(arr4);
		func[5] = new Function(arr5);
		func[6] = new Function(arr6);
		return Math.max(Math.min(40, massCenter(a, func)), -40);
	}

	public double massCenter(double a[], Function func[]) {
		assert (a.length == func.length);
		Function[] g = new Function[func.length];
		for (int i = 0; i < a.length; i++) {
			assert (a[i] >= 0 - 1e-6 && a[i] <= 1 + 1e-6);
			g[i] = func[i].getIntersection(a[i]);
			// System.out.printf("%f ", a[i]);
		}
		prevRecord = g;
		// System.out.println();
		/*
		 * for(int i = 0; i < g.length; i++) { for(int j = 0; j <
		 * g[i].pass.length; j++) System.out.printf("(%.2f %.2f) ",
		 * g[i].pass[j].getX(), g[i].pass[j].getY());
		 * System.out.println("-----"); }
		 */
		double A = 0, B = 0;
		double mxL = 1e+10, mxR = -1e+10, mxVal = 0;
		for (double i = -60; i <= 60; i += 0.1) {
			double mx = 0;
			for (int j = 0; j < g.length; j++) {
				mx = Math.max(mx, g[j].getVal(i, 1));
			}
			if (mxVal < mx) {
				mxL = i;
				mxR = i;
			}
			mxVal = Math.max(mxVal, mx);
			if (Math.abs(mxVal - mx) < 1e-5)
				mxR = i;
			A += mx * i;
			B += mx;
		}
		if (Math.abs(B) < 1e-6)
			return 0;/*
					 * System.out.printf("A / B = %f / %f %f %f\n", A, B, mxL,
					 * mxR); System.out.println(A / B);
					 */
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
