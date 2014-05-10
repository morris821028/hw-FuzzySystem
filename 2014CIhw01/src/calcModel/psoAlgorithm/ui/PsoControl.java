package calcModel.psoAlgorithm.ui;

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

import calcModel.psoAlgorithm.Particle;
import calcModel.psoAlgorithm.ParticlePair;
import calcModel.psoAlgorithm.PsoMachine;
import CarSimulator.Car;
import CarSimulator.CarControlPanel;

public class PsoControl {
	private static PsoControl singleton;

	public static PsoControl getInstance() {
		if (singleton == null)
			singleton = new PsoControl();
		return singleton;
	}

	public PsoMachine psoMachine;
	public ArrayList<Particle> dataBase = new ArrayList<Particle>();
	public final String dbFileName = new String("particleState.txt");

	private PsoControl() {
		psoMachine = new PsoMachine(null, null);
		try {
			Scanner cin = new Scanner(new InputStreamReader(
					new FileInputStream(new File(dbFileName)), "UTF8"));
			int n = Particle.getXLength();
			while (cin.hasNext()) {
				Particle g = new Particle();
				for (int i = 0; i < n; i++) {
					g.getX()[i] = cin.nextDouble();
				}
				g.on();
				dataBase.add(g);
			}
			System.out.println("PSO Read local database " + dataBase.size());
			cin.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"提示:\n找不到之前設置的 particleState.txt，本次結束將新增 particleState.txt",
					"PSO Building", JOptionPane.WARNING_MESSAGE);
			System.out.println(e.getMessage());
		}
	}

	public void setGeneEnvironment(double[][] dataInput, double[] dataOutput) {
		psoMachine.dataInput = dataInput;
		psoMachine.dataOutput = dataOutput;
		dataRebase(dataInput, dataOutput);
	}

	public void dataRebase(double[][] dataInput, double[] dataOutput) {
		ParticlePair[] A = new ParticlePair[dataBase.size()];
		for (int i = 0; i < A.length; i++) {
			double f = dataBase.get(i).calcuateFitness(dataOutput, dataInput);
			// small better than large.
			A[i] = new ParticlePair(f, dataBase.get(i));
		}
		Arrays.sort(A);
		dataBase.clear();
		for (int i = 0; i < 32 && i < A.length; i++) {
			dataBase.add(A[i].sand);
		}
	}

	public void restartMachine(Particle prevBest) {
		if (psoMachine.dataOutput.length < 10)
			return;
		PsoDialog customDialog = new PsoDialog(null, "geisel");
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
			psoMachine.setGenePool(size, pc, rc, pm, rm);
			psoMachine.numberOfoffspring = (Integer) argu[5];
			Particle[] array = dataBase.toArray(new Particle[dataBase.size()]);
			psoMachine.init(array);
			psoMachine.on();
		}
	}

	public void storeMachine() {
		try {
			PrintWriter cout = new PrintWriter(new File(dbFileName));
			int n = Particle.getXLength();
			for (int i = 0; i < dataBase.size(); i++) {
				for (int j = 0; j < n; j++)
					cout.printf("%f ", dataBase.get(i).getX()[j]);
				cout.println();
			}
			cout.close();
		} catch (Exception e) {
			System.out.println("write file fail");
			System.out.println(e.getMessage());
		}
	}

	public Particle getBestSand() {
		Particle g = psoMachine.getBestSand();
		if (g != null)
			return g;
		if (dataBase.size() > 0)
			return dataBase.get((int) (Math.random() * dataBase.size()));
		return null;
	}

	TimerTask testTask;
	java.util.Timer testTimer = new java.util.Timer("Test Timer");

	public void driveCar(int fps) {
		testTask = new TimerTask() {
			Particle engine;
			Car car;

			public TimerTask init(Particle engine, Car car) {
				this.engine = engine;
				this.car = car;
				return this;
			}

			public void run() {
				try {
					double d[] = CarControlPanel.getInstance().getSensorInfo();
					double d1 = d[0];
					double d2 = d[1];
					double d3 = d[2];
					d1 = Math.min(d1, 30);
					d2 = Math.min(d2, 30);
					d3 = Math.min(d3, 30);
					double deltaTheta = engine.rbf.calcuateOutput(new double[] {
							d1, d2, d3 });
					deltaTheta = deltaTheta * 80 - 40;
					deltaTheta = Math.max(Math.min(deltaTheta, 40), -40);
					deltaTheta = deltaTheta / 180.0 * Math.PI;
					car.run(deltaTheta);
					CarControlPanel.getInstance().carMap.recordCarPath();
					CarControlPanel.getInstance().carMap.repaint();
				} catch (Exception e) {
					e.getStackTrace();
				}
				Thread.yield();
			}
		}.init(PsoControl.getInstance().getBestSand(),
				CarControlPanel.getInstance().carMap.cars.get(0));
		testTimer.scheduleAtFixedRate(testTask, 100, 1000 / fps);
	}

	public void stopCar() {
		if (testTask != null)
			testTask.cancel();
		testTask = null;
	}
}

