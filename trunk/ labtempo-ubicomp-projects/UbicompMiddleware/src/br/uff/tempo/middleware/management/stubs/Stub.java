package br.uff.tempo.middleware.management.stubs;

import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;

public class Stub {
	
	public Caller caller;
	
	public Stub(String calleeAgent) {
		caller = new Caller(calleeAgent);
	}
	
	protected Object makeCall(String methodName, List<Tuple> params) {

		try {
			// Create message
			String msg = JSONHelper.createMethodCall(methodName, params);

			// Get answer from remote method call and return
			return JSONHelper.getMessage(caller.sendMessage(msg));

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
