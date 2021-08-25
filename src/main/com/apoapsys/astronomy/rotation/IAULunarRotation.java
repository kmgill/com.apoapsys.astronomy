package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAULunarRotation extends IAURotation {

	
	private double[] computeElements(DateTime dt) {
		double[] E = new double[14];
		double jd = (dt.getJulianDay() - 2451545.0);
		
		E[0] = 0;
		E[1]  = (125.045 -  0.0529921 * jd);
        E[2]  = (250.089 -  0.1059842 * jd);
        E[3]  = (260.008 + 13.012009 * jd);
        E[4]  = (176.625 + 13.3407154 * jd);
        E[5]  = (357.529 +  0.9856993 * jd);
        E[6]  = (311.589 + 26.4057084 * jd);
        E[7]  = (134.963 + 13.0649930 * jd);
        E[8]  = (276.617 +  0.3287146 * jd);
        E[9]  = ( 34.226 +  1.7484877 * jd);
        E[10] = ( 15.134 -  0.1589763 * jd);
        E[11] = (119.743 +  0.0036096 * jd);
        E[12] = (239.961 +  0.1643573 * jd);
        E[13] = ( 25.053 + 12.9590088 * jd);
		
		return E;
	}
	
	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double[] E = computeElements(dt);
		double jd = (dt.getJulianDay() - 2451545.0);
		
		double meridian = (38.3213
                + 13.17635815 * jd
                - 1.4e-12 * (jd * jd)
                + 3.5610 * MathExt.sin_d(E[1])
                + 0.1208 * MathExt.sin_d(E[2])
                - 0.0642 * MathExt.sin_d(E[3])
                + 0.0158 * MathExt.sin_d(E[4])
                + 0.0252 * MathExt.sin_d(E[5])
                - 0.0066 * MathExt.sin_d(E[6])
                - 0.0047 * MathExt.sin_d(E[7])
                - 0.0046 * MathExt.sin_d(E[8])
                + 0.0028 * MathExt.sin_d(E[9])
                + 0.0052 * MathExt.sin_d(E[10])
                + 0.0040 * MathExt.sin_d(E[11])
                + 0.0019 * MathExt.sin_d(E[12])
                - 0.0044 * MathExt.sin_d(E[13]));
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double[] E = computeElements(dt);
		double t = dt.getJulianCentury();
		
		double ra = 269.9949
		            + 0.0013 * t
		            - 3.8787 * MathExt.sin_d(E[1]) 
		            - 0.1204 * MathExt.sin_d(E[2])
		            + 0.0700 * MathExt.sin_d(E[3])
		            - 0.0172 * MathExt.sin_d(E[4])
		            + 0.0072 * MathExt.sin_d(E[6])
		            - 0.0052 * MathExt.sin_d(E[10])
		            + 0.0043 * MathExt.sin_d(E[13]);
            
		double dec = 66.5392
		            + 0.0130 * t
		            + 1.5419 * MathExt.cos_d(E[1])
		            + 0.0239 * MathExt.cos_d(E[2])
		            - 0.0278 * MathExt.cos_d(E[3])
		            + 0.0068 * MathExt.cos_d(E[4])
		            - 0.0029 * MathExt.cos_d(E[6])
		            + 0.0009 * MathExt.cos_d(E[7])
		            + 0.0008 * MathExt.cos_d(E[10])
		            - 0.0009 * MathExt.cos_d(E[13]);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
