package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public abstract class IAURotation extends BaseRotation implements Rotation {
	
	@Override
	public RotationalOrientation getOrientation(DateTime dt) {

		EquatorialRightAscension result = calculateOrientation(dt);
		double node = result.getRightAscension().getDegrees() + 90.0;
		double inclination = 90 - result.getDeclination().getDegrees();
		
		return new RotationalOrientation(result, Angle.fromDegrees(node), Angle.fromDegrees(inclination));
	}
	
	public Angle getSiderealRotation(DateTime dt) {
		return calculateMeridian(dt);
	}
	

	

	
	protected abstract Angle calculateMeridian(DateTime dt);
	protected abstract EquatorialRightAscension calculateOrientation(DateTime dt);
	
	protected static double clampCenturies(double t) {
		return MathExt.clamp(t, -Constants.IAU_SECULAR_TERM_VALID_CENTURIES, Constants.IAU_SECULAR_TERM_VALID_CENTURIES);
    }
}
