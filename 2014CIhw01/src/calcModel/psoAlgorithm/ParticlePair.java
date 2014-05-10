package calcModel.psoAlgorithm;

public class ParticlePair implements Comparable<ParticlePair> {
	double f;
	public Particle sand;

	public ParticlePair(double f, Particle sand) {
		this.f = f;
		this.sand = sand;
	}

	@Override
	public int compareTo(ParticlePair o) {
		if (f == o.f)
			return 0;
		return f < o.f ? -1 : 1;
	}
}
