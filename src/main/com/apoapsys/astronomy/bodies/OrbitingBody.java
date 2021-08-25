package com.apoapsys.astronomy.bodies;

import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;

public class OrbitingBody extends SingleBody {
	
	private Orbit<?> orbit;
	private Ephemeris ephemeris;
	
	public OrbitingBody() {
		
	}
	
	public Orbit<?> getOrbit() {
		return orbit;
	}

	public OrbitingBody setOrbit(Orbit<?> orbit) {
		this.orbit = orbit;
		return this;
	}

	public Ephemeris getEphemeris() {
		return ephemeris;
	}
	
	public OrbitingBody setEphemeris(Ephemeris ephemeris) {
		this.ephemeris = ephemeris;
		return this;
	}
	
	
}
