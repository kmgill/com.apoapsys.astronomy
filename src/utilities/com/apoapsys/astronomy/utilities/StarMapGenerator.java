package com.apoapsys.astronomy.utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.coords.Horizon;
import com.apoapsys.astronomy.geo.exception.MapProjectionException;
import com.apoapsys.astronomy.geo.projection.EquirectangularProjection;
import com.apoapsys.astronomy.geo.projection.MapPoint;
import com.apoapsys.astronomy.geo.projection.MapProjection;
import com.apoapsys.astronomy.image.ImageException;
import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

public class StarMapGenerator {
	
	private static Color SPEC_CLASS_O = new Color(0x9db4ff);
	private static Color SPEC_CLASS_B = new Color(0xaabfff);
	private static Color SPEC_CLASS_A = new Color(0xcad8ff);
	private static Color SPEC_CLASS_F = new Color(0xfbf8ff);
	private static Color SPEC_CLASS_G = new Color(0xfff4e8);
	private static Color SPEC_CLASS_K = new Color(0xffddb4);
	private static Color SPEC_CLASS_M = new Color(0xffbd6f);
	private static Color SPEC_CLASS_L = new Color(0xf84235);
	private static Color SPEC_CLASS_T = new Color(0xba3059);
	private static Color SPEC_CLASS_Y = new Color(0x605170);
	
	private Color transparent = new Color(0, 0, 0, 0);
	private MapPoint point = new MapPoint();
	
	private int width;
	private int height;
	private MapProjection projection;
	private BufferedImage image;
	private Graphics2D g2d;
	
	private double radiusMultiple = 1.4;
	
	private double minimumMagnitude;
	private double maximumMagnitude;
	
	private double maxStarWidth;
	private double minStarWidth;
	
	private double maximumLatitude = 90;
	private double minimumLatitude = -90;
	private double maximumLongitude = 180;
	private double minimumLongitude = -180;
	
	private List<Star> stars = Lists.newArrayList();
	private List<Planet> planets = Lists.newArrayList();
	
