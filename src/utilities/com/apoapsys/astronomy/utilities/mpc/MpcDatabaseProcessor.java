package com.apoapsys.astronomy.utilities.mpc;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.util.ByteConversions;
import com.google.common.collect.Lists;

public class MpcDatabaseProcessor {
	
	public static char[] charMap = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 
									'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
									'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
									'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	
	public static void main(String[] args) {
		
		
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public static int charToInt(char c) {
		for (int i = 0; i < charMap.length; i++) {
			if (charMap[i] == c) {
				return (i + 1);
			}
		}
		return 0;
	}
	
	
	public static DateTime parseEpoch(String epoch) {
		

		int year = Integer.parseInt(epoch.substring(1, 3));
		year += (charToInt(epoch.substring(0, 1).charAt(0)) * 100);
		
		int month = charToInt(epoch.substring(3, 4).charAt(0));
		int day = charToInt(epoch.substring(4, 5).charAt(0));

		//System.err.println(year + "/" + month + "/" + day);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		
		DateTime dt = new DateTime(cal.getTime().getTime());
		
		return dt;
	}
	
	public static double parseDouble(String line, int start, int end) {
		return Double.parseDouble(line.substring(start, end));
	}
	
	public static Angle parseAngle(String line, int start, int end) {
		return Angle.fromDegrees(parseDouble(line, start, end));
	}
	
	public static void doWork() throws Exception {
		
		DateTime now = new DateTime();
		int frames = 1000;
		DateTime end = new DateTime(now.getJulianDay() + (20.0 * 365.25));
		
		double daysPerFrame = (end.getJulianDay() - now.getJulianDay()) / frames;
		
		
		System.err.println("Days per frame: " + daysPerFrame);
		
		
		String databaseFilePath = "C:/Users/kgill/Google Drive/Star Catalogs/Minor Planet Center Orbit Database//MPCORB.DAT";
		
		
		
		
		List<String> lines = IOUtils.readLines(new FileInputStream(databaseFilePath));
		System.err.println("Read " + lines.size() + " lines from file");
		
		List<MinorPlanet> minorPlanets = Lists.newArrayList();
		
		for (int i = 41; i < lines.size() - 1; i++) {
			String line = lines.get(i);
			
			if (line.length() < 100) {
				continue;
			}
			
			Ephemeris ephemeris = new Ephemeris();

			ephemeris.epoch = parseEpoch(line.substring(20, 25)).getJulianDay();
			ephemeris.meanAnomalyAtEpoch = parseDouble(line, 26, 35);
			ephemeris.argOfPeriapsis = parseAngle(line, 37, 46);
			ephemeris.ascendingNode = parseAngle(line, 48, 57);
			ephemeris.inclination = parseAngle(line, 59, 68);
			ephemeris.eccentricity = parseDouble(line, 70, 79);
			ephemeris.meanMotion = parseDouble(line, 80, 91);
			ephemeris.derivativeOfMeanMotion = 0.0;
			ephemeris.semiMajorAxis = parseDouble(line, 92, 103);
			ephemeris.pericenterDistance = ephemeris.semiMajorAxis * (1 - ephemeris.eccentricity);
			ephemeris.derivativeOfMeanMotion = 0;
			
			ephemeris.period = 365.25 / ((ephemeris.meanMotion * 365.25) / 360.0);
			ephemeris.meanMotion = 1.0 / ephemeris.period;
			
			String flagsStr = line.substring(161, 165);
			int flagsInt = Integer.parseInt(flagsStr, 16);
			byte[] flagsBytes = ByteConversions.intToBytes(flagsInt);
			int flags = (flagsBytes[3] & 0x3F);
			String orbitType = getOrbitType(flags);
			
			minorPlanets.add(new MinorPlanet(ephemeris, orbitType));

		}
		
		System.err.println("Processed " + minorPlanets.size() + " minor planets from file");
		
		
		for (double i = 0; i < frames; i++) {
			System.err.println("Frame #" + ((int)i));
			DateTime onDate = new DateTime(now.getJulianDay() + (daysPerFrame * i));
			String outputFilePath = "C:/jdem/mpc/positions-" + ((int)i) + ".dat";
			createLocationFile(minorPlanets, onDate, outputFilePath);
		}
		
	}
	
	public static void createLocationFile(List<MinorPlanet> minorPlanets, DateTime onDate, String outputFilePath) throws Exception {
		
		
		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFilePath));

		for (MinorPlanet minorPlanet : minorPlanets) {
			
			Vector position = minorPlanet.getPosition(onDate);
			StringBuffer line = new StringBuffer();
			line.append(minorPlanet.orbitType);
			line.append("|");
			line.append(position.x);
			line.append("|");
			line.append(position.y);
			line.append("|");
			line.append(position.z);
			line.append("\n");
			
			//System.err.println(line.toString());
			
			out.write(line.toString().getBytes());
			
		}
		
		
		out.close();
		System.err.println("Done");
	}
	
	
	public static String getOrbitType(int flags) {
		
		
		
		switch(flags) {
		case 1:
			return "Atira";
		case 2:
			return "Aten";
		case 3:
			return "Apollo";
		case 4:
			return "Amor";
		case 5:
			return "Object with q < 1.665 AU";
		case 6:
			return "Hungaria";
		case 7:
			return "Phocaea";
		case 8:
			return "Hilda";
		case 9:
			return "Jupiter Trojan";
		case 10:
			return "Distant Object";
		default:
			return "Uncatagorized";
		}
	}
	
	
	public static class MinorPlanet {
		public Ephemeris ephemeris;
		public String orbitType;
		
		public MinorPlanet(Ephemeris ephemeris, String orbitType) {
			this.ephemeris = ephemeris;
			this.orbitType = orbitType;
		}
		
		
		public Vector getPosition(DateTime dt) {
			EllipticalOrbit orbit = new EllipticalOrbit(ephemeris);
			OrbitPosition position = orbit.positionAtTime(dt);
			return position.getPosition();
		}
		
	}
	
}
