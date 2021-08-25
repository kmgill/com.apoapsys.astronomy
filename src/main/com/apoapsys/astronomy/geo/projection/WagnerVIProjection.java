package com.apoapsys.astronomy.geo.projection;

import com.apoapsys.astronomy.geo.exception.MapProjectionException;
import com.apoapsys.astronomy.logging.Log;
import com.apoapsys.astronomy.logging.Logging;


public class WagnerVIProjection extends AbstractBaseProjection
{
	@SuppressWarnings("unused")
	private static Log log = Logging.getLog(WagnerVIProjection.class);
	
	
	public WagnerVIProjection()
	{
		
	}
	
	public WagnerVIProjection(double north, double south, double east, double west, double width, double height)
	{
		super(north, south, east, west, width, height);
	}

	@Override
	public void project(double latitude, double longitude, double elevation, MapPoint point) throws MapProjectionException
	{
		point.column = longitude * Math.sqrt(1 - 3 * Math.pow(latitude / Math.PI, 2));
		point.row = latitude;
	}
	
	/*
	@Override
	public void getPoint(double latitude, double longitude, double elevation, MapPoint point) throws MapProjectionException
	{
		latitude = Math.toRadians(latitude);
		longitude = Math.toRadians(longitude);
		
		double x = longitude * Math.sqrt(1 - 3 * Math.pow(latitude / Math.PI, 2));
		double y = latitude;
		
		x = Math.toDegrees(x);
		y = Math.toDegrees(y);
		
		point.column = x;
		point.row = y;
	}
	*/
}
