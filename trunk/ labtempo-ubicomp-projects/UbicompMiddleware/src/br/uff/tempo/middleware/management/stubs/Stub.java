package br.uff.tempo.middleware.management.stubs;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.current.api.Caller;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.comm.current.api.Tuple;

public class Stub implements Serializable {

	public Caller caller;

	public Stub(String calleeAgent) {
		caller = new Caller(calleeAgent);
	}

	public Object makeCall(String methodName, List<Tuple> params) {

		try {
			// Create message
			String msg = JSONHelper.createMethodCall(methodName, params);

			// Get answer from remote method call and return
			String ret = caller.sendMessage(msg);
			return JSONHelper.getMessage(ret);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
