package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class Bed extends ResourceAgent implements IBed {
	
	private static final long serialVersionUID = 1L;
	
	private Integer numUsers = 0;
	private boolean hasSomeone;

	public Bed(String name) {
		// FIXME: get correct id
		super(name, "br.uff.tempo.middleware.resources.Bed", 6);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}

	@Override
	public boolean occupied() {
		return hasSomeone;
	}

	@Override
	public void lieDown() {
		this.hasSomeone = true;
		
		notifyStakeholders("occupied", true);
	}

	@Override
	public void getOut() {
		this.hasSomeone = true;
		
		notifyStakeholders("occupied", false);
	}
}
