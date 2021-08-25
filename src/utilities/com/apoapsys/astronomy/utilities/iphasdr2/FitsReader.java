package com.apoapsys.astronomy.utilities.iphasdr2;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.ImageHDU;

import com.apoapsys.astronomy.coords.CoordinateConversionUtil;
import com.apoapsys.astronomy.coords.Galactic;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.utilities.StarMapGenerator;

public class FitsReader {
	
	
	public static void main(String[] args) {
		
		int width = 8192;
		int height = 4096;
		
		char[] ab = {'a', 'b'};
		
		try {
			//loadFile("C:/jdem/IPHAS-DR2/iphas-dr2-080b-light.fits.gz");
			
			StarMapGenerator starMap = new StarMapGenerator(width, height, -1.46, 22.0, 2, 6);

			FileMinMaxes minmax = new FileMinMaxes();
				
			for (int lon = 25; lon <= 95; lon += 5) {
				for (char v : ab) {
					FileMinMaxes fileMinmax = loadFile("D:/iphas_dr2/iphas-dr2-0" + lon + v + ".fits.gz", starMap);
					
					minmax.minMag = MathExt.min(minmax.minMag, fileMinmax.minMag);
					minmax.maxMag = MathExt.max(minmax.maxMag, fileMinmax.maxMag);
					minmax.minLon = MathExt.min(minmax.minLon, fileMinmax.minLon);
					minmax.maxLon = MathExt.max(minmax.maxLon, fileMinmax.maxLon);
					minmax.minLat = MathExt.min(minmax.minLat, fileMinmax.minLat);
					minmax.maxLat = MathExt.max(minmax.maxLat, fileMinmax.maxLat);
					minmax.minRmi = MathExt.min(minmax.minRmi, fileMinmax.minRmi);
					minmax.maxRmi = MathExt.max(minmax.maxRmi, fileMinmax.maxRmi);
					
					System.gc();
				}
			}
			
			starMap.setMaximumMagnitude(minmax.maxMag);
			starMap.setMinimumMagnitude(minmax.minMag);
			
			starMap.setMaximumLongitude(minmax.maxLon);
			starMap.setMinimumLongitude(minmax.minLon);
			starMap.setMaximumLatitude(minmax.maxLat);
			starMap.setMinimumLatitude(minmax.minLat);
			
			System.err.println("Max Magnitude: " + minmax.maxMag);
			System.err.println("Min Magnitude: " + minmax.minMag);
			System.err.println("Max Latitude: " + minmax.maxLat);
			System.err.println("Min Latitude: " + minmax.minLat);
			System.err.println("Max Longitude: " + minmax.maxLon);
			System.err.println("Min Longitude: " + minmax.minLon);
			System.err.println("Max R-I Color: " + minmax.maxRmi);
			System.err.println("Min R-I Color: " + minmax.minRmi);
			
			starMap.render();
			
			System.err.println("Saving JPEG image to disk...");
			starMap.save("D:/iphas_dr2/iphas_dr2_composite.jpg");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static FileMinMaxes loadFile(String path, StarMapGenerator starMap) throws Exception {
		
		
		
		
		Fits f = new Fits(path);
		

		ImageHDU hdu = (ImageHDU) f.getHDU(0);
		BinaryTableHDU binTable = (BinaryTableHDU) f.getHDU(1);
		
		int ncols = binTable.getNCols();
		int nrows = binTable.getNRows();
		
		System.err.println("ncols: " + ncols);
		System.err.println("nrows: " + nrows);
		
		
		
		FileMinMaxes minmax = new FileMinMaxes();

		for (int i = 0; i < nrows; i++) {
			Object[] rowData = binTable.getRow(i);
			IphasDr2Row row = new IphasDr2RowFull(rowData);
			
			if (row.isSimplifiedA10()) {
				double magnitude = 20.0;
				if (!Double.isNaN(row.getrMagnitude())) {
					magnitude = row.getrMagnitude();
				} else if (!Double.isNaN(row.getiMagnitude())) {
					magnitude = row.getiMagnitude();
				} else if (!Double.isNaN(row.getHaMagnitude())) {
					magnitude = row.getHaMagnitude();
				}
				
				magnitude = row.getHaMagnitude();
				//magnitude = 3.0;
				
				minmax.minMag = MathExt.min(minmax.minMag, magnitude);
				minmax.maxMag = MathExt.max(minmax.maxMag, magnitude);
				
				Galactic galactic = CoordinateConversionUtil.convertEquatorialRightAscensionToGalactic(row.getEquatorialCoordinates());
				
				minmax.minLon = MathExt.min(minmax.minLon, galactic.getLongitude().getDegrees());
				minmax.maxLon = MathExt.max(minmax.maxLon, galactic.getLongitude().getDegrees());
				
				minmax.minLat = MathExt.min(minmax.minLat, galactic.getLatitude().getDegrees());
				minmax.maxLat = MathExt.max(minmax.maxLat, galactic.getLatitude().getDegrees());
				
				minmax.minRmi = MathExt.min(minmax.minRmi, row.getRmi());
				minmax.maxRmi = MathExt.max(minmax.maxRmi, row.getRmi());
				
				starMap.addStar(galactic.getLatitude().getDegrees(), galactic.getLongitude().getDegrees(), magnitude, getRelativeSpectralClass(row.getRmi()));
			}
		}
		
		
		return minmax;
	}

	public static String getRelativeSpectralClass(double rmi) {
		double d = Math.abs(rmi - -0.32);
		String sc = "O";
		
		double d0 = Math.abs(rmi - -0.29);
		if (d0 < d) {
			d = d0;
			sc = "B";
		}
		
		d0 = Math.abs(rmi - -0.02);
		if (d0 < d) {
			d = d0;
			sc = "A";
		}
		
		
		d0 = Math.abs(rmi - .17);
		if (d0 < d) {
			d = d0;
			sc = "F";
		}
		
		d0 = Math.abs(rmi - .31);
		if (d0 < d) {
			d = d0;
			sc = "G";
		}
		
		d0 = Math.abs(rmi - .42);
		if (d0 < d) {
			d = d0;
			sc = "K";
		}
		
		d0 = Math.abs(rmi - .91);
		if (d0 < d) {
			d = d0;
			sc = "M";
		}
		
		
		return sc;
	}
	
}
