import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;

import java.awt.*;

public class Main extends JFrame {
	public Main() {

		this.setTitle("2014 HW 01 2D map(road) paint tool.");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		Car car = new Car(new Engine());
		CarMap carmap = new CarMap(car);
		this.setLayout(new BorderLayout());
		this.add(carmap, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
				UIManager.put("TabbedPane.contentOpaque", Boolean.TRUE);
			}
		});
		Main f = new Main();
		f.setVisible(true);
	}
}
