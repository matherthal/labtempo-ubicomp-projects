package br.uff.tempo.middleware.comm.interest.api;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.comm.current.api.JSONRPC;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceContainer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class NewDispatcher {
	private static NewDispatcher instance;
	
	public synchronized static NewDispatcher getInstance() {
		if (instance == null) {
			instance = new NewDispatcher();
		}
		return instance;
	}

	private NewDispatcher() {}
	
	public String dispatch(String rai, String jsonRPCString) throws SmartAndroidException {
//		JsonParser parser = new JsonParser();
//		JsonArray array = parser.parse(jsonRPCString).getAsJsonArray();
		
//		Type collectionType = new TypeToken<HashMap<String, Object>>() {
//		}.getType();
//		
//	    JsonParser parser = new JsonParser();
//	    JsonArray array = parser.parse(jsonRPCString).getAsJsonArray();
//	    HashMap<String, Object> message = (new Gson()).fromJson(array.get(0), collectionType);
		
		JSONRPC jsonRpc = (new Gson()).fromJson(jsonRPCString, JSONRPC.class);
		
//	    JSONRPC jsonRpc = new JSONRPC((String) message.get("method"), (List<Object>) message.get("params"), (List<String>) message.get("types"));
	    
//		Map<String, Object> methodCall = jsonRpc.getMethod();
		String methodName = jsonRpc.getMethod();
//		List<Tuple<String, Object>> params = jsonRpc.getParams();
//		Map<String, Object> methodCall = getMethodCall(jsonRPCString);
//		String methodName = getMethodName(methodCall);
		
		List<Tuple<String, String>> params = getParams(jsonRpc);

		Object[] paramsArray = paramsToArray(params);

		ArrayList<Method> methods = ResourceContainer.getInstance().getMethods(rai);
		
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Log.d("SmartAndroid", String.format("Executing method %s with params %s", method, paramsArray));
				Object obj = execute(rai, method, paramsArray);
				String response = JSONHelper.createReply(obj, method.getReturnType());
				Log.d("SmartAndroid", String.format("Method %s returns response %s", method, response));
				return response;				
			}
		}
		
		throw new SmartAndroidException(String.format("Method %s with params %s doesn't exist in rai: %s", methodName, paramsArray, rai));
	}
	
	private Object execute(String rai, Method method, Object[] args) throws SmartAndroidException {
		ResourceAgent rA = ResourceContainer.getInstance().get(rai);
		
		try {
			return method.invoke(rA, args);
		} catch (IllegalArgumentException e) {
			throw new SmartAndroidException("Exception in execute method from Dispacher", e);
		} catch (IllegalAccessException e) {
			throw new SmartAndroidException("Exception in execute method from Dispacher", e);
		} catch (InvocationTargetException e) {
			throw new SmartAndroidException("Exception in execute method from Dispacher", e);
		}
	}
	
	public static Class getClassOf(String resource) throws ClassNotFoundException {
		return Class.forName(resource);
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

	private List<Tuple<String, String>> getParams(JSONRPC jsonRpc) {
		List<Tuple<String, String>> result = new ArrayList<Tuple<String, String>>();
		List<String> params = jsonRpc.getParams();
		List<String> types = jsonRpc.getTypes();
		Iterator<String> itParams = params.iterator();
		Iterator<String> itTypes = types.iterator();

		Tuple<String, String> tp;

		while (itParams.hasNext()) {
			String strType = itTypes.next();
			String param = itParams.next();
			tp = new Tuple<String, String>(strType, param);
			result.add(tp);
		}
	
		return result;
	}

	
	private Object[] paramsToArray(List<Tuple<String, String>> argList) {
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++){
			Tuple<String, String> tuple = argList.get(i);
			if (tuple.value != null) {
//				if (tuple.key.equals(String.class.getName())) {
//					args[i] = tuple.value;
//				} else if (tuple.key.equals(Position.class.getName())) {
//					args[i] = new Gson().fromJson(tuple.value.toString(), Position.class);
//				} else {
					try {
						Class type = getClassOf(tuple.key);
						args[i] = new Gson().fromJson(tuple.value.toString(), type);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//				}
			} else {
				args[i] = null;
			}
		}			
		return args;
	}
}
