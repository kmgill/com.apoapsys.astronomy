package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUGanymedeRotation extends IAUJovianMoonRotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double jd = (dt.getJulianDay() - 2451545.0);
		
		double meridian = 44.064 + 50.3176081 * jd
								+ 0.033 * MathExt.sin_d(J[4])
								- 0.389 * MathExt.sin_d(J[5])
								- 0.082 * MathExt.sin_d(J[6]);
		
		
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double t = dt.getJulianCentury();
		
		double ra = 268.20 - 0.009 * t
								- 0.037 * MathExt.sin_d(J[4]) 
								+ 0.431 * MathExt.sin_d(J[5]) 
								+ 0.091 * MathExt.sin_d(J[6]);
		
		
		double dec = 64.57 + 0.003 * t
								- 0.016 * MathExt.cos_d(J[4]) 
								+ 0.186 * MathExt.cos_d(J[5])
								+ 0.039 * MathExt.cos_d(J[6]);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}
}
