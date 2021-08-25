package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.time.DateTime;

public interface Orbit<T extends OrbitPosition> {
	
	public T positionAtTime(DateTime dt);
	
	public double getPeriod();
	public DateTime getEpoch();
}
