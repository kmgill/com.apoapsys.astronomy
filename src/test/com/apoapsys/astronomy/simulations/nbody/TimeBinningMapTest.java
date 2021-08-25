package com.apoapsys.astronomy.simulations.nbody;

import junit.framework.Assert;

import org.junit.Test;

import com.apoapsys.astronomy.math.Vector;

public class TimeBinningMapTest {
	
	@Test
	public void testTimeBinningVectorMap() {
		TimeBinningVectorMap map = new TimeBinningVectorMap();
		
		VectorState state0 = new VectorState("Moon", 0, new Vector(0, 0, 0));
		map.put(state0);
		
		VectorState state1 = map.get("Moon", state0.getTime());
		Assert.assertNotNull(state1);
		Assert.assertEquals(state0.getTime(), state1.getTime());
		
		
		VectorState state2 = map.get("Sun", 0);
		Assert.assertNull(state2);
		
		VectorState state3 = map.get("Moon", 1);
		Assert.assertNull(state3);
		
		VectorState state4 = new VectorState("Moon", 1, new Vector(0, 0, 0));
		map.put(state4);
		
		VectorState state5 = map.get("Moon", state4.getTime());
		Assert.assertNotNull(state5);
		Assert.assertEquals(state4.getTime(), state5.getTime());
		
		
		VectorState state6 = new VectorState("Moon", 1914, new Vector(0, 0, 0));
		map.put(state6);
		
		VectorState state7 = map.get("Moon", state6.getTime());
		Assert.assertNotNull(state7);
		Assert.assertEquals(state6.getTime(), state7.getTime());
		
		VectorState state8 = new VectorState("Moon", 251914, new Vector(0, 0, 0));
		map.put(state8);
		
		VectorState state9 = map.get("Moon", state8.getTime());
		Assert.assertNotNull(state9);
		Assert.assertEquals(state8.getTime(), state9.getTime());
	}
	
}
