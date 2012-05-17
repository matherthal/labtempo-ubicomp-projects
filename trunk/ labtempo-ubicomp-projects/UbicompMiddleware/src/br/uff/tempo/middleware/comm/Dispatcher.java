package br.uff.tempo.middleware.comm;

import org.json.JSONException;
import org.json.JSONObject;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceRegister;

public class Dispatcher  {
	//Dispatcher is a Singleton
	public String dispatch(String msg) {
		JSONObject reqIn = null;
    	
    	try {
    		reqIn = new JSONObject(msg);
			// How to extract the request data
			//printOnScreen("Parsed request with properties :");
			//printOnScreen("\tmethod     : " + reqIn.getString("method"));
			//printOnScreen("\tparameters : " + reqIn.getJSONArray("params"));
			//printOnScreen("\tid         : " + reqIn.getString("id") + "\n\n");

			// Process request...
    		boolean result =false;
    		final int regCode= 0;
    		switch(regCode)
    		{
    		case 0:
    			result = ResourceRegister.getInstance().register((ResourceAgent)reqIn.get("rA"));
    			
    		}

			// Create the response (note that the JSON-RPC ID must be echoed back)
			
			JSONObject respOut = new JSONObject();
			respOut.put("result", result);
			msg = respOut.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// Serialise response to JSON-encoded string
    	
		return msg;		
	}
    
    private String getMessage(String jsonString) {
    	// Parse request string
    	JSONObject reqIn = null;
    	
    	try {
    		reqIn = new JSONObject(jsonString);
    		
    	} catch (JSONException e) {
    		System.out.println(e.getMessage());
    		// Handle exception...
    	}
    	jsonString = "";
		try {
			// How to extract the request data
			//printOnScreen("Parsed request with properties :");
			//printOnScreen("\tmethod     : " + reqIn.getString("method"));
			//printOnScreen("\tparameters : " + reqIn.getJSONArray("params"));
			//printOnScreen("\tid         : " + reqIn.getString("id") + "\n\n");

			// Process request...

			// Create the response (note that the JSON-RPC ID must be echoed back)
			String result = "received";
			JSONObject respOut = new JSONObject();
			respOut.put("result", result);
			respOut.put("id", reqIn.getString("id"));
			jsonString = respOut.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// Serialise response to JSON-encoded string
    	
		return jsonString;
    	// The response string can now be sent back to the client...
    }
}
