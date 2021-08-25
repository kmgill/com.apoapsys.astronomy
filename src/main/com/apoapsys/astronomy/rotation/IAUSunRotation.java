package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class IAUSunRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double meridian = 84.176 + 14.1844000 * (dt.getJulianDay() - 2451545.0);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		return new EquatorialRightAscension(Angle.fromDegrees(286.13), Angle.fromDegrees(63.87));
	}

}
