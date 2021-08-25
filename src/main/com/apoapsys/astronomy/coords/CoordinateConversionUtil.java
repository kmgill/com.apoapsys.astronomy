package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Matrix;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.time.DateTime;

public class CoordinateConversionUtil {
	
	protected static Vector toColumnVector(Angle mu, Angle v) {
		double x = MathExt.cos(mu.getRadians()) * MathExt.cos(v.getRadians());
		double y = MathExt.sin(mu.getRadians()) * MathExt.cos(v.getRadians());
		double z = MathExt.sin(v.getRadians());
		
		Vector columnVector = new Vector(x, y, z);
		return columnVector;
	}
	
	public static EquatorialHourAngle convertHorizonToEquatorial(Horizon h, Coordinate latitude) {
		
		Vector column = toColumnVector(h.getAzimuth(), h.getAltitude());
		
		double s = MathExt.sin(MathExt.radians(latitude.toDecimal()));
		double c = MathExt.cos(MathExt.radians(latitude.toDecimal()));
		
		Matrix m = new Matrix(-s, 0, c, 0,
								0, -1, 0, 0,
								c, 0, s, 0,
								0, 0, 0, 1);
		column.applyMatrix(m);
		
		double theta = MathExt.atan2(column.y, column.x);
		double psi = MathExt.asin(column.z);
		
		EquatorialHourAngle e = new EquatorialHourAngle(Angle.fromRadians(theta), Angle.fromRadians(psi));
		
		return e;
	}
	
	public static Horizon convertEquatorialToHorizon(EquatorialHourAngle e, Coordinate latitude) {
		return null;
	}
	
	public static Horizon convertEquatorialRightAscensionToHorizon(EquatorialRightAscension eq, DateTime dt, Coordinate latitude, Coordinate longitude) {
		double ra = eq.getRightAscension().getRadians();
		double dec = eq.getDeclination().getRadians();
		
		double lat = MathExt.radians(latitude.getDecimal());
		double lon = MathExt.radians(longitude.getDecimal());
		
		double lst = dt.getLMST(longitude);

		double ha = lst - (((MathExt.degrees(ra)) / 15.0));

		double H = MathExt.radians(ha * 15.0);
		double alt = Math.asin(Math.sin(dec) * Math.sin(lat) + Math.cos(dec) * Math.cos(lat) * Math.cos(H));
		//double az = Math.asin(-Math.sin(H) * Math.cos(dec) / Math.cos(alt));
		//alt += (h * KMG.PI_BY_180);
		
		double Y = -Math.sin(H);
		double X = Math.cos(lat) * Math.tan(dec) - Math.sin(lat) * Math.cos(H);
		double az = Math.atan2(Y, X);

		if (az < 0.0) {
			az += 2.0 * Math.PI;
		}
		
		return new Horizon(Angle.fromRadians(az), Angle.fromRadians(alt));
		
	}
	
	public static EquatorialHourAngle convertEquatorialRightAscensionToEquatorialHourAngle(EquatorialRightAscension e, double lst) {
		return null;
	}
	
	public static EquatorialRightAscension convertEquatorialHourAngleToEquatorialRightAscension(EquatorialHourAngle e, double lst) {
		return null;
	}
	
	public static Ecliptic convertEquatorialRightAscensionToEcliptic(EquatorialRightAscension e) {
		return convertEquatorialRightAscensionToEcliptic(e, Angle.fromDegrees(23.4));
	}
	
	public static Ecliptic convertEquatorialRightAscensionToEcliptic(EquatorialRightAscension eq, Angle obliquityOfTheEcliptic) {
		
		double ra = eq.getRightAscension().getRadians();
		double dec = eq.getDeclination().getRadians();
		double e = obliquityOfTheEcliptic.getRadians();
		
		double Y = MathExt.sin(ra) * MathExt.cos(e) + MathExt.tan(dec) * MathExt.sin(e);
		double X = MathExt.cos(ra);
		
		double l = MathExt.atan2(Y, X);
		double b = MathExt.asin(MathExt.sin(dec) * MathExt.cos(e) - MathExt.cos(dec) * MathExt.sin(e) * MathExt.sin(ra));
		
		return new Ecliptic(Angle.fromRadians(b), Angle.fromRadians(l));
	}
	
