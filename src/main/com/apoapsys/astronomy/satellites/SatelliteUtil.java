package com.apoapsys.astronomy.satellites;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.geo.CoordinateTypeEnum;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Spheres;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

/**
 * Temporary home for many of these methods
 * 
 * @author kmgill
 * 
 */
public class SatelliteUtil {

	/**
	 * Based largely on code from
	 * http://www.satellite-calculations.com/TLETracker/SatTracker.htm
	 * 
	 * @param dt
	 * @param orbit
	 */
	public static SatellitePosition calculateSatellitePosition(DateTime dt, EllipticalOrbit orbit) {

		OrbitPosition position = orbit.positionAtTime(dt);

		double E = position.getEccentricAnomaly().getRadians();
		double M = position.getMeanAnomaly().getRadians();
		double trueAnomaly = position.getTrueAnomaly().getRadians();

		Ephemeris ephemeris = orbit.getEphemeris();

		double semiMajorAxis = ephemeris.semiMajorAxis;
		double arg_per = ephemeris.argOfPeriapsis.getRadians();
		double RAAN = ephemeris.rightAscension.getRadians();
		double i = ephemeris.inclination.getRadians();
		double e = ephemeris.eccentricity;

		double Epoch_now = dt.getDayNumber();
		double Epoch_start = ephemeris.epochStart;
		double Earth_equatorial_radius = 6378.135;

		double first_derative_mean_motion = ephemeris.derivativeOfMeanMotion;
		double Satellite_rev_sidereal_day = ephemeris.meanMotion;

		double TCdecimal = (1440 / ((1 * Satellite_rev_sidereal_day) + (first_derative_mean_motion * (Epoch_now - Epoch_start)))) / 60; // Period
																																		// in
																																		// hours

		double RangeA = Math.pow((6028.9 * (TCdecimal * 60)), (2 / 3));

		double apogee = RangeA * (1 + e * 1); // apogee
		double perigee = RangeA * (1 - e * 1); // perigee
		double semimajoraxsis = (1 * apogee + 1 * perigee) / 2; // semimajoraxsis

		double perigee_perturbation = (Epoch_now - Epoch_start) * 4.97 * Math.pow((Earth_equatorial_radius / (1 * semimajoraxsis)), 3.5) * (5 * Math.cos(i) * Math.cos(i) - 1)
				/ ((1 - e * e) * (1 - e * e));

		// perturbation of ascending node

		double ascending_node_perturbation = (Epoch_now - Epoch_start) * 9.95 * Math.pow((Earth_equatorial_radius / (1 * semimajoraxsis)), 3.5) * Math.cos(i) / ((1 - e * e) * (1 - e * e));

		// perbutation of perigee
		arg_per = arg_per + perigee_perturbation;
		RAAN = RAAN - ascending_node_perturbation;

		arg_per = MathExt.radians(arg_per);
		RAAN = MathExt.radians(RAAN);

		double X0 = 1.0 * semiMajorAxis * (Math.cos(E) - e); // =
																// r*Cos(trueanomaly)
		double Y0 = 1.0 * semiMajorAxis * Math.sqrt(1 - e * e) * Math.sin(E); // =
																				// r*sin
																				// (trueanomaly)
		double r = Math.sqrt(X0 * X0 + Y0 * Y0); // distance

		X0 *= Constants.AU_TO_KM;
		Y0 *= Constants.AU_TO_KM;
		r *= Constants.AU_TO_KM;

		double Px = Math.cos(arg_per) * Math.cos(RAAN) - Math.sin(arg_per) * Math.sin(RAAN) * Math.cos(i);
		double Py = Math.cos(arg_per) * Math.sin(RAAN) + Math.sin(arg_per) * Math.cos(RAAN) * Math.cos(i);
		double Pz = Math.sin(arg_per) * Math.sin(i);

		double Qx = -Math.sin(arg_per) * Math.cos(RAAN) - Math.cos(arg_per) * Math.sin(RAAN) * Math.cos(i);
		double Qy = -Math.sin(arg_per) * Math.sin(RAAN) + Math.cos(arg_per) * Math.cos(RAAN) * Math.cos(i);
		double Qz = Math.cos(arg_per) * Math.sin(i);

		double x = Px * X0 + Qx * Y0;
		double y = Py * X0 + Qy * Y0;
		double z = Pz * X0 + Qz * Y0;

		double dec = Math.atan2(z, Math.sqrt(x * x + y * y));
		double ra = Math.atan2(y, x);
		ra = ra % (2 * Math.PI);

		double gmst = dt.getGMST2();

		double lon = Math.atan2(y, x) - (MathExt.radians(gmst));
		lon = MathExt.clamp(lon, 2 * Math.PI);

		if (lon > Math.PI) {
			lon = -1 * (2 * Math.PI - lon);
		}

		double lat = Math.atan2(z, Math.sqrt(x * x + y * y));

		double radius = Spheres.sphericalToEllipsoidRadiusGeocentric(Earth_equatorial_radius, 0.0033528, lat);

		double altitude = Math.sqrt(x * x + y * y + z * z) - radius;
		double rh = (radius + altitude);
		double theta = Math.acos(radius / rh);
		double t = (6.284 * rh) * (Math.sqrt(rh / 398600)) / 60;

		double angularRange = radius * Math.tan(theta);
		double surfaceRange = 220 * (MathExt.degrees(theta));
		double visibilityTimeMinutes = ((2 * MathExt.degrees(theta)) / 360) * t;

		double maxAngularRange = .5 * Math.PI * 6378.135;
		if (angularRange > maxAngularRange) {
			angularRange = maxAngularRange;
		}

		//@formatter:off
		SatellitePosition satPosition = new SatellitePosition(new EquatorialRightAscension(Angle.fromRadians(ra / 15), Angle.fromRadians(dec)),
																new Coordinate(MathExt.degrees(lat), CoordinateTypeEnum.LATITUDE),
																new Coordinate(MathExt.degrees(lon), CoordinateTypeEnum.LONGITUDE),
																altitude, 
																angularRange, 
																surfaceRange, 
																visibilityTimeMinutes,
																position.getEccentricAnomaly(), 
																position.getMeanAnomaly(),
																position.getTrueAnomaly(), 
																new Vector(x, y, z));
		//@formatter:on

		return satPosition;
	}

