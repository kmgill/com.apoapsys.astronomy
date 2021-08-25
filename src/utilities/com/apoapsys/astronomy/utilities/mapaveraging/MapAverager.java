package com.apoapsys.astronomy.utilities.mapaveraging;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.math.MathExt;

public class MapAverager {
	
	public static void main(String ... args) {
		
		try {
			doIt();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static int WIDTH = 2048;
	public static int HEIGHT = 1024;
	
	public static void doIt() throws Exception {

		File imagesPath = new File("C:/jdem/repos/fetchcuriositysol/images/gibs");
		
		String[] files = imagesPath.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jpg");
			}
			
		});
		
		List<File> fileList = new ArrayList<>();
		for (String fileName : files) {
			File file = new File(imagesPath, fileName);
			fileList.add(file);
			System.err.println(file.getAbsolutePath());
		}
		
		createAveragedImage(fileList, "C:/jdem/repos/fetchcuriositysol/images/avg-img.jpg");
	}
	
	
	
	
	public static void createAveragedImage(List<File> files, String saveTo) throws Exception {
		int numFrames = files.size();
		
		MapPixel[][] map = new MapPixel[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				map[y][x] = new MapPixel(files.size());
			}
		}

		int[] rgba = new int[4];
		
		for (int f = 0; f < numFrames; f++) {
			System.err.println("Loading frame #" + f);
			BufferedImage image = loadMinIntensityImage(files.get(f));
			WritableRaster raster = image.getRaster();
			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					raster.getPixel(x, y, rgba);
					map[y][x].set(f, rgba[0], rgba[1], rgba[2]);
				}
			}
		}

		BufferedImage computedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		WritableRaster computedRaster = computedImage.getRaster();
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				PixelRGB averaged = map[y][x].getWithinStdDev(.5);
				rgba[0] = (int) MathExt.round(averaged.red);
				rgba[1] = (int) MathExt.round(averaged.green);
				rgba[2] = (int) MathExt.round(averaged.blue);
				rgba[3] = 255;
				computedRaster.setPixel(x, y, rgba);
			}
		}
		
		ImageWriter.saveImage(computedImage, saveTo);
	}
	
	public static double intensity(int r, int g, int b) {
		double v = r * 0.2989 + g * 0.587 + b * 0.114;
		return v;
	}
	
	public static void clearPixelBuffer(int[][][] pixels) {
		for (int y = 0; y < 960; y++) {
			for (int x = 0; x < 1920; x++) {
				for (int z = 0; z < 3; z++) {
					pixels[y][x][z] = 0;
				}
			}
		}
	
	}
	
	public static BufferedImage loadMinIntensityImage(File file) throws Exception {
		return loadImage(file.getAbsolutePath());
	}
	
	
	public static BufferedImage loadImage(String path) throws Exception {
		BufferedImage image = ImageIO.read(new File(path));
		return image;
	}
	
}
