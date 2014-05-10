package calcModel.psoAlgorithm;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class ParticleUtilities {
	public static Point2D.Double mappedPlane2D(Particle p) {
		Point2D.Double ret = new Point2D.Double();
		for (int i = 0; i < Particle.getXLength(); i++) {
			if (i % 2 == 0) {
				ret.x += p.getX()[i];
			} else {
				ret.y += p.getX()[i];
			}
		}
		return ret;
	}

	public static Point2D.Double mappedPlane2DWith(Particle p,
			Point2D.Double friend, double dist) {
		Point2D.Double ret = new Point2D.Double();
		double hash = 0;
		for (int i = 0; i < Particle.getXLength(); i++) {
			if (i % 2 == 0) {
				hash += p.getX()[i];
			} else {
				hash += p.getX()[i];
			}
		}
		ret.x = friend.x + dist * Math.sin(hash);
		ret.y = friend.y + dist * Math.cos(hash);
		return ret;
	}
}
