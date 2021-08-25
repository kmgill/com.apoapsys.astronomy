package com.apoapsys.astronomy.orbits.vsop87;

import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.time.DateTime;

public class VSOP87Orbit implements Orbit<EclipticOrbitPosition> {
	
	private double[][][] L;
	private double[][][] B;
	private double[][][] R;
	private double period;
	
	public VSOP87Orbit(double[][][] L, double[][][] B, double[][][] R, double period) {
		this.L = L;
		this.B = B;
		this.R = R;
		this.period = period;
	}
	
	@Override
	public double getPeriod() {
		return period;
	}
	
	@Override
	public DateTime getEpoch() {
		return new DateTime();
	}
	
	
	@Override
	public EclipticOrbitPosition positionAtTime(DateTime dt) {
		return positionAtTime(dt, false);
	}
	
	public EclipticOrbitPosition positionAtTime(DateTime dt, boolean preserveDirection) {
		//double jd = dt.getJulianDay();
		double t = (dt.getJulianDay() - 2451545) / 365250;
		//double t = dt.getJulianCentury();
		
		double l = 0;
		double b = 0;
		double r = 0;
		
		double T = 1;
		for (int i = 0; i < L.length; i++) {
			double s = 0;
			for (int j = 0; j < L[i].length; j++) {
				s += L[i][j][0] * Math.cos(L[i][j][1] + L[i][j][2] * t);
			}
			l += s * T;
            T = t * T;
		}
		
		T = 1;
		for (int i = 0; i < B.length; i++) {
			double s = 0;
			for (int j = 0; j < B[i].length; j++) {
				s += B[i][j][0] * Math.cos(B[i][j][1] + B[i][j][2] * t);
			}
			b += s * T;
            T = t * T;
		}
		
		T = 1;
		for (int i = 0; i < R.length; i++) {
			double s = 0;
			for (int j = 0; j < R[i].length; j++) {
				s += R[i][j][0] * Math.cos(R[i][j][1] + R[i][j][2] * t);
			}
			r += s * T;
            T = t * T;
		}
		

		l = MathExt.clamp(l, 2 * Math.PI);

		double modB = (preserveDirection) ? 0 : -(Math.PI / 2);
		double modL = (preserveDirection) ? 0 : Math.PI;
		b += modB;
		l += modL;
		
		double x = Math.cos(l) * Math.sin(b) * r;
		double y = Math.cos(b) * r;
		double z = -Math.sin(l) * Math.sin(b) * r;
		
		Vector v = new Vector(x, y, z);
		EclipticOrbitPosition position = new EclipticOrbitPosition(v, new Ecliptic(Angle.fromRadians(b - modB), Angle.fromRadians(l - modL), r));

		return position;
	}
	
	
}
