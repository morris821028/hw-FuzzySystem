package calcModel.geneAlgorithm;

import java.util.Arrays;
import java.util.Random;

public class GenePool {
	Gene[] gene;
	Gene[] newGene;
	GeneMachine geneMachine;
	public int poolSize = 512;
	public double probabilityOfCrossover = 0.5;
	public double ratioOfCrossover = 0.5;
	public double probabilityOfMutation = 0.5;
	public double ratioOfMutation = 0.5;

	public GenePool(GeneMachine gm, int poolSize,
			double probabilityOfCrossover, double ratioOfCrossover,
			double probabilityOfMutation, double ratioOfMutation) {
		
		this.poolSize = poolSize;
		this.probabilityOfCrossover = probabilityOfCrossover;
		this.ratioOfCrossover = ratioOfCrossover;
		this.probabilityOfMutation = probabilityOfMutation;
		this.ratioOfMutation = ratioOfMutation;
		
		gene = new Gene[poolSize];
		newGene = new Gene[poolSize];
		for (int i = 0; i < gene.length; i++)
			gene[i] = new Gene();
		geneMachine = gm;
		init(null);
	}

	public void init(Gene[] prevBest) {
		if (prevBest == null || prevBest.length == 0) {
			for (int i = 0; i < gene.length; i++)
				gene[i].randomBuild();
		} else {
			// System.out.println("reused");
			int percent25 = gene.length / 4;
			for (int i = 0; i < percent25; i++) {
				int x = (int) ((Math.random() * prevBest.length));
				gene[i] = prevBest[x];
			}
			for (int i = percent25; i < gene.length; i++)
				gene[i].randomBuild();
		}
		for (int i = 0; i < gene.length; i++)
			gene[i].on();
	}

	public void geneCrossover(int x, int y, Gene xg, Gene yg) {
		double ratio = (Math.random() - 0.5) * 2 * this.ratioOfCrossover;
		Gene nx = new Gene(), ny = new Gene();
		for (int i = 0; i < xg.getDNA().length; i++) {
			nx.getDNA()[i] = (int)Math.round(xg.getDNA()[i] + ratio * (xg.getDNA()[i] - yg.getDNA()[i]));
			ny.getDNA()[i] = (int)Math.round(yg.getDNA()[i] - ratio * (xg.getDNA()[i] - yg.getDNA()[i]));
		}
		newGene[x] = nx;
		newGene[y] = ny;
	}

	public void geneMutation(Gene g) {
		double ratio = (Math.random() - 0.5) * 2 * this.ratioOfMutation;
		for (int i = 0; i < g.getDNA().length; i++) {
			if (Math.random() < ratio)
				g.getDNA()[i] = (int)Math.round(g.getDNA()[i] + ratio * Math.random() * g.getDNA()[i]);
		}
	}

	public double crossover() {
		GenePair[] A = new GenePair[gene.length];
		for (int i = 0; i < gene.length; i++) {
			double f = gene[i].calcuateFitness();
			// small better than large.
			A[i] = new GenePair(f, gene[i]);
		}
		Arrays.sort(A);
		double bestF = A[0].f;
		Random intRand = new Random();
		double[] copyP = new double[A.length];
		double sumF = 0;

		for (int i = 0; i < A.length; i++) {
			sumF += A[i].f;
		}

		for (int i = 0; i < A.length; i++) {
			copyP[i] = (A[i].f) / sumF;
		}
		
		int bestClone = poolSize / 10;

		for (int i = 0; i < bestClone; i++) {
			newGene[i] = A[0].gene.clone();
		}

		for (int i = 0, j = bestClone; j < A.length; i = (i + 1) % A.length) {
			double p = Math.random() * 10;
			if (copyP[i] > p) {
				newGene[j++] = A[i].gene.clone();
			}
		}
		for (int i = 0; i < A.length; i++) {
			gene[i] = newGene[i];
		}
		int reserve = 0;
		for (int i = 0; i < A.length; i++) {
			if (Math.random() < this.probabilityOfCrossover) {
				int x = Math.abs(intRand.nextInt()) % (A.length - reserve)
						+ reserve;
				geneCrossover(i, x, gene[i], gene[x]);
			}
		}

		for (int i = 0; i < A.length; i++) {
			gene[i] = newGene[i];
		}

		for (int i = reserve; i < A.length; i++) {
			double p = Math.random();
			if (p < this.probabilityOfMutation) {
				geneMutation(gene[i]);
			}
		}

		for (int i = 0; i < A.length; i++) {
			gene[i].on();
		}

		return bestF;
	}
}