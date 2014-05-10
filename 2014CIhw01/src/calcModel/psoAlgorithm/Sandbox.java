package calcModel.psoAlgorithm;

import java.util.Arrays;
import java.util.Random;

public class Sandbox {
	Particle[] sand;
	Particle[] newSand;
	PsoMachine psoMachine;
	public int sandSize = 512;
	public double maxVelocity = 40;
	public double maxXposition = 40;
	public double weightOfCognition = 0.3;
	public double weightOfSocial = 0.3;
	public double nearRatio = 0.2;
	
	private ParticleImitate[] process;
	
	public Sandbox(PsoMachine pm, int sandSize, double maxVelocity,
			double maxXposition, double weightOfCognition, double weightOfSocial) {

		this.sandSize = sandSize;
		this.maxVelocity = maxVelocity;
		this.maxXposition = maxXposition;
		this.weightOfCognition = weightOfCognition;
		this.weightOfSocial = weightOfSocial;

		sand = new Particle[sandSize];
		newSand = new Particle[sandSize];
		process = new ParticleImitate[sandSize];
		
		for (int i = 0; i < sand.length; i++)
			sand[i] = new Particle();
		psoMachine = pm;
		init(null);
	}

	public void init(Particle[] prevBest) {
		if (prevBest == null || prevBest.length == 0) {
			for (int i = 0; i < sand.length; i++)
				sand[i].randomBuild();
		} else {
			// System.out.println("reused");
			int percent25 = sand.length / 4;
			for (int i = 0; i < percent25; i++) {
				int x = (int) ((Math.random() * prevBest.length));
				sand[i] = prevBest[x];
			}
			for (int i = percent25; i < sand.length; i++)
				sand[i].randomBuild();
		}
		
		for (int i = 0; i < sand.length; i++)
			sand[i].on();
	}
	
	public ParticleImitate imitate(ParticlePair A, ParticlePair[] swarm) {
		Particle ret;
		Particle globalBest, nearBest;
		ParticleDistPair[] B = new ParticleDistPair[sand.length - 1];

		ret = A.sand.clone();
		globalBest = swarm[0].sand;

		for (int i = 0, j = 0; i < swarm.length; i++) {
			if (swarm[i] == A)
				continue;
			B[j++] = new ParticleDistPair(A.sand.distTo(swarm[i].sand), swarm[i]);
		}
		Arrays.sort(B);

		double minf = B[0].p.f; // minimum better
		nearBest = B[0].p.sand;
		for (int i = 0; i < sandSize * nearRatio && i < sandSize; i++) {
			if (B[i].p.f < minf) {
				minf = B[i].p.f;
				nearBest = B[i].p.sand;
			}
		}
		
		double vLen = 0;
		for (int i = 0; i < Particle.getVLength(); i++) {
			ret.getV()[i] = A.sand.getV()[i] + this.weightOfSocial
					* (globalBest.getX()[i] - A.sand.getX()[i])
					+ this.weightOfCognition
					* (nearBest.getX()[i] - A.sand.getX()[i]);
			vLen += ret.getV()[i] * ret.getV()[i]; 
		}
		
		vLen = Math.sqrt(vLen);
		if(vLen > this.maxVelocity) {
			for(int i = 0; i < Particle.getVLength(); i++) {
				ret.getV()[i] = ret.getV()[i] / vLen * this.maxVelocity;
			}
		}
		
		ret.move(); // I'm flying.
		
		ParticleImitate pp = new ParticleImitate();
		pp.before = A.sand;
		pp.after = ret;
		pp.friend = nearBest;
		pp.fitness = A.f;
		pp.dist = minf;
		return pp;
	}

	public double clustering(double[][] dataInput, double[] dataOutput) {
		ParticlePair[] A = new ParticlePair[sand.length];
		for (int i = 0; i < sand.length; i++) {
			double f = sand[i].calcuateFitness(dataOutput, dataInput);
			// small better than large.
			A[i] = new ParticlePair(f, sand[i]);
		}
		Arrays.sort(A);
		double bestF = A[0].f;

		for (int i = 0; i < A.length; i++) {
			ParticleImitate s = imitate(A[i], A);
			process[i] = s;
			newSand[i] = s.after;
		}

		for (int i = 0; i < A.length; i++) {
			sand[i] = newSand[i];
		}

		for (int i = 0; i < A.length; i++) {
			sand[i].on();
		}
		
		return bestF;
	}
	
	public ParticleImitate[] getProcessStep() {
		return process;
	}
}
