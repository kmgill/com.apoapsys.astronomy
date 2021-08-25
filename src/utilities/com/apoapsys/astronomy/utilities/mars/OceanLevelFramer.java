package com.apoapsys.astronomy.utilities.mars;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import com.apoapsys.astronomy.image.ImageWriter;

public class OceanLevelFramer {
	
	public static void main(String ... args) {
		
		
		String heightmap = "H:/Data/mola64_oc180_height_8192x4096.png";
		
		try {
			doWork(heightmap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public static void doWork(String heightmap) throws Exception {
		BufferedImage srcData = ImageIO.read(new File(heightmap));
		
		
		//BufferedImage srcData = ImageUtilities.getScaledInstance(srcDataFull, 8192, 4096, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		//BufferedImage srcData = downSample(srcDataFull, 8192, 4096);
		BufferedImage dstData = new BufferedImage(8192, 4096, BufferedImage.TYPE_INT_ARGB);
		
		WritableRaster srcRaster = srcData.getRaster();
		WritableRaster dstRaster = dstData.getRaster();
		
		double numFrames = 599.0;
		double elevStep = 8199.0 / (numFrames - 1);
		
		
		//for (double e = -8199.000; e <= 0.0; e+=elevStep) {
		for (int i = 0; i < numFrames; i++) {
			double e = -8199.000 + (double) i * elevStep;
			System.err.println("Frame #" + i + " at " + e + " meters");
			resetDestImage(dstData);
			createOceanAtElevation(srcRaster, dstRaster, e);
			ImageWriter.saveImage(dstData, "H:/Data/frames/mars-ocean-frame-" + (10000 + i) + ".png");
			
		}
		
		
		
	}
	
	public static void createOceanAtElevation(WritableRaster srcRaster, WritableRaster dstRaster, double elevation) {
		
		int[] rgb = {0, 0, 0, 0};
		int[] setTo = {0, 0, 0, 255};
		
		for (int x = 0; x < 8192; x++) {
			for (int y = 0; y < 4096; y++) {
				srcRaster.getPixel(x, y, rgb);
				double val = -8199.0  + (((double)rgb[0] / 65535.0) * 29417.0);
				if (val <= elevation) {
					dstRaster.setPixel(x, y, setTo);
				}
				
			}
		}	
		
		
	}
	
	
	public static BufferedImage downSample(BufferedImage src, int width, int height) {
		BufferedImage dst = new BufferedImage(width, height, src.getType());
		Graphics2D g = (Graphics2D) dst.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(src, 0, 0, width, height, null);
		g.dispose();
		return dst;
	}
	
	public static void resetDestImage(BufferedImage img) {
		Graphics g = img.createGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.dispose();
	}
	
	
}
