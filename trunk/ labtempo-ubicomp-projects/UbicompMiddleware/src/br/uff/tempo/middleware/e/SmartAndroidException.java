package br.uff.tempo.middleware.e;

import android.util.Log;

public class SmartAndroidException extends Exception {

	private static final long serialVersionUID = 1L;

	public SmartAndroidException(String message, Throwable t) {
		super(message, t);
		Log.d("SmartAndroid", String.format("SmartAndroidException. Message: %s with %s", message, t));
	}
	
	public SmartAndroidException(String message) {
		super(message);
	}
}
