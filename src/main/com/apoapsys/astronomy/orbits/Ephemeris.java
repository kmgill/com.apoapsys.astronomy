package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.time.EpochEnum;
import com.apoapsys.astronomy.time.JulianUtil;

public class Ephemeris {
	
	public double semiMajorAxis = 0;
	public double longitudeOfPerihelion = 0;
	public double eccentricity = 0;
	public Angle inclination = null;
	public Angle ascendingNode = null;
	public Angle argOfPeriapsis = null;
	public double meanAnomalyAtEpoch = 0;
	public double period = 0;
	public double meanMotion = 0;
	public double pericenterDistance = 0;
	public double derivativeOfMeanMotion = 0;
	public double epoch = EpochEnum.J2000.julianDay();
	public Angle rightAscension;
	public double epochStart;
	
	public Ephemeris() {
		
	}
	
	public Ephemeris(double semiMajorAxis,
					double longitudeOfPerihelion,
					double eccentricity,
					Angle inclination,
					Angle ascendingNode,
					Angle argOfPeriapsis,
					double meanAnomalyAtEpoch,
					double period,
					double epoch) {
		this.semiMajorAxis = semiMajorAxis;
		this.longitudeOfPerihelion = longitudeOfPerihelion;
		this.eccentricity = eccentricity;
		this.ascendingNode = ascendingNode;
		this.argOfPeriapsis = argOfPeriapsis;
		this.meanAnomalyAtEpoch = meanAnomalyAtEpoch;
		this.period = period;
		this.epoch = epoch;
	}
	
	
	/**
	 * Via http://orbitsimulator.com/formulas/OrbitalElements.html
	 * @param position
	 * @param velocity
	 * @param mass
	 * @param primaryMass
	 * @return
	 */
	public static Ephemeris fromStateVectors(Vector position, Vector velocity, double mass, double primaryMass) {
		
		Ephemeris eph = new Ephemeris();
		
		Vector Rv = position;
		Vector Vv = velocity;
		
		double Mu = Constants.G * (mass + primaryMass);
		double R = position.length();
		double V = velocity.length();
		
		double a = 1.0 / (2.0 / R - V * V / Mu); // Semi-Major axis
		
		Vector Hv = Rv.crossProduct(Vv);
		double H = Hv.length();
		
		double p = H * H / Mu;
		double q = Rv.dotProduct(Vv, false);
		
		double E = MathExt.sqrt(1.0 - p / a); // eccentricity
		
		double Ex = 1.0 - R / a;
		double Ey = q / MathExt.sqrt(a * Mu);
		
		double i = MathExt.acos(Hv.z / H);
		if (Double.isNaN(i) || Double.isInfinite(i)) {
			i = 0.0;
		}
		double lan = (i != 0.0) ? MathExt.atan2(Hv.x, -Hv.y) : 0.0;
		
		double TAx = H * H / (R * Mu) - 1.0;
		double TAy = H * q / (R * Mu);
		double TA = MathExt.atan2(TAy, TAx);
		double Cw = (Rv.x * MathExt.cos(lan) + Rv.y * MathExt.sin(lan)) / R;
		
		double Sw = 0.0;
		if (i == 0 || i == MathExt.PI){
			Sw = (Rv.y * MathExt.cos(lan) - Rv.x * MathExt.sin(lan)) / R;
		} else {
			Sw = Rv.z / (R * MathExt.sin(i));
		}
		
		double W = MathExt.atan2(Sw, Cw) - TA;
		W += (W < 0) ? 2.0 * Math.PI : 0.0;
		
		double u = MathExt.atan2(Ey, Ex); // eccentric anomoly
		double M = u - E * MathExt.sin(u); // Mean Anomaly
		double TL = W + TA + lan; // True longitude
		
		while(TL >= 2 * MathExt.PI){
			TL -= 2 * MathExt.PI;
		}
		
		double PM = a * E;
		double periapsis = a - PM;
		double apoapsis = a + PM;
		double period = 2 * Math.PI * MathExt.sqrt((a*a*a / Mu)) / 86400.0;
		
		
		if (Double.isNaN(E) || Double.isInfinite(E)) {
			E = 0.0;
		}
		
		
		eph.semiMajorAxis = a/149597870691.0;
		eph.eccentricity = E;
		eph.meanAnomalyAtEpoch = MathExt.clamp(M);
		eph.period = period;
		//eph.inclination = Angle.fromRadians(Math.PI * 0.5 - i);
		eph.inclination = Angle.fromRadians(i);
		eph.ascendingNode = Angle.fromRadians(lan);
		eph.argOfPeriapsis = Angle.fromRadians(W);
		eph.pericenterDistance = periapsis/149597870691.0;
		eph.meanMotion = 1.0 / eph.period;
		eph.epoch = JulianUtil.julianNow();
		
		// More to do
		//view-source:http://orbitsimulator.com/formulas/OrbitalElements.html
		
		
		
		return eph;
	}
	
	
}
