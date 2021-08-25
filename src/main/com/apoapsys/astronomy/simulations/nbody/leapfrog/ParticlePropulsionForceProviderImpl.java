package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.math.Plane;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

/*
 * Eventually this should support, other than lots more physics,
 * the ability to apply a program to construct an accurate mission
 * trajectory and/or a form of autopilot. Recommend extending this
 * class for more specific propulsion methods
 * 
 * Note: This is currently a vast oversimplification
 */
public class ParticlePropulsionForceProviderImpl implements SimulationForceProvider {

	/*
	 * More or less Kerbal derived orientations
	 */
	public enum OrientationType {
		MANUAL("Manual"),
		PROGRADE("Prograde"),            // Facing towards the path of motion
		RETROGRADE("Retrograde"),        // Facing opposite the path of motion
		NORMAL("Normal"),                // Facing to the normal (perpendicular, up) to the path of motion
		ANTINORMAL("Anti-normal"),       // Facing to the normal (perpendicular, down) to the path of motion
		RADIAL("Radial (in)"),           // Perpendicular to the prograde (parallel to path), in
		ANTIRADIAL("Radial (out)");      // Perpendicular to the prograde (parallel to path), out
		
		private final String name;
		OrientationType(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	// Whether or not the thrust is being applied (engine is on/off)
	private boolean enabled = false;
	
	private OrientationType orientation = OrientationType.MANUAL;
	
	// The direction of thrust, accelerating the particle in the opposite direction
	private Vector facing = new Vector(1, 0, 0);
	
	// A percentage of thrust applied
	private double throttleLevel = 0.0;
	
	// Used more as an acceleration in m/s for now
	private double thrust = 0.0;
	
	@Override
	public Vector onParticle(double deltaT, Particle particle, Particle[] allParticles) {

		if (enabled) {
			// Oh, if only it were this simple...
			Vector force = null;
			
			if (OrientationType.MANUAL.equals(orientation)) {
				force = facing.clone().normalize().inverse();
			} else if (OrientationType.PROGRADE.equals(orientation)) {
				force = particle.velocity.clone().normalize();
			} else if (OrientationType.RETROGRADE.equals(orientation)) {
				force = particle.velocity.clone().normalize().inverse();
			} else if (OrientationType.NORMAL.equals(orientation)) {
				force = calculateNormal(particle);
			} else if (OrientationType.ANTINORMAL.equals(orientation)) {
				force = calculateNormal(particle).inverse();
			} else if (OrientationType.RADIAL.equals(orientation)) {
				force = new Vector(0, 0, 0);
			} else if (OrientationType.ANTIRADIAL.equals(orientation)) {
				force = new Vector(0, 0, 0);
			}
			
			force.multiplyScalar(thrust * throttleLevel);
			return force;
		} else {
			return new Vector(0, 0, 0);
		}
	}

	private Vector calculateNormal(Particle particle) {
		if (particle.previousPositions.size() >= 2) {
			Vector a = particle.position;
			Vector b = particle.previousPositions.get(0);
			Vector c = particle.previousPositions.get(1);
			
			Vector v = Plane.findPlane(a, b, c);
			v.normalize();

			return v;
		} else {
			return new Vector(0, 0, 0);
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public OrientationType getOrientation() {
		return orientation;
	}

	public void setOrientation(OrientationType orientation) {
		this.orientation = orientation;
	}

	public Vector getFacing() {
		return facing;
	}

	public void setFacing(Vector facing) {
		this.facing = facing;
	}

	public double getThrottleLevel() {
		return throttleLevel;
	}

	public void setThrottleLevel(double throttleLevel) {
		this.throttleLevel = throttleLevel;
	}

	public double getThrust() {
		return thrust;
	}

	public void setThrust(double thrust) {
		this.thrust = thrust;
	}
	
	

}
