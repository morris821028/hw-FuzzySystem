package calcModel.geneAlgorithm;

import java.awt.BorderLayout;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class GeneMachine {
	public int numberOfoffspring = 256;
	public double[][] dataInput;
	public double[] dataOutput;
	private GenePool genePool;
	public int processOfoffspring;

	public GeneMachine(double[][] dataInput, double[] dataOutput) {
		genePool = new GenePool(this);
		this.dataInput = dataInput;
		this.dataOutput = dataOutput;
	}

	public void init(Gene prevBest[]) {
		genePool.init(prevBest);
	}

	JDialog dlg;
	JProgressBar dpb;
	JLabel bestEn;

	public void openDialog() {
		Thread r = new Thread() {
			public void run() {
				SwingWorker worker = new SwingWorker() {

					@Override
					protected void done() {
						dlg = new JDialog();
						dpb = new JProgressBar(0, 100);
						bestEn = new JLabel("Best E(n) = X");

						dlg.setTitle("Gene Algorithm Build");
						dpb.setStringPainted(true);
						dlg.add(BorderLayout.CENTER, dpb);
						dlg.add(BorderLayout.SOUTH, bestEn);
						dlg.add(BorderLayout.NORTH, new JLabel("Iterator "
								+ numberOfoffspring + " times."));
						dlg.setSize(320, 120);
						dlg.setAlwaysOnTop(true);
						dlg.setLocationRelativeTo(null);
						dlg.setVisible(true);
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

	public void on() {
		openDialog();
		Thread r = new Thread() {
			public void run() {
				for (int i = 0; i < numberOfoffspring; i++) {
					processOfoffspring = i;
					double best = genePool.crossover(dataInput, dataOutput);
					if (bestEn != null)
						bestEn.setText("Best E(n) = " + best);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (dpb != null) {
								dpb.setValue(processOfoffspring * 100
										/ numberOfoffspring + 1);
							}
						}
					});

				}
				Gene g = getBestGene();
				if (g != null) {
					GeneControl.getInstance().dataBase.add(g);
					GeneControl.getInstance().storeMachine();
					System.out.println("Data size = "
							+ GeneControl.getInstance().dataBase.size());
				}
				dlg.dispose();
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
}
