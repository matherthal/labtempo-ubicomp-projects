package br.uff.tempo.middleware.management;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;

public class ResourceConnection implements ServiceConnection {

	private ResourceAgent rA;

	public ResourceAgent getrA() {
		return rA;
	}

	public void onServiceConnected(ComponentName className, IBinder service) {
		ResourceBinder binder = (ResourceBinder) service;
		rA = binder.getService();
	}

	public void onServiceDisconnected(ComponentName className) {
		rA = null;
	}

}
