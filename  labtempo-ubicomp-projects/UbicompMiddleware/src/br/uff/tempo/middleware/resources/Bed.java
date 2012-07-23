package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class Bed extends ResourceAgent implements IBed {

	private Integer numUsers = 0;
	private boolean hasSomeone;

	@Override
	public void notificationHandler(String change) {
	}

	@Override
	public boolean occupied() {
		return hasSomeone;
	}

	@Override
	public void lieDown() {
		this.hasSomeone = true;
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "lieDown", ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getOut() {
		this.hasSomeone = true;
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "getOut", ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
