package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class Bed extends ResourceAgent implements IBed {

	private boolean hasSomeone;
	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasSomeoneIn() {
		
		return hasSomeone;
	}

	@Override
	public void setSomeoneIn(boolean someone) {
		
		this.hasSomeone = someone;
	}

}
