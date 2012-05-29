package br.uff.tempo.middleware.comm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.uff.tempo.middleware.management.IResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.ResourceRegister;
import br.uff.tempo.middleware.management.ResourceRepository;

public class Dispatcher  {
	//Dispatcher is a Singleton
	private static Dispatcher instance;
	private Map<String,ResourceAgent> instances;
	private Map<String,ArrayList<Method>> interfaces;//IAR and method list
	
	private Dispatcher()
	{
		instances = new HashMap<String, ResourceAgent>();
		interfaces = new HashMap<String,ArrayList<Method>>();
	}
	private void update() throws ClassNotFoundException
	{
		
		ArrayList<String> resources = ResourceDiscovery.getInstance().search("");//only localhost
		for (int i = 0; i< resources.size(); i++)
		{
			String resource = resources.get(i);
			ArrayList<Method> methodsList = new ArrayList<Method>();
			Class agent = getClassOf(resource);
			Method[] methods= agent.getMethods();
			for (Method method : methods)
				methodsList.add(method);
			interfaces.put(resource, methodsList);
		}
		instances.put("br.uff.tempo.middleware.management.ResourceDiscovery",ResourceDiscovery.getInstance());
		instances.put("br.uff.tempo.middleware.management.ResourceRegister",ResourceRegister.getInstance());
		instances.put("br.uff.tempo.middleware.management.ResourceRepository", ResourceRepository.getInstance());
		
	}
	
	private Class getClassOf(String resource) throws ClassNotFoundException
	{
		//ResourceAgent rA = ResourceDiscovery.getInstance().get(resource);
		return Class.forName(resource);
	}
	
	
	
	public static Dispatcher getInstance()
	{		
		if (instance == null)
			instance = new Dispatcher();
		return instance;
	}
	
	public String dispatch(String calleeID, String msg) throws ClassNotFoundException, JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		update();
		
		Type collectionType = new TypeToken<HashMap<String, Object>>(){}.getType();
		Map methodCall = getMethodCall(msg);
		String method = getMethodName(methodCall); 
		
		ArrayList<Method> methods = interfaces.get(calleeID);
		for (int i = 0; i< methods.size(); i++)				
			if (methods.get(i).getName().equals(method))
			{
				Object obj = execute(calleeID,methods.get(i),getParams(methodCall));
				return JSONHelper.createReply(obj);    	
			}
		return msg;		
	}
	
	private Map<String,Object> getMethodCall(String call)
	{
		Gson gson = new Gson();
		Type collectionType = new TypeToken<HashMap<String, Object>>(){}.getType();
		return gson.fromJson(call, collectionType);
	}
	private String getMethodName(Map<String,Object> methodCall)
	{	
		return (String) methodCall.get("method");
	}
	
	private Map<String,Object> getParams(Map<String,Object> methodCall)
	{
		return (Map<String, Object>) methodCall.get("params");
	}
	
	private Object execute(String resource, Method method, Map<String, Object> params) throws JSONException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ResourceAgent rA = instances.get(resource);
		Object [] paramsArray = params.values().toArray();
		Object[] args = new Object[params.size()];
		for (int i = 0; i< paramsArray.length; i++)
			args[i] = paramsArray[i];	
		return method.invoke(rA, args);
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