	public static Galactic convertEquatorialRightAscensionToGalactic(EquatorialRightAscension eq) {
		return convertEquatorialRightAscensionToGalactic(eq, Angle.fromDegrees(23.4));
	}
	
	public static Galactic convertEquatorialRightAscensionToGalactic(EquatorialRightAscension eq, Angle obliquityOfTheEcliptic) {
		
		double ra = eq.getRightAscension().getRadians();
		double dec = eq.getDeclination().getRadians();
		double e = obliquityOfTheEcliptic.getRadians();
		
		double b = MathExt.asin(MathExt.cos(dec) * MathExt.cos(MathExt.radians(27.4)) * Math.cos(ra - MathExt.radians(192.25)) + MathExt.sin(dec) * MathExt.sin(MathExt.radians(27.4)));
		
		double Y = MathExt.sin(dec) - MathExt.sin(b) * MathExt.sin(MathExt.radians(27.4));
		double X = MathExt.cos(dec) * MathExt.cos(MathExt.radians(27.4)) * MathExt.sin(ra - MathExt.radians(192.25));
		double l = MathExt.atan2(Y, X) + MathExt.radians(33.0);
		
		l = MathExt.clamp(l + Math.PI, 2 * Math.PI) - Math.PI;
		
		return new Galactic(Angle.fromRadians(b), Angle.fromRadians(l));
	}
	
	
	public Galactic convertSuperGalacticToGalactic(SuperGalactic g) {
		return null;
	}
	
	public static SuperGalactic convertGalacticToSuperGalactic(Galactic g) {
		return null;
	}
	
	
	protected static Angle obliquity(DateTime dt) {
		double t = (dt.getJulianDay() - 2415020.0) / 36525.0;
		return Angle.fromDegrees((2.345229444E1 - ((((-1.81E-3*t)+5.9E-3)*t+4.6845E1)*t)/3600.0));
	}
	
	private static class Nutation {
		Angle dpsi;
		Angle deps;
		public Nutation(Angle deps, Angle dpsi) {
			this.deps = deps;
			this.dpsi = dpsi;
		}
	}
	
	protected static Nutation nutation(DateTime dt) {
		
		double t = (dt.getJulianDay() - 2415020.0) / 36525.0;
		double ls, ld;	// sun's mean longitude, moon's mean longitude
		double ms, md;	// sun's mean anomaly, moon's mean anomaly
		double nm;	    // longitude of moon's ascending node
		double t2;
		double tls, tnm, tld;	// twice above
		double a, b;

	    t2 = t*t;

	    a = 100.0021358*t;
	    b = 360.*(a-Math.floor(a));
	    ls = 279.697+.000303*t2+b;

	    a = 1336.855231*t;
	    b = 360.*(a-Math.floor(a));
	    ld = 270.434-.001133*t2+b;

	    a = 99.99736056000026*t;
	    b = 360.*(a-Math.floor(a));
	    ms = 358.476-.00015*t2+b;

	    a = 13255523.59*t;
	    b = 360.*(a-Math.floor(a));
	    md = 296.105+.009192*t2+b;

	    a = 5.372616667*t;
	    b = 360.*(a-Math.floor(a));
	    nm = 259.183+.002078*t2-b;

	    //convert to radian forms for use with trig functions.
	    tls = 2*MathExt.radians(ls);
	    nm = MathExt.radians(nm);
	    tnm = 2*MathExt.radians(nm);
	    ms = MathExt.radians(ms);
	    tld = 2*MathExt.radians(ld);
	    md = MathExt.radians(md);

	    // find delta psi and eps, in arcseconds.
	    double dpsi = (-17.2327-.01737*t)*Math.sin(nm)+(-1.2729-.00013*t)*Math.sin(tls)
	        +.2088*Math.sin(tnm)-.2037*Math.sin(tld)+(.1261-.00031*t)*Math.sin(ms)
	        +.0675*Math.sin(md)-(.0497-.00012*t)*Math.sin(tls+ms)
	        -.0342*Math.sin(tld-nm)-.0261*Math.sin(tld+md)+.0214*Math.sin(tls-ms)
	        -.0149*Math.sin(tls-tld+md)+.0124*Math.sin(tls-nm)+.0114*Math.sin(tld-md);
	    double deps = (9.21+.00091*t)*Math.cos(nm)+(.5522-.00029*t)*Math.cos(tls)
	        -.0904*Math.cos(tnm)+.0884*Math.cos(tld)+.0216*Math.cos(tls+ms)
	        +.0183*Math.cos(tld-nm)+.0113*Math.cos(tld+md)-.0093*Math.cos(tls-ms)
	        -.0066*Math.cos(tls-nm);

	    // convert to radians.
	    dpsi = MathExt.radians(dpsi/3600.0);
	    deps = MathExt.radians(deps/3600.0);
	    
	    return new Nutation(Angle.fromRadians(deps), Angle.fromRadians(dpsi));
	}
	
