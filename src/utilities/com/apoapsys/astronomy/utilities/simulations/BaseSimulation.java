package com.apoapsys.astronomy.utilities.simulations;

import java.util.List;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.bodies.Body;
import com.apoapsys.astronomy.bodies.MajorBodiesLoader;
import com.apoapsys.astronomy.bodies.OrbitingBody;
import com.apoapsys.astronomy.bodies.SingleBody;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.apoapsys.astronomy.time.DateTime;

public abstract class BaseSimulation {
	
	
	public abstract void start();
	
	public static Body getBodyByName(String name, List<Body> bodies) throws Exception {
		for (Body body : bodies) {
			if (body.getName() != null && body.getName().equals(name)) {
				return body;
			}
			
			if (body.getOrbitingBodies().size() > 0) {
				Body moon = getBodyByName(name, body.getOrbitingBodies());
				if (moon != null) {
					return moon;
				}
			}
		}
		return null;
	}
	
	public static Body getBodyByName(String name) throws Exception {
		List<Body> bodies = MajorBodiesLoader.load();
		return getBodyByName(name, bodies);
	}
	
	public static SingleBody getEarth() throws Exception {
		return (SingleBody) getBodyByName("Earth");
	}

	public static SingleBody getSun() throws Exception {
		return (SingleBody) getBodyByName("Sun");
	}
	
	public static Particle createPreInitialState(OrbitingBody body, DateTime dt) {
		Particle particle = new Particle();
		particle.body = body;
		particle.velocity = new Vector();
		particle.position = (body.getOrbit() != null) ? body.getOrbit().positionAtTime(dt).getPosition() : new Vector();
		//particle.position.y = particle.position.z;
		//particle.position.z = 0;
		particle.position.multiplyScalar(Constants.AU_TO_KM * 1000);
		particle.velocity.multiplyScalar(Constants.AU_TO_KM * 1000);
		return particle;
	}
	
	public static Particle createInitialState(OrbitingBody body, DateTime dt) {
		
		Particle initialState = createPreInitialState(body, dt);
		
		
		Ephemeris ephemeris = body.getEphemeris();
		ephemeris.meanMotion = 1.0 / ephemeris.period;
		EllipticalOrbit orbit = new EllipticalOrbit(ephemeris);
		
		OrbitPosition position = orbit.positionAtTime(dt);
		OrbitPosition velocity = orbit.velocityAtTime(dt);
		
		initialState.position = position.getPosition();
		initialState.velocity = velocity.getPosition();
		
		initialState.position.multiplyScalar(Constants.AU_TO_KM * 1000);
		initialState.velocity.multiplyScalar(Constants.AU_TO_KM * 1000);
		
		return initialState;
	}
	
	
	public static double getEstimatedMass(double radius, double density) {
		double mass = (MathExt.PI * MathExt.cube(radius * 1000 * 2) * density) / 6.0;
		return mass;
	}
	
	public static double getEstimatedAsteroidDensityFromSpectralType(char type) {
		
		switch(type) {
		case 'C':
		case 'D':
		case 'P':
		case 'T':
		case 'B':
		case 'G':
		case 'F':
			return 1.38;
		case 'S':
		case 'K':
		case 'Q':
		case 'V':
		case 'R':
		case 'A':
		case 'E':
			return 2.71;
		case 'M':
			return 5.32;
		default:
			return 2.0;
		}
		
		
	}
}
