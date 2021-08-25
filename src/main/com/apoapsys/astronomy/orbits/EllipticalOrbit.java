package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Matrix;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.time.DateTime;
import com.apoapsys.astronomy.time.EpochEnum;

/**
 * Adapted from http://sourceforge.net/p/celestia/code/5229/tree/trunk/celestia/src/celephem/orbit.cpp
 * @author Kevin M. Gill
 *
 */
public class EllipticalOrbit implements Orbit<OrbitPosition> 
{	
	private Ephemeris ephemeris;
	
	private Matrix orbitPlaneRotation;
	
	public EllipticalOrbit(Ephemeris ephemeris) {
		this.ephemeris = ephemeris;

		Matrix ascendingNodeRotation = Matrix.makeRotationZ(ephemeris.ascendingNode.getRadians());
		Matrix inclinationRotation = Matrix.makeRotationX(ephemeris.inclination.getRadians());
		Matrix argOfPeriapsisRotation = Matrix.makeRotationZ(ephemeris.argOfPeriapsis.getRadians());

		orbitPlaneRotation = ascendingNodeRotation;
		orbitPlaneRotation.multiply(inclinationRotation);
		orbitPlaneRotation.multiply(argOfPeriapsisRotation);
	}
	
	@Override
	public DateTime getEpoch()
	{
		return new DateTime(ephemeris.epoch, EpochEnum.J2000);
	}
	
	
	@Override
	public double getPeriod() {
		return (ephemeris != null) ? ephemeris.period : 0.0;
	}
	
	protected Angle eccentricAnomaly(Angle meanAnomaly) {
		double M = meanAnomaly.getRadians();
		double E = 0;
		if (ephemeris.eccentricity == 0.0) {
			E = M;
		} else if (ephemeris.eccentricity < 0.2) {
			IterativeSolver solver = new KeplerFunc1Solver(ephemeris.eccentricity, M);
			double[] sol = solver.solveIterationFixed(M, 5);
			E = sol[0];
		} else if (ephemeris.eccentricity < 0.9) {
			IterativeSolver solver = new KeplerFunc2Solver(ephemeris.eccentricity, M);
			double[] sol = solver.solveIterationFixed(M, 6);
			E = sol[0];
		} else if (ephemeris.eccentricity < 1.0) {
			double E0 = M + 0.85 * ephemeris.eccentricity * ((Math.sin(M) >= 0.0) ? 1 : -1);
			IterativeSolver solver = new KeplerLaguerreConwaySolver(ephemeris.eccentricity, M);
			double[] sol = solver.solveIterationFixed(E0, 8);
			E = sol[0];
		} else if (ephemeris.eccentricity == 1.0) {
			E = M;
		} else {
			double E0 = Math.log(2 * M / ephemeris.eccentricity + 1.85);
			IterativeSolver solver = new KeplerLaguerreConwayHypSolver(ephemeris.eccentricity, M);
			double[] sol = solver.solveIterationFixed(E0, 30);
			E = sol[0];
		}
		return Angle.fromRadians(E);
	}
	
	protected Vector positionAtE(Angle eccentricAnomaly) {
		double E = eccentricAnomaly.getRadians();
		double x, y;
		
		if (ephemeris.eccentricity < 1.0) {
			double a = ephemeris.pericenterDistance / (1.0 - ephemeris.eccentricity);
			x = a * (Math.cos(E) - ephemeris.eccentricity);
			y = a * Math.sqrt(1 - ephemeris.eccentricity * ephemeris.eccentricity) * Math.sin(E);
		} else if (ephemeris.eccentricity > 1.0) {
			double a = ephemeris.pericenterDistance / (1.0 - ephemeris.eccentricity);
			x = -a * (ephemeris.eccentricity - MathExt.cosh(E));
			y = -a * Math.sqrt(ephemeris.eccentricity * ephemeris.eccentricity - 1) * MathExt.sinh(E);
		} else {
			x = 0.0;
			y = 0.0;
		}
		
		
		Vector pos = new Vector(x, y, 0);
		pos.applyMatrix(orbitPlaneRotation);
		return pos;
	}
	