	public static EquatorialRightAscension convertEclipticToEquatorialRightAscension(Ecliptic e, DateTime dt) {
		return convertEclipticToEquatorialRightAscension(e, dt, obliquity(dt), nutation(dt));
	}
	
	protected static EquatorialRightAscension convertEclipticToEquatorialRightAscension(Ecliptic eq, DateTime dt, Angle obliquityOfTheEcliptic, Nutation nutation) {
		
		double fEclLat = eq.getLatitude().getRadians();
		double fEclLon = eq.getLongitude().getRadians();
		
		double seps, ceps;	// sin and cos of mean obliquity
		double sx, cx, sy, cy, ty;
		double eps;
		double dec, ra;
		
		
		double t = (dt.getJulianDay() - 2415020.0) / 36525.0;
	   // t = (2451545.0 - 2415020.0) / 36525.0;
	   // t = 0;
	    eps = obliquityOfTheEcliptic.getRadians();		// mean obliquity for date

	    double deps = nutation.deps.getRadians();
	    double dpsi = nutation.dpsi.getRadians();
	    
	    eps += deps;
	    seps = Math.sin(eps);
	    ceps = Math.cos(eps);

	    sy = Math.sin(fEclLat);
	    cy = Math.cos(fEclLat);				// always non-negative
	    if (Math.abs(cy)<1e-20)
	        cy = 1e-20;		// insure > 0
	    ty = sy/cy;
	    cx = Math.cos(fEclLon);
	    sx = Math.sin(fEclLon);
	    dec = Math.asin((sy*ceps)+(cy*seps*sx));
	    
	  //  ra = Math.atan(((sx*ceps)-(ty*seps))/cx);
		ra = Math.atan2(((sx*ceps)-(ty*seps)), cx);
	    //if (cx<0)
	   //     ra += Math.PI;		// account for atan quad ambiguity
		ra = MathExt.clamp(ra, 2.0 * Math.PI);
	    
		
		return new EquatorialRightAscension(Angle.fromRadians(ra), Angle.fromRadians(dec));
		/*
		double l = eq.getLongitude().getRadians();
		double b = eq.getLatitude().getRadians();
		double e = obliquityOfTheEcliptic.getRadians();
		
		double X = MathExt.sin(l) * MathExt.cos(e) - MathExt.tan(b) * MathExt.sin(e);
		double Y = MathExt.cos(l);
		
		double a = MathExt.atan2(Y, X);
		
		double d = MathExt.asin(MathExt.sin(b) * MathExt.cos(e) + MathExt.cos(b) * MathExt.sin(e) * MathExt.sin(l));
		
		
		return new EquatorialRightAscension(Angle.fromRadians(a), Angle.fromRadians(d));
		*/
	}
	
//	public static Galactic convertEquatorialRightAscensionToGalactic(EquatorialRightAscension e) {
//		return null;
	//}
	
	public static EquatorialRightAscension convertGalacticToEquatorialRightAscension(Galactic g) {
		return null;
	}
	
	
	public static Vector geocentricToECI(DateTime dt, Coordinate latitude, Coordinate longitude, double alt) {
		
		double F = 0.0033528;
		double mfactor = 2 * Math.PI * (1.00273790934 / 86400.0); 
		
		double lat = latitude.getDecimal();
		double lon = longitude.getDecimal();
		
		//double theta = KMG.Astro.getGMST2(0.0, dt.getJulianDay());
		double theta = dt.getGMST2();
		theta = MathExt.radians(MathExt.clamp(theta + lon, 360));;
		
		lat = MathExt.radians(lat);
		lon = MathExt.radians(lon);

		double c = 1.0 / Math.sqrt(1.0 + (1.0 / 298.26) * (F - 2.0) * MathExt.sqr(Math.sin(lat)));   
		double s = MathExt.sqr(1.0 - F) * c;   
		double achcp = (Constants.AU_TO_KM * c + alt) * Math.cos(lat);   
		
		double x = achcp * Math.cos(theta);
		double y = achcp * Math.sin(theta);
		double z = (Constants.AU_TO_KM * s + alt) * Math.sin(lat);
		double w = Math.sqrt(x*x + y*y + z*z);
		
		return new Vector(x, y, z, w);
	}
	
