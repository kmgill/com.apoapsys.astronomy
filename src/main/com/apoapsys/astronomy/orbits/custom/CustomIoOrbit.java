package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class CustomIoOrbit extends CustomJovianOrbit {

	@Override
	public EclipticOrbitPosition positionAtTime(DateTime dt) {
		// double t = (jd - 2451545) / 36525;
		// double t = jd - 2443000.5;
		// double t = (jd - 2443000.5) / 36525;
		// double e = KMG.Jupiter.computeElements(t);

		JovianElements e = computeElements(dt);

		double LPEJ = e.pi;

		// Calculate periodic terms for longitude
		double sigma1 = 0.47259 * MathExt.sin_d(2 * (e.l[0] - e.l[1]))
				- 0.03478 * MathExt.sin_d(e.p[2] - e.p[3]) + 0.01081
				* MathExt.sin_d(e.l[1] - 2 * e.l[2] + e.p[2]) + 7.38e-3
				* MathExt.sin_d(e.phi) + 7.13e-3
				* MathExt.sin_d(e.l[1] - 2 * e.l[2] + e.p[1]) - 6.74e-3
				* MathExt.sin_d(e.p[0] + e.p[2] - 2 * LPEJ - 2 * e.G) + 6.66e-3
				* MathExt.sin_d(e.l[1] - 2 * e.l[2] + e.p[3]) + 4.45e-3
				* MathExt.sin_d(e.l[0] - e.p[2]) - 3.54e-3
				* MathExt.sin_d(e.l[0] - e.l[1]) - 3.17e-3
				* MathExt.sin_d(2 * (e.psi - LPEJ)) + 2.65e-3
				* MathExt.sin_d(e.l[0] - e.p[3]) - 1.86e-3 * MathExt.sin_d(e.G)
				+ 1.62e-3 * MathExt.sin_d(e.p[1] - e.p[2]) + 1.58e-3
				* MathExt.sin_d(4 * (e.l[0] - e.l[1])) - 1.55e-3
				* MathExt.sin_d(e.l[0] - e.l[2]) - 1.38e-3
				* MathExt.sin_d(e.psi + e.w[2] - 2 * LPEJ - 2 * e.G) - 1.15e-3
				* MathExt.sin_d(2 * (e.l[0] - 2 * e.l[1] + e.w[1])) + 8.9e-4
				* MathExt.sin_d(e.p[1] - e.p[3]) + 8.5e-4
				* MathExt.sin_d(e.l[0] + e.p[2] - 2 * LPEJ - 2 * e.G) + 8.3e-4
				* MathExt.sin_d(e.w[1] - e.w[2]) + 5.3e-4
				* MathExt.sin_d(e.psi - e.w[1]);
		sigma1 = MathExt.clamp(sigma1, 360.0);
		// sigma1 = degToRad(sigma1);
		double L = e.l[0] + sigma1;

		// Calculate periodic terms for the tangent of the latitude
		double B = 6.393e-4 * MathExt.sin_d(L - e.w[0]) + 1.825e-4
				* MathExt.sin_d(L - e.w[1]) + 3.29e-5
				* MathExt.sin_d(L - e.w[2]) - 3.11e-5
				* MathExt.sin_d(L - e.psi) + 9.3e-6 * MathExt.sin_d(L - e.w[3])
				+ 7.5e-6
				* MathExt.sin_d(3 * L - 4 * e.l[1] - 1.9927 * sigma1 + e.w[1])
				+ 4.6e-6 * MathExt.sin_d(L + e.psi - 2 * LPEJ - 2 * e.G);
		B = MathExt.atan_d(B);

		// Calculate the periodic terms for distance
		double R = -4.1339e-3 * MathExt.cos_d(2 * (e.l[0] - e.l[1])) - 3.87e-5
				* MathExt.cos_d(e.l[0] - e.p[2]) - 2.14e-5
				* MathExt.cos_d(e.l[0] - e.p[3]) + 1.7e-5
				* MathExt.cos_d(e.l[0] - e.l[1]) - 1.31e-5
				* MathExt.cos_d(4 * (e.l[0] - e.l[1])) + 1.06e-5
				* MathExt.cos_d(e.l[0] - e.l[2]) - 6.6e-6
				* MathExt.cos_d(e.l[0] + e.p[2] - 2 * LPEJ - 2 * e.G);
		R = 5.90569 * RADIUS * (1 + R) / Constants.AU_TO_KM;

		double T = (dt.getJulianDay() - 2433282.423) / 36525.0;
		double P = 1.3966626 * T + 3.088e-4 * T * T;
		L += P;

		L = L * Constants.PI_BY_180;
		B = B * Constants.PI_BY_180;

		B -= Math.PI / 2;
		L += Math.PI;

		double x = Math.cos(L) * Math.sin(B) * R;
		double y = Math.cos(B) * R;
		double z = -Math.sin(L) * Math.sin(B) * R;

		Vector v = new Vector(x, y, z);
		EclipticOrbitPosition position = new EclipticOrbitPosition(v, new Ecliptic(Angle.fromRadians(B), Angle.fromRadians(L), R));
		return position;
	}

}
