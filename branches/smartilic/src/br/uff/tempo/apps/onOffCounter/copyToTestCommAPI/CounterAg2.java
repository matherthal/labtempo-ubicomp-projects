package br.uff.tempo.apps.onOffCounter.copyToTestCommAPI;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;

public class CounterAg2 extends ResourceAgent {
	
	private static final long serialVersionUID = 1L;
	
	private CounterApp2 view;

	public CounterAg2 (String name, String rans, CounterApp2 view) {
		super(name, "br.uff.tempo.apps.onOffCounter.CounterAg", rans);
		this.view = view;
	}
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		if (rai.equals(view.getCurrentRAI()) && method.equals("isOn")) {
			
			boolean b = Boolean.valueOf(value.toString());
			
			if (view.getCurrentState() != b) {
				
				view.inc();
				view.turnOnOff(b);
				
				Log.d("CounterLamp", "called inc... value from lamp = " + b);
			}
				
		};
	}
}
