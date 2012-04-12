package util.random;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import util.random.MersenneTwisterFast;


public class MersenneTwisterFastTest {
	
	@Test(timeout=500)
	public void testIt() {
		int MAX = 10, SAMPLES = 100000;
		int[] slots = new int[MAX];
		
		MersenneTwisterFast rnd = new MersenneTwisterFast();
		for (int i = 0; i < MAX * SAMPLES; i++) {
			int t = rnd.nextInt(MAX);
			slots[t] = slots[t] + 1;
		}
		
		int lowerBound = (int) (SAMPLES * 0.97);
		int upperBound = (int) (SAMPLES * 1.03);
		for (int i = 0; i < slots.length; i++) {
			int j = slots[i];
			assertThat(j > lowerBound, is(true));
			assertThat(j < upperBound, is(true));
		}
	}

}