	public static SatelliteHorizonPosition getPositionAzimuthal(DateTime dt, EllipticalOrbit orbit, Coordinate latitude, Coordinate longitude) {

		SatellitePosition satPosition = calculateSatellitePosition(dt, orbit);

		double dlon = longitude.getDecimal();
		double dlat = latitude.getDecimal();

		double lon = MathExt.radians(longitude.getDecimal());
		double lat = MathExt.radians(latitude.getDecimal());

		double ra = satPosition.getPosition().getRightAscension().getRadians();
		double dec = satPosition.getPosition().getDeclination().getRadians();

		double gmst = dt.getGMST2();
		double lst = gmst + (dlon / 15);

		double ha = lst - (((MathExt.degrees(ra)) / 15));
		double f = 0.0033528;
		double e2 = 2 * f - f * f;
		// double
		// C=1/Math.sqrt(1+0.0033528*(0.0033528-2)*Math.sin(lat)*Math.sin(lat)
		// );
		double omega = MathExt.radians(1 * gmst + 1 * dlon);

		double Re = 6378.135;
		double C = 1 / Math.sqrt(1 - e2 * Math.pow(Math.sin(lat), 2));
		double S = (1 - 0.0033528) * (1 - 0.0033528) * C;
		double R = Re * Math.cos(lat);
		double a = Re;

		double x_ = a * C * Math.cos(lat) * Math.cos(omega);
		double y_ = a * C * Math.cos(lat) * Math.sin(omega);
		double z_ = 6378.135 * S * Math.sin(lat);

		double xs = satPosition.getEciVector().x;
		double ys = satPosition.getEciVector().y;
		double zs = satPosition.getEciVector().z;

		double xo = x_;
		double yo = y_;
		double zo = z_;

		double rx = xs - xo;
		double ry = ys - yo;
		double rz = zs - zo;

		double fi = MathExt.radians(1 * gmst + 1 * dlon);
		double rS = Math.sin(lat) * Math.cos(fi) * rx + Math.sin(lat) * Math.sin(fi) * ry - Math.cos(lat) * rz;
		double rE = -Math.sin(fi) * rx + Math.cos(fi) * ry;
		double rZ = Math.cos(lat) * Math.cos(fi) * rx + Math.cos(lat) * Math.sin(fi) * ry + Math.sin(lat) * rz;

		double range = Math.sqrt(rS * rS + rE * rE + rZ * rZ);
		double Elevation = Math.asin(rZ / range);
		double Azimuth = Math.atan(-rE / rS);

		if (rS > 0)
			Azimuth = Azimuth + Math.PI;
		if (Azimuth < 0)
			Azimuth = Azimuth + 2 * Math.PI;

		double alt = Elevation;
		double az = Azimuth;

		if (az < 0) {
			az += 2 * Math.PI;
		}

		//@formatter:off
		SatelliteHorizonPosition position = new SatelliteHorizonPosition(Angle.fromRadians(az),
																			Angle.fromRadians(alt),
																			Angle.fromDegrees(ha),
																			range,
																			gmst,
																			lst,
																			satPosition);
		//@formatter:on
		return position;
	}
	
	
	
	
	public static boolean isSatelliteInSunlight(DateTime dt, Vector satelliteECI, Vector sunECI) {
		
		satelliteECI.divideScalar(Constants.AU_TO_KM);
		Vector earthPos = satelliteECI.getInversed();
		
		satelliteECI = new Vector(0, 0, 0);
		
		sunECI.add(earthPos);
		
		double RE = 6378.135;
		double RS = 696342;
		
		double pE = satelliteECI.getDistanceTo(earthPos) * Constants.AU_TO_KM;
		double pS = satelliteECI.getDistanceTo(sunECI) * Constants.AU_TO_KM;
		double rS = earthPos.getDistanceTo(sunECI) * Constants.AU_TO_KM;
		
		double thetaE = Math.asin(RE / pE);
		double thetaS = Math.asin(RS / pS);
		
		double sunAngularSemiDiameter = MathExt.degrees(thetaS);
		double earthAngularSemiDiameter = MathExt.degrees(thetaE);
		
		satelliteECI.normalize();
		earthPos.normalize();
		sunECI.normalize();
		
		double dot = earthPos.dotProduct(sunECI);
		double angleOfSeperation = sunECI.clone().subtract(satelliteECI).angle(earthPos.clone().subtract(satelliteECI));
		
		double theta = Math.acos(dot / (pE * pS));
		
		boolean isEclipsed = (MathExt.degrees(angleOfSeperation) < (earthAngularSemiDiameter - sunAngularSemiDiameter));
		//boolean isEclipsed = false;
		
		if (thetaE > thetaS && theta  < (thetaE - thetaS)) {
			isEclipsed = true;
		}
		
		// Penumbral eclipse
		if (Math.abs(thetaE - thetaS) < theta && theta < (thetaE + thetaS)) {
			isEclipsed = true;
		}
		
		// Annular eclipse
		if (thetaS > thetaE && theta < (thetaS - thetaE)) {
			isEclipsed = true;
		}
		
		return !isEclipsed;
	}
	
}
