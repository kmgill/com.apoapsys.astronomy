package com.apoapsys.astronomy.utilities.newhorizons;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.math.MathExt;

public class Main {
	
	public static String files[];
	
	
	
	
	public void doIt() throws Exception {
		
		files = new String[2];
		for (int i = 0; i < files.length; i++) {
			files[i] = "frame_" + i + ".jpg";
		}
		
		
		List<LoadedImage> images = new ArrayList<>();
		
		System.err.println("Loading Images...");
		for (String fileName : files) {
			images.add(new LoadedImage(ImageIO.read(Main.class.getResourceAsStream("/" + fileName))));
		}
		
		System.err.println("Computing Center Offsets...");
		List<Offset> offsets = new ArrayList<>();
		for (LoadedImage image : images) {
			image.offset = computeFeatureOffset(image.image);
		}
		
		int width = images.get(0).image.getWidth();
		int height = images.get(0).image.getHeight();
		
		System.err.println("Computing Averages...");
		BufferedImage composite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster compositeRaster = composite.getRaster();
		
		double[] rgba0 = new double[4];
		double[] rgba1 = new double[4];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				double value = 0;
				double count = 0;
				for (LoadedImage image : images) {
					double offsetX = x-image.offset.x;
					double offsetY = y-image.offset.y;
					if (MathExt.isBetween(offsetX, 0, width - 1) && MathExt.isBetween(offsetY, 0, height - 1)) {
						
						value += image.getInterpolatedPixel(offsetX, offsetY);
						count++;
					}
				}
				
				value /= count;
				
				rgba0[0] = value;
				rgba0[1] = value;
				rgba0[2] = value;
				rgba0[3] = 255.0;
				compositeRaster.setPixel(x, y, rgba0);
			}
		}
		
		System.err.println("Saving Composite...");
		ImageWriter.saveImage(composite, "D:/tmp/charon.jpg");
		
		System.err.println("Done");
	}

	
	
	public Offset computeFeatureOffset(BufferedImage image) {
		double Ox = 0;
		double Oy = 0;
		
		WritableRaster raster = image.getRaster();
		double threshold = 60.0;
		
		double[] rgba = new double[4];
		double count = 0;
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				raster.getPixel(x, y, rgba);
				if (rgba[0] >= threshold) {
					Ox += x;
					Oy += y;
					count++;
				}
			}
		}
		
		Ox = ((double)image.getWidth() / 2.0) - Ox / (double) count;
		Oy = ((double)image.getHeight() / 2.0) - Oy / (double) count;
		
		return new Offset(Ox, Oy);
	}
	
	public static void main(String ... args) {
		Main main = new Main();
		try {
			main.doIt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