	protected Vector velocityAtE(Angle E) {
		double x, y;

		if (ephemeris.eccentricity < 1.0) {
			double a = ephemeris.pericenterDistance / (1.0 - ephemeris.eccentricity);
			double sinE = MathExt.sin(E.getRadians());
			double cosE = MathExt.cos(E.getRadians());
        
			x = -a * sinE;
			y =  a * Math.sqrt(1 - MathExt.sqr(ephemeris.eccentricity)) * cosE;
		
			double meanMotion = 2.0 * Math.PI / ephemeris.period;
			double edot = meanMotion / (1 - ephemeris.eccentricity * cosE);
			x *= edot;
			y *= edot;
		} else if (ephemeris.eccentricity > 1.0) {
			double a = ephemeris.pericenterDistance / (1.0 - ephemeris.eccentricity);
			x = -a * (ephemeris.eccentricity - MathExt.cosh(E.getRadians()));
			y = -a * Math.sqrt(MathExt.sqr(ephemeris.eccentricity) - 1) * MathExt.sinh(E.getRadians());
		} else {
			// TODO: Handle parabolic orbits
			x = 0.0;
			y = 0.0;
		}
		
		Vector v = new Vector(x, y, 0);
		v.applyMatrix(orbitPlaneRotation);
		return new Vector(v.x, v.z, -v.y);
	};
	
	
	public Angle meanAnomalyAtTime(DateTime dt) {
		double timeSinceEpoch = dt.getJulianDaySinceEpoch(ephemeris.epoch);
		double meanAnomaly = ephemeris.meanAnomalyAtEpoch*1+(360*(ephemeris.meanMotion*(timeSinceEpoch)+0.5*ephemeris.derivativeOfMeanMotion*(timeSinceEpoch)*(timeSinceEpoch))) ; 
		meanAnomaly = MathExt.clamp(meanAnomaly, 360);
		return new Angle(meanAnomaly);
	}
	
	public Angle trueAnomalyAtTime(DateTime dt, Angle meanAnomaly, Angle eccentricAnomaly) {
		if (meanAnomaly == null) {
			meanAnomaly = this.meanAnomalyAtTime(dt);
		}
		if (eccentricAnomaly == null) {
			eccentricAnomaly = eccentricAnomaly(meanAnomaly);
		}
		
		double true_anomaly=MathExt.acos((  MathExt.cos(eccentricAnomaly.getRadians())-ephemeris.eccentricity)/(1-ephemeris.eccentricity*MathExt.cos(eccentricAnomaly.getRadians()))  ) ;
		return Angle.fromRadians(true_anomaly);
	}
	
	public Angle eccentricAnomalyAtTime(DateTime dt) {
		Angle meanAnomaly = this.meanAnomalyAtTime(dt);
		Angle E = eccentricAnomaly(meanAnomaly);
		return E;
	}
	
	
	public OrbitPosition velocityAtTime(DateTime dt) {
		Angle meanAnomaly = this.meanAnomalyAtTime(dt);
		Angle E = eccentricAnomaly(meanAnomaly);
		Vector pos = this.velocityAtE(E);
		pos.multiplyScalar(1.0 / 86400.0);
		//Vector v = new Vector(pos.x, pos.z, -pos.y);
		OrbitPosition position = new OrbitPosition(pos, E, meanAnomaly, trueAnomalyAtTime(dt, meanAnomaly, E));
		return position;
	}
	
	@Override
	public OrbitPosition positionAtTime(DateTime dt) {
		Angle meanAnomaly = this.meanAnomalyAtTime(dt);
		Angle E = eccentricAnomaly(meanAnomaly);
		Vector pos = this.positionAtE(E);
		Vector v = new Vector(pos.x, pos.z, -pos.y);
		OrbitPosition position = new OrbitPosition(v, E, meanAnomaly, trueAnomalyAtTime(dt, meanAnomaly, E));
		return position;
	}
	
	public double distanceAtTime(DateTime dt) {
		Angle trueAnomaly = trueAnomalyAtTime(dt, null, null);
		double p = ephemeris.semiMajorAxis * (1 - MathExt.sqr(ephemeris.eccentricity));
		double r = p / (1 + ephemeris.eccentricity * MathExt.cos(trueAnomaly.getRadians()));
		return r;
	}
	
	
	public Ephemeris getEphemeris() {
		return ephemeris;
	}
}
