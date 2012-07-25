package br.uff.tempo.middleware.resources;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class Bed extends ResourceAgent implements IBed {
	private Integer numUsers = 0;
	private boolean hasSomeone;

	public Bed(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Bed", 6);
	}

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
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "occupied", true));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getOut() {
		this.hasSomeone = true;
		try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), "occupied", false));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
