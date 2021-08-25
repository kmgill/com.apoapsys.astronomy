package com.apoapsys.astronomy.utilities.simulations;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import com.apoapsys.astronomy.bodies.Body;
import com.apoapsys.astronomy.bodies.MajorBodiesLoader;
import com.apoapsys.astronomy.bodies.OrbitingBody;
import com.apoapsys.astronomy.bodies.SingleBody;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.Collision;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.LeapFrogSimulator;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.NewtonianGravityForceProviderImpl;
import com.apoapsys.astronomy.time.DateTime;
import com.google.common.collect.Lists;

/** Simulator based on the objects found in major_bodies.json
 * 
 * @author kgill
 *
 */
public class SolarSystemSimulator extends BaseSimulation {
	
	private SimpleOrbitVisual simWorker;
	private int queueSize = 50;
	
	private List<String> skipMoonsOf = Lists.newArrayList();
	private List<String> skipBodies = Lists.newArrayList();
	
	public SolarSystemSimulator() throws Exception {
		this(true);
	}
	
	

	
	public SolarSystemSimulator(boolean doUnpause) throws Exception {

		/*
		skipMoonsOf.add("Earth");
		skipMoonsOf.add("Jupiter");
		skipMoonsOf.add("Saturn");
		skipMoonsOf.add("Uranus");
		skipMoonsOf.add("Neptune");
		skipMoonsOf.add("Pluto");
		
		skipBodies.add("Mercury");
		skipBodies.add("Venus");
		skipBodies.add("Moon");
		skipBodies.add("Mars");
		skipBodies.add("Vesta");
		skipBodies.add("Ceres");
		skipBodies.add("Jupiter");
		skipBodies.add("Saturn");
		skipBodies.add("Uranus");
		skipBodies.add("Neptune");
		skipBodies.add("Pluto");
		skipBodies.add("Eris");
		skipBodies.add("Makemake");
		skipBodies.add("Sedna");
		skipBodies.add("Haumea");
		skipBodies.add("2012 VP113");
		skipBodies.add("LADEE");
		
		skipMoonsOf.add("Earth");
		skipMoonsOf.add("Antiearth");
		skipMoonsOf.add("Jupiter");
		skipMoonsOf.add("Saturn");
		skipMoonsOf.add("Uranus");
		skipMoonsOf.add("Neptune");
		skipMoonsOf.add("Pluto");
		
		skipBodies.add("Vesta");
		skipBodies.add("Ceres");
		skipBodies.add("Uranus");
		skipBodies.add("Neptune");
		skipBodies.add("Pluto");
		skipBodies.add("Eris");
		skipBodies.add("Makemake");
		skipBodies.add("Sedna");
		skipBodies.add("Haumea");
		skipBodies.add("2012 VP113");
		*/
		simWorker = new SimpleOrbitVisual(new LeapFrogSimulator());
		//simWorker = new SimpleSimulationEngineWorker(new LeapFrogSimulator());
		
		simWorker.getThread().setPaused(true);
		
		
		simWorker.setDeltaT(1.0);
		simWorker.setQueueSize(queueSize);
		
		simWorker.setShowTrails(false);
		simWorker.setMaxDistance(7.963174266363782E11);
		//simWorker.setMaxDistance(2.0E13);
		//simWorker.setMaxDistance(3.879026661785389E8);
		simWorker.setDrawElementsBasedOrbits(true);
		//simWorker.setMaxDistance(7.257578919669991E9);
		//simWorker.setMaxDistance(1.0477430052945459E9); // Jupiter
		
		//simWorker.setMaxDistance(1.793975715186211E10);
		//simWorker.setShiftX(1463);
		//simWorker.setShiftY(3138);
		
		//simWorker.setShiftX(5912);
		//simWorker.setShiftY(-19921);
		//simWorker.setShiftX(38106);
		//simWorker.setShiftY(-15912);
		//simWorker.setMaxDistance(3.6359260872074776E9);
		//simWorker.setColorGradient(new FadingGravitationColorGradient(0x000000, 0xFF6600));
		simWorker.setColorGradient(new HypsometricGravitationColorGradient());
		simWorker.setDrawGravitationalPotentialGradient(false);
		simWorker.setIterationsPerPositionSampling(10);
		simWorker.setQueueSize(100);
		simWorker.getSimulator().setMergingOnCollision(true);
		simWorker.getSimulator().addSimulationForceProvider(new NewtonianGravityForceProviderImpl());
		//simWorker.getSimulator().addSimulationForceProvider(new TidalAccelerationProviderImpl());
		simWorker.getSimulator().setCheckForGravitationalBinding(false);
		simWorker.getSimulator().setCheckingForCollisions(true);
		
		
		DateTime dt = new DateTime();//2456752.000000000);
		
		OrbitingBody sunBody = (OrbitingBody) getSun();
		Particle sunInitialState = createPreInitialState(sunBody, dt);
		TrackedBody sunTrack = new TrackedBody(sunInitialState, Color.ORANGE, queueSize);
		simWorker.getTracked().add(sunTrack);
		
		
		
		simWorker.setCenterObjectTrack(sunTrack);
		
		// WARNING: Ugly & rushed & unthoughtout code to follow
		List<Body> bodies = MajorBodiesLoader.load();
		for (Body body : bodies) {
			if (body.getName().equalsIgnoreCase("sun")) {
				continue;
			}
			
			OrbitingBody orbitingBody = (OrbitingBody) body;
			createTrackedBody(orbitingBody, dt, null); 
			
		}
		
		for (TrackedBody trackedBody : simWorker.getTracked()) {
			simWorker.getSimulator().addParticle(trackedBody.particle);
		}
		
		simWorker.setCenterObjectTrack(simWorker.getTrackedBodyByName("Sun"));
		simWorker.setFocusObjectTrack(simWorker.getTrackedBodyByName("Sun"));
		//simWorker.createGravityDem(4098, 4098, "C:/Users/kgill/Google Drive/JDem846/gravity/gravitymap.dat");
		
		
		//simWorker.getSimulator().getParticles().get(0).
		TrackedBody earth = simWorker.getTrackedBodyByName("Earth");
		System.err.println(earth);
		/*
		Ephemeris antiEarthEphemeris = ((OrbitingBody)earth.particle.body).getEphemeris();
		OrbitingBody antiEarth = new OrbitingBody().setEphemeris(antiEarthEphemeris);
		Particle antiEarthParticle = createInitialState(antiEarth, new DateTime());
		antiEarthParticle.position.multiplyScalar(-1.0);
		antiEarthParticle.velocity.multiplyScalar(-1.0);
		TrackedBody antiEarthTrackedBody = simWorker.introduceParticle("Antiearth", antiEarthParticle.position, antiEarthParticle.velocity, earth.particle.body.getMass(), earth.particle.body.getRadius());
		*/
		
				
		//Vector map = simWorker.screenCoordinatesToMap((int)(800*.49), -(int)(800*.15));
		/*TrackedBody blackHole = simWorker.introduceParticle("Central Black Hole",
													new Vector(map.x-1, 0.0, map.y),
													new Vector(0.0, 0.0, 1000000.0),
													(1.988544E+30) * 2.2,
													1,
													false);
			*/										
		//simWorker.setCenterObjectTrack(blackHole);
		//simWorker.setFocusObjectTrack(blackHole);
		
		
		//System.err.println(simWorker.getTrackedBodyByName("Moon").particle.getAngularMomentumSpinningBody());
		simWorker.addSimulationEventHandler(new SimulationEventHandler() {

			@Override
			public void onCollision(Collision collision, DateTime date) {
				Particle b1 = collision.particle0;
				Particle b2 = collision.particle1;
				
				double distance = b1.position.getDistanceTo(b2.position);
				distance /= 1000.0;
				
				System.err.println("Collision of " + collision.particle0.body.getName() + " and " + collision.particle1.body.getName() + " at " + date.toString() + " at a center distance of " + distance + " km");
				System.exit(0);
			}

			long last = System.currentTimeMillis();
			
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			@Override
			public void onStep(double deltaT, DateTime date) {
				
				if (System.currentTimeMillis() - last >= 1000) {
					System.out.println("Simulation Time: " + date.toString());
					last = System.currentTimeMillis();
					
					String b1Name = "Adrastea";
					String b2Name = "Metis";
					
					//String b1Name = "Moon";
					//String b2Name = "LADEE";
					
					TrackedBody b1 = simWorker.getTrackedBodyByName(b1Name);
					TrackedBody b2 = simWorker.getTrackedBodyByName(b2Name);
					
					double distance = b1.particle.position.getDistanceTo(b2.particle.position);
					distance /= 1000.0;
					
					min = MathExt.min(min, distance);
					max = MathExt.max(max, distance);
					
					System.out.println("Distance between " + b1Name + " and " + b2Name + ": " + distance + " km (min: " + min + " km, max: " + max + " km)");
				}
			}
			
		});
		
		Vector sunPos = simWorker.getSimulator().getParticles().get(0).position;
		
		try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:/jdem/temp/snapshot/solarsys.txt")))) {
			
			for (Particle particle : simWorker.getSimulator().getParticles()) {
				SingleBody body = particle.body;
				Vector pos = particle.position;
				pos = pos.subtract(sunPos);
				Vector vel = particle.velocity;
				//BODY_SNAPSHOT(Sun, 1000000, 1988544000000000000000000000000.000000, 696342.000000, BODY_POSITION(446550500.647113, -21109252.211727, 95716077.739894), BODY_VELOCITY(0.000000, 0.000000, 0.000000), BODY_IS_MAJOR)
				
				System.out.printf("BODY_SNAPSHOT(%s, %d, %f, %f, BODY_POSITION(%f, %f, %f), BODY_VELOCITY(%f, %f, %f), BODY_IS_MAJOR)\n",
									body.getName(),
									body.getId(),
									body.getMass(),
									body.getRadius(),
									pos.x, 
									pos.y,
									pos.z,
									vel.x,
									vel.y,
									vel.z
						);
				
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.exit(0);
		
		simWorker.step();
		
		if (doUnpause) {
			simWorker.getThread().setPaused(false);
		}
	}
	
	protected void createTrackedBody(OrbitingBody orbitingBody, DateTime dt, TrackedBody parent) {
		if (orbitingBody.getMass() == 0) {
			return;
		}
		
		if (skipBodies.contains(orbitingBody.getName())) {
			return;
		}
		
		if (parent != null && skipMoonsOf.contains(parent.particle.body.getName())) {
			return;
		}
		
		Particle bodyState = createInitialState(orbitingBody, dt);
		
		if (parent != null) {
			bodyState.position.add(parent.particle.position);
			bodyState.velocity.add(parent.particle.velocity);
		}
		
		TrackedBody track = new TrackedBody(bodyState, Color.RED, queueSize, parent);
		simWorker.getTracked().add(track);
		
		for (Body moon : orbitingBody.getOrbitingBodies()) {
			OrbitingBody orbitingMoon = (OrbitingBody) moon;
			createTrackedBody(orbitingMoon, dt, track);
		}
		
	}
	
	public void start() {
		simWorker.beginSimulation();
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		try {
			SolarSystemSimulator app = new SolarSystemSimulator();
			app.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
