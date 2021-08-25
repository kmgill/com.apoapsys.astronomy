package com.apoapsys.astronomy.bodies;

@SuppressWarnings("serial")
public class MajorBodiesLoadException extends Exception {
	
	
	public MajorBodiesLoadException(String message) {
		super(message);
	}
	
	public MajorBodiesLoadException(String message, Throwable thrown) {
		super(message, thrown);
	}
	
}
