package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class Lamp extends ResourceAgent implements ILamp {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = "Lamp";
	private boolean isOn;
	public static final String CV_ISON = "isOn";

	public Lamp(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Lamp", 15);
	}

	@Override
	@ContextVariable(name = "Lampada ligada")
	public boolean isOn() {
		return isOn;
	}

	@Override
	@Service(name = "Acender lampada")
	public void turnOn() {
		Log.i(TAG, "Turn Lamp on");
		isOn = true;
		
		notifyStakeholders(CV_ISON, isOn);
	}

	@Override
	@Service(name = "Apagar lampada")
	public void turnOff() {
		Log.i(TAG, "Turn Lamp off");
		isOn = false;
		
		notifyStakeholders(CV_ISON, isOn);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
