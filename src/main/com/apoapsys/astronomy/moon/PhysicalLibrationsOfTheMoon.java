package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class PhysicalLibrationsOfTheMoon extends MoonUtil {

	private DateTime dt;
	private NutationAndObliquity nutationAndObliquity;
	private PositionOfTheMoon positionOfTheMoon;
	private OpticalLibrationsOfTheMoon opticalLibrations;

	private double physicalLibrationInLatitude;
	private double physicalLibrationInLongitude;

	private double rho;
	private double sigma;
	private double tau;
	
	protected PhysicalLibrationsOfTheMoon(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			OpticalLibrationsOfTheMoon opticalLibrations) {
		this.dt = dt;
		this.nutationAndObliquity = nutationAndObliquity;
		this.positionOfTheMoon = positionOfTheMoon;
		this.opticalLibrations = opticalLibrations;

		double T = dt.getJulianCentury();



		double A = opticalLibrations.getA();
		double M_ = positionOfTheMoon.getMeanAnomalyOfTheMoon();
		double M = positionOfTheMoon.getMeanAnomalyOfTheSun();
		double F = positionOfTheMoon.getMoonsArgumentOfLatitude();
		double D = positionOfTheMoon.getMeanElongationOfTheMoon();
		double E = 1 - 0.002516 * T - 0.0000074 * (T * T);
		double omega = nutationAndObliquity
				.getLongitudeOfTheAscendingNodeOfTheMoonsMeanOrbit();
		;

		double b_ = opticalLibrations.getOpticalLibrationInLatitude();

		double K1 = 119.75 + 131.849 * T;
		double K2 = 72.56 + 20.186 * T;

		double rho = -0.02752 * cos(M_) - 0.02245 * sin(F) + 0.00684
				* cos(M_ - 2 * F) - 0.00293 * cos(2 * F) - 0.00085
				* cos(2 * F - 2 * D) - 0.00054 * cos(M_ - 2 * D) - 0.00020
				* sin(M_ + F) - 0.00020 * cos(M_ + 2 * F) - 0.00020
				* cos(M_ - F) + 0.00014 * cos(M_ + 2 * F - 2 * D);

		double sigma = -0.02816 * sin(M_) + 0.02244 * cos(F) - 0.00682
				* sin(M_ - 2 * F) - 0.00279 * sin(2 * F) - 0.00083
				* sin(2 * F - 2 * D) + 0.00069 * sin(M_ - 2 * D) + 0.00040
				* cos(M_ + F) - 0.00025 * sin(2 * M_) - 0.00023
				* sin(M_ + 2 * F) + 0.00020 * cos(M_ - F) + 0.00019
				* sin(M_ - F) + 0.00013 * sin(M_ + 2 * F - 2 * D) - 0.00010
				* cos(M_ - 3 * F);

		double tau = +0.02520 * E * sin(M) + 0.00473 * sin(2 * M_ - 2 * F)
				- 0.00467 * sin(M_) + 0.00396 * sin(K1) + 0.00276
				* sin(2 * M_ - 2 * D) + 0.00196 * sin(omega) - 0.00183
				* cos(M_ - F) + 0.00115 * sin(M_ - 2 * D) - 0.00096
				* sin(M_ - D) + 0.00046 * sin(2 * F - 2 * D) - 0.00039
				* sin(M_ - F) - 0.00032 * sin(M_ - M - D) + 0.00027
				* sin(2 * M_ - M - 2 * D) + 0.00023 * sin(K2) - 0.00014
				* sin(2 * D) + 0.00014 * cos(2 * M_ - 2 * F) - 0.00012
				* sin(M_ - 2 * F) - 0.00012 * sin(2 * M_) + 0.00011
				* sin(2 * M_ - 2 * M - 2 * D);

		double l__ = -tau + (rho * cos(A) + sigma * sin(A)) * tan(b_);
		double b__ = sigma * cos(A) - rho * sin(A);

		physicalLibrationInLatitude = b__;
		physicalLibrationInLongitude = l__;
		this.rho = rho;
		this.sigma = sigma;
		this.tau = tau;
	}

	public DateTime getDt() {
		return dt;
	}

	public NutationAndObliquity getNutationAndObliquity() {
		return nutationAndObliquity;
	}

	public PositionOfTheMoon getPositionOfTheMoon() {
		return positionOfTheMoon;
	}

	public OpticalLibrationsOfTheMoon getOpticalLibrations() {
		return opticalLibrations;
	}

	public double getPhysicalLibrationInLatitude() {
		return physicalLibrationInLatitude;
	}

	public double getPhysicalLibrationInLongitude() {
		return physicalLibrationInLongitude;
	}
	
	

	public double getRho() {
		return rho;
	}

	public double getSigma() {
		return sigma;
	}

	public double getTau() {
		return tau;
	}

	public static PhysicalLibrationsOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static PhysicalLibrationsOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt,
				nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static PhysicalLibrationsOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon) {
		OpticalLibrationsOfTheMoon opticalLibrations = OpticalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				opticalLibrations);
	}

	public static PhysicalLibrationsOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			OpticalLibrationsOfTheMoon opticalLibrations) {
		return new PhysicalLibrationsOfTheMoon(dt, nutationAndObliquity,
				positionOfTheMoon, opticalLibrations);
	}

}
