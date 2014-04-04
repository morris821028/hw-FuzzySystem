package calcModel.geneAlgorithm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import CarSimulator.CarControlPanel;

public class GeneControl {
	private static GeneControl singleton;

	public static GeneControl getInstance() {
		if (singleton == null)
			singleton = new GeneControl();
		return singleton;
	}

	public GeneMachine geneMachine;
	public Gene prevBestGene;

	private GeneControl() {
		geneMachine = new GeneMachine(null, null);
	}

	public void setGeneEnvironment(double[][] dataInput, double[] dataOutput) {
		geneMachine.dataInput = dataInput;
		geneMachine.dataOutput = dataOutput;
	}

	public void restartMachine(Gene prevBest) {	
		if (geneMachine.dataOutput.length < 10)
			return;
		geneMachine.init(prevBest);
		geneMachine.on();
		prevBestGene = geneMachine.getBestGene();
		double f = prevBestGene.calcuateFitness(geneMachine.dataOutput,
				geneMachine.dataInput);
		System.out.println(f);
	}

	public Gene getBestGene() {
		return prevBestGene;
	}
}