	public static Ecliptic convertVectorToEcliptic(Vector vec) {
		return convertVectorToEcliptic(vec, false);
	}
	
	public static Ecliptic convertVectorToEcliptic(Vector vec, boolean skipRotation) {

		double r = Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2) + Math.pow(vec.z, 2));
		double b = Math.abs(Math.acos(vec.y / r)) * ((vec.z < 0) ? -1 : 1);
		double l = 2 * Math.PI - Math.acos((vec.x * (1 / Math.sin(b))) / r); 
		
		double rotB = (skipRotation) ? 0 : Math.PI / 2;
		double rotL = (skipRotation) ? 0 : -Math.PI;
		
		Ecliptic ecliptic = new Ecliptic(Angle.fromRadians(b + rotB), Angle.fromRadians(l + rotL), r);
		return ecliptic;
	}
	
	
	public static EquatorialRightAscension convertEpoch(DateTime fromDt, DateTime toDt, EquatorialRightAscension from) {
		
		double jdFrom = fromDt.getJulianDay();
		double jdTo = toDt.getJulianDay();
		
		double a0 = from.getRightAscension().getRadians();
		double d0 = from.getDeclination().getRadians();
		
		double a, d;
		
	    double T = (jdFrom - 2451545.0) / 36525.0;
	    double t = (jdTo - jdFrom) / 36525.0;

	    double zeta = (2306.2181 + 1.39656 * T - 0.000139 * T * T) * t +
	        (0.30188 - 0.000344 * T) * t * t + 0.017998 * t * t * t;
	    double z = (2306.2181 + 1.39656 * T - 0.000139 * T * T) * t +
	        (1.09468 + 0.000066 * T) * t * t + 0.018203 * t * t * t;
	    double theta = (2004.3109 - 0.85330 * T - 0.000217 * T * T) * t -
	        (0.42665 + 0.000217 * T) * t * t - 0.041833 * t * t * t;
	    zeta  = MathExt.radians(zeta / 3600.0);
	    z     = MathExt.radians(z / 3600.0);
	    theta = MathExt.radians(theta / 3600.0);

	    double A = Math.cos(d0) * Math.sin(a0 + zeta);
	    double B = Math.cos(theta) * Math.cos(d0) * Math.cos(a0 + zeta) -
	        Math.sin(theta) * Math.sin(d0);
	    double C = Math.sin(theta) * Math.cos(d0) * Math.cos(a0 + zeta) +
	        Math.cos(theta) * Math.sin(d0);

	    a = Math.atan2(A, B) + z;
	    d = Math.asin(C);
	    
		return new EquatorialRightAscension(Angle.fromRadians(a), Angle.fromRadians(d));
	}
	
	public static EquatorialRightAscension convertVectorToEquatorial(DateTime dt, Vector heliocentricPosition) {
		return convertVectorToEquatorial(dt, heliocentricPosition, new Vector(0, 0, 0));
	}
	
	public static EquatorialRightAscension convertVectorToEquatorial(DateTime dt, Vector heliocentricPosition, Vector fromHeliocentricPosition) {
		
		if (fromHeliocentricPosition != null) {
			heliocentricPosition.subtract(fromHeliocentricPosition);
		}
		
		Ecliptic ecliptic = convertVectorToEcliptic(heliocentricPosition);
		EquatorialRightAscension equatorial = convertEclipticToEquatorialRightAscension(ecliptic, dt);
		equatorial = convertEpoch(dt, new DateTime(2451545.0), equatorial);
		
		Angle ra = equatorial.getRightAscension();
		Angle dec = equatorial.getDeclination();
		
		if (dec.getDegrees() < 0.0) {
			ra = Angle.fromDegrees(ra.getDegrees() + 180.0);
		}

		return new EquatorialRightAscension(ra, dec);
	}
}
