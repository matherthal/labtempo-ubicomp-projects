package br.uff.tempo.middleware.e;

public class SmartAndroidException extends Exception {

	private static final long serialVersionUID = 1L;

	public SmartAndroidException(String message, Throwable t) {
		super(message, t);
	}
	
	public SmartAndroidException(String message) {
		super(message);
	}
}
