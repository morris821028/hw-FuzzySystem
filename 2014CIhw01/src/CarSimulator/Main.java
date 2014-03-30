package CarSimulator;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;

import calcModel.Engine;

import java.awt.*;
import java.util.Hashtable;
import javax.swing.UIManager.*;
public class Main extends JFrame {
	private Hashtable imgTable = new Hashtable();

	private void loadImage() {
		String fileIdx[] = { "menuIcon", "menuSetting", "menuPlay", "menuEye",
				"menuConsole" };
		String fileName[] = { "1395469821_menu.png", "1395468969_settings.png",
				"1395470381_video.png", "1395469784_eye.png",
				"1395469835_bubble.png" };
		Image img;
		try {
			for (int i = 0; i < fileName.length; i++) {
				img = ImageIO.read(this.getClass().getResource(
						"image/" + fileName[i]));
				imgTable.put(fileIdx[i], new ImageIcon(img));
			}
		} catch (Exception e) {
			System.out.println("Err !!!!!! " + e.getMessage());
		}
	}

	public Main() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		this.setTitle("100502205 - HW1");
		this.setSize(1024, 720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.loadImage();
		this.setLayout(new BorderLayout());

		MSlideBar slideBar = new MSlideBar();
		Car car = new Car(new Engine());
		CarMap carmap = new CarMap(car);
		CarControlPanel ctrlPanel = CarControlPanel.getInstance();

		JToggleButton ctrlBtn = new JToggleButton();
		ctrlBtn.setIcon((ImageIcon) imgTable.get("menuIcon"));
		slideBar.addSlideItem(ctrlBtn, null);
		JToggleButton settingBtn = new JToggleButton();
		settingBtn.setIcon((ImageIcon) imgTable.get("menuSetting"));
		slideBar.addSlideItem(settingBtn, null);
		JToggleButton playBtn = new JToggleButton();
		playBtn.setIcon((ImageIcon) imgTable.get("menuPlay"));
		slideBar.addSlideItem(playBtn, null);
		JToggleButton eyeBtn = new JToggleButton();
		eyeBtn.setIcon((ImageIcon) imgTable.get("menuEye"));
		slideBar.addSlideItem(eyeBtn, null);
		JToggleButton bubbleBtn = new JToggleButton();
		bubbleBtn.setIcon((ImageIcon) imgTable.get("menuConsole"));
		slideBar.addSlideItem(bubbleBtn, ctrlPanel.consolePanel);

		slideBar.setMaximumSize(new Dimension(250, this.getHeight()));

		slideBar.setAlignmentX(1.0f);
		slideBar.setAlignmentY(0.5f);

		OverlayLayout ov = new OverlayLayout(carmap);
		carmap.setLayout(ov);
		carmap.add(slideBar);

		this.add(carmap, BorderLayout.CENTER);
		this.add(ctrlPanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		Main f = new Main();
		f.setVisible(true);
		/*
		 * JFrame.setDefaultLookAndFeelDecorated(true);
		 * JDialog.setDefaultLookAndFeelDecorated(true);
		 * SwingUtilities.invokeLater(new Runnable() { public void run() {
		 * SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());
		 * UIManager.put("TabbedPane.contentOpaque", Boolean.TRUE); } });
		 */
	}
}
