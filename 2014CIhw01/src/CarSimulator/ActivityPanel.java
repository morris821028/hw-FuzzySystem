package CarSimulator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ActivityPanel extends JPanel {
	JTable table;
	public int limitRecord = 1024;
	JLabel rowCount;

	public ActivityPanel() {
		super();

		this.setBorder(new TitledBorder("Fuzzy System"));

		initializePanel();
	}

	public void recordData(double d1, double d2, double d3, double theta) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[] { d1, d2, d3, theta });
		if (model.getRowCount() > limitRecord) {
			model.removeRow(0);
		}
		rowCount.setText(model.getRowCount() + " item(s)");
	}

	public double[][] getDataInput() {
		double[][] ret;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int inColumn = 3;
		ret = new double[model.getRowCount()][inColumn];
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < inColumn; j++) {
				ret[i][j] = (Double) model.getValueAt(i, j);
				ret[i][j] = Math.min(30, ret[i][j]);
			}
		}
		return ret;
	}

	public double[] getDataOutput() {
		double[] ret;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int outColumnIdx = 3;
		ret = new double[model.getRowCount()];
		for (int i = 0; i < model.getRowCount(); i++) {
			ret[i] = (((Double) model.getValueAt(i, outColumnIdx) + 40)) / 80.0;
			// Normalization
		}
		return ret;
	}

	private void initializePanel() {
		//
		// Defines table's column names.
		//
		String[] columnNames = { "d1(前)", "d2(右)", "d3(左)", "Theta(deg)" };

		//
		// Defines table's data.
		//
		Object[][] data = { { 0f, 0f, 0f, 0f } };

		//
		// Defines table's column width.
		//
		// int[] columnsWidth = { 50, 50, 50, 70 };

		//
		// Creates an instance of JTable and fill it with data and
		// column names information.
		//
		DefaultTableModel model = new DefaultTableModel();
		table = new JTable(model);
		model.setColumnIdentifiers(columnNames);
		// table = new JTable(data, columnNames);
		//
		// Configures table's column width.
		//
		int i = 0;
//		for (int width : columnsWidth) {
//			TableColumn column = table.getColumnModel().getColumn(i++);
//			column.setMinWidth(width);
//			column.setMaxWidth(width);
//			column.setPreferredWidth(width);
//		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(220, 80));
		JButton clrButton = new JButton("empty");
		JButton importButton = new JButton("import");
		JButton exportButton = new JButton("export");
		clrButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				while (model.getRowCount() > 0)
					model.removeRow(0);
				rowCount.setText("0 item(s)");
			}
		});
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importFile();
			}
		});
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportFile();
			}
		});
		JPanel panel = new JPanel(new GridLayout(1, 4));
		rowCount = new JLabel("0 item(s)");
		panel.add(rowCount);
		panel.add(importButton);
		panel.add(exportButton);
		panel.add(clrButton);
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
	}

	private File importDirectory = new java.io.File(".");
	private File exportDirectory = new java.io.File(".");

	private void importFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(importDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text file (.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			importDirectory = chooser.getCurrentDirectory();
			File file = chooser.getSelectedFile();
			try {
				Scanner fileScanner = new Scanner(new InputStreamReader(
						new FileInputStream(file), "UTF8"));
				while (fileScanner.hasNext()) {
					double d1 = fileScanner.nextDouble();
					double d2 = fileScanner.nextDouble();
					double d3 = fileScanner.nextDouble();
					double theta = fileScanner.nextDouble();
					recordData(d1, d2, d3, theta);
				}
				fileScanner.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void exportFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(exportDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text file (.txt)", "txt");

		chooser.setSelectedFile(new File("fileToSave.txt"));
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			exportDirectory = chooser.getCurrentDirectory();
			File file = chooser.getSelectedFile();
			try {

				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8"));
				double[][] dataIn = getDataInput();
				double[] dataOut = getDataOutput();
				for(int i = 0; i < dataIn.length; i++) {
					for(int j = 0; j < dataIn[i].length; j++) {
						writer.printf("%f ", dataIn[i][j]);
					}
					writer.println(dataOut[i] * 80 - 40);
				}
				writer.close();
			    JOptionPane.showMessageDialog(null, "提示:\n儲存完畢", "Gene Building", JOptionPane.WARNING_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
