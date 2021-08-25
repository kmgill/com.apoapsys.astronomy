package com.apoapsys.astronomy.tle;

import junit.framework.Assert;

import org.junit.Test;

import com.apoapsys.astronomy.time.DateTime;

public class TLEParserTest {
	
	@Test
	public void testTleParse() throws TLEParseException {
		
		String tle = "ISS (ZARYA)\n" + 
					 "1 25544U 98067A   14071.55665703  .00023589  00000-0  39943-3 0  8548\n" + 
					 "2 25544  51.6469 218.5438 0002751 242.0531 238.2635 15.51424123876330";
		
		TLEEphemeris eph = TLEParser.parse(tle, new DateTime(2456729.321424));
		
		double delta = 0.0000001;
		Assert.assertEquals(0.00004539278001739879, eph.semiMajorAxis, 0.00001);
		Assert.assertEquals(0.0, eph.longitudeOfPerihelion);
		Assert.assertEquals(0.0002751, eph.eccentricity, delta);
		Assert.assertEquals(51.6469, eph.inclination.getDegrees(), delta);
		Assert.assertEquals(218.5438, eph.ascendingNode.getDegrees(), delta);
		Assert.assertEquals(242.0531, eph.argOfPeriapsis.getDegrees(), delta);
		Assert.assertEquals(238.2635, eph.meanAnomalyAtEpoch, delta);
		Assert.assertEquals(0.06445664812314271, eph.period, 0.001);
		Assert.assertEquals(15.51424123, eph.meanMotion, delta);
		Assert.assertEquals(0.0, eph.pericenterDistance, delta);
		Assert.assertEquals(0.00023589, eph.derivativeOfMeanMotion, delta);
		Assert.assertEquals(2456729.05665703, eph.epoch, delta);
		Assert.assertEquals(218.5438, eph.rightAscension.getDegrees(), delta);
		Assert.assertEquals(5185.55665703, eph.epochStart, delta);
		Assert.assertEquals("ISS (ZARYA)", eph.name);
		Assert.assertEquals("25544", eph.satelliteNumber);
		Assert.assertEquals("U", eph.classification);
		Assert.assertEquals(98, eph.launchYear);
		Assert.assertEquals(67, eph.launchNumberForYear);
		Assert.assertEquals("A", eph.launchPieceNumber);
		Assert.assertEquals(0.00039943, eph.dragTerm, delta);
		Assert.assertEquals("0", eph.ephemerisType);
		Assert.assertEquals(854, eph.elementNumber);
		Assert.assertEquals(87633, eph.revolutionNumber);
		Assert.assertEquals(false, eph.isDebris);
		Assert.assertEquals("8", eph.checksum);
		
	}
	
}
