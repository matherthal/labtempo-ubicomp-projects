package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IGeneric;

public class Generic extends ResourceAgent implements IGeneric {
	
	private static final long serialVersionUID = 1L;
	
	private String TAG = "Generic";
	protected String name = "";

	public Generic(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.Generic", rans);
		this.name = name;
		Log.d(TAG, "New Generic class. Name: " + name);
	}

	public Generic(String name, String rans, IResourceAgent ra, String cv) {
		super(name, "br.uff.tempo.middleware.resources.Generic", rans);
		this.name = name;
		Log.d(TAG, "New Generic class. Name: " + name);

		if (ra != null) {
			
			this.identify();
			Log.d(TAG, "Generic stakeholder " + name + " identified");
			
			ra.registerStakeholder(cv, this.getRANS());
			Log.d(TAG, "Generic stakeholder " + name + " subscribed");
		}
		
	}
	@Override
	public void notificationHandler(String rai, String method, Object value) {}
}
