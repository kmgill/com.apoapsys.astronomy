package com.apoapsys.astronomy.utilities.mpc;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.bodies.Body;
import com.apoapsys.astronomy.bodies.MajorBodiesLoader;
import com.apoapsys.astronomy.bodies.OrbitingBody;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class PlanetPathProcessor {
		
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd YYYY HH:mm:ss");
	
	
	public static void main(String[] args) {
		
		try {
			doIt();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String formateDate(DateTime dt) throws Exception {
		Date date = new Date(dt.getMillis());
		return sdf.format(date) + " UT";
	}
	
	public static OrbitingBody createMRO() {
		/*
		 * JDCT     Epoch Julian Date, Coordinate Time
      EC     Eccentricity, e                                                   
      QR     Periapsis distance, q (AU)                                        
      IN     Inclination w.r.t xy-plane, i (degrees)                           
      OM     Longitude of Ascending Node, OMEGA, (degrees)                     
      W      Argument of Perifocus, w (degrees)                                
      Tp     Time of periapsis (Julian day number)                             
      N      Mean motion, n (degrees/day)                                      
      MA     Mean anomaly, M (degrees)                                         
      TA     True anomaly, nu (degrees)                                        
      A      Semi-major axis, a (AU)                                           
      AD     Apoapsis distance (AU)                                            
      PR     Sidereal orbit period (day) 
      
		 * 2456880.500000000 = A.D. 2014-Aug-11 00:00:00.0000 (CT)
 EC= 8.962715295995866E-03 QR= 2.429246399283445E-05 IN= 1.072504817325727E+02
 OM= 1.424609882219523E+02 W = 2.859397258641848E+02 Tp=  2456880.516832485329
 N = 4.613618578943260E+03 MA= 2.823413327684683E+02 TA= 2.813356583414316E+02
 A = 2.451215949972049E-05 AD= 2.473185500660652E-05 PR= 7.802985744054665E-02
		 */
		Ephemeris ephemeris = new Ephemeris();
		ephemeris.pericenterDistance = 2.429246399283445E-05 * 100.0;
		ephemeris.ascendingNode = Angle.fromDegrees(1.424609882219523E+02);
		ephemeris.semiMajorAxis = 2.451215949972049E-05 * 100.0;
		ephemeris.argOfPeriapsis = Angle.fromDegrees(2.859397258641848E+02);
		ephemeris.epoch = 2456880.500000000;
		ephemeris.period = 7.802985744054665E-02;
		ephemeris.meanAnomalyAtEpoch = 2.823413327684683E+02;
		ephemeris.eccentricity = 8.962715295995866E-03;
		ephemeris.inclination = Angle.fromDegrees(1.072504817325727E+02);
		ephemeris.meanMotion = 4.613618578943260E+03;
		
		OrbitingBody body = new OrbitingBody();
		body.setEphemeris(ephemeris);
		body.setName("Mars Reconnaissance Orbiter");
		return body;
	}
	
	public static OrbitingBody createMarsOdyssey() {
		/*
		 * 
		 * EC= 1.140804946298419E-02 QR= 2.503071562601574E-05 IN= 9.376938331906773E+01
 OM= 3.532458998373982E+02 W = 2.363231433984174E+02 Tp=  2456880.461803521030
 N = 4.394705262246192E+03 MA= 1.678622672693024E+02 TA= 1.681333538697002E+02
 A = 2.531956244678984E-05 AD= 2.560840926756393E-05 PR= 8.191675630506318E-02
		 */
		
		Ephemeris ephemeris = new Ephemeris();
		ephemeris.pericenterDistance = 2.503071562601574E-05 * 100.0;
		ephemeris.ascendingNode = Angle.fromDegrees(3.532458998373982E+02);
		ephemeris.semiMajorAxis = 2.531956244678984E-05 * 100.0;
		ephemeris.argOfPeriapsis = Angle.fromDegrees(2.363231433984174E+02);
		ephemeris.epoch = 2456880.500000000;
		ephemeris.period = 8.191675630506318E-02;
		ephemeris.meanAnomalyAtEpoch = 1.678622672693024E+02;
		ephemeris.eccentricity = 1.140804946298419E-02;
		ephemeris.inclination = Angle.fromDegrees(9.376938331906773E+01);
		ephemeris.meanMotion = 4.394705262246192E+03;
		
		OrbitingBody body = new OrbitingBody();
		body.setEphemeris(ephemeris);
		body.setName("Mars Odyssey");
		return body;
	}

	
	public static OrbitingBody createSidingSpring() {
		Ephemeris ephemeris = new Ephemeris();
		ephemeris.pericenterDistance = 1.398716447099736;
		ephemeris.ascendingNode = Angle.fromDegrees(300.9770352032243);
		ephemeris.semiMajorAxis = -1628.528957671197;
		ephemeris.argOfPeriapsis = Angle.fromDegrees(2.435532763313843);
		ephemeris.epoch = 2456881.079166667;
		ephemeris.period = 1.157407291666667e+95;
		ephemeris.meanAnomalyAtEpoch = 0.001120897365205541;
		ephemeris.eccentricity = 1.000858883374785;
		ephemeris.inclination = Angle.fromDegrees(129.0274374566839);
		ephemeris.meanMotion = 1.498702498928125E-05;
		
		OrbitingBody body = new OrbitingBody();
		body.setEphemeris(ephemeris);
		body.setName("Siding Spring");
		return body;
	}
	
	public static List<String> loadSidingSpringVectors() throws Exception {
		
		String filePath = "C:\\Users\\GillFamily\\Google Drive\\Apoapsys\\Animations\\Siding Spring\\horizons_results_long_4.txt";
		InputStream in = new FileInputStream(filePath);
		List<String> lines = IOUtils.readLines(in);
		in.close();
		return lines;
	}
	
	public static void doIt() throws Exception {
			
			int index = 16;
	
			List<Body> bodies = MajorBodiesLoader.load();
			for (Body body : bodies) {
				OrbitingBody orbitingBody = (OrbitingBody) body;
				
				if (orbitingBody.getEphemeris() == null || orbitingBody.getEphemeris().ascendingNode == null) {
					continue;
				}
				
				orbitingBody.getEphemeris().meanMotion = 1.0 / orbitingBody.getEphemeris().period;
			}
			
		
			//bodies.add(createSidingSpring());
			
			DateTime now = new DateTime(2456938.500000000); // Oct 8
			int frames = 1000;
			DateTime end = new DateTime(2456958.500000000); // Oct 28
			
			double daysPerFrame = (end.getJulianDay() - now.getJulianDay()) / frames;
			System.err.println("Days per frame: " + daysPerFrame);
			
			OrbitingBody mro = createMRO();
			OrbitingBody mo = createMarsOdyssey();
			createObjectPath(mro, "mro", now);
			createObjectPath(mo, "mo", now);
			bodies.add(mro);
			bodies.add(mo);
			
			int ssStartIndex = 35079;
			int ssEndIndex = 39577;
			int ssFrameCount = ssEndIndex - ssStartIndex;
			List<String> sidingSpringVectors = loadSidingSpringVectors();
			
			for (double i = 0; i <= frames; i++) {
				//System.err.println("Frame #" + ((int)i));
				DateTime onDate = new DateTime(now.getJulianDay() + (daysPerFrame * i));
				
				String dateFilePath = "D:/Minor Planets Positions/date-ss-frame" + ((int)i) + ".dat";
				OutputStream dateOut = new BufferedOutputStream(new FileOutputStream(dateFilePath));
				String formattedDate = formateDate(onDate);
				System.err.println("Generating for date: " + formattedDate);
				dateOut.write(formattedDate.getBytes());
				dateOut.close();
				
				String outputFilePath = "D:/Minor Planets Positions/planet-positions-ss-frame" + ((int)i) + ".dat";
				//System.err.println("Writing locations to " + outputFilePath);
				OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFilePath));
				
				for (Body body : bodies) {
					OrbitingBody orbitingBody = (OrbitingBody) body;
					
					if (orbitingBody.getEphemeris() == null || orbitingBody.getEphemeris().ascendingNode == null) {
						continue;
					}
					
					//orbitingBody.getEphemeris().meanMotion = 1.0 / orbitingBody.getEphemeris().period;
					EllipticalOrbit orbit = new EllipticalOrbit(orbitingBody.getEphemeris());
					
					
					OrbitPosition position = orbit.positionAtTime(onDate);
					
					StringBuffer s = new StringBuffer();
					s.append(orbitingBody.getName());
					s.append("|");
					s.append(position.getPosition().x);
					s.append("|");
					s.append(position.getPosition().y);
					s.append("|");
					s.append(position.getPosition().z);
					s.append("\n");
					
					//System.err.println(s.toString() + ": " + onDate);
					out.write(s.toString().getBytes());
					
				}
				
				int d_ssIndex = ssStartIndex + (int) MathExt.round((i / frames) * ssFrameCount);
				String ssLine = sidingSpringVectors.get((int)i);
				//2456881.083333333, A.D. 2014-Aug-11 14:00:00.0000,  1.308355752505018E+00, -4.998128981797062E-01, -1.065285104654788E+00,
				String[] parts = ssLine.split(",");
				
				/*
				-entry[1],
				entry[3],
				-entry[2]
						
				-x
				z
				-y
				*/
				
				StringBuffer s = new StringBuffer();
				s.append("Siding Spring");
				s.append("|");
				s.append(Double.parseDouble(parts[2]));
				s.append("|");
				s.append(-Double.parseDouble(parts[4]));
				s.append("|");
				s.append(-Double.parseDouble(parts[3]));
				s.append("\n");
				
				//System.err.println(s.toString() + ": " + onDate);
				out.write(s.toString().getBytes());
				
				
				out.close();
			}
			
	}
	
	public static void createObjectPath(OrbitingBody body, String name, DateTime start) throws Exception {
		
		int index = 16;

		System.err.println(body.getName() + ": " + body.getEphemeris().period);
		
		DateTime now = start;
		int frames = 1000;
		DateTime end = new DateTime(now.getJulianDay() + body.getEphemeris().period);
		
		double daysPerFrame = (end.getJulianDay() - now.getJulianDay()) / frames;
		System.err.println("Days per frame: " + daysPerFrame);
		
		String outputFilePath = "D:/Minor Planets Positions/" + name + "-positions-1yr.dat";
		System.err.println("Writing locations to " + outputFilePath);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFilePath));
		
		//body.getEphemeris().meanMotion = 1.0 / body.getEphemeris().period;
		EllipticalOrbit orbit = new EllipticalOrbit(body.getEphemeris());
		
		for (double i = 0; i <= frames; i++) {
			//System.err.println("Frame #" + ((int)i));
			DateTime onDate = new DateTime(now.getJulianDay() + (daysPerFrame * i));
			OrbitPosition position = orbit.positionAtTime(onDate);
			
			StringBuffer s = new StringBuffer();
			s.append(position.getPosition().x);
			s.append("|");
			s.append(position.getPosition().y);
			s.append("|");
			s.append(position.getPosition().z);
			s.append("\n");
			
			//System.err.println(s.toString() + ": " + onDate);
			out.write(s.toString().getBytes());
		}
		
		out.close();
	}
	
	
}
