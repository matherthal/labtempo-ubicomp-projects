package br.uff.tempo.middleware.e;

import android.util.Log;

public class SmartAndroidRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SmartAndroidRuntimeException(String message, Throwable t) {
		super(message, t);
		Log.e("SmartAndroid", String.format("SmartAndroidRuntimeException. Message: %s with %s", message, t));
	}
	
	public SmartAndroidRuntimeException(String message) {
		super(message);
	}
}
