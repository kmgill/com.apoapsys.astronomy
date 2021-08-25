package com.apoapsys.astronomy.perlin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NoiseModifierTest {
	
	@Test
	public void testNoiseRangeModifier() {
		NoiseRangeModifier modifier = new NoiseRangeModifier(-1.0, 1.0, 0.0, 1.0);
		
		assertEquals(0.0, modifier.onTotal(.5), 0.00001);
		
		
	}
	
}
