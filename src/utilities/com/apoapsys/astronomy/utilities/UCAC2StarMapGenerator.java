package com.apoapsys.astronomy.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.coords.CoordinateConversionUtil;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;

public class UCAC2StarMapGenerator {
	public static void main(String[] args) {
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static void doWork() throws Exception {
		
		int width = 8192;
		int height = 4096;

		
		String cataloguePath = "C:/Users/kgill/Google Drive/Star Catalogs/UCAC2 BSS//ucac2bss.dat";
		
		File f = new File(cataloguePath);
		FileInputStream in = new FileInputStream(f);
		
		List<String> lines = IOUtils.readLines(in);
		
		System.err.println("Loaded " + lines.size() + " lines");
		
		System.err.println("Gathering statistics...");
		
		double maxMag = -10000;
		double minMag = 10000;
		
		double maxSpec = -10000;
		double minSpec = 10000;
		for (String line : lines) {
			String sMag = line.substring(137, 143).trim();
			double visualMagnitude = (sMag.length() > 0) ? Double.parseDouble(sMag) : 13;
			maxMag = MathExt.max(visualMagnitude, maxMag);
			minMag = MathExt.min(visualMagnitude, minMag);
			
			
			String sSpec = line.substring(109, 115).trim();
			double spectralIndex = (sSpec.length() > 0) ? Double.parseDouble(sSpec) : 0;
			maxSpec = MathExt.max(spectralIndex, maxSpec);
			minSpec = MathExt.min(spectralIndex, minSpec);
		}
		
		System.err.println("Minimum Magnitude: " + minMag);
		System.err.println("Maximum Magnitude: " + maxMag);
		
		System.err.println("Minimum Spectral Index: " + minSpec);
		System.err.println("Maximum Spectral Index: " + maxSpec);
		
		System.err.println("Creating map image buffer...");
		StarMapGenerator starMap = new StarMapGenerator(width, height, minMag, maxMag, 2, 11);
		
		System.err.println("Rendering stars...");
		int renderedStars = 0;
		for (String line : lines) {
			
			String sRA = line.substring(9, 21).trim();
			String sDec = line.substring(22, 34).trim();
			String sMag = line.substring(137, 143).trim();
			String sSpec = line.substring(109, 115).trim();
			
			
			double ra = -Double.parseDouble(sRA);
			double dec = Double.parseDouble(sDec);
			
			EquatorialRightAscension eq = new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
			Ecliptic coord = CoordinateConversionUtil.convertEquatorialRightAscensionToEcliptic(eq);
			
			double visualMagnitude = (sMag.length() > 0) ? Double.parseDouble(sMag) : maxMag;
			double spectralIndex = (sSpec.length() > 0) ? Double.parseDouble(sSpec) : 0;
			
			if (visualMagnitude <= 8.0) {
				starMap.addStar(coord.getLatitude().getDegrees(), coord.getLongitude().getDegrees(), visualMagnitude, spectralIndex);
				renderedStars++;
			}
		}
		
		System.err.println("Rendered " + renderedStars + " stars to map");
		System.err.println("Saving JPEG image to disk...");
		starMap.save("C:/jdem/temp/ucac2-star-map.jpg");
		
		System.err.println("Saving PNG image to disk...");
		starMap.save("C:/jdem/temp/ucac2-star-map.png");
		
		System.err.println("Done!");
	}
}
