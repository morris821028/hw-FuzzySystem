package convexhull;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import frame.ConvexHullCanvas;

public class Algorithm {
	public Point2D.Double[] D;
	public Point2D.Double[] CH;
	
	public void parsingInput(String stdin) {
		String[] tokens = stdin.split("\\s+");
		ArrayList<Point2D.Double> r = new ArrayList<Point2D.Double>();
		for (int i = 0; i + 1 < tokens.length; i += 2) {
			try {
				double x, y;
				x = Double.parseDouble(tokens[i]);
				y = Double.parseDouble(tokens[i + 1]);
				r.add(new Point2D.Double(x, y));
			} catch (Exception e) {

			}
		}
		D = r.toArray(new Point2D.Double[r.size()]);
	}
	
	public void run(ConvexHullCanvas canvas) {
		
	}
	
	public void paint(Graphics g) {
		
	}
}
