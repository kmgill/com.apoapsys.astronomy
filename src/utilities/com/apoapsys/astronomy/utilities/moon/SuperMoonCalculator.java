package com.apoapsys.astronomy.utilities.moon;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.apoapsys.astronomy.moon.IlluminatedFractionOfTheMoonsDisk;
import com.apoapsys.astronomy.moon.PositionOfTheMoon;
import com.apoapsys.astronomy.time.DateTime;

public class SuperMoonCalculator {
	
	
	
	public static void main(String[] args) {
		
		double epoch = 2451545.0 + 365.25;
		double end = epoch + (100 * 365.25);
		
		double resolution = 1.0 / 24.0 / 60.0;
		
		double closestApproach = 356577;
		double furthestApproach = 406655;
		double superWithinPercentage = .9;
		
		double s = (furthestApproach - closestApproach) * superWithinPercentage;
		
		
		
		double superDistance = furthestApproach - s; // http://www.astropro.com/features/articles/supermoon/
		double miniDistance = closestApproach + s;

		
		
		int monthNum = 1;
		int fullMoonsInMonth = 0;
		MoonPos lastMoonPos = null;
		
		for (double jd = epoch; jd <= end; jd += resolution) {
			
			DateTime dt = new DateTime(jd);

			IlluminatedFractionOfTheMoonsDisk frac = IlluminatedFractionOfTheMoonsDisk.calculate(dt);
			PositionOfTheMoon pos = PositionOfTheMoon.calculate(dt);
			
			double distance = pos.getDistanceBetweenTheCentersOfTheEarthAndMoon();
			double illumFrac = frac.getIlluminatedFractionOfTheMoonsDisk();
			
			
			
			MoonPos moonPos = new MoonPos(dt, distance, illumFrac);
			
			if (dt.getMonth() != monthNum) {
				monthNum = dt.getMonth();
				fullMoonsInMonth = 0;
			}
			
			
			if (lastMoonPos != null) {
				
				if (moonPos.illumFrac < lastMoonPos.illumFrac && moonPos.illumFrac >= 0.98) {

					
					if (lastMoonPos.isSuperMoonFull(superDistance)) {
						System.err.println(formatRow(lastMoonPos, "Super", "Full Moon"));
						jd += 5.0;
						moonPos = null;
					} else if (lastMoonPos.isMiniMoonFull(miniDistance)) {
						System.err.println(formatRow(lastMoonPos, "Mini", "Full Moon"));
						jd += 5.0;
						moonPos = null;
					} 
				}
				
				
				if (moonPos != null && moonPos.illumFrac > lastMoonPos.illumFrac && moonPos.illumFrac <= 0.05) {
					if (lastMoonPos.isSuperMoonNew(superDistance)) {
						System.err.println(formatRow(lastMoonPos, "Super", "New Moon"));
						jd += 5.0;
						moonPos = null;
					} else if (lastMoonPos.isMiniMoonNew(miniDistance)) {
						System.err.println(formatRow(lastMoonPos, "Mini", "New Moon"));
						jd += 5.0;
						moonPos = null;
					}
				}
			}
					
			lastMoonPos = moonPos;

		}
		
		
		
		
	}
	
	
	public static String formatRow(MoonPos moonPos, String type, String phase) {
		StringBuffer s = new StringBuffer();
		
		s.append(type);
		s.append(",");
		
		s.append(phase);
		s.append(",");
		
		s.append(formatDate(moonPos.julianDate));
		s.append(",");
		
		s.append(moonPos.distance);
		s.append(",");
		
		s.append(moonPos.illumFrac);

		
		return s.toString();
	}
	
	public static String formatDate(DateTime dt) {
		Date date = new Date(dt.getMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/y HH:mm");
		
		return sdf.format(date);
	}
	
	
	public static class MoonPos {
		
		double distance;
		double illumFrac;
		DateTime julianDate;
		
		
		public MoonPos(DateTime julianDate, double distance, double illumFrac) {
			this.julianDate = julianDate;
			this.distance = distance;
			this.illumFrac = illumFrac;
		}
		
		public boolean isFullMoon() {
			return (illumFrac >= .99);
		}
		
		public boolean isNewMoon() {
			return (illumFrac <= 0.01);
		}
		
		public boolean isMiniMoonNew(double miniDistance) {
			return (isNewMoon() && distance >= miniDistance);
		}
		
		public boolean isMiniMoonFull(double miniDistance) {
			return (isFullMoon() && distance >= miniDistance);
		}
		
		public boolean isSuperMoonNew(double superDistance) {
			return (isNewMoon() && distance <= superDistance);
		}
		
		public boolean isSuperMoonFull(double superDistance) {
			return (isFullMoon() && distance <= superDistance);
		}
	}
	
}
