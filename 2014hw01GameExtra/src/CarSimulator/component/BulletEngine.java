package CarSimulator.component;

public class BulletEngine {
	public void runDeltaT(Bullet bullet, double phi, double theta) {
		double x, y;
		x = bullet.getX() + Math.cos(phi)
				+ Math.sin(theta) * Math.sin(phi) * 0.5;
		y = bullet.getY() + Math.sin(phi)
				- Math.sin(theta) * Math.cos(phi) * 0.5;
		bullet.setX(x);
		bullet.setY(y);
	}
}
