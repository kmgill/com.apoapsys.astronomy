package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class IAUEarthRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double meridian = 190.147 + 360.9856235 * (dt.getJulianDay() - 2451545.0);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double ra = 0.00 - 0.641 * dt.getJulianCentury();
		//double dec = 90.00 - 0.557 * dt.getJulianCentury();
		double dec = 90.00 - 23.4;
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
