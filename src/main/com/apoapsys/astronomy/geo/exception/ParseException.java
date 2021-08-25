package com.apoapsys.astronomy.geo.exception;

@SuppressWarnings("serial")
public class ParseException extends Exception
{
	
	public ParseException(String message)
	{
		super(message);
	}
	
	public ParseException(String message, Throwable thrown)
	{
		super(message, thrown);
	}
	
	
}
