package com.apoapsys.astronomy.simulations.nbody;

import com.apoapsys.astronomy.bodies.SingleBody;
import com.apoapsys.astronomy.math.Vector;

public class TimeAwareParticle extends Particle {
	
	private VectorTimeMap vectorTimeMap;
	
	public TimeAwareParticle(VectorTimeMap vectorTimeMap) {
		this(null, vectorTimeMap);
	}
	
	public TimeAwareParticle(SingleBody body, VectorTimeMap vectorTimeMap) {
		this(body, new Vector(), new Vector(), vectorTimeMap);
	}
	
	public TimeAwareParticle(SingleBody body, Vector position, Vector velocity, VectorTimeMap vectorTimeMap) {
		super(body, position, velocity);
		this.vectorTimeMap = vectorTimeMap;
	}
	
	
	public Vector positionAtTime(double time) {
		VectorState state = vectorTimeMap.get(this.identifier, time);
		return (state != null) ? state.getPosition() : null;
	}
	
	
	public Vector getPositionLightTime(Particle other) {
		
		// Simple:
		double d = this.position.getDistanceTo(other.position);
		
		//Constants.C
		return null;
	}
}
