package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IBed;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class Lamp extends ResourceAgent implements ILamp {

	private boolean isOn;

	public Lamp(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Lamp", 15);
	}

	@Override
	public void notificationHandler(String change) {
	}

	@Override
	public boolean isOn() {
		
		return isOn;
	}

	@Override
	public void turnOn() {
		
		isOn = true;
		
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "isOn", isOn));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void turnOff() {
		
		isOn = false;
		
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "isOn", isOn));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
