package br.uff.tempo.middleware.comm.current.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceContainer;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.ResourceRepository;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRepositoryStub;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Dispatcher extends Thread {
	// Dispatcher is a Singleton
	private static Dispatcher instance;
	private ResourceContainer instances;
	private Map<String, ArrayList<Method>> interfaces;// IAR and method list
	SocketService socket;

	private Dispatcher() {
		instances = ResourceContainer.getInstance();
		interfaces = new HashMap<String, ArrayList<Method>>();
		socket = new SocketService();
	}

	private void update() throws ClassNotFoundException {
		ArrayList<String> resources = new ArrayList<String>();

		IResourceRepository rR;

		// if discovery agent

		String rdsHost = (new ResourceAgentIdentifier(IResourceDiscovery.RDS_ADDRESS)).getPath();

		if (rdsHost.equals(ResourceAgentIdentifier.getLocalIpAddress())) {

			rR = ResourceRepository.getInstance();

			resources = ResourceDiscovery.getInstance().search(ResourceAgentIdentifier.getLocalIpAddress());
		} else {

			IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
			rR = new ResourceRepositoryStub(discovery.search("ResourceRepository").get(0));

			resources = discovery.search(ResourceAgentIdentifier.getLocalIpAddress());
		}

		// only localhost
		for (int i = 0; i < resources.size(); i++) {
			String resource = resources.get(i);
			ArrayList<Method> methodsList = new ArrayList<Method>();
			ResourceAgentIdentifier rai = new ResourceAgentIdentifier(resource);
			Class agent = getClassOf(rai.getType().get(rai.getType().size() - 1));
			Method[] methods = agent.getMethods();
			for (Method method : methods)
				methodsList.add(method);
			interfaces.put(resource, methodsList);
		}
	}

	private Class getClassOf(String resource) throws ClassNotFoundException {
		// ResourceAgent rA = ResourceDiscovery.getInstance().get(resource);
		return Class.forName(resource);
	}

	public static Dispatcher getInstance() {
		if (instance == null)
			instance = new Dispatcher();
		return instance;
	}

	public String dispatch(String calleeID, String msg) throws ClassNotFoundException, JSONException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		update();

		Map<String, Object> methodCall = getMethodCall(msg);
		String method = getMethodName(methodCall);
		List<Object> params = getParams(methodCall);

		Object[] paramsArray = paramsToArray(params);

		ArrayList<Method> methods = interfaces.get(calleeID);
		for (int i = 0; i < methods.size(); i++)
			if (methods.get(i).getName().equals(method)) {
				Object obj = null;
				try {
					obj = execute(calleeID, methods.get(i), paramsArray);
				} catch (IllegalArgumentException e) {
					// java.lang.IllegalArgumentException: argument 1 should
					// have type int, got java.lang.Double
					String[] error = e.getMessage().split(",");
					if (error[0].contains("int")) {
						if (error[1].contains("Double"))
						{
							for (int j = 0; j < paramsArray.length; j++) {
								if (paramsArray[j].getClass().equals(Double.class))
									paramsArray[j] = (int) Math.round((Double) paramsArray[j]);
							}
						}
					} else if (error[0].contains("float")) {
						if (error[1].contains("Double"))
						{
							for (int j = 0; j < paramsArray.length; j++) {
								if (paramsArray[j].getClass().equals(Double.class))
									paramsArray[j] = Float.parseFloat(paramsArray[j].toString());
							}
						}
					}

					obj = execute(calleeID, methods.get(i), paramsArray);
				}
				return JSONHelper.createReply(obj);
			}
		return msg;
	}

	private Map<String, Object> getMethodCall(String call) {
		Gson gson = new Gson();
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		return gson.fromJson(call, collectionType);
	}

	private String getMethodName(Map<String, Object> methodCall) {
		return (String) methodCall.get("method");
	}

	private List<Object> getParams(Map<String, Object> methodCall) {
		return (List<Object>) methodCall.get("params");
	}

	private Object[] paramsToArray(List<Object> params) {
		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++)
			args[i] = params.get(i);
		return args;
	}

	private Object execute(String resource, Method method, Object[] args) throws JSONException, IllegalArgumentException,
			IllegalAccessException {
		ResourceAgent rA = instances.get(resource);
		try {
			return method.invoke(rA, args);
		} catch (InvocationTargetException e) {
			return null;
		}
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
			// printOnScreen("Parsed request with properties :");
			// printOnScreen("\tmethod     : " + reqIn.getString("method"));
			// printOnScreen("\tparameters : " + reqIn.getJSONArray("params"));
			// printOnScreen("\tid         : " + reqIn.getString("id") +
			// "\n\n");

			// Process request...

			// Create the response (note that the JSON-RPC ID must be echoed
			// back)
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

	public void run() {
		while (true) {
			try {
				socket.receiveSend(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("ReceiverError", e.getMessage());
			}
		}
	}
}
