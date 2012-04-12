package util.random;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.google.common.collect.Maps;

public class StandardDistributedRandomTest {

	private static ConcurrentMap<Integer, AtomicLong> keyDistribution = Maps.newConcurrentMap();
	
	@Test()
	public void testIt() {
		StandardDistributedRandom rnd = new StandardDistributedRandom();
		for (int i = 0; i < 100; i++) {
			Integer key = rnd.nextInt(5, 5);
			AtomicLong cnt = keyDistribution.putIfAbsent(key, new AtomicLong(1));
			if (cnt != null)
				cnt.incrementAndGet();
		}
		
		System.out.println("key distribution");
		
		
		for (Integer key : keyDistribution.keySet()) {
			System.out.println(key + "," + keyDistribution.get(key));
		}

		
	}
}
