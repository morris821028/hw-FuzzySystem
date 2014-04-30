package calcModel.geneAlgorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import calcModel.geneAlgorithm.ui.GeneControl;

public class GeneMachine {
	public int numberOfoffspring = 256;
	public double[][] dataInput;
	public double[] dataOutput;
	private GenePool genePool;
	public int processOfoffspring;

	public GeneMachine(double[][] dataInput, double[] dataOutput) {
		genePool = new GenePool(this, 512, 0.5, 0.5, 0.5, 0.5);
		this.dataInput = dataInput;
		this.dataOutput = dataOutput;
	}
	public void setGenePool(int size, double pc, double rc, double pm, double rm) {
		genePool = new GenePool(this, size, pc, rc, pm, rm);
	}
	public void init(Gene prevBest[]) {
		genePool.init(prevBest);
	}

	JDialog dialog;
	JProgressBar progressBar;
	JLabel bestEn;
	DefaultCategoryDataset dataset;
	JTable bestGeneTable;

	private CategoryDataset setDataset() {
		// 將rows資料存入陣列
		// String[] series = { "第一季", "第二季", "第三季" };
		// 將columns資料存入陣列...
		// String[] category = { "咖啡", "啤酒", "果汁", "汽水", "茶" };
		// 將table資料存入陣列...
		// double[][] data = { { 8.0, 5.5, 10.2 }, { 4.2, 7.8, 23.0 },
		// { 3.5, 5.3, 6.8 }, { 9.2, 14.5, 10.7 }, { 6.6, 8.3, 7.9 } };
		dataset = new DefaultCategoryDataset();
		// 將資料填入dataset實體...
		// for (int i = 0; i < category.length; i++) {
		// for (int j = 0; j < series.length; j++) {
		// dataset.addValue(data[i][j], series[j], category[i]);
		// }
		// }
		return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createLineChart("Gene Algorithm Chart", // 圖表標題
				"Iterator times", // X軸標題
				"Top E(n)", // Y軸標題
				dataset,// dataset 物件變數
				PlotOrientation.VERTICAL,// Bar的方向
				true, // 加入圖例說明?
				true,// 顯示提示資料?
				false// URLs?
				);
		return chart;
	}

	private double[][] getBestGeneTableData() {
		double[][] ret;
		DefaultTableModel model = (DefaultTableModel) bestGeneTable.getModel();
		ret = new double[model.getRowCount()][model.getColumnCount()];
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				ret[i][j] = (Double) model.getValueAt(i, j);
			}
		}
		return ret;
	}

	private void recordBestGene() {
		if (bestGeneTable != null) {
			Gene gene = this.getBestGene();
			DefaultTableModel model = (DefaultTableModel) bestGeneTable
					.getModel();
			Object[] row = new Object[gene.getDNALength()];
			for (int i = 0; i < row.length; i++)
				row[i] = gene.getDNA()[i];
			model.addRow(row);
		}
	}

	private JScrollPane initializeBestGeneTable() {

		String[] columnNames = Gene.getDNAName();
		DefaultTableModel model = new DefaultTableModel();
		bestGeneTable = new JTable(model);
		model.setColumnIdentifiers(columnNames);

		JScrollPane scrollPane = new JScrollPane(bestGeneTable);
		scrollPane.setPreferredSize(new Dimension(300, 150));
		return scrollPane;
	}

	public boolean openDialog() {
		Thread r = new Thread() {
			public void run() {
				SwingWorker worker = new SwingWorker() {

					@Override
					protected void done() {
						dialog = new JDialog();

						progressBar = new JProgressBar(0, 100);
						bestEn = new JLabel("Best E(n) = X");

						JPanel showPanel = new JPanel(), toolPanel = new JPanel();
						ChartPanel chartPanel = new ChartPanel(
								createChart(setDataset()));
						chartPanel.setPreferredSize(new Dimension(300, 330));

						JButton exportButton = new JButton("export");
						JButton showButton = new JButton("show gene");
						toolPanel.setLayout(new GridLayout(1, 3));
						toolPanel.add(exportButton);
						toolPanel.add(showButton);
						toolPanel.add(bestEn);

						exportButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								exportFile();
							}
						});

						showPanel.setLayout(new BorderLayout());
						showPanel.add(initializeBestGeneTable(),
								BorderLayout.NORTH);
						showPanel.add(chartPanel, BorderLayout.CENTER);
						showPanel.add(toolPanel, BorderLayout.SOUTH);

						progressBar.setStringPainted(true);
						dialog.setTitle("Gene Algorithm Build");
						dialog.add(BorderLayout.NORTH, new JLabel("Iterator "
								+ numberOfoffspring + " times."));
						dialog.add(BorderLayout.CENTER, progressBar);
						dialog.add(BorderLayout.SOUTH, showPanel);

						dialog.setSize(800, 600);
						dialog.setAlwaysOnTop(true);
						dialog.setLocationRelativeTo(null);
						dialog.setVisible(true);
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
		return true;
	}

	public void on() {
		openDialog();
		Thread r = new Thread() {
			public void run() {
				for (int i = 0; i < numberOfoffspring; i++) {
					processOfoffspring = i;
					double best = genePool.crossover(dataInput, dataOutput);
					if (bestEn != null) {
						bestEn.setText("Best E(n) = " + best);
						if(dataset != null)
							dataset.addValue(best, "M1", "" + i);
						recordBestGene();
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (progressBar != null) {
								progressBar.setValue(processOfoffspring * 100
										/ numberOfoffspring + 1);
							}
						}
					});

				}
				JOptionPane.showMessageDialog(dialog,
						"提示:\n您可以啟動 Run(Gene X) 執行當前最好的結果 RBF。",
						"Gene Building", JOptionPane.WARNING_MESSAGE);
				Gene g = getBestGene();
				if (g != null) {
					GeneControl.getInstance().dataBase.add(g);
					GeneControl.getInstance().storeMachine();
					System.out.println("Data size = "
							+ GeneControl.getInstance().dataBase.size());
				}
			}
		};
		r.start();
	}

	public Gene getBestGene() {
		if (dataOutput == null || dataInput == null)
			return null;
		assert (dataInput.length == dataOutput.length);

		Gene ret = genePool.gene[0];
		double best = genePool.gene[0].calcuateFitness(dataOutput, dataInput);
		for (int i = 0; i < genePool.gene.length; i++) {
			double f = genePool.gene[i].calcuateFitness(dataOutput, dataInput);
			if (f < best) {
				best = f;
				ret = genePool.gene[i];
			}
		}
		return ret;
	}

	private File exportDirectory = new java.io.File(".");

	private void exportFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(exportDirectory);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Text file (.txt)", "txt");

		chooser.setSelectedFile(new File("iteratorBestGene.txt"));
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(dialog);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			exportDirectory = chooser.getCurrentDirectory();
			File file = chooser.getSelectedFile();
			try {

				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-8"));

				double[][] dataIn = getBestGeneTableData();
				for (int i = 0; i < dataIn.length; i++) {
					for (int j = 0; j < dataIn[i].length; j++) {
						writer.printf("%f ", dataIn[i][j]);
					}
					writer.println();
				}

				writer.close();
				JOptionPane.showMessageDialog(dialog, "提示:\n儲存完畢",
						"Gene Building", JOptionPane.WARNING_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
