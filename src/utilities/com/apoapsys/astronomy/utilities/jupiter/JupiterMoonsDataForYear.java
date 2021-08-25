package com.apoapsys.astronomy.utilities.jupiter;

import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.orbits.custom.CustomCallistoOrbit;
import com.apoapsys.astronomy.orbits.custom.CustomEuropaOrbit;
import com.apoapsys.astronomy.orbits.custom.CustomGanymedeOrbit;
import com.apoapsys.astronomy.orbits.custom.CustomIoOrbit;
import com.apoapsys.astronomy.orbits.vsop87.EarthVSOPOrbit;
import com.apoapsys.astronomy.orbits.vsop87.JupiterVSOPOrbit;
import com.apoapsys.astronomy.rotation.IAUJupiterRotation;
import com.apoapsys.astronomy.time.DateTime;

public class JupiterMoonsDataForYear {
	
	
	public static void main(String[] args) {
			
			//double start = 2457023.500000; // Midnight, January 1st, 2015
			double start = 2457046.000000; // Noon, January 23rd
			//double stop = 2457388.500000; // Midnight, January 1st, 2016
			//double stop = 2457047.000000; // Noon, January 24th
			double stop = start + (365 * 40);
			double steps = 1797.0;
			//double step = (stop - start) / steps;
			double step = 1.0 / 24.0 / 60.0 * 1.0;
			EarthVSOPOrbit earthOrbit = new EarthVSOPOrbit();
			JupiterVSOPOrbit jupiterOrbit = new JupiterVSOPOrbit();
			CustomCallistoOrbit callistoOrbit = new CustomCallistoOrbit();
			CustomEuropaOrbit europaOrbit = new CustomEuropaOrbit();
			CustomGanymedeOrbit ganymedeOrbit = new CustomGanymedeOrbit();
			CustomIoOrbit ioOrbit = new CustomIoOrbit();
			
			IAUJupiterRotation jupiterRotation = new IAUJupiterRotation();
			
			//String outPath = "D:/Apoapsys Graphics/scenes/Jupiter Moons 2015/data.txt";
			//try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(outPath))))) {
				
				Vector a = new Vector(1.0, 0.0, 0.0);
				Vector b = new Vector(0.0, 0.0, 1.0);
				
				double i = start;
				while (i <= stop) {
					DateTime dt = new DateTime(i);
					
					//EclipticOrbitPosition earthPos = earthOrbit.positionAtTime(dt);
					//EclipticOrbitPosition jupiterPos = jupiterOrbit.positionAtTime(dt);
					EclipticOrbitPosition callistoPos = callistoOrbit.positionAtTime(dt);
					EclipticOrbitPosition europaPos = europaOrbit.positionAtTime(dt);
					EclipticOrbitPosition ganymedePos = ganymedeOrbit.positionAtTime(dt);
					EclipticOrbitPosition ioPos = ioOrbit.positionAtTime(dt);
					
					Vector v0 = ioPos.getPosition();
					Vector v1 = europaPos.getPosition();
					Vector v2 = ganymedePos.getPosition();
					Vector v3 = callistoPos.getPosition();
					
					v0.y = v1.y = v2.y = v3.y = 0.0;
					
					
					double an0 = MathExt.degrees(v0.angle(a));
					double an1 = MathExt.degrees(v1.angle(a));
					double an2 = MathExt.degrees(v2.angle(a));
					double an3 = MathExt.degrees(v3.angle(a));
					
					double bn0 = MathExt.degrees(v0.angle(b));
					double bn1 = MathExt.degrees(v1.angle(b));
					double bn2 = MathExt.degrees(v2.angle(b));
					double bn3 = MathExt.degrees(v3.angle(b));
					
					double max = 5;
					if (MathExt.abs(an0 - an1) <= max
							&& MathExt.abs(an0 - an2) <= max
							//&& MathExt.abs(an0 - an3) <= max
							&& MathExt.abs(bn0 - bn1) <= max
							&& MathExt.abs(bn0 - bn2) <= max
							//&& MathExt.abs(bn0 - bn3) <= max
							
							
							) {
						System.err.printf("By Angle: %s (%f)\n", dt.format("MMMM dd yyyy HH:mm:ss"), MathExt.abs(an0 - an1));
					}
					
					//Angle jupiterRot = jupiterRotation.getSiderealRotation(dt);
					
					/*
					out.printf("%f,%f,%f,", earthPos.getPosition().x, earthPos.getPosition().y, earthPos.getPosition().z);
					out.printf("%f,%f,%f,", jupiterPos.getPosition().x, jupiterPos.getPosition().y, jupiterPos.getPosition().z);
					
					out.printf("%f,%f,%f,", ioPos.getPosition().x, ioPos.getPosition().y, ioPos.getPosition().z);
					out.printf("%f,%f,%f,", europaPos.getPosition().x, europaPos.getPosition().y, europaPos.getPosition().z);
					out.printf("%f,%f,%f,", ganymedePos.getPosition().x, ganymedePos.getPosition().y, ganymedePos.getPosition().z);
					out.printf("%f,%f,%f,", callistoPos.getPosition().x, callistoPos.getPosition().y, callistoPos.getPosition().z);
					out.printf("%f,", MathExt.clamp(jupiterRot.getDegrees(), 360.0));
					out.printf("%s,", dt.format("MMMM dd yyyy HH:mm:ss"));
					
					out.print("\n");
					
					System.err.println(dt.format("MMMM dd yyyy HH:mm:ss"));
					*/
					i += step;
				}
				
			//} catch (Exception ex) {
			//	ex.printStackTrace();
			//}
			
	}
	
	public static boolean inSameQuad2D(Vector a, Vector b) {
		
		//1, 1
		if (a.x >= 0.0 && b.x >= 0.0 && a.z >= 0.0 && b.z >= 0.0) {
			return true;
		}
		
		// 1, -1
		if (a.x >= 0.0 && b.x >= 0.0 && a.z < 0.0 && b.z < 0.0) {
			return true;
		}
		
		// -1, 1
		if (a.x < 0.0 && b.x < 0.0 && a.z >= 0.0 && b.z >= 0.0) {
			return true;
		}
		
		// -1, -1
		if (a.x < 0.0 && b.x < 0.0 && a.z < 0.0 && b.z < 0.0) {
			return true;
		}
		
		return false;
	}
	
	
}
