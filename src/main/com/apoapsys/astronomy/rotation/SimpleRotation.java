package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class SimpleRotation extends IAURotation {
	
	
	private EquatorialRightAscension orientation;
	private double rotationalPeriod;
	private Angle meridian;
	private DateTime meridianEpoch;
	
	
	/** Rotation with no axial tilt
	 * 
	 */
	public SimpleRotation(Angle meridian, DateTime meridianEpoch, double rotationalPeriod) {
		this(new EquatorialRightAscension(Angle.fromDegrees(90), Angle.fromDegrees(90)), meridian, meridianEpoch, rotationalPeriod);
	}
	
	public SimpleRotation(EquatorialRightAscension orientation, Angle meridian, DateTime meridianEpoch, double rotationalPeriod) {
		this.orientation = orientation;
		this.rotationalPeriod = rotationalPeriod;
		this.meridian = meridian;
		this.meridianEpoch = meridianEpoch;
	}

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double m = meridian.getDegrees();
		m -= (360 * rotationalPeriod * (dt.getJulianDay() - meridianEpoch.getJulianDay()));
		return Angle.fromDegrees(m);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		return orientation;
	}

}
