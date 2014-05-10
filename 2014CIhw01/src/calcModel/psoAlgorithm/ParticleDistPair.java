package calcModel.psoAlgorithm;

public class ParticleDistPair implements Comparable<ParticleDistPair> {
	double d;
	public ParticlePair p;

	public ParticleDistPair(double d, ParticlePair p) {
		this.d = d;
		this.p = p;
	}

	@Override
	public int compareTo(ParticleDistPair o) {
		if (d == o.d)
			return 0;
		return d < o.d ? -1 : 1;
	}
}
