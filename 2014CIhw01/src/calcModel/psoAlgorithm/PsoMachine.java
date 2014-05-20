package calcModel.psoAlgorithm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import orsonCharts.DemoPanel;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.axis.LabelOrientation;
import com.orsoncharts.axis.LogAxis3D;
import com.orsoncharts.axis.NumberAxis3D;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import com.orsoncharts.style.ChartStyler;

import calcModel.geneAlgorithm.Gene;
import calcModel.geneAlgorithm.GenePool;
import calcModel.geneAlgorithm.ui.GeneControl;
import calcModel.psoAlgorithm.ui.PsoControl;

public class PsoMachine {
	public int numberOfoffspring = 256;
	public double[][] dataInput;
	public double[] dataOutput;
	private Sandbox sandbox;
	public int processOfoffspring;

	public PsoMachine(double[][] dataInput, double[] dataOutput) {
		sandbox = new Sandbox(this, 512, 0.5, 0.5, 0.5, 0.5);
		this.dataInput = dataInput;
		this.dataOutput = dataOutput;
	}

	public void setGenePool(int size, double pc, double rc, double pm, double rm) {
		sandbox = new Sandbox(this, size, pc, rc, pm, rm);
	}

	public void init(Particle prevBest[]) {
		sandbox.init(prevBest);
	}

	JDialog dialog;
	JProgressBar progressBar;
	JLabel bestEn;
	DefaultCategoryDataset dataset;
	JTable bestGeneTable;

