package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public class ResourceAgentStub extends Stub implements IResourceAgent{

	public ResourceAgentStub(String calleeID) {
		super(calleeID);
	}
	public void notificationHandler(String change)
	{
		String method = "notificationHandler";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("change", change));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public String getResourceClassName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void registerStakeholder(String method, String url) {
		String methodCall = "registerStakeholder";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("method", method));
			params.add(new Tuple<String, Object>("url", url));
			// Create message
			String msg = JSONHelper.createMethodCall(methodCall, params);
			// Get answer from remote method call
			JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
