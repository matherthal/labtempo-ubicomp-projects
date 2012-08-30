package br.uff.tempo.apps.onOffCounter;

import br.uff.tempo.middleware.management.ResourceAgent;

public class CounterAg extends ResourceAgent {

	public CounterAg (String name) {
		
		super(name, "br.uff.tempo.apps.onOffCounter.CounterAg", 461);		
	}
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

}
