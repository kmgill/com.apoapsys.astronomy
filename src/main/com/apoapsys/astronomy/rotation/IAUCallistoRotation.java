package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUCallistoRotation extends IAUJovianMoonRotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double jd = (dt.getJulianDay() - 2451545.0);
		
		double meridian = 259.51 + 21.5710715 * jd
				+ 0.061 * MathExt.sin_d(J[5]) 
				- 0.533 * MathExt.sin_d(J[6])
				- 0.009 * MathExt.sin_d(J[8]);
		
		
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double t = dt.getJulianCentury();
		
		double ra = 268.72 - 0.009 * t
				- 0.068 * MathExt.sin_d(J[5]) 
				+ 0.590 * MathExt.sin_d(J[6])
				+ 0.010 * MathExt.sin_d(J[8]);
	
	
		double dec = 64.83 + 0.003 * t
				- 0.029 * MathExt.cos_d(J[5]) 
				+ 0.254 * MathExt.cos_d(J[6])
				- 0.004 * MathExt.cos_d(J[8]);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}
}
