package com.apoapsys.astronomy.image;

@SuppressWarnings("serial")
public class ImageException extends Exception
{
	
	public ImageException()
	{
		super();
	}
	
	
	public ImageException(String message)
	{
		super(message);
	}
	
	public ImageException(String message, Throwable thrown)
	{
		super(message, thrown);
	}
	
}
