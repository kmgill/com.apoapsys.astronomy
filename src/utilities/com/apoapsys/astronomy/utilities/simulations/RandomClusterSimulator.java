package com.apoapsys.astronomy.utilities.simulations;

import java.util.Random;

import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.LeapFrogSimulator;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.NewtonianGravityForceProviderImpl;


@SuppressWarnings("serial")
public class RandomClusterSimulator extends BaseSimulation {
	private SimpleOrbitVisual simWorker;
	public RandomClusterSimulator() throws Exception {
		//super(new LeapFrogSimulator());
		simWorker = new SimpleOrbitVisual(new LeapFrogSimulator());
		simWorker.getThread().setPaused(true);
		
		
		simWorker.setDeltaT(2.0);
		simWorker.setQueueSize(100);
		
		simWorker.setShowTrails(true);
		simWorker.getSimulator().setMergingOnCollision(true);
		simWorker.getSimulator().addSimulationForceProvider(new NewtonianGravityForceProviderImpl());
		//simWorker.getSimulator().addSimulationForceProvider(new TidalAccelerationProviderImpl());
		simWorker.getSimulator().setCheckForGravitationalBinding(false);
		simWorker.getSimulator().setCheckingForCollisions(false);
		simWorker.setDrawElementsBasedOrbits(false);
		int totalParticles = 1000;
		
		int width = 800;
		int height = 800;
		
		double fieldRadius = (width > height) ? width : height;
		
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < totalParticles; i++) {
			
			int x = (int) Math.round((random.nextDouble() * fieldRadius));
			int y = (int) Math.round((random.nextDouble() * fieldRadius));
			
			Vector map = simWorker.screenCoordinatesToMap(x, y);
			
			
			double rnd = random.nextDouble();
			
			double sizeMultiple = (rnd * 20);
			double mass = 1.988544E+30 * sizeMultiple;
			
			double radius = MathExt.pow((mass / 5.97219E+24), 0.3) * 6378.135;
			
			
			
			simWorker.introduceParticle("Particle #" + (i),
								new Vector(map.x, 0.0, map.y),
								new Vector((random.nextBoolean() ? -1 : 1) * 70000.0, 0, (random.nextBoolean() ? -1 : 1) * 70000.0),
								mass,
								radius);
			
		}
		
		Vector map = simWorker.screenCoordinatesToMap((int)(fieldRadius*.5), -(int)(fieldRadius*.15));
		simWorker.centerObjectTrack = simWorker.introduceParticle("Central Black Hole",
													new Vector(map.x-1, 0.0, map.y),
													new Vector(0.0, 0.0, 5000000.0),
													(1.988544E+30) * 10000000.0,
													1,
													false);
		
				
		//this.centerObjectTrack.particle.blackHole = true;
		//System.err.println("Central Black Hole Mass: " + this.centerObjectTrack.particle.body.getMass() + " kg");
		
		simWorker.step();
		simWorker.getThread().setPaused(false);
	}
	
	public static void main(String[] args) {
		try {
			RandomClusterSimulator app = new RandomClusterSimulator();
			app.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void start() {
		simWorker.beginSimulation();
	}
	
}
