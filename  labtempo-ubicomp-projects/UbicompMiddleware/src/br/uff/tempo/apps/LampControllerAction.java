package br.uff.tempo.apps;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.interfaces.ILamp;
import br.uff.tempo.middleware.resources.stubs.LampStub;

public class LampControllerAction extends Generic {
	private final String TAG = "LampControllerAction";
	
	private static final long serialVersionUID = 1L;

	private ILamp lamp;
	private String roomFullRAI;
	private String roomEmptyRAI;
	
	public LampControllerAction(String name, String rans, String roomFullRAI, String roomEmptyRAI) {
		super(name, rans);
		this.roomEmptyRAI = roomEmptyRAI;
		this.roomFullRAI = roomFullRAI;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);

		// Get lamp to turn on or off
		//lamp = new LampStub(discovery.search(ResourceData.RANS, lRAI).get(0).getRans());
		// Verify if lamp is blocked
		/*String lName = lamp.getName();
		Tuple tp = lampDictionary.get(lName);
		if ((Boolean) tp.value) {
			Log.i(TAG, "Lamp " + lName + " is blocked");
			toastMessage("Lamp " + lName + " is blocked");
			return;
		}*/
		
		// Test rule's identification
		if (rai.equals(roomFullRAI))
			lamp.turnOn();
		else if (rai.equals(roomEmptyRAI))
			lamp.turnOff();
		else
			return;
		Log.i("Lamp", "RAI: " + lamp.getName() + " / Is on: " + lamp.isOn());
		/*if (lamp.isOn())
			toastMessage("Lâmpada " + lamp.getName() + " ligada");
		else
			toastMessage("Lâmpada " + lamp.getName() + " desligada");*/
	}
	
	public void setLamp(String rai) {
		//String lRAI = ""; 
		/*IRuleInterpreter r = new RuleInterpreterStub(rai);
		List<ResourceAgent> l = r.getInterests();
		Iterator<ResourceAgent> it = l.iterator();
		while (it.hasNext()) {
			List<ResourceData> lr = discovery.search(ResourceData.RANS, l.iterator().next().getRANS());
			ResourceData rd = lr.iterator().next();
			if (rd.getType() == "Lamp"){
				lRAI = rd.getRans();
				break;
			}
		}*/
		this.lamp = new LampStub(rai);
	}
}
