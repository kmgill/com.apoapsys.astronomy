package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUIoRotation extends IAUJovianMoonRotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double jd = (dt.getJulianDay() - 2451545.0);
		
		double meridian = 200.39 + 203.4889538 * jd - 0.085 * MathExt.sin_d(J[3]) - 0.022 * MathExt.sin_d(J[4]);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		
		double[] J = this.calculateJovianMoonElements(dt);
		double t = dt.getJulianCentury();
		
		double ra = 268.05 - 0.009 * t + 0.094 * MathExt.sin_d(J[3]) + 0.024 * MathExt.sin_d(J[4]);
		double dec = 64.50 + 0.003 * t + 0.040 * MathExt.cos_d(J[3]) + 0.011 * MathExt.cos_d(J[4]);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
