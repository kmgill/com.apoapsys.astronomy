package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class CustomCallistoOrbit extends CustomJovianOrbit {
	
	@Override
	public double getPeriod() {
		return 3.5511810791;
	}
	
	@Override
	public EclipticOrbitPosition positionAtTime(DateTime dt) {
		JovianElements e = computeElements(dt);
		
		
		double LPEJ = e.gamma;
		double psi = e.psi;
		double phi = e.phi;

		//Calculate periodic terms for lone.Gitude
		double sigma1 =
			0.84287*MathExt.sin_d(e.l[3] - e.p[3])
			+ 0.03431*MathExt.sin_d(e.p[3] - e.p[2])
			- 0.03305*MathExt.sin_d(2*(psi - LPEJ))
			- 0.03211*MathExt.sin_d(e.G)
			- 0.01862*MathExt.sin_d(e.l[3] - e.p[2])
			+ 0.01186*MathExt.sin_d(psi - e.w[3])
			+ 6.23e-3*MathExt.sin_d(e.l[3] + e.p[3] - 2*e.G - 2*LPEJ)
			+ 3.87e-3*MathExt.sin_d(2*(e.l[3] - e.p[3]))
			- 2.84e-3*MathExt.sin_d(5*e.G_ - 2*e.G + 0.9115)
			- 2.34e-3*MathExt.sin_d(2*(psi - e.p[3]))
			- 2.23e-3*MathExt.sin_d(e.l[2] - e.l[3])
			- 2.08e-3*MathExt.sin_d(e.l[3] - LPEJ)
			+ 1.78e-3*MathExt.sin_d(psi + e.w[3] - 2*e.p[3])
			+ 1.34e-3*MathExt.sin_d(e.p[3] - LPEJ)
			+ 1.25e-3*MathExt.sin_d(2*(e.l[3] - e.G - LPEJ))
			- 1.17e-3*MathExt.sin_d(2*e.G)
			- 1.12e-3*MathExt.sin_d(2*(e.l[2] - e.l[3]))
			+ 1.07e-3*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + 4*e.p[3])
			+ 1.02e-3*MathExt.sin_d(e.l[3] - e.G - LPEJ)
			+ 9.6e-4*MathExt.sin_d(2*e.l[3] - psi - e.w[3])
			+ 8.7e-4*MathExt.sin_d(2*(psi - e.w[3]))
			- 8.5e-4*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + e.p[2] + 3*e.p[3])
			+ 8.5e-4*MathExt.sin_d(e.l[2] - 2*e.l[3] + e.p[3])
			- 8.1e-4*MathExt.sin_d(2*(e.l[3] - psi))
			+ 7.1e-4*MathExt.sin_d(e.l[3] + e.p[3] - 2*LPEJ - 3*e.G)
			+ 6.1e-4*MathExt.sin_d(e.l[0] - e.l[3])
			- 5.6e-4*MathExt.sin_d(psi - e.w[2])
			- 5.4e-4*MathExt.sin_d(e.l[2] - 2*e.l[3] + e.p[2])
			+ 5.1e-4*MathExt.sin_d(e.l[1] - e.l[3])
			+ 4.2e-4*MathExt.sin_d(2*(psi - e.G - LPEJ))
			+ 3.9e-4*MathExt.sin_d(2*(e.p[3] - e.w[3]))
			+ 3.6e-4*MathExt.sin_d(psi + LPEJ - e.p[3] - e.w[3])
			+ 3.5e-4*MathExt.sin_d(2*e.G_ - e.G + 3.2877)
			- 3.5e-4*MathExt.sin_d(e.l[3] - e.p[3] + 2*LPEJ - 2*psi)
			- 3.2e-4*MathExt.sin_d(e.l[3] + e.p[3] - 2*LPEJ - e.G)
			+ 3.0e-4*MathExt.sin_d(2*e.G_ - 2*e.G + 2.6032)
			+ 2.9e-4*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + 2*e.p[2] + 2*e.p[3])
			+ 2.8e-4*MathExt.sin_d(e.l[3] - e.p[3] + 2*psi - 2*LPEJ)
			- 2.8e-4*MathExt.sin_d(2*(e.l[3] - e.w[3]))
			- 2.7e-4*MathExt.sin_d(e.p[2] - e.p[3] + e.w[2] - e.w[3])
			- 2.6e-4*MathExt.sin_d(5*e.G_ - 3*e.G + 3.2877)
			+ 2.5e-4*MathExt.sin_d(e.w[3] - e.w[2])
			- 2.5e-4*MathExt.sin_d(e.l[1] - 3*e.l[2] + 2*e.l[3])
			- 2.3e-4*MathExt.sin_d(3*(e.l[2] - e.l[3]))
			+ 2.1e-4*MathExt.sin_d(2*e.l[3] - 2*LPEJ - 3*e.G)
			- 2.1e-4*MathExt.sin_d(2*e.l[2] - 3*e.l[3] + e.p[3])
			+ 1.9e-4*MathExt.sin_d(e.l[3] - e.p[3] - e.G)
			- 1.9e-4*MathExt.sin_d(2*e.l[3] - e.p[2] - e.p[3])
			- 1.8e-4*MathExt.sin_d(e.l[3] - e.p[3] + e.G)
			- 1.6e-4*MathExt.sin_d(e.l[3] + e.p[2] - 2*LPEJ - 2*e.G);
		sigma1 = MathExt.clamp(sigma1, 360.0);
		//sigma1 = dee.GToRad(sigma1);
		double L = e.l[3] + sigma1;

		//Calculate periodic terms for the tane.Gent of the latitude
		double B =
			- 7.6579e-3 * MathExt.sin_d(L - psi)
			+ 4.4134e-3 * MathExt.sin_d(L - e.w[3])
			- 5.112e-4  * MathExt.sin_d(L - e.w[2])
			+ 7.73e-5   * MathExt.sin_d(L + psi - 2*LPEJ - 2*e.G)
			+ 1.04e-5   * MathExt.sin_d(L - psi + e.G)
			- 1.02e-5   * MathExt.sin_d(L - psi - e.G)
			+ 8.8e-6    * MathExt.sin_d(L + psi - 2*LPEJ - 3*e.G)
			- 3.8e-6    * MathExt.sin_d(L + psi - 2*LPEJ - e.G);
		B = MathExt.atan_d(B);

		//Calculate the periodic terms for distance
		double R =
			- 7.3546e-3 * MathExt.cos_d(e.l[3] - e.p[3])
			+ 1.621e-4  * MathExt.cos_d(e.l[3] - e.p[2])
			+ 9.74e-5   * MathExt.cos_d(e.l[2] - e.l[3])
			- 5.43e-5   * MathExt.cos_d(e.l[3] + e.p[3] - 2*LPEJ - 2*e.G)
			- 2.71e-5   * MathExt.cos_d(2*(e.l[3] - e.p[3]))
			+ 1.82e-5   * MathExt.cos_d(e.l[3] - LPEJ)
			+ 1.77e-5   * MathExt.cos_d(2*(e.l[2] - e.l[3]))
			- 1.67e-5   * MathExt.cos_d(2*e.l[3] - psi - e.w[3])
			+ 1.67e-5   * MathExt.cos_d(psi - e.w[3])
			- 1.55e-5   * MathExt.cos_d(2*(e.l[3] - LPEJ - e.G))
			+ 1.42e-5   * MathExt.cos_d(2*(e.l[3] - psi))
			+ 1.05e-5   * MathExt.cos_d(e.l[0] - e.l[3])
			+ 9.2e-6    * MathExt.cos_d(e.l[1] - e.l[3])
			- 8.9e-6    * MathExt.cos_d(e.l[3] - LPEJ -e.G)
			- 6.2e-6    * MathExt.cos_d(e.l[3] + e.p[3] - 2*LPEJ - 3*e.G)
			+ 4.8e-6    * MathExt.cos_d(2*(e.l[3] - e.w[3]));

		R = 26.36273 * RADIUS * (1 + R) / Constants.AU_TO_KM;
		double T = (dt.getJulianDay() - 2433282.423) / 36525.0;
		double P = 1.3966626*T + 3.088e-4*T*T;
		L += P;
		//L += degToRad(P);

		//L += JupAscendingNode;

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
