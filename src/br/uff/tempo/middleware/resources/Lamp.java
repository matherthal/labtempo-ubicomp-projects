package br.uff.tempo.middleware.resources;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class Lamp extends ResourceAgent implements ILamp {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = "Lamp";
	private boolean isOn;
	public static final String CV_ISON = "isOn";

	public Lamp(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.Lamp", rans);
	}
	
	public Lamp(String name, String rans, Position position) {
		super(name, "br.uff.tempo.middleware.resources.Lamp", rans, position);
	}

	@Override
	@ContextVariable(name = "Lampada ligada", type = "OnOff")
	public boolean isOn() {
		return isOn;
	}

	@Override
	@Service(name = "Acender lampada", type = "TurnOnOff")
	public void turnOn() {
		Log.i(TAG, this.getName() + " - turn Lamp on");
		isOn = true;
		
		notifyStakeholders(CV_ISON, isOn);
	}

	@Override
	@Service(name = "Apagar lampada", type = "TurnOnOff")
	public void turnOff() {
		Log.i(TAG, this.getName() + " - turn Lamp off");
		isOn = false;
		
		notifyStakeholders(CV_ISON, isOn);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
