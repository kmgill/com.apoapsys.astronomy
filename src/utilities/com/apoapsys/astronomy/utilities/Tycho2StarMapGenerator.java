package com.apoapsys.astronomy.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.coords.CoordinateConversionUtil;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.coords.Horizon;
import com.apoapsys.astronomy.geo.Latitude;
import com.apoapsys.astronomy.geo.Longitude;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.utilities.StarMapGenerator.Star;
import com.google.common.collect.Lists;

public class Tycho2StarMapGenerator {
	public static void main(String[] args) {
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static List<StarRecord> readFile(String path) throws Exception {
		
		List<StarRecord> stars = Lists.newArrayList();
		
		
		File f = new File(path);
		FileInputStream in = new FileInputStream(f);
		List<String> lines = IOUtils.readLines(in);
		
		for (String line : lines) {

			String sRA = line.substring(15, 27).trim();
			String sDec = line.substring(28, 40).trim();
			

			String sBTMag = line.substring(110, 116).trim();
			String sVTMag = line.substring(123, 129).trim();

			if (sRA.length() == 0 || sDec.length() == 0) {
				continue;
			}
			
			
			
			double ra = Double.parseDouble(sRA);
			double dec = Double.parseDouble(sDec);
			double btMag = (sBTMag != null && sBTMag.length() > 0) ? Double.parseDouble(sBTMag) : -100;
			double vtMag = (sBTMag != null && sVTMag.length() > 0) ? Double.parseDouble(sVTMag) : -100;
			
			
			
			double visualMagnitude = 10;
			double bv = 0;
			
			
			if (vtMag == -100) {
				vtMag = btMag;
			}
			if (btMag == -100) {
				btMag = vtMag;
			}
			
			if (vtMag != -100 && btMag != -100) {
				visualMagnitude = vtMag - 0.090 * (btMag - vtMag);
				
				double v = vtMag - 0.090 * (btMag - vtMag);
				bv = 0.850 * (btMag - vtMag);

			} else {
				visualMagnitude = 10.0;
				bv = 3.0;
			} 

			
			stars.add(new StarRecord(ra, dec, visualMagnitude, bv * 0.4));
			//double visualMagnitude = btMag;

			//EquatorialRightAscension eq = new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
			//Galactic g = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(eq);
			//Ecliptic coord = new Ecliptic(g.getLatitude(), Angle.fromDegrees(g.getLongitude().getDegrees() * -1.0));
			//Ecliptic coord = CoordinateConversionUtil.convertEquatorialRightAscensionToEcliptic(eq);
			
			//Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(eq, dt, latitude, longitude);
			
			//if (horizon.getAltitude().getDegrees() >= 0.0 && visualMagnitude <= 8.5) {
			//	Star star = new Star(horizon, visualMagnitude, bv);
			//	stars.add(star);
			//}
		}
		
		
		System.err.println("Read " + stars.size() + " stars from file");
		return stars;
	}
	
	
	public static List<StarRecord> readAll() throws Exception {
		List<StarRecord> allStars = Lists.newArrayList();
		
		
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.00.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.01.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.02.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.03.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.04.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.05.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.06.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.07.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.08.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.09.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.10.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.11.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.12.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.13.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.14.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.15.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.16.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.17.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.18.txt"));
		allStars.addAll(readFile("C:/Users/GillFamily/Google Drive/Star Catalogs/Tycho-2/tyc2.dat.19.txt"));
		
		
		return allStars;
	}
	
	public static void doWork() throws Exception {
		
		Latitude latitude = new Latitude(42.7575);
		Longitude longitude = new Longitude(-71.464444);
		
		int width = 8192;
		int height = 4096;
		
		//DateTime dt = new DateTime(2456741.500000);//JulianUtil.julianNow() - (1.0 / 24.0) * 4.0);
		//System.err.println(dt.getJulianDay());
		
		List<StarRecord> stars = readAll();
		
		double maxMag = -10000;
		double minMag = 10000;
		
		for (StarRecord star : stars) {
			maxMag = MathExt.max(maxMag, star.visualMagnitude);
			minMag = MathExt.min(minMag, star.visualMagnitude);
		}
		
		System.err.println("Minimum Magnitude: " + minMag);
		System.err.println("Maximum Magnitude: " + maxMag);
		
		double startJd = 2456741.708333;
		double endJd = 2456742.708333;
		double numSeconds = 60.0;
		double framesPerSecond = 30.0;
		double numFrames = numSeconds * framesPerSecond;
		
		double frameStep = (endJd - startJd) / numFrames;
		
		int frame = 0;
		//for (double jd = startJd; jd <= endJd; jd += frameStep) {
		//	System.err.println("FRAME #" + (++frame) + " of " + numFrames);
		//	DateTime dt = new DateTime(jd);
			create(stars, width, height, null, latitude, longitude, minMag, maxMag);
		//}
		//create(stars, width, height, dt, latitude, longitude, minMag, maxMag);
		
	}
	
	public static void create(List<StarRecord> stars, int width, int height, DateTime dt, Latitude latitude, Longitude longitude, double minMag, double maxMag) throws Exception {

		int planetSizeMultiple = 1;
		
		
		System.err.println("Starting...");


		System.err.println("Read a total of " + stars.size() + " stars");

		System.err.println("Gathering statistics...");
		
		
		
		System.err.println("Creating map image buffer...");
		StarMapGenerator starMap = new StarMapGenerator(width, height, minMag, maxMag, 1, 5);
		
		System.err.println("Adding stars...");
		
		for (StarRecord star : stars) {
			
			EquatorialRightAscension equatorial = new EquatorialRightAscension(Angle.fromDegrees(star.ra), Angle.fromDegrees(star.dec));
			Ecliptic ecliptic = CoordinateConversionUtil.convertEquatorialRightAscensionToEcliptic(equatorial);
			//Galactic galactic = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(equatorial);
			starMap.addStar(new Star(ecliptic, star.visualMagnitude, star.bv));
			//Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(equatorial, dt, latitude, longitude);
			
			//if (horizon.getAltitude().getDegrees() >= 0 && star.visualMagnitude >= 10.0) {
			//	starMap.addStar(new Star(horizon, star.visualMagnitude, star.bv));
			//}
		}
		/*
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new MercuryVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 1 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new VenusVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 2 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new MarsVSOPOrbit(), latitude, longitude), new Color(0xF7C497), 2 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new JupiterVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 3 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new SaturnVSOPOrbit(), latitude, longitude), new Color(0xFFFEE3), 1 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new UranusVSOPOrbit(), latitude, longitude), new Color(0xC2F1FF), 1 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new NeptuneVSOPOrbit(), latitude, longitude), new Color(0xADC3FF), 1 * planetSizeMultiple));
		*/
		
		System.err.println("Rendering to map...");
		starMap.render();
		
		System.err.println("Saving PNG image to disk...");
		starMap.save("C:/Users/GillFamily/Google Drive/Apoapsys/Apoapsys Graphics/Apoapsys Graphics/images/Star Maps/tycho2-star-map-ecliptic-" + width + "x" + height + ".png");
		
		//System.err.println("Saving JPEG image to disk...");
		//starMap.save("C:/jdem/temp/Tycho-Horizon/tycho2-star-map-horizon-" + dt.getMillis() + "-" + width + "x" + height + ".jpg");
		
		System.err.println("Done!");
	}
	
	
	public static Horizon getEclipticPositionOfOrbit(DateTime dt, Orbit o, Latitude latitude, Longitude longitude) {
		OrbitPosition pos = o.positionAtTime(dt);
		Vector v = pos.getPosition();
		
		EquatorialRightAscension equatorial = CoordinateConversionUtil.convertVectorToEquatorial(dt, v);

		Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(equatorial, dt, latitude, longitude);
		
		return horizon;
	}
	
	
	static class StarRecord {
		double ra = 0;
		double dec = 0;
		double visualMagnitude = 10;
		double bv = 0;
		
		public StarRecord(double ra, double dec, double visualMagnitude, double bv) {
			this.ra = ra;
			this.dec = dec;
			this.visualMagnitude = visualMagnitude;
			this.bv = bv;
		}
	}
}
