package br.uff.tempo.middleware.comm.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceContainer;

public class Dispatcher {
	private static Dispatcher instance;
	
	
	public synchronized static Dispatcher getInstance() {
		if (instance == null) {
			instance = new Dispatcher();
		}
		return instance;
	}

	private Dispatcher() {}
	
	public String dispatch(String rans, String jsonRPCString) {
		String id = JSONHelper.getId(jsonRPCString);
		String methodName = JSONHelper.getMethodName(jsonRPCString);
		Object[] paramsArray = JSONHelper.getParamsArray(jsonRPCString);
		
		ArrayList<Method> methods = ResourceContainer.getInstance().getMethods(rans);
		
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Log.d("SmartAndroid", String.format("    %s#%s executed with params %s", method.getDeclaringClass().getName().substring(method.getDeclaringClass().getName().lastIndexOf(".") + 1), method.getName(), new ArrayList<Object>(Arrays.asList(paramsArray)))); // getSimpleName here doesn't work as expected 
				Object obj = execute(rans, method, paramsArray);
				String response = JSONHelper.createReply(id, obj, method.getReturnType());
				Log.d("SmartAndroid", String.format("    %s#%s executed with response %s", method.getDeclaringClass().getName().substring(method.getDeclaringClass().getName().lastIndexOf(".") + 1), method.getName(), response)); // getSimpleName here doesn't work as expected
				return response;				
			}
		}
		
		throw new SmartAndroidRuntimeException(String.format("Method %s with params %s doesn't exist in rai: %s", methodName, paramsArray, rans));
	}
	
	private Object execute(String rans, Method method, Object[] args) {
		ResourceAgent rA = ResourceContainer.getInstance().get(rans);
		
		try {
			return method.invoke(rA, args);
		} catch (IllegalArgumentException e) {
			throw new SmartAndroidRuntimeException("Exception in execute method from Dispacher", e);
		} catch (IllegalAccessException e) {
			throw new SmartAndroidRuntimeException("Exception in execute method from Dispacher", e);
		} catch (InvocationTargetException e) {
			throw new SmartAndroidRuntimeException("Exception in execute method from Dispacher", e);
		}
	}
}
