package com.apoapsys.astronomy.utilities;

import java.io.InputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

public class BrightStarMapGenerator {
	
	public static void main(String[] args) {
		try {
			doWork();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static void doWork() throws Exception {
		
		int width = 4096;
		int height = 2048;
		
		String cataloguePath = "catalogues/bright-star-catalog.json";

		InputStream in = BrightStarMapGenerator.class.getClassLoader().getResourceAsStream(cataloguePath);
		System.err.println("Reading source...");
		String jsonTxt = IOUtils.toString( in );
		
		System.err.println("Parsing from text...");
		JSONArray stars = JSONArray.fromObject(jsonTxt);

		System.err.println("Creating image buffer...");
		
		StarMapGenerator starMap = new StarMapGenerator(width, height, -1.46, 9.42, 2, 6);
		
		System.err.println("Adding " + stars.size() + " stars to the map...");
		for (int i = 0; i < stars.size(); i++) {
			
			JSONObject star = stars.getJSONObject(i);

			double visualMagnitude = star.getDouble("Vmag");
			double longitude = -star.getDouble("eclipticLongitude");
			double latitude = star.getDouble("eclipticLatitude");
			String spectralClass = star.getString("SpClass");
			
			starMap.addStar(latitude, longitude, visualMagnitude, spectralClass);
			
		}
		
		
		System.err.println("Saving JPEG image to disk...");
		starMap.save("C:/jdem/temp/bright-star-map.jpg");
		
		System.err.println("Saving PNG image to disk...");
		starMap.save("C:/jdem/temp/bright-star-map.png");
		
		System.err.println("Done!");
	}
	
}
