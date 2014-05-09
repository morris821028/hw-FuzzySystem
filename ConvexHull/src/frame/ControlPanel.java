package frame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import convexhull.Algorithm;
import convexhull.MonotoneChain;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ControlPanel extends JPanel implements ActionListener {
	private static ControlPanel singleton;

	public static ControlPanel getInstance() {
		if (singleton == null)
			singleton = new ControlPanel();
		return singleton;
	}

	public JTextArea inputArea;
	// public CarMap carMap;
	public java.util.Timer testTimer = new java.util.Timer("Test Timer");
	public TimerTask testTask;
	//
	public JPanel settingPanel;
	public JPanel consolePanel;
	//
	public Algorithm convexHullBuild;

	private ControlPanel() {
		convexHullBuild = new MonotoneChain();

		consolePanel = createInputPanel();
		settingPanel = createSettingPanel();

		this.setLayout(new GridLayout(2, 1));
		this.add(consolePanel);
		this.add(settingPanel);
		this.setMaximumSize(new Dimension(200, 300));
	}

	protected JPanel createInputPanel() {
		JPanel consolePanel;
		inputArea = new JTextArea();
		inputArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// System.out.println(inputArea.getText());
				convexHullBuild.parsingInput(inputArea.getText());
				ConvexHullCanvas.getInstance().plane2DPoint = convexHullBuild.D;
				ConvexHullCanvas.getInstance().repaint();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// System.out.println(inputArea.getText());
				convexHullBuild.parsingInput(inputArea.getText());
				ConvexHullCanvas.getInstance().plane2DPoint = convexHullBuild.D;
				ConvexHullCanvas.getInstance().repaint();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
		});

		consolePanel = new JPanel();
		consolePanel.setLayout(new BorderLayout());
		consolePanel.add(new JLabel("Point Input"), BorderLayout.NORTH);
		consolePanel.add(inputArea, BorderLayout.CENTER);
		consolePanel.setBorder(new TitledBorder("Information"));
		return consolePanel;
	}

	protected JPanel createSettingPanel() {
		JPanel settingPanel = new JPanel();
		settingPanel.setBorder(new TitledBorder("Setting"));

		String[] ALname = { "Monotone" };
		JComboBox ALchooser = new JComboBox(ALname);
		settingPanel.setLayout(new GridLayout(4, 2));

		JButton autoRunButton;

		autoRunButton = new JButton("Auto-Run");

		autoRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread r = new Thread() {
					public void run() {
						SwingWorker worker = new SwingWorker() {

							@Override
							protected void done() {
								convexHullBuild.run(ConvexHullCanvas.getInstance());
							}

							@Override
							protected void process(List chunks) {
								// Here you can process the result of "doInBackGround()"
								// Set a variable in the dialog or etc.

							}

							@Override
							protected Object doInBackground() throws Exception {
								// Do the long running task here
								// Call "publish()" to pass the data to "process()"
								// return something meaningful

								return null;
							}
						};

						worker.execute();
					}
				};
				r.start();
			}
		});

		settingPanel.add(new JLabel("Algorithm"));
		settingPanel.add(ALchooser);
		settingPanel.add(new JButton("Random"));
		settingPanel.add(new JLabel(""));
		settingPanel.add(new JButton("Step Run"));
		settingPanel.add(autoRunButton);
		settingPanel.add(new JButton("import"));
		settingPanel.add(new JButton("export"));
		return settingPanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// carMap.repaint();
	}

}