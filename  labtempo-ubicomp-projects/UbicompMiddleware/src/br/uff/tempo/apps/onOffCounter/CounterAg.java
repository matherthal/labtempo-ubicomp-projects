package br.uff.tempo.apps.onOffCounter;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;

public class CounterAg extends ResourceAgent {
	
	private CounterApp view;

	public CounterAg (String name, CounterApp view) {
		
		super(name, "br.uff.tempo.apps.onOffCounter.CounterAg", 461);
		this.view = view;
	}
	@Override
	public void notificationHandler(String change) {
		
		String id = JSONHelper.getChange("id", change).toString();
		String mtd = JSONHelper.getChange("method", change).toString();
		Object val = JSONHelper.getChange("value", change);
		
		if (id.equals(view.getCurrentURL()) && mtd.equals("isOn")) {
			
			boolean b = Boolean.valueOf(val.toString());
			
			if (view.getCurrentState() != b) {
				
				view.inc();
				view.turnOnOff(b);
				
				Log.d("CounterLamp", "called inc... value from lamp = " + b);
			}
				
		};
	}

}