	private CategoryDataset setDataset() {
		dataset = new DefaultCategoryDataset();
		return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createLineChart("PSO Algorithm Chart", // 圖表標題
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
				ret[i][j] = (java.lang.Double) model.getValueAt(i, j);
			}
		}
		return ret;
	}

	private void recordBestSand() {
		if (bestGeneTable != null) {
			Particle sand = this.getBestSand();
			DefaultTableModel model = (DefaultTableModel) bestGeneTable
					.getModel();
			Object[] row = new Object[sand.getXLength()];
			for (int i = 0; i < row.length; i++)
				row[i] = sand.getX()[i];
			model.addRow(row);
		}
	}

	JPanel chart3DPanel;

	private static XYZDataset createDataset(ParticleImitate[] process) {
		XYZSeriesCollection dataset = new XYZSeriesCollection();
		XYZSeries s1 = new XYZSeries("S1");
		XYZSeries s2 = new XYZSeries("S2");

		int L = (int) Math.sqrt(process.length);

		Arrays.sort(process);

		double minf = process[0].fitness;
		double[] f = new double[process.length];
		for (int i = 0; i < process.length; i++) {
			f[i] = process[i].fitness;
		}

		Arrays.sort(f);
		minf = f[process.length / 2];

		for (int i = 0; i < process.length; i++) {
			Point2D.Double p = new Point2D.Double(i / L * 50, i % L * 50);
			if (process[i].fitness < minf)
				s2.add(p.x, process[i].fitness, p.y);
			else
				s1.add(p.x, process[i].fitness, p.y);
		}
		dataset.add(s1);
		dataset.add(s2);
		return dataset;
	}

	private void recordSandState() {
		DemoPanel content = new CustomDemoPanel(new BorderLayout());
		XYZDataset dataset = createDataset(sandbox.getProcessStep());
		Chart3D chart = Chart3DFactory.createScatterChart("Swarm 3D", null,
				dataset, "X", "Fitness", "Y");

		XYZPlot plot = (XYZPlot) chart.getPlot();
		ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
		plot.setDimensions(new Dimension3D(10, 6, 10));
		renderer.setSize(0.1);
		renderer.setColors(new Color(255, 128, 128), new Color(128, 255, 128));
		LogAxis3D yAxis = new LogAxis3D("Fitness E(n)");
		yAxis.setTickLabelOrientation(LabelOrientation.PERPENDICULAR);
		yAxis.receive(new ChartStyler(chart.getStyle()));
		plot.setYAxis(yAxis);
		chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(40));
		Chart3DPanel chartPanel = new Chart3DPanel(chart);
		content.setChartPanel(chartPanel);
		content.add(new DisplayPanel3D(chartPanel));
		chartPanel.zoomToFit(new Dimension(380, 240));
		if (chart3DPanel != null) {
			chart3DPanel.removeAll();
			chart3DPanel.add(content, BorderLayout.CENTER);
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
						JPanel chartBlock = new JPanel();
						ChartPanel chartPanel = new ChartPanel(
								createChart(setDataset()));
						chart3DPanel = new JPanel();

						chart3DPanel.setLayout(new BorderLayout());
						chart3DPanel.setPreferredSize(new Dimension(150, 430));

						chartPanel.setBorder(new TitledBorder(""));
						chart3DPanel.setBorder(new TitledBorder(""));

						chartBlock.setLayout(new GridLayout(1, 2));
						chartBlock.add(chartPanel);
						chartBlock.add(chart3DPanel);
						chartBlock.setPreferredSize(new Dimension(300, 430));

						JButton exportButton = new JButton("export");
						JButton showButton = new JButton("show particle");
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
						showPanel.add(chartBlock, BorderLayout.CENTER);
						showPanel.add(toolPanel, BorderLayout.SOUTH);

						progressBar.setStringPainted(true);
						dialog.setTitle("PSO Algorithm Build");
						dialog.add(BorderLayout.NORTH, new JLabel("Iterator "
								+ numberOfoffspring + " times."));
						dialog.add(BorderLayout.CENTER, progressBar);
						dialog.add(BorderLayout.SOUTH, showPanel);

						dialog.setSize(900, 700);
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
				int D3 = 0;
				for (int i = 0; i < numberOfoffspring; i++) {
					processOfoffspring = i;
					double best = sandbox.clustering(dataInput, dataOutput);

					if (bestEn != null) {
						bestEn.setText("Best E(n) = " + best);
						if (dataset != null)
							dataset.addValue(best, "M1", "" + i);
						recordBestSand();
						if (i % 2 == 0 && D3++ < 50)
							recordSandState();
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
						"提示:\n您可以啟動 Run(PSO X) 執行當前最好的結果 RBF。", "PSO Building",
						JOptionPane.WARNING_MESSAGE);
				Particle g = getBestSand();
				if (g != null) {
					PsoControl.getInstance().dataBase.add(g);
					PsoControl.getInstance().storeMachine();
					System.out.println("Pso Data size = "
							+ PsoControl.getInstance().dataBase.size());
				}
			}
		};
		r.start();
	}

	public Particle getBestSand() {
		if (dataOutput == null || dataInput == null)
			return null;
		assert (dataInput.length == dataOutput.length);

		Particle ret = this.sandbox.sand[0];
		double best = this.sandbox.sand[0].calcuateFitness(dataOutput,
				dataInput);
		for (int i = 0; i < this.sandbox.sand.length; i++) {
			double f = this.sandbox.sand[i].calcuateFitness(dataOutput,
					dataInput);
			if (f < best) {
				best = f;
				ret = this.sandbox.sand[i];
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

		chooser.setSelectedFile(new File("iteratorBestSand.txt"));
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
						"PSO Building", JOptionPane.WARNING_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	static class CustomDemoPanel extends DemoPanel implements ActionListener {

		private JCheckBox checkBox;

		public CustomDemoPanel(LayoutManager layout) {
			super(layout);
			this.checkBox = new JCheckBox("Logarithmic Scale");
			this.checkBox.setSelected(true);
			this.checkBox.addActionListener(this);
			JPanel controlPanel = new JPanel(new FlowLayout());
			controlPanel.add(this.checkBox);
			add(controlPanel, BorderLayout.SOUTH);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Chart3D chart = (Chart3D) getChartPanel().getDrawable();
			XYZPlot plot = (XYZPlot) chart.getPlot();
			if (this.checkBox.isSelected()) {
				LogAxis3D logAxis = new LogAxis3D("Y (logarithmic scale)");
				logAxis.setTickLabelOrientation(LabelOrientation.PERPENDICULAR);
				logAxis.receive(new ChartStyler(chart.getStyle()));
				plot.setYAxis(logAxis);
			} else {
				NumberAxis3D yAxis = new NumberAxis3D("Y");
				yAxis.setTickLabelOrientation(LabelOrientation.PERPENDICULAR);
				yAxis.receive(new ChartStyler(chart.getStyle()));
				plot.setYAxis(yAxis);
			}
		}
	}
}
