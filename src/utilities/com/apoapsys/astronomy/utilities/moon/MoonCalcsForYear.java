package com.apoapsys.astronomy.utilities.moon;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.moon.IlluminatedFractionOfTheMoonsDisk;
import com.apoapsys.astronomy.moon.OpticalLibrationsOfTheMoon;
import com.apoapsys.astronomy.moon.PhaseOfTheMoon;
import com.apoapsys.astronomy.moon.PositionAngleOfTheMoon;
import com.apoapsys.astronomy.moon.PositionOfTheMoon;
import com.apoapsys.astronomy.moon.RelativePositionOfTheSun;
import com.apoapsys.astronomy.moon.SelenographicPositionOfTheSun;
import com.apoapsys.astronomy.moon.TotalLibrationsOfTheMoon;
import com.apoapsys.astronomy.rotation.IAUEarthRotation;
import com.apoapsys.astronomy.rotation.IAULunarRotation;
import com.apoapsys.astronomy.time.DateTime;

public class MoonCalcsForYear {

	
	public static void main(String[] args) {
		
		double start = 2457023.500000; // Midnight, January 1st, 2015
		double stop = 2457388.500000; // Midnight, January 1st, 2016
		
		double steps = 1800.0;
		double step = (stop - start) / steps;
		
		MinMax distanceMinMax = calculateDistanceMinMax(start, stop, steps);
		
		String outPath = "C:/Users/GillFamily/Google Drive/Apoapsys/Apoapsys Graphics/Apoapsys Graphics/scenes/Moon Phases for Year/data.txt";
		//String outPath = "C:/jdem/temp/data.txt";
		IAUEarthRotation earthRot = new IAUEarthRotation();
		IAULunarRotation moonRot = new IAULunarRotation();
		
		try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(outPath))))) {
			
			double i = start;
			while (i <= stop) {
				DateTime dt = new DateTime(i);
	
				IlluminatedFractionOfTheMoonsDisk frac = IlluminatedFractionOfTheMoonsDisk.calculate(dt);
				PositionOfTheMoon pos = PositionOfTheMoon.calculate(dt);
				TotalLibrationsOfTheMoon ttlLibs = TotalLibrationsOfTheMoon.calculate(dt);
				RelativePositionOfTheSun relPosSun = RelativePositionOfTheSun.calculate(dt);
				PositionAngleOfTheMoon posAngleMoon = PositionAngleOfTheMoon.calculate(dt);
				PhaseOfTheMoon phaseMoon = PhaseOfTheMoon.calculate(dt);
				SelenographicPositionOfTheSun selePosSun = SelenographicPositionOfTheSun.calculate(dt);
				OpticalLibrationsOfTheMoon optLibrMoon = OpticalLibrationsOfTheMoon.calculate(dt);
				
				double distance = pos.getDistanceBetweenTheCentersOfTheEarthAndMoon();
				double illumFrac = frac.getIlluminatedFractionOfTheMoonsDisk();
				
				Angle earthRotOnDay = earthRot.getSiderealRotation(dt);
				Angle moonRotOnDay  = moonRot.getSiderealRotation(dt);
				
				out.printf("%f,", ttlLibs.getTotalLibrationsInLatitude());
				out.printf("%f,", ttlLibs.getTotalLibrationsInLongitude());
				out.printf("%f,", posAngleMoon.getPositionAngleOfTheMooon());
				out.printf("%6.3f,", MathExt.round(distance * 1000.0) / 1000.0);
				out.printf("%f,", (distance - distanceMinMax.min) / (distanceMinMax.max - distanceMinMax.min));
				out.printf("%3.2f,", (illumFrac * 100.0));
				out.printf("%f,", selePosSun.getOpticalLibrationInLongitude());
				out.printf("%f,", selePosSun.getOpticalLibrationInLatitude());
				out.printf("%f,", pos.getGeocentricLongitudeOfTheCenterOfTheMoon());
				out.printf("%f,", MathExt.clamp(earthRotOnDay.getDegrees(), 360.0));
				out.printf("%f,", MathExt.clamp(moonRotOnDay.getDegrees(), 360.0));
				out.printf("%f,", selePosSun.getSelenographicLongitudeOfTheSun());
				out.printf("%f,", selePosSun.getSelenographicLatitudeOfTheSun());
				out.printf("%f,", selePosSun.getSelenographicColongitudeOfTheSun());
				out.printf("%s,", dt.format("MMMM dd yyyy HH:mm:ss"));
				
				// 15
				Angle dec = Angle.fromDegrees(pos.getApparentDeclination());
				out.printf("%s,", dec.formatDMS());
				
				// 16
				out.printf("%s,", Angle.fromDegrees(pos.getApparentRightAscension() / 15.0).formatHMS());
				
				// 17
				// Total Libration Formatted:
				out.printf("%s,", Angle.fromDegrees(ttlLibs.getTotalLibrationsInLatitude()).formatDegrees("N", "S") + "   " + Angle.fromDegrees(ttlLibs.getTotalLibrationsInLongitude()).formatDegrees("E", "W"));
				
				// 18
				// Subsolar Formatted:
				out.printf("%s,", Angle.fromDegrees(selePosSun.getOpticalLibrationInLatitude()).formatDegrees("N", "S") + "   " + Angle.fromDegrees(selePosSun.getOpticalLibrationInLongitude()).formatDegrees("E", "W"));
				
				// 19
				// Sub-Earth Formatted:
				out.printf("%s,", Angle.fromDegrees(optLibrMoon.getOpticalLibrationInLatitude()).formatDegrees("N", "S") + "   " + Angle.fromDegrees(optLibrMoon.getOpticalLibrationInLongitude()).formatDegrees("E", "W"));
				
				out.printf("%s,", phaseMoon.getPhase());

				/*
				x January 1 2015 12:00 AM UTC
				x Distance: 383565.191 km
				x Right Ascension: 03h 15m 51.72s
				x Declination: 15° 6' 10.25"N
				x Illum Fraction: 82.421%
				x Subsolar: 1.558°N 54.916°E
				x Sub-Earth: 3.797°N 5.326°E
				x Total Libration: 3.844°N 5.327°E
				x Angle Rot. Axis: -16.465°
				Phase: Waxing Gibbous
				*/
				
				
				/*
				 * sunlightIndicator.rotation.set(moonContext.seleSunPosition.b0 * KMG.PI_BY_180 + rotation.x,
											moonContext.seleSunPosition.l0 * KMG.PI_BY_180 + rotation.y ,
											0 + rotation.z,
											'XZY');
				 */
				
				out.print("\n");
				System.err.println("Distance: " + distance);
				
				i += step;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	

	
	public static MinMax calculateDistanceMinMax(double start, double stop, double steps) {
		MinMax minMax = new MinMax();
		
		double step = (stop - start) / steps;
		
		double i = start;
		while (i < stop) {
			DateTime dt = new DateTime(i);

			PositionOfTheMoon pos = PositionOfTheMoon.calculate(dt);
			
			double distance = pos.getDistanceBetweenTheCentersOfTheEarthAndMoon();
			minMax.min(distance);
			minMax.max(distance);
			i += step;
		}
		return minMax;
	}
	
	
	
	static class MinMax {
		public double min = 10000000;
		public double max = -10000000;
		
		public double min(double v) {
			min = MathExt.min(min, v);
			return min;
		}
		
		public double max(double v) {
			max = MathExt.max(max, v);
			return max;
		}
	}
}
