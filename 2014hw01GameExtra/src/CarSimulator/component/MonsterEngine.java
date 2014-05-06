package CarSimulator.component;

import CarSimulator.CarMap;

public class MonsterEngine {
	public void runDeltaT(Monster ms, double phi, double theta) {
		double x, y; 
		theta = Math.PI / 6;
		phi = Math.atan2(CarMap.getInstance().car.getY() - ms.getY(), CarMap.getInstance().car.getX() - ms.getX());
		x = ms.getX() + (Math.cos(phi)
				+ Math.sin(theta) * Math.sin(phi) * 0.5) / 2;
		y = ms.getY() + (Math.sin(phi)
				- Math.sin(theta) * Math.cos(phi) * 0.5) / 2;
		ms.setX(x);
		ms.setY(y);
	}
}
