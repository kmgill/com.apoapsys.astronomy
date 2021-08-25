package com.apoapsys.astronomy.utilities.imaging;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import com.apoapsys.astronomy.image.ImageWriter;
import com.google.common.collect.Lists;

public class CenteringTool {
	
	
	public static void main(String ... args) {

		try {
			
			/*
			if (args.length > 0) {
				File f = new File(args[0]);
				if (f.exists()) {
					doTest(args[0]);
				} else {
					System.err.println("File not found: " + args[0]);
				}
			} else {
				System.err.println("Please specify a file");
			}
			*/
			
			File root = new File("F:\\Jupiter_Wide");
			File files[] = root.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".PNG");
				}
				
			});
			
			for (File f : files) {
				doTest(f.getAbsolutePath());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public static void doTest(String path) throws Exception {
		
		InputStream in = new BufferedInputStream(new FileInputStream(path));
		
		BufferedImage image = ImageIO.read(in);
		in.close();
		
		int threshold = 5000;
		
		WritableRaster raster = image.getRaster();
		
		int[] rgb = new int[4];

		List<Point> points = Lists.newArrayList();
		
		
		for (int y =250; y <= 800; y++) {
			
			for (int x = 250; x <= 800; x++) {
				raster.getPixel(x, y, rgb);
				if (rgb[0] >= threshold) {
					points.add(new Point(x, y));
				}
			}
			
		}
		
		int ttlX = 0;
		int ttlY = 0;
		
		for (Point point : points) {
			ttlX += point.x;
			ttlY += point.y;
		}
		
		int avgX = (points.size() > 0 ) ? ttlX / points.size() : image.getWidth();
		int avgY = (points.size() > 0 ) ? ttlY / points.size() : image.getHeight();
		
		System.err.println("File: " + path);
		System.err.println("Detected " + points.size() + " points");
		System.err.println("Center: " + avgX + "/" + avgY);
		
		BufferedImage shiftedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics2D g2d = (Graphics2D) shiftedImage.getGraphics();
		
		int shiftX = (image.getWidth() / 2) - avgX;
		int shiftY = (image.getHeight() / 2) - avgY;
		
		g2d.drawImage(image, shiftX, shiftY, null);
		
		
		g2d.dispose();
		
		File f = new File(path);
		
		ImageWriter.saveImage(shiftedImage, f.getParent() + "/fixed/" + f.getName());
	}
	
	static class Point {
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
}
