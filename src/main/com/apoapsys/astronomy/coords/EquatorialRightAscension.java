package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

public class EquatorialRightAscension {
	
	private Angle rightAscension;
	private Angle declination;
	
	public EquatorialRightAscension() {
		
	}
	
	public EquatorialRightAscension(Angle rightAscension, Angle declination) {
		this.rightAscension = rightAscension;
		this.declination = declination;
	}

	public Angle getRightAscension() {
		return rightAscension;
	}

	public void setRightAscension(Angle rightAscension) {
		this.rightAscension = rightAscension;
	}

	public Angle getDeclination() {
		return declination;
	}

	public void setDeclination(Angle declination) {
		this.declination = declination;
	}
	
	
}
