package br.uff.tempo.middleware.resources;

import java.util.Random;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IGeneric;

public class Generic extends ResourceAgent implements IGeneric {
	private String TAG = "Generic";
	protected String name = "";
	protected String url = "";

	public Generic(String name) {
		// FIXME: consertar id passada por param
		super(name, "br.uff.tempo.middleware.resources.Generic", new Random().nextInt() + 1000);
		this.name = name;
		Log.i(TAG, "New Generic class. Name: " + name);
	}

	@Override
	public void notificationHandler(String change) {

	}
}
