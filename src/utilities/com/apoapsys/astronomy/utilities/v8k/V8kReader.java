package com.apoapsys.astronomy.utilities.v8k;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.apoapsys.astronomy.math.Spheres;
import com.apoapsys.astronomy.math.Vector;

public class V8kReader {
	
	
	
	public static void main(String[] args) {
		String path = "/catalogues/V8K.csv";
		
		List<String> lines = null;
		try {
			lines = IOUtils.readLines(V8kReader.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Read " + lines.size() + " lines");
		
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File("C:/Users/kgill/Google Drive/Apoapsys/Animations/110 - Size of the Universe/galaxy-coordinate-3d-space-with-mtypes.txt")));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		
		for (int l = 5; l < lines.size(); l++) {
			String line = lines.get(l);
			
			String[] columns = line.split("\\,");

			double id = Double.parseDouble(columns[0]);
			double galacticLongitude = Double.parseDouble(columns[1]);
			double galacticLatitude = Double.parseDouble(columns[2]);
			String morphType = columns[10];
			double distance = Double.parseDouble(columns[12]); // km/s
			String commonName = columns[16];
			
			commonName = commonName.replace("\"", "").trim();
			
			Vector vec = Spheres.getPoint3D(galacticLongitude, galacticLatitude, distance);
			String f = String.format("%14s|%14s|%9.10f|%9.10f|%9.10f|%9.10f|%9.10f|%9.10f\n", commonName, morphType, galacticLongitude, galacticLatitude, distance, vec.x, vec.y, vec.z);
			
			try {
				writer.write(f);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
