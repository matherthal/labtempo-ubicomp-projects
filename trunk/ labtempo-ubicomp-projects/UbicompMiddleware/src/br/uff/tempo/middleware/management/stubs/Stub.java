package br.uff.tempo.middleware.management.stubs;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.current.api.Caller;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.comm.current.api.Tuple;

public class Stub implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Caller caller;

	public Stub(String calleeAgent) {
		caller = new Caller(calleeAgent);
	}
	
	public Object makeCall(String methodName, List<Tuple<String, Object>> params, Type returnType) {

		try {
			// Create message
			String msg = JSONHelper.createMethodCall(methodName, params);

			// Get answer from remote method call and return
			String ret = caller.sendMessage(msg);
			Object result = JSONHelper.getMessage(ret, returnType);
			return result;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
