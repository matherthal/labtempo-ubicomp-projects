package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IPresenceSensor;

public class PresenceSensor extends ResourceAgent implements IPresenceSensor {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = "PresenceSensor";
	private boolean presence = false;

	public PresenceSensor(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.PresenceSensor", 16);
	}

	public static final String CV_GETPRESENCE = "getPresence";
	@ContextVariable(name = "Presença")
	public boolean getPresence() {
		return presence;
	}

	public static String S_SETPRESENCE = "setPresence";
	@Service(name = "Detectar Presença")
	public void setPresence(boolean p) {
		if (p)
			Log.i(TAG, "Presence sensor " + this.getName() + " detects somebody");
		else
			Log.i(TAG, "Presence sensor " + this.getName() + " detects somebody has left");
		this.presence = p;
		
		notifyStakeholders(this.CV_GETPRESENCE, p);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
