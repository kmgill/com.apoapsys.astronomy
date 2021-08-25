package com.apoapsys.astronomy.bodies;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class MajorBodiesLoaderTest {
	
	@Test
	public void testLoad() throws Exception {
		
		List<Body> bodies = MajorBodiesLoader.load();
		
		Assert.assertTrue(bodies.size() > 0);
	}
	
}
