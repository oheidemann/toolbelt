package util.random;

public class StandardDistributedRandom {

	private final MersenneTwisterFast rnd = new MersenneTwisterFast();
	
	public int nextInt(int def, int range) {
		return rnd.nextInt(def) + rnd.nextInt(def);
	}
}
