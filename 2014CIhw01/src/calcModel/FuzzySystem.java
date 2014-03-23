package calcModel;

import java.awt.Graphics;

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
		double c1, r1, c2, r2, c3, r3, c4, r4, c5, r5, c6, r6;
		c1 = mf_d1(d1, LARGE);
		r1 = 40;
		c2 = mf_d2(d2, SMALL);
		r2 = 40 * (d3 + 4) / (d3 + d2 + 4);
		c3 = mf_d3(d3, SMALL);
		r3 = -40 * (d2 + 4) / (d2 + d3 + 4);
		c4 = mf_d2(d2, LARGE);
		r4 = -35 * (d2 + 4) / (d1 + d2 + 4);
		c5 = mf_d3(d3, LARGE);
		r5 = 35 * (d3 + 4) / (d1 + d3 + 4);
		c6 = mf_d1(d1, SMALL);
		r6 = 0;

		double A = (c1 * r1 + c2 * r2 + c3 * r3 + c4 * r4 + c5 * r5 + c6 * r6);
		double B = (c1 + c2 + c3 + c4 + c5 + c6);
		double rotate = A / B;
		System.out.printf("%f %f %f\n", d1, d2, d3);
		System.out.printf("%f %f %f %f %f\n", c1, c2, c3, c4, c5);
		System.out.printf("%f %f\n", A, B);
		if (Math.abs(B) < 1e-3)
			return 0;
		System.out.printf("R = %f\n", rotate);
		return rotate;
	}

	/**
	 * membership function of D1
	 * 
	 * @param d1
	 * @return
	 */
	public static double mf_d1(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 4)
				return 1;
			if (d1 < 4.5)
				return -2 * d1 + 9;
			return 0;
		case MEDIUM:
			if (d1 < 7)
				return 0;
			if (d1 < 13)
				return d1 / 6 - 7 / 6.0;
			if (d1 < 20)
				return 1;
			if (d1 < 21)
				return -d1 + 21;
			return 0;
		case LARGE:
			if (d1 < 4)
				return 0;
			if (d1 < 10)
				return d1 / 6.0 - 4 / 6.0;
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
	public static double mf_d2(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 4)
				return 1;
			if (d1 < 4.5)
				return -2 * d1 + 9;
			return 0;
		case MEDIUM:
			if (d1 < 7)
				return 0;
			if (d1 < 13)
				return d1 / 6 - 7 / 6.0;
			if (d1 < 20)
				return 1;
			if (d1 < 21)
				return -d1 + 21;
			return 0;
		case LARGE:
			if (d1 < 4)
				return 0;
			if (d1 < 10)
				return d1 / 6.0 - 4 / 6.0;
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
	public static double mf_d3(double d1, int kind_size) {
		switch (kind_size) {
		case SMALL:
			if (d1 < 4)
				return 1;
			if (d1 < 4.5)
				return -2 * d1 + 9;
			return 0;
		case MEDIUM:
			if (d1 < 7)
				return 0;
			if (d1 < 13)
				return d1 / 6 - 7 / 6.0;
			if (d1 < 20)
				return 1;
			if (d1 < 21)
				return -d1 + 21;
			return 0;
		case LARGE:
			if (d1 < 4)
				return 0;
			if (d1 < 10)
				return d1 / 6.0 - 4 / 6.0;
			return 1;
		}
		return 0;
	}
}