	public StarMapGenerator(int width, int height, double minimumMagnitude, double maximumMagnitude, double minStarWidth, double maxStarWidth) {
		this.width = width;
		this.height = height;
		this.minimumMagnitude = minimumMagnitude;
		this.maximumMagnitude = maximumMagnitude;
		this.minStarWidth = minStarWidth;
		this.maxStarWidth = maxStarWidth;
		
		//AitoffProjection
		//WinkelTripelProjection
		//MollweideProjection
		projection = new EquirectangularProjection(90, -90, 180, -180, width, height);
		//projection = new MollweideProjection(90, -90, 180, -180, width, height);
		
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D) image.getGraphics();


	}
	
	
	public void save(String path) throws ImageException {
		ImageWriter.saveImage(image, path);
	}
	
	public void addStar(double latitude, double longitude, double visualMagnitude, double bv) throws MapProjectionException {
		String spectralClass = bvToSpectralClass(bv);
		addStar(latitude, longitude, visualMagnitude, spectralClass);
	}
	
	public void addStar(double latitude, double longitude, double visualMagnitude, String spectralClass) throws MapProjectionException {
		Star star = new Star(new Ecliptic(Angle.fromDegrees(latitude), Angle.fromDegrees(longitude)), visualMagnitude, spectralClass);
		addStar(star);
	}
	
	public void addStar(Star star) {
		stars.add(star);
	}
	
	public void addStars(List<Star> stars) {
		this.stars.addAll(stars);
	}
	
	public void addPlanet(Planet planet) {
		this.planets.add(planet);
	}
	
	public void render() throws Exception {
		Ordering<Star> starOrdering = new Ordering<Star>() {
			public int compare(Star left, Star right) {
				return Doubles.compare(left.visualMagnitude, right.visualMagnitude);
			}
		};
		
		System.err.println("Is Ordered: " + starOrdering.isOrdered(stars));
		Collections.sort(stars, starOrdering);
		System.err.println("Is Ordered: " + starOrdering.isOrdered(stars));
		Collections.reverse(stars);
		System.err.println("Is Ordered: " + starOrdering.reverse().isOrdered(stars));
		
		
		// Render Background
		System.err.println("Rendering image background...");
	//	g2d.setColor(Color.WHITE);
		//g2d.fillRect(0, 0, width, height);
		
		
		// Render projection background
		System.err.println("Rendering projection background...");

		double lonStep = 0.25;
		double latStep = 0.25;
		
		double north = 90;
		double south = -90;
		double east = 180;
		double west = -180;
		/*
		Path2D path = new Path2D.Double();
		g2d.setColor(Color.BLACK);
		for (double latitude = north; latitude > south; latitude-=latStep) {
			for (double longitude = west; longitude < east; longitude+=lonStep) {
				
				path.reset();
				projection.getPoint(latitude, longitude, 0, point);
				path.moveTo(getMapX(point.column), getMapY(point.row));
				
				projection.getPoint(latitude-latStep, longitude, 0, point);
				path.lineTo(getMapX(point.column), getMapY(point.row)+1.0);
				
				projection.getPoint(latitude-latStep, longitude+lonStep, 0, point);
				path.lineTo(getMapX(point.column)+1.0, getMapY(point.row)+1.0);
				
				projection.getPoint(latitude, longitude+lonStep, 0, point);
				path.lineTo(getMapX(point.column)+1.0, getMapY(point.row));
				
				path.closePath();
				g2d.fill(path);
			}
		}
		*/
		
		g2d.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
		        RenderingHints.KEY_FRACTIONALMETRICS,
		        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		System.err.println("Rendering " + stars.size() + " stars to image...");
		for (Star star : stars) {
			//if (star.coordinates.getAltitude().getDegrees() >= 0) {
				renderStar(star);
			//}
		}
		
		System.err.println("Rendering " + planets.size() + " planets to image...");
		for (Planet planet : planets) {
			if (planet.coordinates.getAltitude().getDegrees() >= 0) {
				renderPlanet(planet);
			}
		}
		
		/*
		Color atmosphere0 = new Color(80, 52, 110, 20);
		Color atmosphere1 = new Color(80, 52, 110, 40);
		Color atmosphere2 = new Color(53, 52, 110, 100);
		drawGradient(atmosphere0, atmosphere1, atmosphere2, new float[]{0.0f, 0.5f, 1.0f});
		
		Color lightPollution0 = new Color(0, 0, 0, 0);
		Color lightPollution1 = new Color(255, 220, 159, 20);
		Color lightPollution2 = new Color(255, 220, 159, 100);
		drawGradient(lightPollution0, lightPollution1, lightPollution2, new float[]{0.0f, 0.55f, 1.0f});
		*/
		
		
		//BufferedImage image = ImageIO.read(new File("C:/jdem/temp/spherical-horizon-template-1920x1080-fullsize.png"));
		//g2d.drawImage(image, 0, 0, null);

	}
	
	protected void drawGradient(Color a, Color b, Color c, float[] dist) {
		
		Paint defaultPaint = g2d.getPaint();
		double radius = height / 2.0;
		Point2D center = new Point2D.Double(width / 2.0, radius);
		Color[] colors = {a, b, c};
		RadialGradientPaint p = new RadialGradientPaint(center, (float) (height / 2.0f * radiusMultiple), dist, colors);

		g2d.setPaint(p);
		g2d.fillOval((int)(width/2.0 - height / 2.0 * radiusMultiple), 0, (int) (height * radiusMultiple), (int) (height * radiusMultiple));
		g2d.setPaint(defaultPaint);
	}
	
	protected double getMapX(double lon) {
		double x = ((lon + 180) / 360) * width;
		return x;
	}
	
	protected double getMapY(double lat) {
		double y = height - ((point.row + 90) / 180) * height;
		return y;
	}
	
	protected void renderPlanet(Planet planet) throws MapProjectionException {
		//double latitude = planet.coordinates.getLatitude().getDegrees();
		//double longitude = planet.coordinates.getLongitude().getDegrees();
		
		//projection.getPoint(latitude, longitude, 0, point);
		
		//double x = getMapX(point.column);
		//double y = getMapY(point.row);
		
		//double R = ((width > height) ? height : width) / 4.0;
		double R = (height / 4.0) * radiusMultiple;
		double L = -planet.coordinates.getAzimuth().getRadians() + Constants.RAD_90;
		
		double b = planet.coordinates.getAltitude().getRadians();

		double x =  -2 * R * MathExt.tan(.5 *  (Constants.RAD_90 - b)) * MathExt.cos(L);
		double y =  -2 * R * MathExt.tan(.5 * (Constants.RAD_90 - b))  * MathExt.sin(L);
		
		x += width / 2.0;
		y += height / 2.0;
		
		Color colorOpaque = planet.color;
		Color colorAlpha = new Color(colorOpaque.getRed(), colorOpaque.getGreen(), colorOpaque.getBlue(), 0);
		
		double radius = planet.radius;//maxStarWidth * 1.0;
		int size = (int) Math.ceil(radius * 2);
		
		Point2D center = new Point2D.Double(x+radius-1, y+radius-1);
		
		Paint defaultPaint = g2d.getPaint();
		
		
		
		float[] dist = {0.0f, 0.4f, 1.0f};
		Color[] colors = {colorOpaque, colorOpaque, colorAlpha};
		RadialGradientPaint p = new RadialGradientPaint(center, (float) radius + 1.0f, dist, colors);
		

		g2d.setPaint(p);
		//g2d.setColor(c);
		g2d.fillOval((int)Math.round(x-1), (int)Math.round(y-1), size+2, size+2);
		
		
		g2d.setPaint(defaultPaint);
		g2d.setColor(colorOpaque);
		g2d.fillOval((int)Math.round(x), (int)Math.round(y), size, size);
	}
	
	protected void renderStar(Star star) throws MapProjectionException {
		
		//double latitude = star.coordinates.getLatitude().getDegrees();
		//double longitude = star.coordinates.getLongitude().getDegrees();
		double visualMagnitude = star.visualMagnitude;
		String spectralClass = star.spectralClass;
		
		double initialAlpha = 1.0 - ((visualMagnitude - minimumMagnitude) / (maximumMagnitude - minimumMagnitude));
		
		//alpha *= 6.3;
		//System.err.println(visualMagnitude + ", " + alpha + ", " + minimumMagnitude + ", " + maximumMagnitude);
		initialAlpha = MathExt.clamp(initialAlpha, 0, 1);
		double alpha = scaleAlphaLog(initialAlpha);
		
		int a = (int) Math.round(alpha * 255.0);
		
		Color colorOpaque = getColor(spectralClass);
		Color colorAlpha = new Color(colorOpaque.getRed(), colorOpaque.getGreen(), colorOpaque.getBlue(), a);

		/*
		double R = (height / 4.0) * radiusMultiple;
		double L = -star.coordinates.getAzimuth().getRadians() + Constants.RAD_90;
		
		double b = star.coordinates.getAltitude().getRadians();

		double x =  -2 * R * MathExt.tan(.5 *  (Constants.RAD_90 - b)) * MathExt.cos(L);
		double y =  -2 * R * MathExt.tan(.5 * (Constants.RAD_90 - b))  * MathExt.sin(L);
		*/
		
		double xRange = this.maximumLongitude - this.minimumLongitude;
		double yRange = this.maximumLatitude - this.minimumLatitude;
		//double halfX
		
		//double x = (1.0 - (star.coordinates.getLongitude().getDegrees() + (xRange / 2.0)) / xRange) * width;
		double x = (1.0 - (star.coordinates.getLongitude().getDegrees() - this.minimumLongitude) / xRange) * width;
		double y = (1.0 - (star.coordinates.getLatitude().getDegrees() - this.minimumLatitude) / yRange) * height;
		//double y = (1.0 - (star.coordinates.getLatitude().getDegrees() + 90.0) / 180.0) * height;
		
		
		//x += width / 2.0;
		//y += height / 2.0;
		
		double radius = maxStarWidth * alpha;
		int size = (int) Math.ceil(radius);
		
		Point2D center = new Point2D.Double(x+(radius / 2.0), y+(radius / 2.0));
		
		Paint defaultPaint = g2d.getPaint();
		
		float[] dist = {0.0f, 0.4f, 1.0f};
		Color[] colors = {colorAlpha, colorAlpha, transparent};
		RadialGradientPaint p = new RadialGradientPaint(center, (float) radius / 2.0f, dist, colors);
		

		g2d.setPaint(p);
		//g2d.setColor(c);
		g2d.fillOval((int)Math.round(x), (int)Math.round(y), size, size);
		
		g2d.setPaint(defaultPaint);
		//g2d.setColor(colorOpaque);
		//g2d.fillOval((int)Math.round(x), (int)Math.round(y), 2, 2);
	}
	

	public static double scaleAlphaLog(double alpha) {
		double range = 1.0;

		double rangeLog = Math.pow(10, range);
		double alphaLog = Math.pow(10, alpha);
		double alphaLogRatio = alphaLog / rangeLog;
	
		return MathExt.clamp(alphaLogRatio + 0.1, 0.0, 1.0);
	}
	
	
	public static String bvToSpectralClass(double bv) {
		if (bv <= -0.04) {
			return "B";
		} else if (bv <= 0.3) {
			return "A";
		} else if (bv <= 0.53) {
			return "F";
		} else if (bv <= 0.74) {
			return "G";
		} else if (bv <= 1.33) {
			return "K";
		} else if (bv <= 1.52) {
			return "M";
		} else {
			return "Y";
		}
	}
	
	

	
	public static Color getColor(String spectralClass) {
		Color c = Color.WHITE;
		if (spectralClass == null) {
			return c;
		}
		
		if (spectralClass.equalsIgnoreCase("O")) {
			c = SPEC_CLASS_O;
		} else if (spectralClass.equalsIgnoreCase("B")) {
			c = SPEC_CLASS_B;
		} else if (spectralClass.equalsIgnoreCase("A")) {
			c = SPEC_CLASS_A;
		} else if (spectralClass.equalsIgnoreCase("F")) {
			c = SPEC_CLASS_F;
		} else if (spectralClass.equalsIgnoreCase("G")) {
			c = SPEC_CLASS_G;
		} else if (spectralClass.equalsIgnoreCase("K")) {
			c = SPEC_CLASS_K;
		} else if (spectralClass.equalsIgnoreCase("M")) {
			c = SPEC_CLASS_M;
		} else if (spectralClass.equalsIgnoreCase("L")) {
			c = SPEC_CLASS_L;
		} else if (spectralClass.equalsIgnoreCase("T")) {
			c = SPEC_CLASS_T;
		} else if (spectralClass.equalsIgnoreCase("Y")) {
			c = SPEC_CLASS_Y;
		}
		
		
		return c;
	}
	
	
	public static class Star {
		public Ecliptic coordinates;
		public double visualMagnitude;
		public String spectralClass;
		
		public Star(Ecliptic coordinates, double visualMagnitude, double bv) {
			this(coordinates, visualMagnitude, bvToSpectralClass(bv));
		}
		
		public Star(Ecliptic coordinates, double visualMagnitude, String spectralClass) {
			this.coordinates = coordinates;
			this.visualMagnitude = visualMagnitude;
			this.spectralClass = spectralClass;
		}
		
	}
	
	
	public static class Planet {
		public Horizon coordinates;
		public Color color;
		public int radius;
		
		public Planet(Horizon coordinates, Color color, int radius) {
			this.color = color;
			this.coordinates = coordinates;
			this.radius = radius;
		}
	}


	public double getMaximumLatitude() {
		return maximumLatitude;
	}


	public void setMaximumLatitude(double maximumLatitude) {
		this.maximumLatitude = maximumLatitude;
	}


	public double getMinimumLatitude() {
		return minimumLatitude;
	}


	public void setMinimumLatitude(double minimumLatitude) {
		this.minimumLatitude = minimumLatitude;
	}


	public double getMaximumLongitude() {
		return maximumLongitude;
	}


	public void setMaximumLongitude(double maximumLongitude) {
		this.maximumLongitude = maximumLongitude;
	}


	public double getMinimumLongitude() {
		return minimumLongitude;
	}


	public void setMinimumLongitude(double minimumLongitude) {
		this.minimumLongitude = minimumLongitude;
	}


	public double getMinimumMagnitude() {
		return minimumMagnitude;
	}


	public void setMinimumMagnitude(double minimumMagnitude) {
		this.minimumMagnitude = minimumMagnitude;
	}


	public double getMaximumMagnitude() {
		return maximumMagnitude;
	}


	public void setMaximumMagnitude(double maximumMagnitude) {
		this.maximumMagnitude = maximumMagnitude;
	}
	
	
	
}
