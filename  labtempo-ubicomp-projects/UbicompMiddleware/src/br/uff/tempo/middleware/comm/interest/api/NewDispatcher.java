package br.uff.tempo.middleware.comm.interest.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceContainer;

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
		String methodName = JSONHelper.getMethodName(jsonRPCString);
		Object[] paramsArray = JSONHelper.getParamsArray(jsonRPCString);
		
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
}
