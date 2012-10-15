package br.uff.tempo.middleware.comm.current.api;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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

	public String dispatch(String calleeID, String msg) throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		update();

		Map<String, Object> methodCall = getMethodCall(msg);
		String method = getMethodName(methodCall);
		List<Tuple<String, Object>> params = getParams(methodCall);

		Object[] paramsArray = paramsToArray(params);

		ArrayList<Method> methods = interfaces.get(calleeID);
		
		for (int i = 0; i < methods.size(); i++) {
			if (methods.get(i).getName().equals(method)) {
				Object obj = null;
				Log.d("SmartAndroid", String.format("Executing method %s with params %s", methods.get(i), paramsArray));
				obj = execute(calleeID, methods.get(i), paramsArray);
				String response = JSONHelper.createReply(obj);
				Log.d("SmartAndroid", String.format("Method %s returns response %s", methods.get(i), response));
				return response;
			}
		}
		return msg;
	}

	private Map<String, Object> getMethodCall(String call) {
		Gson gson = new Gson();
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		
		JsonReader reader = new JsonReader(new StringReader(call));
		reader.setLenient(true);
		
		return gson.fromJson(call, collectionType);
	}

	private String getMethodName(Map<String, Object> methodCall) {
		return (String) methodCall.get("method");
	}

	private List<Tuple<String, Object>> getParams(Map<String, Object> methodCall) {
		List<Tuple<String, Object>> result = new ArrayList<Tuple<String, Object>>();
		List<Object> params = (List<Object>) methodCall.get("params");
		List<String> types = (List<String>) methodCall.get("types");
		Iterator<Object> itParams = params.iterator();
		Iterator<String> itTypes = types.iterator();

		Tuple<String, Object> tp;

		while (itParams.hasNext()) {
			String strType = itTypes.next();
			Object param = itParams.next();
			tp = new Tuple<String, Object>(strType, param);
			result.add(tp);
		}
	
		return result;
	}

	private Object[] paramsToArray(List<Tuple<String, Object>> argList) {
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++){
			Tuple<String, Object> tuple = argList.get(i);
			if (tuple.value != null) {
				if (tuple.key.equals(String.class.getName())) {
					args[i] = tuple.value;
				} else if (tuple.key.equals(Position.class.getName())) {
					args[i] = new Gson().fromJson(tuple.value.toString(), Position.class);
				} else {
					try {
						Class type = getClassOf(tuple.key);
						args[i] = new Gson().fromJson(tuple.value.toString(), type);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				args[i] = null;
			}
		}			
		return args;
	}

	private Object execute(String resource, Method method, Object[] args) throws IllegalArgumentException,
			IllegalAccessException {
		ResourceAgent rA = instances.get(resource);
		try {
			return method.invoke(rA, args);
		} catch (InvocationTargetException e) {
			
			Log.e("SmartAndroid", "Exception in execute method from Dispacher: " + e);
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
				socket.receiveSend();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("ReceiverError", e.getMessage());
			}
		}
	}
}
