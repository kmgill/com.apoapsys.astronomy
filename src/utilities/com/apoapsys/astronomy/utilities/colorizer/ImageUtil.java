package com.apoapsys.astronomy.utilities.colorizer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static BufferedImage convertToStandardFormat(BufferedImage in) {

		BufferedImage newImage = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(in, 0, 0, 1024, 1024, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage loadImage(String path) throws Exception {
		try (InputStream in = ColorCompositor.class.getResourceAsStream(path)) {
			return ImageIO.read(in);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static BufferedImage loadImage(File path) throws Exception {
		try (InputStream in = new BufferedInputStream(new FileInputStream(path))) {
			return ImageIO.read(in);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public static BufferedImage buildComposite(BufferedImage img0, BufferedImage img1, BufferedImage img2, BufferedImage comp, List<Transform> transforms) {
		
		if (comp == null) {
			comp = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
		}
		
		WritableRaster raster0 = (img0 != null) ? img0.getRaster() : null;
		WritableRaster raster1 = (img1 != null) ? img1.getRaster() : null;
		WritableRaster raster2 = (img2 != null) ? img2.getRaster() : null;

		WritableRaster compRaster = comp.getRaster();

		int[] rgbForComp = new int[4];
		rgbForComp[3] = 255;
		
		int[] rgbSampler = new int[4];
		for (int x = 0; x < 1024; x++) {
			for (int y = 0; y < 1024; y++) {
				
				if (raster0 != null) {
					raster0.getPixel(x, y, rgbSampler);
					rgbForComp[0] = rgbSampler[0];
				} else {
					rgbForComp[0] = 0;
				}
				
				if (raster1 != null) {
					raster1.getPixel(x, y, rgbSampler);
					rgbForComp[1] = rgbSampler[0];
				} else {
					rgbForComp[1] = 0;
				}
				
				if (raster2 != null) {
					raster2.getPixel(x, y, rgbSampler);
					rgbForComp[2] = rgbSampler[0];
				} else {
					rgbForComp[2] = 0;
				}
				
				Point p = new Point(x, y);
	
				if (transforms != null) {
					for (Transform trans : transforms) {
						trans.transform(p);
					}
				}
				
				compRaster.setPixel(p.x, p.y, rgbForComp);

			}
		}
		
		return comp;
	}
}
