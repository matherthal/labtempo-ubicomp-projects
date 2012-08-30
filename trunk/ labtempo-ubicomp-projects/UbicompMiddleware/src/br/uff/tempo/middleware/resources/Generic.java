package br.uff.tempo.middleware.resources;

import java.util.Random;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
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

	public Generic(String name, IResourceAgent ra, String cv) {
		// FIXME: consertar id passada por param
		super(name, "br.uff.tempo.middleware.resources.Generic", new Random().nextInt() + 1000);
		this.name = name;
		Log.i(TAG, "New Generic class. Name: " + name);

		this.identify();
		Log.i(TAG, "Generic stakeholder " + name + " identified");
		ra.registerStakeholder(cv, this.getURL());
		Log.i(TAG, "Generic stakeholder " + name + " subscribed");
	}

	@Override
	public void notificationHandler(String change) {

	}
}
