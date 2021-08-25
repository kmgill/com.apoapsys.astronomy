package com.apoapsys.astronomy.tle;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class TLEParser {
	
	public static TLEEphemeris parse(String tle) throws TLEParseException {
		return parse(tle, new DateTime());
	}
	
	public static TLEEphemeris parse(String tle, DateTime epochNow) throws TLEParseException {

		String[] lines = tle.split("\n");
		if (lines.length < 2 || lines.length > 3) {
			throw new TLEParseException("Invalid number of lines: " + lines.length, tle);
		}

		String name = (lines.length == 3) ? lines[0].trim() : "Undefined";

		String line0 = (lines.length == 2) ? lines[0] : lines[1];
		String line1 = (lines.length == 2) ? lines[1] : lines[2];

		String satelliteNumber = line0.substring(2, 7);
		String classification = line0.substring(7, 8);
		String internationalDesignator = line0.substring(9, 17);
		
		int launchYear = parseInt(internationalDesignator.substring(0, 2));
		int launchNumberForYear = parseInt(internationalDesignator.substring(2, 5));
		String launchPieceNumber = internationalDesignator.substring(5, 8).trim();
		int epochYear = parseInt(line0.substring(18, 20));
		double epochDay = parseDouble(line0.substring(20, 32));
		double derivativeOfMeanMotion = parseDouble(line0.substring(33, 43)); // first_derative_mean_motion;
		double dragTerm = parseDouble("0." + line0.substring(53, 61).trim());
		String ephemerisType = line0.substring(62, 63);
		int elementNumber = parseInt(line0.substring(64, 68));
		String checksum = line0.substring(68, 69);
		double inclination = parseDouble(line1.substring(8, 16));
		double rightAscension = parseDouble(line1.substring(17, 25)); // of the ascending
															// node;
		double eccentricity = parseDouble("0." + line1.substring(26, 33).trim());
		double argumentOfPerigee = parseDouble(line1.substring(34, 42)); // argOfPeriapsis;
		double meanAnomaly = parseDouble(line1.substring(43, 51));
		double meanMotion = parseDouble(line1.substring(52, 63));
		int revolutionNumber = parseInt(line1.substring(63, 68));

		boolean isDebris = name.contains("DEB");
		
		
		double epochStart = dayNumberTle(2000 + epochYear, epochDay);
		
		double epochNowJulian = epochNow.getDayNumber();
		double epoch = epochStart + 2451545.0 - 1.5;
				
		double periodHours = (1440.0/((1.0*meanMotion)+(derivativeOfMeanMotion*(epochNowJulian-epochStart )))) /60.0;
		double semiMajorAxis = Math.pow((6028.9* (periodHours*60.0)), 2.0 / 3.0);
		
		TLEEphemeris eph = new TLEEphemeris();
		
		eph.semiMajorAxis = semiMajorAxis / 149597870.700;
		eph.longitudeOfPerihelion = 0;
		eph.eccentricity = eccentricity;
		eph.inclination = Angle.fromDegrees(inclination);
		eph.ascendingNode = Angle.fromDegrees(rightAscension);
		eph.argOfPeriapsis = Angle.fromDegrees(argumentOfPerigee);
		eph.meanAnomalyAtEpoch = meanAnomaly;
		eph.period = periodHours / 24;
		eph.meanMotion = meanMotion;
		eph.pericenterDistance = 0;
		eph.derivativeOfMeanMotion = derivativeOfMeanMotion;
		eph.epoch = epoch;
		eph.rightAscension = Angle.fromDegrees(rightAscension);
		eph.epochStart = epochStart;
		eph.name = name;
		eph.satelliteNumber = satelliteNumber;
		eph.classification = classification;
		eph.launchYear = launchYear;
		eph.launchNumberForYear = launchNumberForYear;
		eph.launchPieceNumber = launchPieceNumber;
		eph.dragTerm = dragTerm;
		eph.ephemerisType = ephemerisType;
		eph.elementNumber = elementNumber;
		eph.revolutionNumber = revolutionNumber;
		eph.isDebris = isDebris;
		eph.checksum = checksum;
		
		return eph;
	}

	
	private static int parseInt(String s) throws TLEParseException {
		try {
			s = s.trim().replaceAll("^0", "");
			return Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			throw new TLEParseException("Invalid number encountered: " + s, ex);
		}
	}
	
	private static double parseDouble(String d) throws TLEParseException {
		try {
			
			d = d.trim();
			d = d.replaceAll("^0", "");
			double exponent = 0;
			if (d.contains("-")) {
				exponent = Integer.parseInt(d.substring(d.indexOf("-")+1));
				d = d.substring(0, d.indexOf("-"));
			}
			
			double value = Double.parseDouble(d.trim());
			if (exponent > 0) {
				value = value / Math.pow(10,exponent);
			}
			return value;
		} catch (NumberFormatException ex) {
			throw new TLEParseException("Invalid number encountered: " + d, ex);
		}
	}
	
	private static double dayNumber(double dd, double mm, double yyyy, double hh, double min, double sec) {
		double d = 367.0 * yyyy - MathExt.div((7.0 * (yyyy + (MathExt.div((mm + 9.0), 12.0)))), 4.0) + MathExt.div((275.0 * mm), 9.0) + dd - 730530.0;
		d = d + hh / 24.0 + min / (60.0 * 24.0) + sec / (24.0 * 60.0 * 60.0);
		return d;
	}
	
	private static double dayNumberTle(double year, double day) {
		double d=dayNumber(1,1,year,0,0,0)+day-1;
		return d;
	}
	
	private static double dayNumberNow() {
		DateTime dt = new DateTime();
		return dt.getDayNumber();
	}
	
}
