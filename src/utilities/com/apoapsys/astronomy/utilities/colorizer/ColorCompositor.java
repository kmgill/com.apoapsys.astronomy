package com.apoapsys.astronomy.utilities.colorizer;


public class ColorCompositor {
	
	
	public static void main(String[] args) {
		
		
		ColorCompositorFrame frame = new ColorCompositorFrame();
		frame.setVisible(true);
		
		/*
		try {
			
			BufferedImage vio = convertToStandardFormat(loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229197_1.IMG.Saturn.CL1.VIO.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage bl1 = convertToStandardFormat(loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229230_1.IMG.Saturn.CL1.BL1.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage grn = convertToStandardFormat(loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229263_1.IMG.Saturn.CL1.GRN.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage red = convertToStandardFormat(loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229296_1.IMG.Saturn.CL1.RED.s1.1024x1024x8.2011-02-25.PNG"));
			BufferedImage irp0 = convertToStandardFormat(loadImage("/com/apoapsys/astronomy/utilities/colorizer/sample2/W1677229330_1.IMG.Saturn.CB2.IRP0.s1.1024x1024x8.2011-02-25.PNG"));

			WritableRaster raster0 = red.getRaster();
			WritableRaster raster1 = grn.getRaster();
			WritableRaster raster2 = bl1.getRaster();
			
			BufferedImage comp = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
			WritableRaster compRaster = comp.getRaster();
			
			int[] rgbForComp = new int[4];
			rgbForComp[3] = 255;
			
			int[] rgbSampler = new int[4];
			for (int x = 0; x < 1024; x++) {
				for (int y = 0; y < 1024; y++) {
					raster0.getPixel(x, y, rgbSampler);
					rgbForComp[0] = rgbSampler[0];
					
					raster1.getPixel(x, y, rgbSampler);
					rgbForComp[1] = rgbSampler[0];
					
					raster2.getPixel(x, y, rgbSampler);
					rgbForComp[2] = rgbSampler[0];
					//System.err.println(rgbForComp[0] + "/" + rgbForComp[1] + "/" + rgbForComp[2]);
					compRaster.setPixel(x, y, rgbForComp);
					
				}
			}
			
			SimpleImageDisplay display = new SimpleImageDisplay(comp);
			display.setVisible(true);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		*/
	}
	
	
	
	
}
