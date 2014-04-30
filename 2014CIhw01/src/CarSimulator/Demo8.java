package CarSimulator;

/*
 * (C) 2004 - Geotechnical Software Services
 * 
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */
import java.awt.*;

import javax.swing.*;

import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

/**
 * G demo program. Demonstrates:
 * 
 * <ul>
 * <li>Nested GObject hierarchy
 * <li>Style inheritance
 * <li>Simple selection interaction
 * </ul>
 * 
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class Demo8 extends JFrame implements GInteraction {
	private GScene scene_;

	public Demo8() {
		super("G Graphics Library - Demo 8");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the GUI
		JPanel topLevel = new JPanel();
		topLevel.setLayout(new BorderLayout());
		getContentPane().add(topLevel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new JLabel(
				"Button 1 to select color, button 2 to unselect"));

		JPanel graphicsPanel = new JPanel();
		topLevel.add(buttonPanel, BorderLayout.NORTH);

		// Create the graphic canvas
		GWindow window = new GWindow();
		topLevel.add(window.getCanvas(), BorderLayout.CENTER);

		// Create scene with default viewport and world extent settings
		scene_ = new GScene(window, "Scene");

		double w0[] = { 0.0, 1200.0, 0.0 };
		double w1[] = { 1200.0, 1200.0, 0.0 };
		double w2[] = { 0.0, 0.0, 0.0 };
		scene_.setWorldExtent(w0, w1, w2);

		GStyle style = new GStyle();
		style.setForegroundColor(new Color(0, 0, 0));
		style.setBackgroundColor(new Color(255, 255, 255));
		style.setFont(new Font("Dialog", Font.BOLD, 14));
		scene_.setStyle(style);

		// Create som graphic objects
		GObject object1 = new TestObject("1", scene_, 500.0, 100.0);

		GObject object2 = new TestObject("2", object1, 250.0, 250.0);
		GObject object3 = new TestObject("3", object1, 500.0, 250.0);
		GObject object4 = new TestObject("4", object1, 625.0, 250.0);

		GObject object5 = new TestObject("5", object2, 150.0, 400.0);
		GObject object6 = new TestObject("6", object2, 250.0, 400.0);
		GObject object7 = new TestObject("7", object2, 350.0, 400.0);

		GObject object8 = new TestObject("8", object4, 625.0, 400.0);

		GObject object9 = new TestObject("9", object7, 250.0, 550.0);
		GObject object10 = new TestObject("10", object7, 350.0, 550.0);

		GObject object11 = new TestObject("11", object8, 475.0, 550.0);
		GObject object12 = new TestObject("12", object8, 600.0, 550.0);
		GObject object13 = new TestObject("13", object8, 725.0, 550.0);
		GObject object14 = new TestObject("14", object8, 850.0, 550.0);

		GObject object15 = new TestObject("15", object9, 150.0, 700.0);
		GObject object16 = new TestObject("16", object9, 250.0, 700.0);
		GObject object17 = new TestObject("17", object9, 350.0, 700.0);

		GObject object18 = new TestObject("18", object13, 725.0, 700.0);

		GObject object19 = new TestObject("19", object17, 350.0, 850.0);

		GObject object20 = new TestObject("20", object19, 250.0, 1000.0);
		GObject object21 = new TestObject("21", object19, 350.0, 1000.0);
		GObject object22 = new TestObject("22", object19, 450.0, 1000.0);

		pack();
		setSize(new Dimension(500, 500));
		setVisible(true);

		window.startInteraction(this);
	}

	public void event(GScene scene, int event, int x, int y) {
		if (event == GWindow.BUTTON1_UP || event == GWindow.BUTTON2_UP) {
			boolean isSelected = event == GWindow.BUTTON1_UP;

			GSegment selectedSegment = scene_.findSegment(x, y);
			if (selectedSegment == null)
				return;

			GStyle style = selectedSegment.getOwner().getStyle();
			if (style == null)
				return;

			if (isSelected)
				style.setBackgroundColor(new Color((float) Math.random(),
						(float) Math.random(), (float) Math.random()));
			else
				style.unsetBackgroundColor();

			scene_.refresh();
		}
	}

	private class TestObject extends GObject {
		private TestObject parent_;
		private double x_, y_;
		private GSegment circle_;
		private GSegment line_;

		TestObject(String name, GObject parent, double x, double y) {
			parent_ = parent instanceof TestObject ? (TestObject) parent : null;

			x_ = x;
			y_ = y;

			line_ = new GSegment();
			addSegment(line_);

			circle_ = new GSegment();
			addSegment(circle_);

			circle_.setText(new GText(name, GPosition.MIDDLE));

			setStyle(new GStyle());

			parent.add(this);
		}

		double getX() {
			return x_;
		}

		double getY() {
			return y_;
		}

		public void draw() {
			if (parent_ != null)
				line_.setGeometry(parent_.getX(), parent_.getY(), x_, y_);

			circle_.setGeometryXy(Geometry.createCircle(x_, y_, 50.0));
		}
	}
	/*
	 * public static void main(String[] args) { new Demo8(); }
	 */
}
