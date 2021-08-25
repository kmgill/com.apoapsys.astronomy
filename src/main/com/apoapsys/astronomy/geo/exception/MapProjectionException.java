package com.apoapsys.astronomy.geo.exception;

@SuppressWarnings("serial")
public class MapProjectionException extends Exception
{
	
	public MapProjectionException(String message)
	{
		super(message);
	}
	
	public MapProjectionException(String message, Throwable thrown)
	{
		super(message, thrown);
	}
	
	
}
