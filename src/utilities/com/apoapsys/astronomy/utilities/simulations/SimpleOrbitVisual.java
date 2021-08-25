package com.apoapsys.astronomy.utilities.simulations;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.bodies.OrbitingBody;
import com.apoapsys.astronomy.image.ImageException;
import com.apoapsys.astronomy.image.ImageWriter;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.math.Vectors;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.Collision;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.LeapFrogSimulator;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.time.JulianUtil;
import com.apoapsys.astronomy.util.ByteConversions;
import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class SimpleOrbitVisual extends JFrame implements SimulationWorker {
	
	protected LeapFrogSimulator simulator;
	protected List<TrackedBody> tracked = Lists.newArrayList();
	protected double maxDistance = 2.0 * Constants.AU_TO_KM * 1000;
	protected double defaultDistance = 2.0 * Constants.AU_TO_KM * 1000;
	
	protected TrackedBody centerObjectTrack;
	protected TrackedBody sunTrack;
	protected TrackedBody focusObjectTrack;
	
	protected SteppingThread thread;
	protected int iterStep = 0;
	protected double deltaT = 1.0;
	
	protected double timePassed = 0;
	protected double startTime = 0;
	protected Particle forceSampleState;
	protected boolean drawGravitationalPotentialGradient = false;
	
	protected int mouseX = -1;
	protected int mouseY = -1;
	
	protected int introducedParticles = 0;
	protected int queueSize = 250;
	
	protected int shiftX = 0;
	protected int shiftY = 0;
	
	protected boolean showTrails = true;
	
	protected BufferedImage buffer = null;
	protected Double imageMutex = new Double(0);
	protected long lastDraw = System.currentTimeMillis();
	protected long writeDelayMillis = 150;
	protected double iterationsPerPositionSampling = 8000;
	
	protected boolean drawElementsBasedOrbits = true;
	
	protected ForceSample forceSample = null;
	
	protected List<SimulationEventHandler> eventHandlers = Lists.newArrayList();
	
	protected GravitationColorGradient colorGradient = new HypsometricGravitationColorGradient();
	
	protected PrintWriter dataWriter;
	
	public SimpleOrbitVisual(LeapFrogSimulator simulator) throws Exception {
		this(simulator, null);
	}
	
	public SimpleOrbitVisual(LeapFrogSimulator simulator, List<TrackedBody> tracked) throws Exception {
		super("Test");
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		DateTime dt = new DateTime();
		
		
		//dataWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream("D:/tmp/AntiEarth Data/data.csv")));
		
		//simulator = new LeapFrogSimulator();
		//simulator.addSimulationForceProvider(new NewtonianGravityForceProviderImpl());
		this.simulator = simulator;
		if (tracked != null) {
			this.tracked = Collections.synchronizedList(tracked);
		} else {
			this.tracked = Collections.synchronizedList(this.tracked);
		}
		
		
		
		OrbitingBody newBody = new OrbitingBody();
		newBody.setName(UUID.randomUUID().toString());
		newBody.setMass(1E4);
		newBody.setRadius(1000);
		forceSampleState = BaseSimulation.createPreInitialState(newBody, new DateTime());
		forceSampleState.position.x = 0;
		forceSampleState.position.y = 0;
		forceSampleState.position.z = 0;
		forceSampleState.velocity.x = 0;
		forceSampleState.velocity.y = 0;
		forceSampleState.velocity.z = 0;

		thread = new SteppingThread();
		thread.start();
		startTime = System.currentTimeMillis();
		
		Timer timer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
		
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'P' || e.getKeyChar() == 'p') {
					boolean paused = thread.isPaused(); // Seperate these to avoid synch deadlock
					thread.setPaused(!paused);
				} else if (e.getKeyChar() == 'F' || e.getKeyChar() == 'f') {
					deltaT *= 1.1;
					System.err.println("Increased deltaT to " + deltaT + " seconds");
				} else if (e.getKeyChar() == 'S' || e.getKeyChar() == 's') {
					deltaT *= 0.9;
					System.err.println("Decreased deltaT to " + deltaT + " seconds");
				} else if (e.getKeyChar() == 'C' || e.getKeyChar() == 'c') {
					centerObjectTrack = null;
				} else if (e.getKeyChar() == 'J' || e.getKeyChar() == 'j') {
					createSaveFile();
				}
				
			}

		});
		
		
		
		class MouseActionListener implements MouseMotionListener, MouseListener, MouseWheelListener {
			
			
			int lastX = -1;
			int lastY = -1;
			
			Random random = new Random(System.currentTimeMillis());
			
			public MouseActionListener() {

			}
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double delta = e.getPreciseWheelRotation();
				maxDistance += maxDistance * (delta * 0.1);
				
				shiftX += shiftX * (delta * 0.1);
				shiftY += shiftY * (delta * 0.1);
				System.err.println("Dist: " + maxDistance);

				if (thread.isPaused()) {
					updateForceMatrix();
					drawSceneSychronized();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (lastX != -1 && lastY != -1) {
					int dX = e.getX() - lastX;
					int dY = e.getY() - lastY;
					shiftX += dX;
					shiftY += dY;
					System.err.println(shiftX + ", " + shiftY);
				}
				lastX = e.getX();
				lastY = e.getY();
				

				if (thread.isPaused()) {
					updateForceMatrix();
					drawSceneSychronized();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				
			}
			

			@Override
			public void mouseExited(MouseEvent e) {
				mouseX = -1;
				mouseY = -1;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					onDoubleClick();
				} else if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
					double mouseX = e.getX();
					double mouseY = e.getY();

					Vector map = screenCoordinatesToMap((int)mouseX, (int)mouseY);

					introduceParticle("Particle #" + (++introducedParticles),
							new Vector(map.x, 0.0, map.y),
							new Vector(30000, 0, 0),
							sunTrack.particle.body.getMass() * 2 * random.nextDouble(),//1898.13E+24 * 5,
							sunTrack.particle.body.getRadius());
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				lastX = -1;
				lastY = -1;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}


			
		}
		MouseActionListener mouseListener = new MouseActionListener();
		
		this.addMouseMotionListener(mouseListener);
		this.addMouseListener(mouseListener);
		this.addMouseWheelListener(mouseListener);
	}
	
	
	public void createSaveFile() {
		
		int width = 2000;
		int height = 2000;
		
		System.err.println(shiftX);
		System.err.println(shiftY);
		System.err.println(maxDistance);
		
		//shiftX += 1000;
		//shiftY -= 1000;
		/*
		BufferedImage image = null;
		synchronized(tracked) {
			ForceSample sample = getForceMatrix(width, height);
			
			sample.maxForce = normalizeForce(sample.maxForce);
			sample.minForce = normalizeForce(sample.minForce);
			
			sample.maxForce = (Double.isInfinite(sample.maxForce) || Double.isNaN(sample.maxForce)) ? 0.0 : sample.maxForce;
			sample.minForce = (Double.isInfinite(sample.minForce) || Double.isNaN(sample.minForce)) ? 0.0 : sample.minForce;
			
			image = drawScene(width, height, sample);
		}
		*/
		try {
			ImageWriter.saveImage(this.buffer, "C:\\jdem\\temp\\grav-" + System.currentTimeMillis() + ".png");
		} catch (ImageException e) {
			e.printStackTrace();
		}
		
		System.err.println("Completed Image");
	}
	
	@Override
	public void beginSimulation() {
		this.setVisible(true);
	}
	
	protected void clearTracks() {
		for (TrackedBody trackedBody : tracked) {
			trackedBody.positions.clear();
		}
	}
	
	protected void onDoubleClick() {
		for (TrackedBody trackedBody : tracked) {

			Vector screen = this.mapCoordinatesToScreen(trackedBody.particle.position, true, getWidth(), getHeight());
			
			if (isMouseNearPoint((int)screen.x, (int)screen.y)) {
				//clearTracks();
				centerObjectTrack = trackedBody;
				break;
			}
		}
	}
	
	public TrackedBody introduceParticle(String name, Vector pos, Vector vel, double mass, double radius) {
		return introduceParticle(name, pos, vel, mass, radius, true);
	}
	
	public TrackedBody introduceParticle(String name, Vector pos, Vector vel, double mass, double radius, boolean hypothetical) {
		//System.err.println("Introducing new object '" + name + "'!");
		introducedParticles++;
		
		
		OrbitingBody newBody = new OrbitingBody();
		newBody.setId(introducedParticles);
		newBody.setName(name);
		newBody.setMass(mass);
		newBody.setRadius(radius);
		Particle newBodyState = BaseSimulation.createPreInitialState(newBody, new DateTime());
		newBodyState.position.x = pos.x;
		newBodyState.position.y = pos.y;
		newBodyState.position.z = pos.z;
		newBodyState.velocity.x = vel.x;
		newBodyState.velocity.y = vel.y;
		newBodyState.velocity.z = vel.z;
		TrackedBody trackedBody = new TrackedBody(newBodyState, Color.WHITE, queueSize);
		trackedBody.isHypothetical = hypothetical;

		
		synchronized(thread.mutex) {
			synchronized(tracked) {
				tracked.add(trackedBody);
			}
			simulator.addParticle(newBodyState);
		}

		return trackedBody;
	}
	
	@Override
	public TrackedBody getTrackedBodyByName(String name) {
		for (TrackedBody trackedBody : tracked) {
			if (trackedBody.particle.body.getName().equalsIgnoreCase(name)) {
				return trackedBody;
			}
		}
		return null;
	}
	
	@Override
	public TrackedBody getTrackedBodyByParticle(Particle particle) {
		for (TrackedBody trackedBody : tracked) {
			if (trackedBody.particle.equals(particle)) {
				return trackedBody;
			}
		}
		return null;
	}
	
	protected void updateForceMatrix() {
		forceSample = getForceMatrix();
		
		if (forceSample != null) {
			//forceSample.maxForce = MathExt.min(forceSample.maxForce, 0.0);
			
			//forceSample.minForce = MathExt.abs(forceSample.maxForce);
			//forceSample.maxForce = MathExt.abs(forceSample.minForce);
			
			
			
			forceSample.maxForce = normalizeForce(forceSample.maxForce);
			forceSample.minForce = normalizeForce(forceSample.minForce);
			
			forceSample.maxForce = (Double.isInfinite(forceSample.maxForce) || Double.isNaN(forceSample.maxForce)) ? 0.0 : forceSample.maxForce;
			forceSample.minForce = (Double.isInfinite(forceSample.minForce) || Double.isNaN(forceSample.minForce)) ? 0.0 : forceSample.minForce;
		}
		int i = 0;
	}
	
	public void step() {
		iterStep ++;
		
		boolean doPaint = false;
		
		if (System.currentTimeMillis() - lastDraw >= writeDelayMillis) {
				
			if (drawGravitationalPotentialGradient) {
				updateForceMatrix();
			}
			
			doPaint = true;
			lastDraw = System.currentTimeMillis();
		}
		
		timePassed += deltaT;
		List<Collision> collisions = simulator.step(deltaT);
		
		double currentTime = startTime + (timePassed * 1000);
		Date date = new Date((long)MathExt.round(currentTime));
		
		
		if (collisions != null) {
			for (Collision collision : collisions) {
				TrackedBody body0 = getTrackedBodyByParticle(collision.particle0);
				TrackedBody body1 = getTrackedBodyByParticle(collision.particle1);
				
				System.err.println("Collision of " + collision.particle0.body.getName() + " and " + collision.particle1.body.getName() + " at " + date.toString());
				
				
				boolean makeCentral = (body0.particle.equals(centerObjectTrack.particle) || body1.particle.equals(centerObjectTrack.particle));
				
				tracked.remove(body0);
				tracked.remove(body1);
				
				TrackedBody trackedBody = new TrackedBody(collision.merged, Color.WHITE, queueSize);
				trackedBody.isHypothetical = (body0.isHypothetical && body1.isHypothetical);
	
				
				tracked.add(trackedBody);
				
				if (makeCentral) {
					centerObjectTrack = trackedBody;
				}
				
			}
		}
		
		
		

		synchronized(tracked) {
			
			Vector centerVector = null;
			if (centerObjectTrack != null && centerObjectTrack.particle.position != null) {
				centerVector = this.centerObjectTrack.particle.position;
			}
			
			if (iterStep % iterationsPerPositionSampling == 0) {
				for (TrackedBody trackedBody : tracked) {
				
					if (trackedBody.drawTrack) {
						Vector position = trackedBody.particle.position.clone();
						
						if (centerVector != null) {
							position.x -= centerVector.x;
							position.y -= centerVector.y;
							position.z -= centerVector.z;
						}
						
						trackedBody.positions.push(position);
					}
				}

				iterStep = 0;
			}
			

			//if (iterStep % 10000 == 0) {
			if (doPaint) {
				drawSceneSychronized();
			}
			//}
		}
	}
	
	
	
	protected void drawSceneSychronized() {
		BufferedImage image = drawScene(getWidth(), getHeight(), this.forceSample);
		synchronized(imageMutex) {
			buffer = image;
		}
	}
	
	class ForceSample {
		double[][] force;
		double minForce;
		double maxForce;
		
		public ForceSample(double[][] force, double minForce, double maxForce) {
			this.force = force;
			this.minForce = minForce;
			this.maxForce = maxForce;
		}
	}
	
	protected ForceSample getForceMatrix() {
		return getForceMatrix(getWidth(), getHeight());
	}
	
	protected ForceSample getForceMatrix(int width, int height) {
		
		if (forceSampleState == null || centerObjectTrack == null) {
			return null;
		}
		
		double[][] force = new double[height][width];
		
		double minForce = Double.NaN;
		double maxForce = Double.NaN;
		
		Particle[] particles = new Particle[tracked.size()];
		for (int i = 0; i < tracked.size(); i++) {
			TrackedBody tb = tracked.get(i);
			particles[i] = tb.particle;
		}
		
		double clampToMax = 514.9021855337;//307408421.997127333;
		double clampToMin = 0.0;
		
		TrackedBody earth = this.getTrackedBodyByName("Earth");
		TrackedBody antiEarth = this.getTrackedBodyByName("antiEarth");
		forceSampleState.velocity = earth.particle.velocity.clone();

		double halfX = width / 2.0 + 10.0;
		double halfY = height / 2.0;
		
		Vector f = new Vector();
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				Vector map = screenCoordinatesToMap(x, y, width, height);
				
				if (x == halfX && y == halfY) {
					System.err.println(centerObjectTrack.particle.position.getDistanceTo(map));
				}
				
				forceSampleState.position.x = map.x;
				forceSampleState.position.z = map.y;
				forceSampleState.velocity = earth.particle.velocity.clone();
				
				//if ( x == 100 && y == 100) {
					double a0 = centerObjectTrack.particle.position.angle(earth.particle.position);
					double a1 = centerObjectTrack.particle.position.angle(forceSampleState.position);
					double aDelta = a1 - a0;
					forceSampleState.velocity.rotate(aDelta, Vectors.Y_AXIS);
					//forceSampleState.velocity.multiplyScalar(1.2);
					//System.err.println(MathExt.degrees(a0) + " -- " + MathExt.degrees(a1) + " -- " + MathExt.degrees(aDelta));
					
			//}
				
				//forceSampleState.velocity = new Vector(0, 0, 0, 0);//earth.particle.velocity.clone();
				double earthForce = forceSampleState.getLagrangianForceThreeBody(centerObjectTrack.particle, earth.particle);
				
				//forceSampleState.velocity = antiEarth.particle.velocity.clone();
				//double antiEarthForce = forceSampleState.getLagrangianForceThreeBody(centerObjectTrack.particle, antiEarth.particle);
				
				force[y][x] = earthForce;//((earthForce + antiEarthForce) / 2.0);
				//force[y][x] = forceSampleState.getLagrangianForceThreeBody(centerObjectTrack.particle, earth.particle);
				//force[y][x] = forceSampleState.getTotal2DForceOnParticle(particles, centerObjectTrack.particle);
				//force[y][x] = forceSampleState.getEffectivePotentialEnergy(particles);
				
				//force[y][x] = MathExt.min(clampToMax, force[y][x]);
				//force[y][x] = MathExt.max(0, force[y][x]);
				
				//force[y][x] = MathExt.abs(force[y][x]);
				//force[y][x] = MathExt.clamp(force[y][x], clampToMin, clampToMax);
				
				minForce = MathExt.min(minForce, force[y][x]);
				maxForce = MathExt.max(maxForce, force[y][x]);

			}
		}

		return new ForceSample(force, minForce, maxForce);
	}

	
	protected double getDistanceFromCursor(int x, int y) {
		double dX = x - mouseX;
		double dY = y - mouseY;
		
		double d = MathExt.sqrt(dX*dX + dY*dY);
		
		//System.err.println(""+mouseX + ", " + mouseY + " ---  " + x + ", " + y + " --- " + dX + ", " + dY + " -- " + d);
		return d;
	}
	
	protected boolean isMouseNearPoint(int x, int y) {
		//System.err.println(""+mouseX + ", " + mouseY + " ---  " + x + ", " + y);
		if (mouseX == -1 || mouseY == -1) {
			return false;
		} else if (getDistanceFromCursor(x, y) <= 10) {
			return true;
		} else {
			return false;
		}
	}
	
	protected int getTextWidth(Graphics2D g2d, String text) {
		FontMetrics fm = g2d.getFontMetrics();
		return fm.stringWidth(text);
	}
	
	protected Vector screenCoordinatesToMap(int x, int y) {
		return screenCoordinatesToMap(x, y, getWidth(), getHeight());
	}
	
	protected Vector screenCoordinatesToMap(int x, int y, int width, int height) {
		
		double _x = (x - shiftX - (width / 2.0)) / (width / 2.0) * maxDistance;
		double _y = (y - shiftY - (height / 2.0)) / (height / 2.0) * maxDistance;
		
		if (centerObjectTrack != null) {
			Vector centerVector = focusObjectTrack.particle.position;
			_x += centerVector.x;
			_y += centerVector.z;
		}
		
		return new Vector(_x, _y, 0.0);
	}
	
	protected Vector mapCoordinatesToScreen(Vector mapCoords, boolean adjustToCenter, double width, double height) {

		double x = mapCoords.x;
		double y = mapCoords.z;
		
		if (adjustToCenter && focusObjectTrack != null) {
			Vector centerVector = focusObjectTrack.particle.position;
			x -= centerVector.x;
			y -= centerVector.z;
		}
		
		x = (x / maxDistance) * (width / 2.0) + (width / 2.0) + shiftX;
		y = (y / maxDistance) * (height / 2.0) + (height / 2.0) + shiftY;
		
		return new Vector(x, y, 0.0);
	}
	
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2dFrame = (Graphics2D) g;
		g2dFrame.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2dFrame.setRenderingHint(
		        RenderingHints.KEY_FRACTIONALMETRICS,
		        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		synchronized(imageMutex) {
			if (buffer != null) {
				g2dFrame.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
			}
		}
		

	}
	
	protected double normalizeForce(double f) {
		//double logF = MathExt.log10(f);
		
		//double n = logF - (1.0 - MathExt.cos_d(logF * 2.0));
		//return n;
		return MathExt.log10(f);
	}
	
	protected BufferedImage drawScene() {
		int width = getWidth();
		int height = getHeight();
		return drawScene(width, height, forceSample);
	}
	
	protected BufferedImage drawScene(int width, int height, ForceSample forceSample) {
		//System.err.println("Draw Scene");
		
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		
		g2d.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
		        RenderingHints.KEY_FRACTIONALMETRICS,
		        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		
		
		
		if (drawGravitationalPotentialGradient && forceSample != null) {
			WritableRaster raster = image.getRaster();
			double[] pixel = new double[4];
			for(int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					double force = MathExt.abs(forceSample.force[y][x]);
					
					//force = MathExt.min(force, 0.0);
					
					force = normalizeForce(force);
					
					//if (Double.isNaN(force) || Double.isInfinite(force)) {
					//	continue;
						//force = 0.0;
					//}
					
					double min = forceSample.minForce;
					double max = forceSample.maxForce;// - normalizeForce(forceSample.minForce);
					
					double ratio = 1.0 - ((force - min) / (max - min));
					
					//double ratio = (force - forceSample.minForce) / (forceSample.maxForce - forceSample.minForce);
					//System.err.println(ratio);
					
					//System.err.println(ratio);
					
					double c = 1.0 - ratio * 255.0;
					
					//c = MathExt.round(c / 3.0) * 3.0;
					
					pixel[0] = pixel[1] = pixel[2] = c;
					pixel[3] = 255.0;
					
					//if (force <= 0.0) {
					//	pixel[0] = 255;
					//}
					
					colorGradient.getColor(ratio, pixel);
					raster.setPixel(x, y, pixel);
					//g2d.setColor(pixelColor);
					//g2d.fillRect(x, y, 1, 1);
				}
				
			}
		}
		

		boolean labelDisplayed = false;
		for (TrackedBody trackedBody : tracked) {
			
			
			
			Color color = (trackedBody.isHypothetical) ? Color.RED : Color.BLACK;
			
			Color darker = new Color(color.getRed(), color.getGreen(), color.getBlue(), 75);
			g2d.setColor(darker);
			
			if (showTrails && trackedBody.drawTrack) {
				Path2D path = createPath(trackedBody.positions.getList());
				g2d.draw(path);
			}

			Vector screen = this.mapCoordinatesToScreen(trackedBody.particle.position, true, width, height);
			
			
			double bodyRadius = (trackedBody.particle.body.getRadius() * 1000.0 / maxDistance) * (getHeight() / 2.0);
			bodyRadius = MathExt.max(bodyRadius, 2);

			g2d.setColor(color);
			g2d.fillOval((int)(screen.x-bodyRadius), (int)(screen.y-bodyRadius), (int)bodyRadius*2, (int)bodyRadius*2);
			
			/*
			Particle boundTo = trackedBody.particle.boundTo;
			if (boundTo != null) {
				Color bingingColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 40);
				g2d.setColor(bingingColor);
				
				Vector bindingPosition = this.mapCoordinatesToScreen(boundTo.position, true);
				g2d.drawLine((int)screen.x, (int)screen.y, (int)bindingPosition.x, (int)bindingPosition.y);
			}
			*/
			
			Vector statePos = trackedBody.particle.position.clone();
			Vector stateVel = trackedBody.particle.velocity.clone();
			
			
			if (trackedBody.parent != null) {
				statePos.subtract(trackedBody.parent.particle.position);
				stateVel.subtract(trackedBody.parent.particle.velocity);
			}
			
			double y = statePos.y;
			statePos.y = -statePos.z;
			statePos.z = y;
			
			y = stateVel.y;
			stateVel.y = -stateVel.z;
			stateVel.z = y;
			
			Ephemeris eph = Ephemeris.fromStateVectors(statePos,
														stateVel, 
														trackedBody.particle.body.getMass(), 
														centerObjectTrack.particle.body.getMass());
			
			
			if (trackedBody != this.centerObjectTrack && drawElementsBasedOrbits) {
				// Skip drawing moon orbits for now
				if (trackedBody.parent != null) {
				//	continue;
				}
				
				g2d.setColor(new Color(0, 255, 0, 100));
				
				
				
				Vector parentPosition = new Vector();
				if (trackedBody.parent != null) {
					parentPosition = trackedBody.parent.particle.position.clone();
				}
				
				if (eph.eccentricity >= 0.0 && eph.eccentricity <= 1.0) {
					eph.epoch = JulianUtil.julianNow();
					
					EllipticalOrbit orbit = new EllipticalOrbit(eph);
					double step = eph.period / 360.0 / 4.0;
					
					int k = 0;
					Path2D futurePath = new Path2D.Double();
					for (double jd = eph.epoch; jd < eph.epoch + eph.period + step; jd+=step) {
						DateTime dt = new DateTime(jd);
						OrbitPosition pos = orbit.positionAtTime(dt);
						Vector posVector = pos.getPosition();
	
						posVector.multiplyScalar(Constants.AU_TO_KM * 1000);
						posVector.add(parentPosition);
						Vector screenXY = this.mapCoordinatesToScreen(posVector, true, width, height);
						
						//screenXY.x -= (parentPosition.x / 2.0);
						//screenXY.y -= parentPosition.y;
						
						if (k == 0) {
							futurePath.moveTo(screenXY.x, screenXY.y);
						} else {
							futurePath.lineTo(screenXY.x, screenXY.y);
						}
						k++;
						
					}
					if (k > 0) {
						futurePath.closePath();

						g2d.draw(futurePath);
					}
				} 
			}
			
			
			if (isMouseNearPoint((int)screen.x, (int) screen.y) && !labelDisplayed) {
				
				Vector velocity = trackedBody.particle.velocity;
				double vel = Math.round(velocity.length() * 1000.0) / 1000.0;
				
				double inclination = Math.round(eph.inclination.getDegrees() * 1000.0) / 1000.0;
				double period = Math.round((eph.period / 360.0) * 1000.0) / 1000.0;
				String line0 = trackedBody.particle.body.getName();
				String line1 = vel + " m/s";
				String line2 = "Radius: " + trackedBody.particle.body.getRadius() + " km";
				String line3 = "Mass: " + trackedBody.particle.body.getMass() + " kg";
				String line4 = "Inclination: " + inclination + " degrees";
				String line5 = "Period: " + period + " years";
				g2d.setColor(Color.BLUE);
				g2d.drawString(line0, (int)(screen.x - (getTextWidth(g2d, line0) / 2.0)), (int)(screen.y + 15));
				g2d.drawString(line1, (int)(screen.x - (getTextWidth(g2d, line1) / 2.0)), (int)(screen.y + 30));
				g2d.drawString(line2, (int)(screen.x - (getTextWidth(g2d, line2) / 2.0)), (int)(screen.y + 45));
				g2d.drawString(line3, (int)(screen.x - (getTextWidth(g2d, line3) / 2.0)), (int)(screen.y + 60));
				g2d.drawString(line4, (int)(screen.x - (getTextWidth(g2d, line4) / 2.0)), (int)(screen.y + 75));
				g2d.drawString(line5, (int)(screen.x - (getTextWidth(g2d, line5) / 2.0)), (int)(screen.y + 90));
				labelDisplayed = true;
			}
			
			
		}
		
		
		double currentTime = startTime + (timePassed * 1000);
		Date date = new Date((long)MathExt.round(currentTime));
		//String dateString = date.toString();
		String dateString = formatDate(date);
		
		TrackedBody earth = this.getTrackedBodyByName("Earth");
		TrackedBody antiEarth = this.getTrackedBodyByName("antiEarth");
		TrackedBody jupiter = this.getTrackedBodyByName("Jupiter");
		
		if (earth != null && antiEarth != null) {
			
			g2d.setColor(Color.BLACK);
			double a = earth.particle.position.angle(antiEarth.particle.position);
			
			double earthJupA = earth.particle.position.angle(jupiter.particle.position);
			double antiearthJupA = antiEarth.particle.position.angle(jupiter.particle.position);
			
			double d = earth.particle.position.getDistanceTo(antiEarth.particle.position);
			//if (dataWriter != null) {
			//	dataWriter.printf("%s, %f, %f, %f, %f\r\n",dateString,  MathExt.degrees(a), MathExt.degrees(earthJupA), MathExt.degrees(antiearthJupA), d);
			//	dataWriter.flush();
			//}
			
			System.err.println(MathExt.degrees(a));
			
			Vector earthScreenXY = this.mapCoordinatesToScreen(earth.particle.position, true, width, height);
			Vector antiearthScreenXY = this.mapCoordinatesToScreen(antiEarth.particle.position, true, width, height);
			
			g2d.drawLine((int)earthScreenXY.x, (int)earthScreenXY.y, (int)antiearthScreenXY.x, (int)antiearthScreenXY.y);
			
			
		}
		
		
		
		
		if (thread.isPaused()) {
			dateString += " <paused>";
		}
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(dateString, 15, 50);
		
		return image;
	}
	
	protected Path2D createPath(List<Vector> vectorList) {
		Path2D path = new Path2D.Double();

		//for (Vector vector : vectorList) {
		for (int i = 0; i < vectorList.size(); i++) {
			Vector screen = this.mapCoordinatesToScreen(vectorList.get(i), true, getWidth(), getHeight());
			
			if (i == 0) {
				path.moveTo(screen.x, screen.y);
			} else {
				path.lineTo(screen.x, screen.y);
			}
		}
		//	i++;
		//}
		
		return path;
	}
	
	public String formatDate(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
		return sdf.format(dt);
	}
	
	
	public void createGravityDem(int width, int height, String writeToPath) throws Exception {
		
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(writeToPath)));
		ForceSample forceMatrix = getForceMatrix(width, height);
		forceMatrix.maxForce -= forceMatrix.minForce;
		
		forceMatrix.maxForce = normalizeForce(forceMatrix.maxForce);
		forceMatrix.minForce = normalizeForce(forceMatrix.minForce);
		
		
		System.err.println("Writing DEM...");
		byte[] floatBytes = new byte[8];
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double force = forceMatrix.force[y][x];
				if (Double.isNaN(force)) {
					continue;
				}
				force = normalizeForce(force);
				
				double ratio = (force - forceMatrix.minForce) / forceMatrix.maxForce;
				
				double e = -11000 + (ratio * (8500 - -11000));
				//-11000
				//8500
				
				
				
				
				//System.err.println(e);
				
				float f = (float) force;
				ByteConversions.doubleToBytes(e, floatBytes);
				
				out.write(floatBytes);
				
				
			}
			
		}
		System.err.println("Done");
		
		out.flush();
		out.close();
	}
	


	
	class SteppingThread extends Thread implements SimulationThread {
		
		public Double mutex = new Double(0.0);
		
		private boolean paused = false;
		private boolean stop = false;
		
		public void run() {
			while(!stop) {
				if (!isPaused()) {
					step();
				} else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		@Override
		public void stopSimulation() {
			stop = true;
		}
		
		@Override
		public boolean isPaused() {
			return paused;
		}
		
		@Override
		public void setPaused(boolean p) {
			synchronized(mutex) {
				this.paused = p;
			}
		}
		
	}

	public LeapFrogSimulator getSimulator() {
		return simulator;
	}

	public void setSimulator(LeapFrogSimulator simulator) {
		this.simulator = simulator;
	}
	
	
	
	public GravitationColorGradient getColorGradient() {
		return colorGradient;
	}

	public void setColorGradient(GravitationColorGradient colorGradient) {
		this.colorGradient = colorGradient;
	}
	
	

	public boolean isDrawGravitationalPotentialGradient() {
		return drawGravitationalPotentialGradient;
	}

	public void setDrawGravitationalPotentialGradient(boolean drawGravitationalPotentialGradient) {
		this.drawGravitationalPotentialGradient = drawGravitationalPotentialGradient;
	}

	public List<TrackedBody> getTracked() {
		return tracked;
	}

	public void setTracked(List<TrackedBody> tracked) {
		this.tracked = tracked;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public double getDefaultDistance() {
		return defaultDistance;
	}

	public void setDefaultDistance(double defaultDistance) {
		this.defaultDistance = defaultDistance;
	}

	public TrackedBody getCenterObjectTrack() {
		return centerObjectTrack;
	}

	public void setCenterObjectTrack(TrackedBody centerObjectTrack) {
		this.centerObjectTrack = centerObjectTrack;
	}
	
	

	public int getShiftX() {
		return shiftX;
	}

	public void setShiftX(int shiftX) {
		this.shiftX = shiftX;
	}

	public int getShiftY() {
		return shiftY;
	}

	public void setShiftY(int shiftY) {
		this.shiftY = shiftY;
	}

	public TrackedBody getFocusObjectTrack() {
		return focusObjectTrack;
	}

	public void setFocusObjectTrack(TrackedBody focusObjectTrack) {
		this.focusObjectTrack = focusObjectTrack;
	}

	public SimulationThread getThread() {
		return thread;
	}


	public double getDeltaT() {
		return deltaT;
	}

	public void setDeltaT(double deltaT) {
		this.deltaT = deltaT;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public boolean isShowTrails() {
		return showTrails;
	}

	public void setShowTrails(boolean showTrails) {
		this.showTrails = showTrails;
	}

	public double getIterationsPerPositionSampling() {
		return iterationsPerPositionSampling;
	}

	public void setIterationsPerPositionSampling(double iterationsPerPositionSampling) {
		this.iterationsPerPositionSampling = iterationsPerPositionSampling;
	}

	public boolean isDrawElementsBasedOrbits() {
		return drawElementsBasedOrbits;
	}

	public void setDrawElementsBasedOrbits(boolean drawElementsBasedOrbits) {
		this.drawElementsBasedOrbits = drawElementsBasedOrbits;
	}

	public long getWriteDelayMillis() {
		return writeDelayMillis;
	}

	public void setWriteDelayMillis(long writeDelayMillis) {
		this.writeDelayMillis = writeDelayMillis;
	}

	@Override
	public void addSimulationEventHandler(SimulationEventHandler handler) {
		eventHandlers.add(handler);
	}

	@Override
	public boolean removeSimulationEventHandler(SimulationEventHandler handler) {
		return eventHandlers.remove(handler);
	}
	

	
	
}
