package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class Bed extends ResourceAgent implements IBed {
	
	private static final long serialVersionUID = 1L;
	
	private boolean hasSomeone = false;

	public Bed(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.Bed", rans);
	}
	
	public Bed(String name, String rans, Position position) {
		super(name, "br.uff.tempo.middleware.resources.Bed", rans, position);
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
