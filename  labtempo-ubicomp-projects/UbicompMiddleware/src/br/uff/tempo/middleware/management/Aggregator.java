package br.uff.tempo.middleware.management;

import org.json.JSONException;

import br.uff.tempo.middleware.management.interfaces.IAggregator;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.stubs.StoveStub;

public class Aggregator extends ResourceAgent implements IAggregator {
	//@Override
	public boolean addContextVariable(Interpreter interpreter) {
		this.contextVariables.add(interpreter);
		
		IResourceDiscovery iRDS = getRDS();
		IStove iStove = new StoveStub(iRDS.search("stove").get(0));
		iStove.registerStakeholder("getOvenIsOn", this.getURL());
		
		return false;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}
}
