package CarSimulator;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
	public Main() {
		this.setTitle("Car Simulator 2014 HW - 01[Extra]");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		this.setLayout(new BorderLayout());
		Car car = new Car(new Engine());
		CarMap carmap = new CarMap(car);
		//CarControlPanel ctrlPanel = CarControlPanel.getInstance();
		this.add(carmap, BorderLayout.CENTER);
		//this.add(ctrlPanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		Main f = new Main();
		f.setVisible(true);
	}
}
