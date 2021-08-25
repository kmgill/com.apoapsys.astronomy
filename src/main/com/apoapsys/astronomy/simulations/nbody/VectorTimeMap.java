package com.apoapsys.astronomy.simulations.nbody;

public interface VectorTimeMap {
	public void put(VectorState state);
	public VectorState get(String bodyId, double time);
}
