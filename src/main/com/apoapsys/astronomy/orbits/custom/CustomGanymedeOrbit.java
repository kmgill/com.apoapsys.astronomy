package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class CustomGanymedeOrbit extends CustomJovianOrbit {
	
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
		double sigma1 = 0.1649*MathExt.sin_d(e.l[2] - e.p[2]) + 0.09081*MathExt.sin_d(e.l[2] - e.p[3])
			  - 0.06907*MathExt.sin_d(e.l[1] - e.l[2]) + 0.03784*MathExt.sin_d(e.p[2] - e.p[3])
			  + 0.01846*MathExt.sin_d(2*(e.l[2] - e.l[3])) - 0.01340*MathExt.sin_d(e.G)
			  - 0.01014*MathExt.sin_d(2*(psi - LPEJ)) + 7.04e-3*MathExt.sin_d(e.l[1] - 2*e.l[2] + e.p[2])
			  - 6.2e-3*MathExt.sin_d(e.l[1] - 2*e.l[2] + e.p[1]) - 5.41e-3*MathExt.sin_d(e.l[2] - e.l[3])
			  + 3.81e-3*MathExt.sin_d(e.l[1] - 2*e.l[2] + e.p[3]) + 2.35e-3*MathExt.sin_d(psi - e.w[2])
			  + 1.98e-3*MathExt.sin_d(psi - e.w[3]) + 1.76e-3*MathExt.sin_d(phi)
			  + 1.3e-3*MathExt.sin_d(3*(e.l[2] - e.l[3])) + 1.25e-3*MathExt.sin_d(e.l[0] - e.l[2])
			  - 1.19e-3*MathExt.sin_d(5*e.G_ - 2*e.G + 0.9115) + 1.09e-3*MathExt.sin_d(e.l[0] - e.l[1])
			  - 1.0e-3*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + 4*e.p[3]) + 9.1e-4*MathExt.sin_d(e.w[2] - e.w[3])
			  + 8.0e-4*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + e.p[2] + 3*e.p[3]) - 7.5e-4*MathExt.sin_d(2*e.l[1] - 3*e.l[2] + e.p[2])
			  + 7.2e-4*MathExt.sin_d(e.p[0] + e.p[2] - 2*LPEJ - 2*e.G) + 6.9e-4*MathExt.sin_d(e.p[3] - LPEJ)
			  - 5.8e-4*MathExt.sin_d(2*e.l[2] - 3*e.l[3] + e.p[3]) - 5.7e-4*MathExt.sin_d(e.l[2] - 2*e.l[3] + e.p[3])
			  + 5.6e-4*MathExt.sin_d(e.l[2] + e.p[2] - 2*LPEJ - 2*e.G) - 5.2e-4*MathExt.sin_d(e.l[1] - 2*e.l[2] + e.p[0])
			  - 5.0e-4*MathExt.sin_d(e.p[1] - e.p[2]) + 4.8e-4*MathExt.sin_d(e.l[2] - 2*e.l[3] + e.p[2])
			  - 4.5e-4*MathExt.sin_d(2*e.l[1] - 3*e.l[2] + e.p[3]) - 4.1e-4*MathExt.sin_d(e.p[1] - e.p[3])
			  - 3.8e-4*MathExt.sin_d(2*e.G) - 3.7e-4*MathExt.sin_d(e.p[2] - e.p[3] + e.w[2] - e.w[3])
			  - 3.2e-4*MathExt.sin_d(3*e.l[2] - 7*e.l[3] + 2*e.p[2] + 2*e.p[3]) + 3.0e-4*MathExt.sin_d(4*(e.l[2] - e.l[3]))
			  + 2.9e-4*MathExt.sin_d(e.l[2] + e.p[3] - 2*LPEJ - 2*e.G) - 2.8e-4*MathExt.sin_d(e.w[2] + psi - 2*LPEJ - 2*e.G)
			  + 2.6e-4*MathExt.sin_d(e.l[2] - LPEJ - e.G) + 2.4e-4*MathExt.sin_d(e.l[1] - 3*e.l[2] + 2*e.l[3])
			  + 2.1e-4*MathExt.sin_d(2*(e.l[2] - LPEJ - e.G)) - 2.1e-4*MathExt.sin_d(e.l[2] - e.p[1])
			  + 1.7e-4*MathExt.sin_d(e.l[2] - e.p[2]);
		sigma1 = MathExt.clamp(sigma1, 360.0);
		//sie.Gma = dee.GToRad(sie.Gma);
		double L = e.l[2] + sigma1;

		//Calculate periodic terms for the tane.Gent of the latitude
		double B = 3.2402e-3*MathExt.sin_d(L - e.w[2]) - 1.6911e-3*MathExt.sin_d(L - psi)
		  + 6.847e-4*MathExt.sin_d(L - e.w[3]) - 2.797e-4*MathExt.sin_d(L - e.w[1])
		  + 3.21e-5*MathExt.sin_d(L + psi - 2*LPEJ - 2*e.G) + 5.1e-6*MathExt.sin_d(L - psi + e.G)
		  - 4.5e-6*MathExt.sin_d(L - psi - e.G) - 4.5e-6*MathExt.sin_d(L + psi - 2*LPEJ)
		  + 3.7e-6*MathExt.sin_d(L + psi - 2*LPEJ - 3*e.G) + 3.0e-6*MathExt.sin_d(2*e.l[1] - 3*L + 4.03*sigma1 + e.w[1])
		  - 2.1e-6*MathExt.sin_d(2*e.l[1] - 3*L + 4.03*sigma1 + e.w[2]);
		B = MathExt.atan_d(B);

		//Calculate the periodic terms for distance
		double R = -1.4388e-3*MathExt.cos_d(e.l[2] - e.p[2]) - 7.919e-4*MathExt.cos_d(e.l[2] - e.p[3])
		  + 6.342e-4*MathExt.cos_d(e.l[1] - e.l[2]) - 1.761e-4*MathExt.cos_d(2*(e.l[2] - e.l[3]))
		  + 2.94e-5*MathExt.cos_d(e.l[2] - e.l[3]) - 1.56e-5*MathExt.cos_d(3*(e.l[2] - e.l[3]))
		  + 1.56e-5*MathExt.cos_d(e.l[0] - e.l[2]) - 1.53e-5*MathExt.cos_d(e.l[0] - e.l[1])
		  + 7.0e-6*MathExt.cos_d(2*e.l[1] - 3*e.l[2] + e.p[2]) - 5.1e-6*MathExt.cos_d(e.l[2] + e.p[2] - 2*LPEJ - 2*e.G);
		R = 14.98832 * RADIUS * (1 + R) / Constants.AU_TO_KM;
		
		double T = (dt.getJulianDay() - 2433282.423) / 36525.0;
		double P = 1.3966626*T + 3.088e-4*T*T;
		L += P;
		//L += dee.GToRad(P);

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
