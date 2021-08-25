package com.apoapsys.astronomy.tle;

import com.apoapsys.astronomy.orbits.Ephemeris;

public class TLEEphemeris extends Ephemeris {
	String name;
	String satelliteNumber;
	String classification;
	
	int launchYear;
	int launchNumberForYear;
	String launchPieceNumber;
	double dragTerm;
	
	
	String ephemerisType;
	int elementNumber;
	int revolutionNumber;
	boolean isDebris;
	
	String checksum;
}
