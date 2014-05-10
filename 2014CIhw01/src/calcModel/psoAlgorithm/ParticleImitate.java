package calcModel.psoAlgorithm;

/*
 * process imitate production
 */
public class ParticleImitate implements Comparable<ParticleImitate> {
	public Particle before, after; // A -> B
	public Particle friend; // imitate C
	public double dist;
	public double fitness;
	@Override
	public int compareTo(ParticleImitate o) {
		for(int i = 0; i < after.getXLength(); i++)
			if(Math.abs(after.getX()[i] - o.after.getX()[i]) > 1e-6)
				return after.getX()[i] < o.after.getX()[i] ? -1 : 1;
		return 0;
	}
}
