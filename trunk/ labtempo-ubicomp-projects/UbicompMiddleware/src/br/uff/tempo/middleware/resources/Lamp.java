package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class Lamp extends ResourceAgent implements ILamp {
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
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), CV_ISON, isOn));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	@Service(name = "Apagar lampada")
	public void turnOff() {
		Log.i(TAG, "Turn Lamp off");
		isOn = false;
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), CV_ISON, isOn));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notificationHandler(String change) {
	}
}
