package com.apoapsys.astronomy.utilities.newhorizons;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import com.apoapsys.astronomy.math.MathExt;

public class LoadedImage {
	public BufferedImage image;
	public Offset offset;
	
	public LoadedImage(BufferedImage image) {
		this.image = image;
	}
	
	public WritableRaster getRaster() {
		return image.getRaster();
	}
	
	public double getInterpolatedPixel(double x, double y) {
		WritableRaster raster = getRaster();
		
		double[] rgba = new double[4];
		
		
		raster.getPixel((int)MathExt.floor(x), (int)MathExt.floor(y), rgba);
		double v00 = rgba[0];
		
		raster.getPixel((int)MathExt.ceil(x), (int)MathExt.floor(y), rgba);
		double v01 = rgba[0];
		
		raster.getPixel((int)MathExt.floor(x), (int)MathExt.ceil(y), rgba);
		double v10 = rgba[0];
		
		raster.getPixel((int)MathExt.ceil(x), (int)MathExt.ceil(y), rgba);
		double v11 = rgba[0];
		
		double value = MathExt.interpolate(v00, v01, v10, v11, x - MathExt.floor(x), y - MathExt.floor(y));
		return value;
	}
}
