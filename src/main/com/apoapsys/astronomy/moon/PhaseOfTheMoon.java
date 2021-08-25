package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class PhaseOfTheMoon extends MoonUtil {

	private DateTime dt;
	private SelenographicPositionOfTheSun selenographicPositionOfTheSun;

	private String phase;

	protected PhaseOfTheMoon(DateTime dt,
			SelenographicPositionOfTheSun selenographicPositionOfTheSun) {
		this.dt = dt;
		this.selenographicPositionOfTheSun = selenographicPositionOfTheSun;

		String p = "";

		double c0 = selenographicPositionOfTheSun
				.getSelenographicColongitudeOfTheSun();

		if (c0 >= 355 || c0 < 5) {
			p = "First Quarter";
		} else if (c0 < 85) {
			p = "Waxing Gibbous";
		} else if (c0 >= 85 && c0 < 95) {
			p = "Full Moon";
		} else if (c0 < 175) {
			p = "Waning Gibbous";
		} else if (c0 >= 175 && c0 < 185) {
			p = "Last Quarter";
		} else if (c0 < 265) {
			p = "Waning Crescent";
		} else if (c0 >= 265 && c0 < 275) {
			p = "New Moon";
		} else if (c0 < 355) {
			p = "Waxing Crescent";
		}

		phase = p;
	}

	public DateTime getDt() {
		return dt;
	}

	public SelenographicPositionOfTheSun getSelenographicPositionOfTheSun() {
		return selenographicPositionOfTheSun;
	}

	public String getPhase() {
		return phase;
	}

	public static PhaseOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt,
				nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon) {
		OpticalLibrationsOfTheMoon opticalLibrations = OpticalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				opticalLibrations);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			OpticalLibrationsOfTheMoon opticalLibrations) {
		PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon = PhysicalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon,
						opticalLibrations);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				physicalLibrationsOfTheMoon);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon) {
		RelativePositionOfTheSun sunPosition = RelativePositionOfTheSun
				.calculate(dt, nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				physicalLibrationsOfTheMoon, sunPosition);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon,
			RelativePositionOfTheSun sunPosition) {
		SelenographicPositionOfTheSun selenographicPositionOfTheSun = SelenographicPositionOfTheSun
				.calculate(dt, nutationAndObliquity, positionOfTheMoon,
						physicalLibrationsOfTheMoon, sunPosition);
		return calculate(dt, selenographicPositionOfTheSun);
	}

	public static PhaseOfTheMoon calculate(DateTime dt,
			SelenographicPositionOfTheSun selenographicPositionOfTheSun) {
		return new PhaseOfTheMoon(dt, selenographicPositionOfTheSun);
	}

}
