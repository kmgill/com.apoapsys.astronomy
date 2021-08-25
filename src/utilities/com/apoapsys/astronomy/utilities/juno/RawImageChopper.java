package com.apoapsys.astronomy.utilities.juno;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.apoapsys.astronomy.image.ImageWriter;

public class RawImageChopper {
	
	
	public static void main(String ... args) {
		
		String rawImage = "C:\\Users\\kgill\\Desktop\\Juno\\efb12.jpg";
		String saveTo = "C:\\Users\\kgill\\Desktop\\Juno\\chopped\\";
		
		try {
			doChopping(rawImage, saveTo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	public static void doChopping(String path, String saveTo) throws Exception {
		
		System.err.println("Reading " + path);
		InputStream in = new BufferedInputStream(new FileInputStream(path));
		BufferedImage origImage = ImageIO.read(in);
		in.close();
		
		int height = origImage.getHeight();
		
		int subImageHeight = 128;
		
		int numImages = height / subImageHeight;
		
		System.err.println("Chopping " + numImages + " sub images from original");
		
		int[] rgb = {0, 0, 0};
		
		for (int i = 0; i < numImages; i++) {
			int channelNum = (i % 3);
			rgb[channelNum]++;
			
			
			
			int top = i * subImageHeight;
			
			String fileName = channelNum + "/subframe_" + i + "_" + rgb[channelNum] + ".jpg";
			System.err.println("Image #" + i + " (" + channelNum + ", " + top + ") - " + fileName);
					
			createSubImage(origImage, top, subImageHeight, saveTo + fileName);
		}
		
		
		
	}
	
	
	public static void createSubImage(BufferedImage origImage, int top, int height, String saveTo) throws Exception {
		
		
		BufferedImage subImage = new BufferedImage(origImage.getWidth(), height, origImage.getType());
		Graphics2D g2d = (Graphics2D) subImage.getGraphics();
		
		g2d.drawImage(origImage, 0, -top, null);
		g2d.dispose();
		
		ImageWriter.saveImage(subImage, saveTo);
		
	}
	
}
