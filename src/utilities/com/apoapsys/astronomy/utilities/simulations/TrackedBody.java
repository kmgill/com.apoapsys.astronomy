package com.apoapsys.astronomy.utilities.simulations;

import java.awt.Color;

import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

public class TrackedBody {
	public Color color;
	public PositionQueue<Vector> positions;
	public Particle particle;
	public boolean isHypothetical = false;
	public boolean drawTrack = true;
	
	public TrackedBody parent = null;
	
	TrackedBody(Particle particle, Color color, int maxElements) {
		this(particle, color, maxElements, null);
	}
	
	TrackedBody(Particle particle, Color color, int maxElements, TrackedBody parent) {
		this.particle = particle;
		this.color = color;
		this.positions = new PositionQueue<Vector>(maxElements);
		this.parent = parent;
	}
}
