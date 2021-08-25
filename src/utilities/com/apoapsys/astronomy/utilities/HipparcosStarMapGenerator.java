package com.apoapsys.astronomy.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.coords.CoordinateConversionUtil;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.coords.Horizon;
import com.apoapsys.astronomy.geo.Latitude;
import com.apoapsys.astronomy.geo.Longitude;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;
import com.google.common.collect.Lists;

public class HipparcosStarMapGenerator {

	public static void main(String[] args) {
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected static float parseFloat(String s) {
		s = s.trim();
		if (s.isEmpty()) {
			return 0.0f;
		} else {
			return Float.parseFloat(s);
		}
	}

	/*
	 * 
	 * --------------------------------------------------------------------------
	 * ------ Bytes Format Units Label Explanations
	 * ------------------------------
	 * -------------------------------------------------- 9- 14 I6 --- HIP
	 * Identifier (HIP number) (H1) 42- 46 F5.2 mag Vmag ? Magnitude in Johnson
	 * V (H5) 52- 63 F12.8 deg RAdeg *? alpha, degrees (ICRS, Epoch=J1991.25)
	 * (H8) 65- 76 F12.8 deg DEdeg *? delta, degrees (ICRS, Epoch=J1991.25) (H9)
	 * 80- 86 F7.2 mas Plx ? Trigonometric parallax (H11) 218-223 F6.3 mag BTmag
	 * ? Mean BT magnitude (H32) 231-236 F6.3 mag VTmag ? Mean VT magnitude
	 * (H34)
	 * 
	 * 246-251 F6.3 mag B-V ? Johnson B-V colour (H37)
	 */

	public static List<Record> parseLines(List<String> lines) {
		List<Record> records = Lists.newArrayList();

		for (String line : lines) {
			String[] fields = line.split("\\|");

			Record record = new Record();
			record.HIP = Integer.parseInt(fields[1].trim());
			record.rightAscension = parseFloat(fields[8]);
			record.declination = parseFloat(fields[9]);
			record.parallax = parseFloat(fields[11]);
			record.Vmag = parseFloat(fields[5]);
			record.magBT = parseFloat(fields[32]);
			record.magVT = parseFloat(fields[34]);
			record.bv = parseFloat(fields[37]);

			records.add(record);
		}

		return records;
	}

	public static void doWork() throws Exception {
			
		//int width = 4096;
		//int height = 2048;
		int width = 8192;
		int height = 4096;
		//int width = 8192 * 2;
		//int height = 8192;
		
		Latitude latitude = new Latitude(42.7575);
		Longitude longitude = new Longitude(-71.464444);
		DateTime dt = new DateTime();
		
		String cataloguePath = "C:/Users/GillFamily/Google Drive/Star Catalogs/Hipparcos/hip_main.dat";
		
		InputStream in = new BufferedInputStream(new FileInputStream(new File(cataloguePath)));
		System.err.println("Reading source...");
		List<String> lines = IOUtils.readLines(in);
		//String jsonTxt = IOUtils.toString( in );
		in.close();
		
		System.err.println("Parsing from text...");
		//JSONArray stars = JSONArray.fromObject(jsonTxt);
		
		List<Record> records = parseLines(lines);
		
		double minMag = 1000;
		double maxMag = -1000;
		
		for (Record record : records) {
			minMag = MathExt.min(minMag, record.Vmag);
			maxMag = MathExt.max(maxMag, record.Vmag);
		}
		
		System.err.println("Minimum Magnitude: " + minMag);
		System.err.println("Maximum Magnitude: " + maxMag);
		

		System.err.println("Creating map image buffer...");
		StarMapGenerator starMap = new StarMapGenerator(width, height, minMag, maxMag, 5, 15);
		
		System.err.println("Processing stars...");
		for (Record record : records) {
			
			EquatorialRightAscension eq = new EquatorialRightAscension(Angle.fromDegrees(record.rightAscension), Angle.fromDegrees(record.declination));
			//Galactic g = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(eq);

			Ecliptic coord = CoordinateConversionUtil.convertEquatorialRightAscensionToEcliptic(eq);
			//Horizon horizon = CoordinateConversionUtil.convertEquatorialRightAscensionToHorizon(eq, dt, latitude, longitude);
			
			//double visualMagnitude = (sMag.length() > 0) ? Double.parseDouble(sMag) : maxMag;
			
			starMap.addStar(coord.getLatitude().getDegrees(), coord.getLongitude().getDegrees(), record.Vmag, record.bv);
		}
		
		starMap.render();
		
		System.err.println("Rendered " + records.size() + " stars to map");
		System.err.println("Saving JPEG image to disk...");
		starMap.save("C:/Users/GillFamily/Google Drive/Star Catalogs/Hipparcos/hipparcos-star-map-" + width + "x" + height + ".jpg");
		
		System.err.println("Saving PNG image to disk...");
		starMap.save("C:/Users/GillFamily/Google Drive/Star Catalogs/Hipparcos/hipparcos-star-map-" + width + "x" + height + ".png");
		
		System.err.println("Done!");
	}

	public static class Record {
		public int HIP;
		public double rightAscension;
		public double declination;
		public double parallax;
		public double Vmag;
		public double magBT;
		public double magVT;
		public double bv;
	}
}
