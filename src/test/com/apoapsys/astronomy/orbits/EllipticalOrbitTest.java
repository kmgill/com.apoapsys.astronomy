package com.apoapsys.astronomy.orbits;

import org.junit.Assert;
import org.junit.Test;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.time.EpochEnum;

public class EllipticalOrbitTest {
	
	@Test
	public void testEarthOrbit() {
		Ephemeris ephemeris = new Ephemeris();
		ephemeris.semiMajorAxis = 1.00000261;
		ephemeris.longitudeOfPerihelion = 102.947;
		ephemeris.eccentricity = 0.0167;
		ephemeris.inclination = new Angle(0.0001);
		ephemeris.ascendingNode = new Angle(348.73936);
		ephemeris.argOfPeriapsis = new Angle(114.20783);
		ephemeris.meanAnomalyAtEpoch = 357.51716;
		ephemeris.period = 1.000017421 * 365.25;
		ephemeris.meanMotion = 1 / ephemeris.period;
		ephemeris.pericenterDistance = ephemeris.semiMajorAxis * (1 - ephemeris.eccentricity);
		ephemeris.derivativeOfMeanMotion = 0;
		ephemeris.epoch = EpochEnum.J2000.julianDay();
		
		EllipticalOrbit orbit = new EllipticalOrbit(ephemeris);
		
		//The Julian date for CE  2014 March 16 00:00:00.0 UT is
		//JD 2456732.500000
		DateTime dt = new DateTime(2456732.500000, EpochEnum.J2000);
		OrbitPosition pos = orbit.positionAtTime(dt);
		
		double delta = 0.0000001;
		Assert.assertEquals(-0.9910441961420376, pos.getPosition().x, delta);
		Assert.assertEquals(-1.9311840897702505e-7, pos.getPosition().y, delta);
		Assert.assertEquals(-0.08450167999929856, pos.getPosition().z, delta);
		
		Assert.assertEquals(1.2439067131945116, pos.getEccentricAnomaly().getRadians(), delta);
		Assert.assertEquals(1.2280910505942546, pos.getMeanAnomaly().getRadians(), delta);
		Assert.assertEquals(1.2597656707728084, pos.getTrueAnomaly().getRadians(), delta);

	}
	
}
