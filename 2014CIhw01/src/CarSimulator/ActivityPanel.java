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
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ActivityPanel extends JPanel {
	JTable table;
	public int limitRecord = 512;
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
		rowCount.setText(model.getRowCount() + "");
		
	}

	public double[][] getDataInput() {
		double[][] ret;
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int inColumn = 3;
		ret = new double[model.getRowCount()][inColumn];
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < inColumn; j++) {
				ret[i][j] = (Double) model.getValueAt(i, j);
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
		String[] columnNames = { "d1", "d2", "d3", "theta" };

		//
		// Defines table's data.
		//
		Object[][] data = { { 0f, 0f, 0f, 0f } };

		//
		// Defines table's column width.
		//
		int[] columnsWidth = { 50, 50, 50, 50 };

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
		for (int width : columnsWidth) {
			TableColumn column = table.getColumnModel().getColumn(i++);
			column.setMinWidth(width);
			column.setMaxWidth(width);
			column.setPreferredWidth(width);
		}

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(220, 80));
		JButton clrButton = new JButton("Clear");
		clrButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				while (model.getRowCount() > 0)
					model.removeRow(0);
			}
		});
		JPanel panel = new JPanel(new GridLayout(1, 2));
		rowCount = new JLabel("0");
		panel.add(rowCount);
		panel.add(clrButton);
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
	}
}
