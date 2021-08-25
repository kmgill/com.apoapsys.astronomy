package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class IAUMarsRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double jd = (dt.getJulianDay() - 2451545.0);
		double meridian = 176.630 + 350.89198226 * jd;
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double ra = 317.68143 - 0.1061 * dt.getJulianCentury();
		double dec = 52.88650 - 0.0609 * dt.getJulianCentury();
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
