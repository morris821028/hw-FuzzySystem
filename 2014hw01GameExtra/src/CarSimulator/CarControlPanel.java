package CarSimulator;

import javax.swing.*;

import CarSimulator.component.Car;

import java.awt.*;

public class CarControlPanel extends JPanel {
	private static CarControlPanel singleton;
	public static CarControlPanel getInstance() {
		if(singleton == null)
			singleton = new CarControlPanel();
		return singleton;
	}
	public JSpinner xSpinner;
	public JSpinner ySpinner;
	public JTextField d1Text, d2Text, d3Text;
	
	private CarControlPanel() {
		SpinnerModel xmodel = new SpinnerNumberModel(0.00, -100, 100, 0.01);
		SpinnerModel ymodel = new SpinnerNumberModel(0.00, -100, 100, 0.01);
		xSpinner = new JSpinner(xmodel);
		ySpinner = new JSpinner(ymodel);
		
		d1Text = new JTextField();
		d2Text = new JTextField();
		d3Text = new JTextField();
		
		xSpinner.setEnabled(false);
		ySpinner.setEnabled(false);
		d1Text.setEditable(false);
		d2Text.setEditable(false);
		d3Text.setEditable(false);
		
		this.setLayout(new GridLayout(5, 2));
		this.add(new JLabel("X : "));
		this.add(xSpinner);
		this.add(new JLabel("Y : "));
		this.add(ySpinner);
		this.add(new JLabel("Head-Sensor : "));
		this.add(d1Text);
		this.add(new JLabel("Right-Sensor : "));
		this.add(d2Text);
		this.add(new JLabel("Left-Sensor : "));
		this.add(d3Text);
	}
	public void updateInfo(Car car, double v1, double v2, double v3) {
		xSpinner.setValue(car.getX());
		ySpinner.setValue(car.getY());
		d1Text.setText(String.format("%.3f", v1));
		d2Text.setText(String.format("%.3f", v2));
		d3Text.setText(String.format("%.3f", v3));
	}
}
