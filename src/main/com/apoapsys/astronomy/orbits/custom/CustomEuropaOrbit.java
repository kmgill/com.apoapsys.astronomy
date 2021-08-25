package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class CustomEuropaOrbit extends CustomJovianOrbit {
	@Override
	public EclipticOrbitPosition positionAtTime(DateTime dt) {
		JovianElements e = computeElements(dt);

		double LPEJ = e.gamma;

		// Calculate periodic terms for lone.Gitude
		double sigma1 = 1.06476 * MathExt.sin_d(2 * (e.l[1] - e.l[2]))
				+ 0.04256 * MathExt.sin_d(e.l[0] - 2 * e.l[1] + e.p[2])
				+ 0.03581 * MathExt.sin_d(e.l[1] - e.p[2]) + 0.02395
				* MathExt.sin_d(e.l[0] - 2 * e.l[1] + e.p[3]) + 0.01984
				* MathExt.sin_d(e.l[1] - e.p[3]) - 0.01778
				* MathExt.sin_d(e.phi) + 0.01654
				* MathExt.sin_d(e.l[1] - e.p[1]) + 0.01334
				* MathExt.sin_d(e.l[1] - 2 * e.l[2] + e.p[1]) + 0.01294
				* MathExt.sin_d(e.p[2] - e.p[3]) - 0.01142
				* MathExt.sin_d(e.l[1] - e.l[2]) - 0.01057 * MathExt.sin_d(e.G)
				- 7.75e-3 * MathExt.sin_d(2 * (e.psi - LPEJ)) + 5.24e-3
				* MathExt.sin_d(2 * (e.l[0] - e.l[1])) - 4.6e-3
				* MathExt.sin_d(e.l[0] - e.l[2]) + 3.16e-3
				* MathExt.sin_d(e.psi - 2 * e.G + e.w[2] - 2 * LPEJ) - 2.03e-3
				* MathExt.sin_d(e.p[0] + e.p[2] - 2 * LPEJ - 2 * e.G) + 1.46e-3
				* MathExt.sin_d(e.psi - e.w[2]) - 1.45e-3
				* MathExt.sin_d(2 * e.G) + 1.25e-3
				* MathExt.sin_d(e.psi - e.w[3]) - 1.15e-3
				* MathExt.sin_d(e.l[0] - 2 * e.l[2] + e.p[2]) - 9.4e-4
				* MathExt.sin_d(2 * (e.l[1] - e.w[1])) + 8.6e-4
				* MathExt.sin_d(2 * (e.l[0] - 2 * e.l[1] + e.w[1])) - 8.6e-4
				* MathExt.sin_d(5 * e.G_ - 2 * e.G + 0.9115) - 7.8e-4
				* MathExt.sin_d(e.l[1] - e.l[3]) - 6.4e-4
				* MathExt.sin_d(3 * e.l[2] - 7 * e.l[3] + 4 * e.p[3]) + 6.4e-4
				* MathExt.sin_d(e.p[0] - e.p[3]) - 6.3e-4
				* MathExt.sin_d(e.l[0] - 2 * e.l[2] + e.p[3]) + 5.8e-4
				* MathExt.sin_d(e.w[2] - e.w[3]) + 5.6e-4
				* MathExt.sin_d(2 * (e.psi - LPEJ - e.G)) + 5.6e-4
				* MathExt.sin_d(2 * (e.l[1] - e.l[3])) + 5.5e-4
				* MathExt.sin_d(2 * (e.l[0] - e.l[2])) + 5.2e-4
				* MathExt.sin_d(3 * e.l[2] - 7 * e.l[3] + e.p[2] + 3 * e.p[3])
				- 4.3e-4 * MathExt.sin_d(e.l[0] - e.p[2]) + 4.1e-4
				* MathExt.sin_d(5 * (e.l[1] - e.l[2])) + 4.1e-4
				* MathExt.sin_d(e.p[3] - LPEJ) + 3.2e-4
				* MathExt.sin_d(e.w[1] - e.w[2]) + 3.2e-4
				* MathExt.sin_d(2 * (e.l[2] - e.G - LPEJ));
		sigma1 = MathExt.clamp(sigma1, 360.0);
		// sigma1 = dee.GToRad(sigma1);
		double L = e.l[1] + sigma1;

		// Calculate periodic terms for the tane.Gent of the latitude
		double B = 8.1004e-3 * MathExt.sin_d(L - e.w[1]) + 4.512e-4
				* MathExt.sin_d(L - e.w[2]) - 3.284e-4
				* MathExt.sin_d(L - e.psi) + 1.160e-4
				* MathExt.sin_d(L - e.w[3]) + 2.72e-5
				* MathExt.sin_d(e.l[0] - 2 * e.l[2] + 1.0146 * sigma1 + e.w[1])
				- 1.44e-5 * MathExt.sin_d(L - e.w[0]) + 1.43e-5
				* MathExt.sin_d(L + e.psi - 2 * LPEJ - 2 * e.G) + 3.5e-6
				* MathExt.sin_d(L - e.psi + e.G) - 2.8e-6
				* MathExt.sin_d(e.l[0] - 2 * e.l[2] + 1.0146 * sigma1 + e.w[2]);
		B = MathExt.atan_d(B);

		// Calculate the periodic terms for distance
		double R = 9.3848e-3 * MathExt.cos_d(e.l[0] - e.l[1]) - 3.116e-4
				* MathExt.cos_d(e.l[1] - e.p[2]) - 1.744e-4
				* MathExt.cos_d(e.l[1] - e.p[3]) - 1.442e-4
				* MathExt.cos_d(e.l[1] - e.p[1]) + 5.53e-5
				* MathExt.cos_d(e.l[1] - e.l[2]) + 5.23e-5
				* MathExt.cos_d(e.l[0] - e.l[2]) - 2.9e-5
				* MathExt.cos_d(2 * (e.l[0] - e.l[1])) + 1.64e-5
				* MathExt.cos_d(2 * (e.l[1] - e.w[1])) + 1.07e-5
				* MathExt.cos_d(e.l[0] - 2 * e.l[2] + e.p[2]) - 1.02e-5
				* MathExt.cos_d(e.l[1] - e.p[0]) - 9.1e-6
				* MathExt.cos_d(2 * (e.l[0] - e.l[2]));
		R = 9.39657 * RADIUS * (1 + R) / Constants.AU_TO_KM;

		double T = (dt.getJulianDay() - 2433282.423) / 36525.0;
		double P = 1.3966626 * T + 3.088e-4 * T * T;
		L += P;
		// L += dee.GToRad(P);
		// L += 22.203;

		// console.info([L, B, R]);

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
