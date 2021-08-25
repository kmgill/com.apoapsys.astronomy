package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Quaternion;

public class RotationalQuaternion {
	
	private Quaternion quaternion;
	private RotationalOrientation orientation;
	private Angle meridian;
	
	public RotationalQuaternion(Quaternion quaternion, RotationalOrientation orientation, Angle meridian) {
		this.quaternion = quaternion;
		this.orientation = orientation;
		this.meridian = meridian;
	}

	public Quaternion getQuaternion() {
		return quaternion;
	}

	public RotationalOrientation getOrientation() {
		return orientation;
	}

	public Angle getMeridian() {
		return meridian;
	}
	
	
}
