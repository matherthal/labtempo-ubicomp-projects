package br.uff.tempo.testeRPC;

import java.util.HashMap;
import java.util.Map;

/*import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;*/
import org.json.*;

public class ResourceRegisterServiceStub {
	//RRS is a Singleton
	/*private static ResourceRegisterServiceStub instance = null;
	private ResourceRegisterServiceStub() {	}
		public static ResourceRegisterServiceStub getInstance() {
		if(instance == null) {
			instance = new ResourceRegisterServiceStub();
		}
		return instance;
	}*/
	public ResourceRegisterServiceStub() {
		// TODO Auto-generated constructor stub
	}
	
	private final String serverIP = "192.168.1.70";
	private Caller caller = new Caller(serverIP);	
	private final String RRS_ID = "regservice";
	
	public void register(ResourceAgent ra) {
		// The remote method to call
    	String method = "register";

    	// The required named parameters to pass
    	JSONArray params = new JSONArray();
    	try {
			params.put(new JSONObject().put("resource", ra));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// The mandatory request ID
    	String id = RRS_ID;

    	//Get answer from remote method call
    	Caller.ResultSetter setter = new Caller.ResultSetter() {
			public void setResult(String result) {
				registerResult(result);
			}
		};
		
		send(method, params, id, setter);
	}
	
	private void registerResult(String result) {
		// Parse response string
		JSONObject respIn = null;
		try {
			respIn = new JSONObject(result);
			result = respIn.getString("result");
			// Check for success or error
			if (!result.equals("")) {
		
				System.out.println("The request succeeded :");
		
				System.out.println("\tresult : " + respIn.getString("result"));
				System.out.println("\tid     : " + respIn.getString("id"));
			}
			else {
				System.out.println("The request failed :");
		
				
			}
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			// Handle exception...
		}
		
	}
	
	public void unregister(ResourceAgent ra) {
		// The remote method to call
    	String method = "unregister";

    	// The required named parameters to pass
    	JSONArray params = new JSONArray();
    	try {
			params.put(new JSONObject().put("resource", ra));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// The mandatory request ID
    	String id = RRS_ID;

    	//Get answer from remote method call
    	Caller.ResultSetter setter = new Caller.ResultSetter() {
			public void setResult(String result) {
				//TODO
			}
		};
		
		send(method, params, id, setter);
	}
	
	private void send(String method, JSONArray params, String id, Caller.ResultSetter setter) {
		/*// Create a new JSON-RPC 2.0 request
    	JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		// Serialise the request to a JSON-encoded string
    	String jsonString = reqOut.toString();
		// jsonString can now be dispatched to the server...*/
		
		try {
			JSONObject json = createMethodCall(method,params);
			String jsonString = json.toString();
			caller.sendMessage(jsonString, setter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public JSONObject createMethodCall(String method, JSONArray params) throws JSONException {
		  JSONObject json = new JSONObject();
		  
		  json.put("jsonrpc", "2.0");
		  json.put("id", "0");   // we don't really use this so value is always zero
		  json.put("method", method);
		  json.put("params", params);

		  return json;
	}
	
	public JSONArray createMethodParams(JSONArray params) throws JSONException {		  
		  // Default params that appear in all method calls
		  params.put(new JSONObject().put("first", "A"));
		  params.put(new JSONObject().put("second", "B"));
		  
		  // To add custom parameters, add to the return value rather than here, or this method becomes messy
		  return params;
	}
}
