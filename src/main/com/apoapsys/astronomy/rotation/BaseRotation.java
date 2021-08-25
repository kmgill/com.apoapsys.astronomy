package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Quaternion;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.math.Vectors;
import com.apoapsys.astronomy.time.DateTime;

public abstract class BaseRotation implements Rotation {
	
	@Override
	public RotationalQuaternion computeRotationalQuaternion(DateTime dt) {
		return computeRotationalQuaternion(dt, false);
	}
	
	@Override
	public RotationalQuaternion computeRotationalQuaternion(DateTime dt, boolean ignoreMeridian) {
		
		RotationalOrientation orientation = getOrientation(dt);
		Angle meridian = getSiderealRotation(dt);
		
		double m = MathExt.radians(MathExt.clamp(meridian.getDegrees(), 360) + 90);
		
		Vector nodeAxis = new Vector(1, 0, 0);
		nodeAxis.rotate((-orientation.getNode().getRadians() + Constants.RAD_90), Vectors.Y_AXIS);
		
		Quaternion inclinationQ = new Quaternion();
		inclinationQ.set(nodeAxis, -orientation.getInclination().getRadians());
		
		if (!ignoreMeridian) {
			Quaternion meridianQ = new Quaternion();
			meridianQ.set(new Vector(0, 1, 0), m);
			inclinationQ.multiply(meridianQ);
		}
		
		return new RotationalQuaternion(inclinationQ, orientation, meridian);
	}
}
