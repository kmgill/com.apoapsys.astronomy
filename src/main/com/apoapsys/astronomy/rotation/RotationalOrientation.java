package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;

public class RotationalOrientation extends EquatorialRightAscension {
	
	
	public Angle node;
	public Angle inclination;
	
	public RotationalOrientation(EquatorialRightAscension orientation, Angle node, Angle inclination) {
		super(orientation.getRightAscension(), orientation.getDeclination());
		this.node = node;
		this.inclination = inclination;
	}
	
	public RotationalOrientation(Angle rightAscension, Angle declination, Angle node, Angle inclination) {
		super(rightAscension, declination);
		this.node = node;
		this.inclination = inclination;
	}

	public Angle getNode() {
		return node;
	}

	public Angle getInclination() {
		return inclination;
	}
	
	
}
