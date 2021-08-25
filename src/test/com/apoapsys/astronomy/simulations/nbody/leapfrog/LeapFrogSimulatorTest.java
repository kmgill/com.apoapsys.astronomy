package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import org.junit.Test;

public class LeapFrogSimulatorTest {
	

	

	
	
	
	
	@Test
	public void testSimpleScenario() throws Exception {
		
		
		
		
		/*
		body = (OrbitingBody) getBodyByName("Saturn");
		bodyInitialState = createInitialState(body, dt);
		tracked.add(new TrackedBody(bodyInitialState, Color.CYAN));
		
		body = (OrbitingBody) getBodyByName("Uranus");
		bodyInitialState = createInitialState(body, dt);
		tracked.add(new TrackedBody(bodyInitialState, Color.MAGENTA));
		
		body = (OrbitingBody) getBodyByName("Neptune");
		bodyInitialState = createInitialState(body, dt);
		tracked.add(new TrackedBody(bodyInitialState, Color.ORANGE));
		*/
		
		//LeapFrogSimulator simulator = new LeapFrogSimulator(dt, deltaT);
		//for (TrackedBody trackedBody : tracked) {
		//	simulator.addParticle(trackedBody.particle);
		//}
		//simulator.addParticle(sunInitialState);
		//simulator.addParticle(earthInitialState);
		
		//System.err.println("Initial Distance: " + sunInitialState.position.getDistanceTo(earthInitialState.position));
		
		//List<Vector> sunPositions = Lists.newArrayList();
		//List<Vector> earthPositions = Lists.newArrayList();
		
		
		
		
		//System.err.println("Max Distance: " + maxDistance);
		
		//createRelativeVelocityMap(maxDistance, tracked);
		//createHeliocentricVelocityMap(maxDistance, tracked, sunTrack);
	}
	
	/*
	protected void createHeliocentricVelocityMap(double maxDistance, List<TrackedBody> tracked, TrackedBody sunTrack) throws Exception {
		BufferedImage map = new BufferedImage(5000, 5000, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) map.getGraphics();
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 5000, 5000);
		
		
		double radius = 2500.0;
		
		//for (TrackedBody trackedBody : tracked) {
		//	Path2D path = createPath(radius, maxDistance, trackedBody.positions, sunTrack.positions);
		//	g2d.setColor(trackedBody.color);
		//	g2d.draw(path);
		//}

		ImageWriter.saveImage(map, "C:/jdem/temp/orbits/map-heliocentric.png");
	}
	
	protected void createRelativeVelocityMap(double maxDistance, List<TrackedBody> tracked) throws Exception {
		BufferedImage map = new BufferedImage(5000, 5000, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) map.getGraphics();
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 5000, 5000);
		
		
		double radius = 2500.0;
		
		//for (TrackedBody trackedBody : tracked) {
		//	Path2D path = createPath(radius, maxDistance, trackedBody.positions, null);
		//	g2d.setColor(trackedBody.color);
		//	g2d.draw(path);
	//	}

		ImageWriter.saveImage(map, "C:/jdem/temp/orbits/map.png");
	}
	*/
	
}
