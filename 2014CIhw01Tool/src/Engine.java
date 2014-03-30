

public class Engine {
	public void runDeltaT(Car car, double theta) {
		double x, y, phi;
		x = car.getX() + Math.cos(car.getPhi() + theta)
				+ Math.sin(car.getPhi()) * Math.sin(theta);
		y = car.getY() + Math.sin(car.getPhi() + theta)
				- Math.cos(car.getPhi()) * Math.sin(theta);
		phi = car.getPhi() - Math.asin((2 * Math.sin(theta)) / (2*car.getRadius()));
		car.setX(x);
		car.setY(y);
		car.setPhi(phi);
	}
}
