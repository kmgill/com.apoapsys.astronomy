package com.apoapsys.astronomy.utilities.perlin;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import com.apoapsys.astronomy.image.ImageException;
import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.perlin.ColorGradientBuilder;
import com.apoapsys.astronomy.perlin.GradientColorStop;
import com.apoapsys.astronomy.perlin.GroovedPerlin;
import com.apoapsys.astronomy.perlin.NoiseClampModifier;
import com.apoapsys.astronomy.perlin.NoiseRangeModifier;
import com.apoapsys.astronomy.perlin.PerlinCompositor;
import com.apoapsys.astronomy.perlin.StandardPerlin;

public class PerlinPanel extends JPanel {
	
	private BufferedImage perlinImage;
	
	public PerlinPanel(int width, int height) {
		perlinImage = drawImage(width, height);
	}
	
	@Override
	public void paint(Graphics p) {
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		
		Graphics2D g2d = (Graphics2D) p;

		g2d.drawImage(perlinImage, 0, 0, perlinImage.getWidth(null), perlinImage.getHeight(null), null);
	}
	
	
	
	protected BufferedImage drawImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		WritableRaster raster = image.getRaster();
	
		PerlinCompositor compositor = new PerlinCompositor();
		compositor.addPerlin(new StandardPerlin(0.55, 16.0, 60.0), 5, new NoiseRangeModifier(-.7, 1.0), new NoiseClampModifier(0.0, NoiseClampModifier.NO_MAXIMUM));
		//compositor.addPerlin(new StandardPerlin(0.35, 16.0, 30.0), 0.5);
		compositor.addPerlin(new GroovedPerlin(0.75, 16.0, 10.0), 0.25, new NoiseRangeModifier(0.0, 0.3));
		
		
		compositor.addModifier(new NoiseRangeModifier(-1.0, 1.0, 0.0, 1.0));
		
		
		ColorGradientBuilder gradient = new ColorGradientBuilder(compositor);
		//gradient.addColorStop(new GradientColorStop(-1.000000, 0.000000, 0.000000, 0.000000, 255.0));
		//gradient.addColorStop(new GradientColorStop( .300000, 200.000000, 200.000000, 200.000000, 255.0));
		//gradient.addColorStop(new GradientColorStop( 1.000000, 255.000000, 255.000000, 255.000000, 255.0));
		/*
		gradient.addColorStop(new GradientColorStop(0.000000, 0.000000*255.0, 0.388235*255.0, 0.274510*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.179381, 0.145098*255.0, 0.513725*255.0, 0.207843*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.301031, 0.894118*255.0, 0.811765*255.0, 0.494118*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.400000, 0.603922*255.0, 0.274510*255.0, 0.000000*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.550515, 0.486275*255.0, 0.133333*255.0, 0.141176*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.779381, 0.474510*255.0, 0.474510*255.0, 0.474510*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(0.929897, 0.7000*255.0, 0.7000*255.0, 0.7000*255.0, 1.0*255.0));
		gradient.addColorStop(new GradientColorStop(1.000000, 0.9000*255.0, 0.9000*255.0, 0.9000*255.0, 1.0*255.0));
		*/
		
		gradient.clear();
		gradient.addColorStop(new GradientColorStop(-1.0000,  0,   0, 128, 255)); // deeps
		gradient.addColorStop(new GradientColorStop(-0.2500,  0,   0, 255, 255)); // shallow
		gradient.addColorStop(new GradientColorStop( 0.0000,   0, 128, 255, 255)); // shore
		gradient.addColorStop(new GradientColorStop( 0.0625, 240, 240,  64, 255)); // sand
		gradient.addColorStop(new GradientColorStop( 0.1250,  32, 160,   0, 255)); // grass
		gradient.addColorStop(new GradientColorStop( 0.3750, 224, 224,   0, 255)); // dirt
		gradient.addColorStop(new GradientColorStop( 0.7500, 128, 128, 128, 255)); // rock
		gradient.addColorStop(new GradientColorStop( 1.0000, 255, 255, 255, 255)); // snow
		
		
		
		double[] rgb = new double[4];
		//rgb[3] = 255.0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				//double noise = compositor.noise(x, y);
				
				//rgb[0] = noise;
				//rgb[1] = noise;
				//rgb[2] = noise;
				
				gradient.color(x, y, rgb);
				/*
				rgb[0] *= 255.0;
				rgb[1] *= 255.0;
				rgb[2] *= 255.0;
				rgb[3] *= 255.0;
				*/
				raster.setPixel(x, y, rgb);
			}
		}

		return image;
	}
	
	public void writeToFile(String path) {
		try {
			ImageWriter.saveImage(perlinImage, path);
		} catch (ImageException e) {
			e.printStackTrace();
		}
	}
	
}
