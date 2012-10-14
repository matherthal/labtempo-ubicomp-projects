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
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceContainer;
import br.uff.tempo.middleware.management.utils.Position;

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
		Map<String, Object> methodCall = getMethodCall(jsonRPCString);
		String methodName = getMethodName(methodCall);
		List<Tuple<String, Object>> params = getParams(methodCall);

		Object[] paramsArray = paramsToArray(params);

		ArrayList<Method> methods = ResourceContainer.getInstance().getMethods(rai);
		
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Log.d("SmartAndroid", String.format("Executing method %s with params %s", method, paramsArray));
				Object obj = execute(rai, method, paramsArray);
				String response = JSONHelper.createReply(obj);
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
	
	private Class getClassOf(String resource) throws ClassNotFoundException {
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
}
