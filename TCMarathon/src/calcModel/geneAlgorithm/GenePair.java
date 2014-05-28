package calcModel.geneAlgorithm;

public class GenePair implements Comparable<GenePair> {
	double f;
	public Gene gene;

	public GenePair(double f, Gene gene) {
		this.f = f;
		this.gene = gene;
	}

	@Override
	public int compareTo(GenePair o) {
		if (f == o.f)
			return 0;
		return f > o.f ? -1 : 1;
	}
}
