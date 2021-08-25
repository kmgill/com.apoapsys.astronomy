package com.apoapsys.astronomy.utilities.hama;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.utilities.StarMapGenerator.Star;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

/** This is all wrong
 * 
 * @author kgill
 *
 */
public class HamaProblemVisualizer extends JPanel {
	
	private static double MAX_Y = 1.50;
	private static double MIN_Y = -1.50;
	
	private static double MAX_X = 90;
	private static double MIN_X = 0;
	
	private static double TIME_STEP = 0.001;
	
	private long lastDraw = System.currentTimeMillis();
	private double time = 0.0;
	
	private long lastRelease = 0;
	
	//private List<Particle> particles = Lists.newArrayList();
	
	//private ParticleList zeroList = new ParticleList(0.0);
	//private ParticleList lowerList = new ParticleList(-0.5);
	
	private List<FlowLineList> particleFlowLines = Lists.newArrayList();
	
	private BufferedImage canvas;
	
	private final int width;
	private final int height;
	
	public HamaProblemVisualizer(int width, int height) {
		this.width = width;
		this.height = height;
		
		double r = 1.0;
		double b = 0.0;
		
		double max = 1.5;
		double min = -1.5;
		double deltaY = .01;

		double step =  (1.0 / ((max - min) / deltaY + 1.0));
		
		
		for (double y = max; y >= min; y-=deltaY) {
			
			
			particleFlowLines.add(new FlowLineList(y, new Color((float)(r-=step), 0.0f, (float)(b+=step))));
		}

		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics = (Graphics2D) canvas.getGraphics();
		imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		time = 0.0;
		int pct = 0;
		
		double maxTime = 50;
		
		while(time < maxTime) {
			releaseParticles(time);
			stepParticles();
			//draw(imageGraphics);
			//time += 0.025;
			time += 0.05;
			int p = (int) MathExt.round(time / maxTime * 100.0);
			//System.err.println((time / 150.0 * 100.0) + "%");
			if (p > pct) {
				System.err.println(p + "%  -- Particle Count: " + particleCount());
				pct = p;
			}
			
			//if (pct > 40) {
			//	break;
			//}
			
		}
		
		
		List<Particle> particles = allParticles();
		Ordering<Particle> rowOrdering = new Ordering<Particle>() {
			public int compare(Particle left, Particle right) {
				return Doubles.compare(left.y(), right.y());
			}
		};
		Ordering<Particle> columnOrdering = new Ordering<Particle>() {
			public int compare(Particle left, Particle right) {
				return Doubles.compare(left.x(), right.x());
			}
		};
		Collections.sort(particles, columnOrdering);
		Collections.sort(particles, rowOrdering);
		
		try {
			OutputStream out = new FileOutputStream(new File("D:\\tmp\\particles.txt"));
			
			for (Particle particle : particles) {
				String xyz = ""+(particle.x() / 10.0)+", 0.0, " + particle.y()+"\r\n";
				out.write(xyz.getBytes());
			}
			out.flush();
			out.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		particleFlowLines.clear();
		
		draw(imageGraphics, particles);
	}
	
	public void releaseParticle(FlowLineList list, double time) {
		list.particles.add(new Particle(list.initialY, 1.0, time, list.color));
	}
	
	@Override
	public void paint(Graphics g) {
		
		if (canvas == null) {
			canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(canvas, 0, 0, null);

	}
	
	private void releaseParticles(double time) {
		for (FlowLineList list : particleFlowLines) {
			releaseParticle(list, time);
		}
	}
	
	private void draw(Graphics2D g2d, List<Particle> particles) {
		g2d.setColor(Color.WHITE);
		//g2d.fillRect(0, 0, getWidth(), 40);
		g2d.fillRect(0, 0, width, height);
		

		//drawList(g2d, particles);
		
		double stepX = (MAX_X - MIN_X) / (double) width;
		double stepY = (MAX_Y - MIN_Y) / (double) height;
		
		for (double x = MIN_X; x < MAX_X; x+=stepX) {
			for (double y = MIN_Y; y < MAX_Y; y+=stepY) {
				NearestParticle np0 = findNearestParticle(particles, x, y, 0);
				NearestParticle np1 = findNearestParticle(particles, x, y, np0.distance);
				NearestParticle np2 = findNearestParticle(particles, x, y, np1.distance);
				
				
				if (np0 != null && np1 != null && np2 != null) {
					
					double td = np0.distance + np1.distance + np2.distance;
					double d0 = 1.0 - (np0.distance / td);
					double d1 = 1.0 - (np1.distance / td);
					double d2 = 1.0 - (np2.distance / td);
					
					int r = (int) Math.round((double)np0.particle.color.getRed() * d0 + (double)np1.particle.color.getRed() * d1 + (double)np2.particle.color.getRed() * d2);
					int g = (int) Math.round((double)np0.particle.color.getGreen() * d0 + (double)np1.particle.color.getGreen() * d1 + (double)np2.particle.color.getGreen() * d2);
					int b = (int) Math.round((double)np0.particle.color.getBlue() * d0 + (double)np1.particle.color.getBlue() * d1 + (double)np2.particle.color.getBlue() * d2);
					
					r = (int) MathExt.clamp(r, 0, 255);
					g = (int) MathExt.clamp(g, 0, 255);
					b = (int) MathExt.clamp(b, 0, 255);
					
					Color color = new Color(r, g, b);
					
					
					int X = (int) MathExt.round((x - MIN_X) / (MAX_X - MIN_X) * width);
					int Y = (int) MathExt.round((1.0 - (y - MIN_Y) / (MAX_Y - MIN_Y)) * height);
					
					g2d.setColor(color);
					g2d.fillOval(X-1, Y-1, 2, 2);
					
				} else {
					System.err.println("What?");
				}
				
			}
		}
		
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 250, 20);
		
		g2d.setColor(Color.BLACK);
		g2d.drawString("T = " + (Math.round(time * 1000.0) / 1000.0) + ", Particle Count: " + particleCount(), 5, 12);

	}
	
	private NearestParticle findNearestParticle(List<Particle> particles, double x, double y, double minimum) {
		
		Particle nearest = null;
		double minDist = 100000;
		for (Particle particle : particles) {
			double r = MathExt.sqrt(MathExt.sqr(x - particle.x()) + MathExt.sqr(y - particle.y()));
			//double r = MathExt.sqrt(MathExt.sqr(x) + MathExt.sqr(y)) - MathExt.sqrt(MathExt.sqr(particle.x()) + MathExt.sqr(particle.y()));
			r = MathExt.abs(r);
			if ((nearest == null || r < minDist) && r > minimum) {
				nearest = particle;
				minDist = r;
			}
		}
		
		return new NearestParticle(nearest, minDist);
	}
	
	class NearestParticle {
		Particle particle;
		double distance;
		public NearestParticle(Particle particle, double distance) {
			this.particle = particle;
			this.distance = distance;
		}
	}
	
	private int particleCount() {
		int c = 0;
		for (FlowLineList particleFlowLine : particleFlowLines) {
			c += particleFlowLine.particles.size();
		}
		
		return c;
	}
	
	private void drawList(Graphics2D g2d, List<Particle> particles) {

		int lastX = -9999;
		int lastY = -1;
		
		
		Paint defaultPaint = g2d.getPaint();
		
		
		
		for (Particle particle : particles) {
			//particle.step(time);
			
			
			
			int X = (int) MathExt.round((particle.x() - MIN_X) / (MAX_X - MIN_X) * width);
			int Y = (int) MathExt.round((1.0 - (particle.y() - MIN_Y) / (MAX_Y - MIN_Y)) * height);
			
			
			if (lastX != -9999) {
			//	g2d.setColor(Color.GRAY);
			//	g2d.drawLine(X, Y, lastX, lastY);
			}
			
			
			double radius = 4;
			int size = (int) Math.ceil(radius);
			
			Point2D center = new Point2D.Double(X+(radius / 2.0), Y+(radius / 2.0));
			
			g2d.setColor(particle.color);

			Color transparent = new Color(particle.color.getRed(), particle.color.getGreen(), particle.color.getBlue(), 0);
			
			
			float[] dist = {0.0f, 0.4f, 1.0f};
			Color[] colors = {particle.color, particle.color, transparent};
			RadialGradientPaint p = new RadialGradientPaint(center, (float) radius / 2.0f, dist, colors);
			g2d.setPaint(p);
			
			g2d.fillOval((int)Math.round(X), (int)Math.round(Y), size, size);
			//g2d.fillOval(X-1, Y-1, 2, 2);
			lastX = X;
			lastY = Y;
			
		}
		
		
		g2d.setPaint(defaultPaint);
		
		//int X = (int) MathExt.round((0.0 - MIN_X) / (MAX_X - MIN_X) * getWidth());
		//int Y = (int) MathExt.round((1.0 - (list.initialY - MIN_Y) / (MAX_Y - MIN_Y)) * getHeight());
		//g2d.setColor(Color.GRAY);
		//g2d.drawLine(X, Y, lastX, lastY);
		
	}
	
	private void stepParticles() {
		List<Particle> staleParticles = Lists.newArrayList();
		
		int removed = 0;
		
		for (FlowLineList particleFlowLine : particleFlowLines) {
			for (Particle particle : particleFlowLine.particles) {
				particle.step(time);
				
				if (particle.x() > MAX_X + 3) {
					staleParticles.add(particle);
				}
			}
			
			if (staleParticles.size() > 0) {
				removed += staleParticles.size();
				particleFlowLine.particles.removeAll(staleParticles);
				staleParticles.clear();
			}
		}
		
		//if (removed > 0) {
		//	System.err.println("Removing " + removed + " stale particles");
		//}
	}
	
	private List<Particle> allParticles() {
		List<Particle> particles = Lists.newArrayList();
		for (FlowLineList particleFlowLine : particleFlowLines) {
			particles.addAll(particleFlowLine.particles);
		}
		return particles;
	}
	
	
	static class FlowLineList {
		
		public List<Particle> particles = Lists.newArrayList();
		public double initialY = 0.0;
		public Color color;
		
		public FlowLineList(double y, Color color) {
			this.initialY = y;
			this.color = color;
			particles = particles;
		}
		
	}
	
	static class Particle {
		
		private double x = 0.0;
		private double y;
		private double velocity = 1.0;
		private double lastTime = 0.0;
		private double initialY = 0.0;
		private Color color;
		
		public Particle(double y, double velocity, double startTime, Color color) {
			this.y = y;
			this.initialY = y;
			this.x = 0.0;
			this.velocity = velocity;
			this.lastTime = startTime;
			this.color = color;
		}
		
		public double u0() {
			return FreeShearFlow.u(y);
		}
		
		public double v0() {
			return FreeShearFlow.v();
		}
		
		public double u(double t) {
			return TravelingWave.u(0.015, x, y, t);
		}
		
		public double v(double t) {
			return TravelingWave.v(0.015, x, y, t);
		}
		
		public double x() {
			return x;
		}
		
		public double y() {
			return y;
		}
		
		public void step(double t) {
			
			double v0 = v0();
			double u0 = u0();
			
			double deltaT = t - lastTime;
			
			double v = v(t);
			double u = u(t);// * 1000;
			
			//v /= Math.PI * 2.0;
			//u /= Math.PI * 2.0;
			
			//v0 /= Math.PI * 2.0;
			//u0 /= Math.PI * 2.0;
			
			u *= deltaT * velocity;
			u0 *= deltaT * velocity;
			
			v *= deltaT * velocity;
			v0 *= deltaT * velocity;
			u = MathExt.degrees(u);
			//u0 = 0;
			//System.err.println("u: " + u);
			x += (u0 + u) / Math.PI * 2.0 ;
			y += (v0 + v) / Math.PI * 2.0;
			
			lastTime = t;
			
			//System.out.println("x: " + x + ", y: " + y + ", u0: " + u0 + ", v0: " + v0 + ", u: " + u + ", v: " + v + ", t: " + t + ", deltaT: " + deltaT);
		}
		
		
	}

}
