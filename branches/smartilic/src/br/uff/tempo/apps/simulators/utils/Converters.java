package br.uff.tempo.apps.simulators.utils;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceData;

public class Converters {

	public static Class getResourceClass(ResourceData res) {
		return Converters.getResourceClass(res.getType());
	}

	public static Class getResourceClass(String type) {
		Class c = null;

		try {
			c = Class.forName(type);
		} catch (ClassNotFoundException ex) {
			Log.e("SmartAndroid", "Cannot cast " + type
					+ " to a class! Returning null");
		}

		return c;
	}
}
