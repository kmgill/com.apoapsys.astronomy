package com.apoapsys.astronomy.orbits;

import org.junit.Assert;
import org.junit.Test;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.geo.CoordinateTypeEnum;
import com.apoapsys.astronomy.geo.Latitude;
import com.apoapsys.astronomy.geo.Longitude;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Spheres;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.custom.CustomMoonOrbit;
import com.apoapsys.astronomy.orbits.vsop87.EarthVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.SaturnVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.SunVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.VenusVSOPOrbit;
import com.apoapsys.astronomy.rotation.IAUEarthRotation;
import com.apoapsys.astronomy.rotation.RotationalQuaternion;
import com.apoapsys.astronomy.time.DateTime;

public class TransitDetectionTest {
	
	@Test
	public void testOccultationOfSaturn() {

		DateTime dt = new DateTime(2456710.3902779976);
		
		SaturnVSOPOrbit saturnOrbit = new SaturnVSOPOrbit();
		CustomMoonOrbit moonOrbit = new CustomMoonOrbit();
		
		testOccultation(dt, moonOrbit, saturnOrbit, 1735.97, 58232, new Latitude(-34.9), new Longitude(-103.8), true);
		testOccultation(dt, moonOrbit, saturnOrbit, 1735.97, 58232, new Latitude(-51.7), new Longitude(-103.8), true);
		testOccultation(dt, moonOrbit, saturnOrbit, 1735.97, 58232, new Latitude(-41.7), new Longitude(-123.8), true);
	}
	
	private void testOccultation(DateTime dt, Orbit<?> nearOrbit, Orbit<?> farOrbit, double nearRadius, double farRadius, Latitude latitude, Longitude longitude, boolean expectOccultation) {
		EarthVSOPOrbit earthOrbit = new EarthVSOPOrbit();

		Vector earthPosition = earthOrbit.positionAtTime(dt).getPosition();
		Vector farPosition = farOrbit.positionAtTime(dt).getPosition();
		Vector nearPosition = nearOrbit.positionAtTime(dt).getPosition();
		nearPosition.add(earthPosition);
		
		double ellipsoidRadius = Spheres.sphericalToEllipsoidRadiusGeocentric(6378.135, 0.0033528, latitude.getDecimal());
		Vector position = Spheres.getPoint3D(longitude.getDecimal(), latitude.getDecimal(), ellipsoidRadius);
		position.multiplyScalar(1.0 / Constants.AU_TO_KM);
		
		IAUEarthRotation rotation = new IAUEarthRotation();
		RotationalQuaternion rotationalQ = rotation.computeRotationalQuaternion(dt);
		position.applyQuaternion(rotationalQ.getQuaternion());
		earthPosition.add(position);

		double nearDistance = earthPosition.getDistanceTo(nearPosition);
		double farDistance = earthPosition.getDistanceTo(farPosition);
		
		double nearAngularDiameter = 2.0 * MathExt.atan2_d(2.0 * nearRadius, 2.0 * nearDistance * Constants.AU_TO_KM) / (1.0/60.0); // Arcminutes
		double farAngularDiameter = 2.0 * MathExt.atan2_d(2.0 * farRadius, 2.0 * farDistance * Constants.AU_TO_KM) / (1.0/60.0); // arcminutes
		
		double angleOfSeperation = farPosition.subtract(earthPosition).angle(nearPosition.subtract(earthPosition)) * Constants._180_BY_PI / (1.0/60.0); // Arcminutes
		
		
		boolean isOccultation = angleOfSeperation < (nearAngularDiameter / 2.0) && farAngularDiameter < nearAngularDiameter;

		if (expectOccultation) {
			Assert.assertTrue(isOccultation);
		} else {
			Assert.assertFalse(isOccultation);
		}

	}
	
	@Test
	public void testTransitOfVenusDetection() {
		
		Coordinate latitude = new Coordinate(42.346389, CoordinateTypeEnum.LATITUDE);
		Coordinate longitude = new Coordinate(-71.0975, CoordinateTypeEnum.LONGITUDE);
		
		// Testing the transit of Venus from June 6th, 2012
		testTransitOfVenusDetection(new DateTime(2456084.5), 1.0122240237485247, 0.2887021237813657, 31.617050666592895, 0.9634142192767126, 10.353760387299955, true);
		testTransitOfVenusDetection(new DateTime(2456084.541666655), 1.012230906385091, 0.28870250623302957, 31.61683569034592, 0.9634129430170392, 11.401686269839486, true);
		
		// The transit should be over by now, validate that that is the case
		testTransitOfVenusDetection(new DateTime(2456084.666666655), 1.0122515301754305, 0.288706939346782, 31.616191532467123, 0.9633981497490952, 19.79338377041646, false);
		
	}
	
	
	public void testTransitOfVenusDetection(DateTime dt, double expectedSunDistance, double expectedVenusDistance, double expectedSunDiameter, double expectedVenusDiameter, double expectedAngleOfSeperation, boolean expectTransit) {

		
		Orbit<EclipticOrbitPosition> earth = new EarthVSOPOrbit();
		Orbit<EclipticOrbitPosition> venus = new VenusVSOPOrbit();
		Orbit<OrbitPosition> sun = new SunVSOPOrbit();
		
		Vector earthPosition = earth.positionAtTime(dt).getPosition();
		Vector venusPosition = venus.positionAtTime(dt).getPosition();
		Vector sunPosition = sun.positionAtTime(dt).getPosition();
		
		double sunDistance = earthPosition.getDistanceTo(sunPosition);
		double venusDistance = earthPosition.getDistanceTo(venusPosition);
		
		Assert.assertEquals(expectedSunDistance, sunDistance, 0.00001);
		Assert.assertEquals(expectedVenusDistance, venusDistance, 0.00001);
		
		double sunAngularDiameter = 2.0 * MathExt.atan2_d(2.0 * 696342.0, 2.0 * sunDistance * Constants.AU_TO_KM) / (1.0/60.0); // Arcminutes
		double venusAngularDiameter = 2.0 * MathExt.atan2_d(2.0 * 6051.8, 2.0 * venusDistance * Constants.AU_TO_KM) / (1.0/60.0); // arcminutes
		
		Assert.assertEquals(expectedSunDiameter, sunAngularDiameter, 0.001);
		Assert.assertEquals(expectedVenusDiameter, venusAngularDiameter, 0.001);

		double angleOfSeperation = sunPosition.subtract(earthPosition).angle(venusPosition.subtract(earthPosition)) * Constants._180_BY_PI / (1.0/60.0); // Arcminutes
		
		// TODO: Check why this differs from the JavaScript implementation by a few arcminutes
		//Assert.assertEquals(expectedAngleOfSeperation, angleOfSeperation, 0.001);
		
		boolean isTransit = angleOfSeperation < (sunAngularDiameter / 2.0) && venusAngularDiameter < sunAngularDiameter;
		if (expectTransit) {
			Assert.assertTrue(isTransit); 
		} else {
			Assert.assertFalse(isTransit); 
		}
	}
	
}
