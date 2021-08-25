package com.apoapsys.astronomy.sun;

import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.geo.CoordinateTypeEnum;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Euler;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.time.DateTime;

public class SunUtil {
	
	
	public static SunPosition getSunPosition(DateTime dt) {
		return getSunPosition(dt, null, null);
	}
	
	public static SunPosition getSunPosition(DateTime dt, Coordinate longitude, Coordinate latitude) {
		
		if (longitude == null) {
			longitude = new Coordinate(0.0, CoordinateTypeEnum.LONGITUDE);
		}
		if (latitude == null) {
			latitude = new Coordinate(0.0, CoordinateTypeEnum.LATITUDE);
		}
		
		
		double jd = dt.getJulianDay();
		double jc = dt.getJulianCentury();
		
		double n = dt.getJulianDay() - 2451545.0; // Number of days from J2000.0
		double L = 280.460 + 0.9856474 * n; // Mean longitude of the Sun,
		
		double g = 357.528 + 0.9856003 * n; // Mean anomaly of the sun
		L = MathExt.clamp(L, 360);
		g = MathExt.clamp(g, 360);

		double _g = MathExt.radians(g);
		double _L = MathExt.radians(L);

		double eclipticLongitude = L + 1.915 * Math.sin(_g) + 0.020 * Math.sin(2 * _g);
		double eclipticLatitude = 0;

		// Distance of the sun in astronomical units
		double R = 1.00014 - 0.01671 * Math.cos(_g) - 0.00014 * Math.cos(2 * _g);

                // Obliquity of the ecliptic
		double e = 23.439 - 0.0000004 * n;

		double eccentricityEarthOrbit = 0.016708634 - jc * (0.000042037 + 0.0000001267 * jc);

		double _eclipticLongitude = MathExt.radians(eclipticLongitude);
		double _eclipticLatitude = MathExt.radians(eclipticLatitude);

		double _e = MathExt.radians(e);

		double N = dt.getDayNumber();

		double rightAscension = Math.atan((Math.sin(_eclipticLongitude) * Math.cos(_e) - Math.tan(_eclipticLatitude) * Math.sin(_e)) / Math.cos(_eclipticLongitude));
		double declination = Math.atan((Math.sin(_e) * Math.sin(_eclipticLongitude) * Math.cos(_eclipticLatitude) + Math.cos(_e) * Math.sin(_eclipticLatitude)));
		double o = -e * (Math.cos(MathExt.radians((360.0 / 365.0) * (N + 10.0))));


		double obliquityCorrection = e + 0.00256 * Math.cos(MathExt.radians(125.04 - 1934.136 * jc));
		double y = Math.pow(Math.tan(MathExt.radians(obliquityCorrection) / 2.0), 2);

		double equationOfTime = MathExt.degrees(y * Math.sin(2.0 * _L) - 2.0 * eccentricityEarthOrbit * Math.sin(_g) + 4.0 * eccentricityEarthOrbit * y * Math.sin(_g) * Math.cos(2.0 * _L)
                                - 0.5 * y * y * Math.sin(4.0 * _L) - 1.25 * eccentricityEarthOrbit * eccentricityEarthOrbit * Math.sin(2.0 * _g)) * 4.0; // in
                                                                                                                                                                                                                                                                                                // minutes
                                                                                                                                                                                                                                                                                                // of
		double tod = dt.getJulianMinutes();
		
		double trueSolarTime = (tod + equationOfTime + 4.0 * longitude.getDecimal() - 60.0 * 0 /* 0==tz*/);

		double ha = 0;
		if (trueSolarTime / 4.0 < 0.0)
			ha = trueSolarTime / 4.0 + 180.0;
		else
			ha = trueSolarTime / 4.0 - 180.0;
		
		


		double rotateY = rightAscension + (ha - longitude.getDecimal());
		double rotateX = declination;

		Vector xyz = new Vector(R * 149598000000.0, 0, 0); // R (AU) in meters
		
		Euler euler = new Euler(MathExt.radians(rotateX), MathExt.radians(-rotateY), MathExt.radians(o), "XYZ");
		xyz.applyEuler(euler);
		
		double zenithAngle = MathExt.degrees(Math.acos(Math.sin(MathExt.radians(latitude.getDecimal()))*Math.sin(MathExt.radians(declination))+Math.cos(MathExt.radians(latitude.getDecimal()))*Math.cos(MathExt.radians(declination))*Math.cos(MathExt.radians(ha))));
		
		double azimuthAngle = 0;
		if (ha > 0) {
			azimuthAngle = MathExt.degrees(Math.acos(((Math.sin(MathExt.radians(latitude.getDecimal()))*Math.cos(MathExt.radians(zenithAngle)))-Math.sin(MathExt.radians(declination)))/(Math.cos(MathExt.radians(latitude.getDecimal()))*Math.sin(MathExt.radians(zenithAngle)))))+180;
		} else {
			azimuthAngle = 540-MathExt.degrees(Math.acos(((Math.sin(MathExt.radians(latitude.getDecimal()))*Math.cos(MathExt.radians(zenithAngle)))-Math.sin(MathExt.radians(declination)))/(Math.cos(MathExt.radians(latitude.getDecimal()))*Math.sin(MathExt.radians(zenithAngle)))));
		}
		azimuthAngle = MathExt.clamp(azimuthAngle, 360);
		
		
		double haSunrise = MathExt.degrees(Math.acos(Math.cos(MathExt.radians(90.833))/(Math.cos(MathExt.radians(latitude.getDecimal()))*Math.cos(MathExt.radians(declination)))-Math.tan(MathExt.radians(latitude.getDecimal())) * Math.tan(MathExt.radians(declination))));
		double sunRiseTimeUTC = 720 - (4.0 * (longitude.getDecimal() + haSunrise)) - equationOfTime;
		double sunSetTimeUTC = 720 - (4.0 * (longitude.getDecimal() - haSunrise)) - equationOfTime;
		
		sunRiseTimeUTC = Math.floor(jd) + (sunRiseTimeUTC / 60 / 24) - .5;
		sunSetTimeUTC = Math.floor(jd) + (sunSetTimeUTC / 60 / 24) - .5;
		
		
		SunPosition sunPosition = new SunPosition(dt,
				Angle.fromRadians(rightAscension),
				Angle.fromRadians(ha),
				trueSolarTime,
				equationOfTime,
				Angle.fromRadians(declination),
				Angle.fromDegrees(zenithAngle),
				Angle.fromDegrees(90 - zenithAngle),
				Angle.fromDegrees(correctedSolarElevation((90 - zenithAngle))),
				Angle.fromDegrees(azimuthAngle),
				sunRiseTimeUTC,
				sunSetTimeUTC,
				R,
				latitude,
				longitude,
				xyz,
				euler);
		
		return sunPosition;
		
		
	}
	
	
	protected static double approxAtmosphericRefraction(double solarElevation) {
		double refr = 0;
		if (solarElevation > 85) {
			refr = 0;
		} else if (solarElevation > 5) {
			refr = 58.1 / Math.tan(MathExt.radians(solarElevation)) - 0.07 / (Math.pow(Math.tan(MathExt.radians(solarElevation)), 3)) + 0.000086 / (Math.pow(Math.tan(MathExt.radians(solarElevation)), 5));
		} else if (solarElevation > -0.575) {
			refr = 1735 + solarElevation *(-518.2 + solarElevation * (103.4 + solarElevation * (-12.79+solarElevation*0.711)));
		} else {
			refr = -20.772 / Math.tan(MathExt.radians(solarElevation));
		}
		return refr / 3600.0;
	}
	
	protected static double correctedSolarElevation(double solarElevation) {
		return solarElevation + approxAtmosphericRefraction(solarElevation);
	}
}
