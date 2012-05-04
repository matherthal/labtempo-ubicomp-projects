package br.uff.tempo.middleware.comm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.management.ResourceAgent;

public class ResourceRegisterServiceStub {
	// RRS is a Singleton
	/*
	 * private static ResourceRegisterServiceStub instance = null; private
	 * ResourceRegisterServiceStub() { } public static
	 * ResourceRegisterServiceStub getInstance() { if(instance == null) {
	 * instance = new ResourceRegisterServiceStub(); } return instance; }
	 */
	public ResourceRegisterServiceStub() {
		result = null;
		// TODO Auto-generated constructor stub
	}

	private final String serverIP = "192.168.1.70";
	private Caller caller = new Caller(serverIP);
	private final String RRS_ID = "regservice";
	private String result;
	
	public void register(ResourceAgent ra) {
		// The remote method to call
		String method = "register";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("resource", ra));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);

			// The mandatory request ID
			String id = RRS_ID;

			// Get answer from remote method call
			Caller.ResultSetter setter = new Caller.ResultSetter() {
				public void setResult(String result) {
					registerResult(result);
				}
			};

			caller.sendMessage(msg, setter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getResult()
	{
		return result;
	}

	private void registerResult(String result) {
		try {
			JSONHelper.getMessage(result);
			System.out.println(result.toString());
			this.result = result;
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
	}

	public void unregister(ResourceAgent ra) {
		// The remote method to call
		String method = "unregister";
	}

	/*
	 * private void send(String method, JSONArray params, String id,
	 * Caller.ResultSetter setter) { // Create a new JSON-RPC 2.0 request
	 * JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id); //
	 * Serialise the request to a JSON-encoded string String jsonString =
	 * reqOut.toString(); // jsonString can now be dispatched to the server...
	 * 
	 * try { JSONObject json = createMethodCall(method,params); String
	 * jsonString = json.toString(); caller.sendMessage(jsonString, setter); }
	 * catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
