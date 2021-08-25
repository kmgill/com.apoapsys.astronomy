package com.apoapsys.astronomy.coords;

import org.junit.Assert;
import org.junit.Test;

import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.geo.CoordinateTypeEnum;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.orbits.vsop87.EarthVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.JupiterVSOPOrbit;
import com.apoapsys.astronomy.time.DateTime;

public class CoordinateConversionUtilTest {
	
	@Test
	public void testConvertVectorToEquatorialWithJavascriptNumbers() {
		
		DateTime dt = new DateTime(2456712.500000);
		EarthVSOPOrbit earthOrbit = new EarthVSOPOrbit();
		JupiterVSOPOrbit jupiterOrbit = new JupiterVSOPOrbit();
		
		EclipticOrbitPosition earthPosition = earthOrbit.positionAtTime(dt);
		EclipticOrbitPosition jupiterPosition = jupiterOrbit.positionAtTime(dt);
		
		double delta = 0.000000000001; // Accuracy to 14.9597871 centimeters or 3.6x10^-9 arcseconds
		Assert.assertEquals(-1.7254729280184944, jupiterPosition.getPosition().x, delta);
		Assert.assertEquals(0.0181877707278726, jupiterPosition.getPosition().y, delta);
		Assert.assertEquals(-4.9181641729744845, jupiterPosition.getPosition().z, delta);
		
		Assert.assertEquals(-0.8975604471526817, earthPosition.getPosition().x, delta);
		Assert.assertEquals(-0.000013064955197443681, earthPosition.getPosition().y, delta);
		Assert.assertEquals(-0.41674676685769696, earthPosition.getPosition().z, delta);
		
		Assert.assertEquals(5.21209424825171, jupiterPosition.getEclipticCoordinates().getRange(), delta);
		Assert.assertEquals(0.1999358735114157, jupiterPosition.getEclipticCoordinates().getLatitude().getDegrees(),  delta);
		Assert.assertEquals(109.33274731848972, jupiterPosition.getEclipticCoordinates().getLongitude().getDegrees(), delta);
		
		Assert.assertEquals(0.9895921504084187, earthPosition.getEclipticCoordinates().getRange(), delta);
		Assert.assertEquals(-0.0007564397030203865, earthPosition.getEclipticCoordinates().getLatitude().getDegrees(), delta);
		Assert.assertEquals(155.09406151165794, earthPosition.getEclipticCoordinates().getLongitude().getDegrees(), delta);
		
		EquatorialRightAscension equatorial = CoordinateConversionUtil.convertVectorToEquatorial(dt, jupiterPosition.getPosition(), earthPosition.getPosition());
		Assert.assertEquals(101.14009237990668, equatorial.getRightAscension().getDegrees(), delta);
		Assert.assertEquals(23.268742954877958, equatorial.getDeclination().getDegrees(), delta);
		
		Coordinate latitude = new Coordinate(41.8999967, CoordinateTypeEnum.LATITUDE);
		Coordinate longitude = new Coordinate(-71.090278, CoordinateTypeEnum.LONGITUDE);
		
		Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(equatorial, dt, latitude, longitude);
		
		
		Assert.assertEquals(65.85283815706163, horizon.getAltitude().getDegrees(), delta);
		Assert.assertEquals(134.74731853735994, horizon.getAzimuth().getDegrees(), delta);
		
	}
	
	

	@Test
	public void testConvertVectorToEquatorialWithJPLNumbers() {
		
		DateTime dt = new DateTime(2456712.500000);
		EarthVSOPOrbit earthOrbit = new EarthVSOPOrbit();
		JupiterVSOPOrbit jupiterOrbit = new JupiterVSOPOrbit();
		
		EclipticOrbitPosition earthPosition = earthOrbit.positionAtTime(dt);
		EclipticOrbitPosition jupiterPosition = jupiterOrbit.positionAtTime(dt);
		
		double delta = 0.75; 

		EquatorialRightAscension equatorial = CoordinateConversionUtil.convertVectorToEquatorial(dt, jupiterPosition.getPosition(), earthPosition.getPosition());
		Assert.assertEquals(101.3522157, equatorial.getRightAscension().getDegrees(), delta);
		Assert.assertEquals(23.2572141, equatorial.getDeclination().getDegrees(), delta);
		
		Coordinate latitude = new Coordinate(41.9, CoordinateTypeEnum.LATITUDE);
		Coordinate longitude = new Coordinate(-71.090278, CoordinateTypeEnum.LONGITUDE);
		
		Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(equatorial, dt, latitude, longitude);
		
		
		Assert.assertEquals(65.6029, horizon.getAltitude().getDegrees(), delta);
		Assert.assertEquals(134.0161, horizon.getAzimuth().getDegrees(), delta);
		
	}
	
}
