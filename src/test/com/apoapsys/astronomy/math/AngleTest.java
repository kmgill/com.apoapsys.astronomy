package com.apoapsys.astronomy.math;

import org.junit.Assert;
import org.junit.Test;

public class AngleTest {
	
	@Test
	public void testDMSToDecimal() {
		Angle a = new Angle(182, 31, 27);
		Assert.assertEquals(182.5241667, a.getDegrees(), 0.0000001);
	}
	
	@Test
	public void testDecimalToDMST() {
		Angle a = new Angle(182.5241667);
		Assert.assertEquals(182d, a.getDMSDegrees(), 0);
		Assert.assertEquals(31, a.getDMSMinutes(), 0);
		Assert.assertEquals(27, a.getDMSSeconds(), 0.001);
	}
	
}
