package com.apoapsys.astronomy.utilities;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.coords.CoordinateConversionUtil;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.coords.Horizon;
import com.apoapsys.astronomy.geo.Latitude;
import com.apoapsys.astronomy.geo.Longitude;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.orbits.vsop87.JupiterVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.MarsVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.MercuryVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.NeptuneVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.SaturnVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.UranusVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.VenusVSOPOrbit;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.utilities.StarMapGenerator.Planet;
import com.apoapsys.astronomy.utilities.StarMapGenerator.Star;

public class SAOStarMapGenerator {
	public static void main(String[] args) {
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static void doWork() throws Exception {
		int width = 1920;
		int height = 1080;
		//DateTime dt = new DateTime(2456741.500000);
		//DateTime dt = new DateTime(2456741.708333);
		Latitude latitude = new Latitude(42.7575);
		Longitude longitude = new Longitude(-71.464444);
		
		double startJd = 2456741.708333;
		double endJd = 2456742.708333;
		double numSeconds = 10.0;
		double framesPerSecond = 15.0;
		double numFrames = numSeconds * framesPerSecond;
		
		double frameStep = (endJd - startJd) / numFrames;
		
		for (double jd = startJd; jd <= endJd; jd += frameStep) {
			DateTime dt = new DateTime(jd);
			create(width, height, latitude, longitude, dt);
		}
	}
	
	public static void create(int width, int height, Latitude latitude, Longitude longitude, DateTime dt) throws Exception {

		String cataloguePath = "C:/Users/kgill/Google Drive/Star Catalogs/SAO Star Catalog//heasarc_sao.tdat";
		
		File f = new File(cataloguePath);
		FileInputStream in = new FileInputStream(f);
		
		List<String> lines = IOUtils.readLines(in);
		
		System.err.println("Loaded " + lines.size() + " lines");
		
		System.err.println("Gathering statistics...");
		
		double maxMag = -10000;
		double minMag = 10000;
		

		int l = 0;
		for (String line : lines) {
			if (l > 75 && l < 259019) {
				String[] parts = line.split("\\|");
				
				String sMag = parts[13];
				
				double visualMagnitude = (sMag.length() > 0) ? Double.parseDouble(sMag) : 5;
				maxMag = MathExt.max(visualMagnitude, maxMag);
				minMag = MathExt.min(visualMagnitude, minMag);
				
			}
			l++;
		}
		
		
		
		
		System.err.println("Minimum Magnitude: " + minMag);
		System.err.println("Maximum Magnitude: " + maxMag);
		

		System.err.println("Creating map image buffer...");
		StarMapGenerator starMap = new StarMapGenerator(width, height, minMag, maxMag, 1, 5);
		
		System.err.println("Processing stars...");

		l = 0;
		int stars = 0;
		for (String line : lines) {
			if (l > 75 && l < 259019) {
			
				
				String[] parts = line.split("\\|");
				
				
				String sRA = parts[1];
				String sDec = parts[5];
				String sMag = parts[13];
				String sSpec = parts[14];
				
				if (sSpec.length() > 0) {
					sSpec = sSpec.substring(0, 1);
				}
				
				
				double ra = Double.parseDouble(sRA);
				double dec = Double.parseDouble(sDec);
				
				//EquatorialRightAscension eq = new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
				//Ecliptic coord = CoordinateConversionUtil.convertEquatorialRightAscensionToEcliptic(eq);
				
				EquatorialRightAscension eq = new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
				//Galactic g = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(eq);
				//sEcliptic coord = new Ecliptic(g.getLatitude(), g.getLongitude());
				
				Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(eq, dt, latitude, longitude);
				
				
				double visualMagnitude = (sMag.length() > 0) ? Double.parseDouble(sMag) : maxMag;
				
				
				if (visualMagnitude <= 9.0 && horizon.getAltitude().getDegrees() >= 0) {
				//	starMap.addStar(new Star(horizon, visualMagnitude, sSpec));
					stars++;
				}
			}
			l++;
		}
		
		int planetSizeMultiple = 1;
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new MercuryVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 1 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new VenusVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 2 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new MarsVSOPOrbit(), latitude, longitude), new Color(0xF7C497), 2 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new JupiterVSOPOrbit(), latitude, longitude), new Color(0xFFFFFF), 3 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new SaturnVSOPOrbit(), latitude, longitude), new Color(0xFFFEE3), 2 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new UranusVSOPOrbit(), latitude, longitude), new Color(0xC2F1FF), 1 * planetSizeMultiple));
		starMap.addPlanet(new Planet(getEclipticPositionOfOrbit(dt, new NeptuneVSOPOrbit(), latitude, longitude), new Color(0xADC3FF), 1 * planetSizeMultiple));
				
		
		System.err.println("Rendering " + stars + " stars to map");
		starMap.render();
		
		System.err.println("Saving PNG image to disk...");
		starMap.save("C:/jdem/temp/SAO-Horizon/heasarc-sao-star-map-horizon-" + dt.getMillis() + "-" + width + "x" + height +".png");
		
		//System.err.println("Saving JPEG image to disk...");
		//starMap.save("C:/jdem/temp/SAO-Horizon/heasarc-sao-star-map-horizon-" + dt.getMillis() + "-" + width + "x" + height +".jpg");
		
		System.err.println("Done!");
	}
	
	

	// Returns an Ecliptic as a fake galactic
	public static Horizon getEclipticPositionOfOrbit(DateTime dt, Orbit o, Latitude latitude, Longitude longitude) {
		
		OrbitPosition pos = o.positionAtTime(dt);
		Vector v = pos.getPosition();
		
		EquatorialRightAscension equatorial = CoordinateConversionUtil.convertVectorToEquatorial(dt, v);

		//EquatorialRightAscension equatorial = CoordinateConversionUtil.convertEclipticToEquatorialRightAscension(eq, dt);
		//Galactic g = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(equatorial);
		//Ecliptic coord = new Ecliptic(g.getLatitude(), g.getLongitude());
		
		Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(equatorial, dt, latitude, longitude);
		
		return horizon;
	}
}
