package com.apoapsys.astronomy.containers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContainersTest {
	
	@Test
	public void testLimitedLengthQueue() {
		
		LimitedLengthLinkedListQueue<Integer> queue = new LimitedLengthLinkedListQueue<>(3);
		queue.add(1);
		queue.add(2);
		queue.add(3);
		queue.add(4);
		queue.add(5);
		
		assertEquals(3, queue.size());
		assertEquals(new Integer(5), queue.get(0));
		assertEquals(new Integer(4), queue.get(1));
		assertEquals(new Integer(3), queue.get(2));
		
	}
	
	
}
