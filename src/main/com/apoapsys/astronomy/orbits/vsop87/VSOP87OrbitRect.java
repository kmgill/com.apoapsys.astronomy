package com.apoapsys.astronomy.orbits.vsop87;

import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public class VSOP87OrbitRect implements Orbit<OrbitPosition> {
	
	private double[][][] X;
	private double[][][] Y;
	private double[][][] Z;
	private double period;
	
	public VSOP87OrbitRect(double[][][] X, double[][][] Y, double[][][] Z, double period) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
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
	
	
	private double evaluate(double[][][] terms, double t) {
		double v = 0;
		
		double T = 1;
		for (int i = 0; i < terms.length; i++) {
			double s = 0;
			for (int j = 0; j < terms[i].length; j++) {
				s += terms[i][j][0] * Math.cos(terms[i][j][1] + terms[i][j][2] * t);
			}
			v += s * T;
            T = t * T;
		}
		
		return v;
	}
	
	@Override
	public OrbitPosition positionAtTime(DateTime dt) {
		
		//double t = dt.getJulianCentury();
		double t = (dt.getJulianDay() - 2451545) / 365250;
		
		double x = 0;
		double y = 0; 
		double z = 0;
		
		double T = 1;
		for (int i = 0; i < X.length; i++) {
			double s = 0;
			for (int j = 0; j < X[i].length; j++) {
				s += X[i][j][0] * Math.cos(X[i][j][1] + X[i][j][2] * t);
			}
			x += s * T;
            T = t * T;
		}
		
		
		T = 1;
		for (int i = 0; i < Y.length; i++) {
			double s = 0;
			for (int j = 0; j < Y[i].length; j++) {
				s += Y[i][j][0] * Math.cos(Y[i][j][1] + Y[i][j][2] * t);
			}
			y += s * T;
            T = t * T;
		}
		
		T = 1;
		for (int i = 0; i < Z.length; i++) {
			double s = 0;
			for (int j = 0; j < Z[i].length; j++) {
				s += Z[i][j][0] * Math.cos(Z[i][j][1] + Z[i][j][2] * t);
			}
			z += s * T;
            T = t * T;
		}
		
		Vector v = new Vector(x, z, -y);
		OrbitPosition position = new OrbitPosition(v);

		return position;
	}

}
