package com.apoapsys.astronomy.geo;

public class Longitude extends Coordinate {
	 public Longitude(int hour, int minute, double second, CardinalDirectionEnum direction) {
		 super(hour, minute, second, direction, CoordinateTypeEnum.LONGITUDE);
	 }
	    
	 public Longitude(double decimal) {
		 super(decimal, CoordinateTypeEnum.LONGITUDE);
	 }
	
}
