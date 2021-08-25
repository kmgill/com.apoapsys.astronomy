package com.apoapsys.astronomy.geo;

public class Latitude extends Coordinate {
	
	 public Latitude(int hour, int minute, double second, CardinalDirectionEnum direction) {
		 super(hour, minute, second, direction, CoordinateTypeEnum.LATITUDE);
	 }
	    
	 public Latitude(double decimal) {
		 super(decimal, CoordinateTypeEnum.LATITUDE);
	 }
	
}
