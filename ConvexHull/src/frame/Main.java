package frame;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.util.Hashtable;

import javax.swing.UIManager.*;

public class Main extends JFrame {
	public Main() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
		this.setTitle("[Convex Hull Debug] 12311 - All-Pair Farthest Points");		this.setSize(1024, 720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		ConvexHullCanvas cc = ConvexHullCanvas.getInstance();
		this.add(cc, BorderLayout.CENTER);
		this.add(ControlPanel.getInstance(), BorderLayout.WEST);
		
		this.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		Main f = new Main();
		f.setVisible(true);
	}
}
