package calcModel.geneAlgorithm.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import calcModel.geneAlgorithm.Gene;
import calcModel.geneAlgorithm.GeneMachine;
import calcModel.geneAlgorithm.GenePair;

public class GeneControl {
	private static GeneControl singleton;

	public static GeneControl getInstance() {
		if (singleton == null)
			singleton = new GeneControl();
		return singleton;
	}

	public GeneMachine geneMachine;
	public ArrayList<Gene> dataBase = new ArrayList<Gene>();
	public final String dbFileName = new String("genePool.txt");

	private GeneControl() {
		geneMachine = new GeneMachine(null, null);
		try {
			Scanner cin = new Scanner(new InputStreamReader(
					new FileInputStream(new File(dbFileName)), "UTF8"));
			int n = Gene.getDNALength();
			while (cin.hasNext()) {
				Gene g = new Gene();
				for (int i = 0; i < n; i++) {
					g.getDNA()[i] = (int) cin.nextDouble();
				}
				g.on();
				dataBase.add(g);
			}
			System.out.println("Read local database " + dataBase.size());
			cin.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"genePool.txt",
					"Gene Building", JOptionPane.WARNING_MESSAGE);
			System.out.println(e.getMessage());
		}
	}

	public void setGeneEnvironment(double[][] dataInput, double[] dataOutput) {
		geneMachine.dataInput = dataInput;
		geneMachine.dataOutput = dataOutput;
		dataRebase();
	}

	public void dataRebase() {
		GenePair[] A = new GenePair[dataBase.size()];
		for (int i = 0; i < A.length; i++) {
			double f = dataBase.get(i).calcuateFitness();
			// small better than large.
			A[i] = new GenePair(f, dataBase.get(i));
		}
		Arrays.sort(A);
		dataBase.clear();
		for (int i = 0; i < 32 && i < A.length; i++) {
			dataBase.add(A[i].gene);
		}
	}

	public void restartMachine(Gene prevBest) {
		GenePoolDialog customDialog = new GenePoolDialog(null, "geisel");
		customDialog.pack();
		customDialog.setLocationRelativeTo(null);
		customDialog.setVisible(true);
		String s = customDialog.getValidatedText();
		if (s != null) {
			Object[] argu = customDialog.getInputValue();
			int size;
			double pc, rc, pm, rm;
			size = (Integer) argu[0];
			pc = (Double) argu[1];
			rc = (Double) argu[2];
			pm = (Double) argu[3];
			rm = (Double) argu[4];
			geneMachine.setGenePool(size, pc, rc, pm, rm);
			geneMachine.numberOfoffspring = (Integer) argu[5];
			Gene[] array = dataBase.toArray(new Gene[dataBase.size()]);
			geneMachine.init(array);
			geneMachine.on();
		}
	}

	public void storeMachine() {
		try {
			PrintWriter cout = new PrintWriter(new File(dbFileName));
			int n = Gene.getDNALength();
			for (int i = 0; i < dataBase.size(); i++) {
				for (int j = 0; j < n; j++)
					cout.printf("%f ", dataBase.get(i).getDNA()[j]);
				cout.println();
			}
			cout.close();
		} catch (Exception e) {
			System.out.println("write file fail");
			System.out.println(e.getMessage());
		}
	}

	public Gene getBestGene() {
		Gene g = geneMachine.getBestGene();
		if (g != null)
			return g;
		if (dataBase.size() > 0)
			return dataBase.get((int) (Math.random() * dataBase.size()));
		return null;
	}
}
