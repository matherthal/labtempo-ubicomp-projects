package br.uff.tempo.apps.onOffCounter;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;

public class CounterAg extends ResourceAgent {
	
	private CounterApp view;

	public CounterAg (String name, CounterApp view) {
		
		super(name, "br.uff.tempo.apps.onOffCounter.CounterAg", 461);
		this.view = view;
	}
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		if (rai.equals(view.getCurrentURL()) && method.equals("isOn")) {
			
			boolean b = Boolean.valueOf(value.toString());
			
			if (view.getCurrentState() != b) {
				
				view.inc();
				view.turnOnOff(b);
				
				Log.d("CounterLamp", "called inc... value from lamp = " + b);
			}
				
		};
	}
}
