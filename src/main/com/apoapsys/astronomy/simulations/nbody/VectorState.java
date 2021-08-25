package com.apoapsys.astronomy.simulations.nbody;

import com.apoapsys.astronomy.math.Vector;

public class VectorState {
	private String bodyId;
	private double time;
	private Vector position;
	
	public VectorState(String bodyId, double time, Vector position) {
		this.bodyId = bodyId;
		this.time = time;
		this.position = position.clone();
	}

	public String getBodyId() {
		return bodyId;
	}

	public double getTime() {
		return time;
	}

	public Vector getPosition() {
		return position;
	}
	
	
}
