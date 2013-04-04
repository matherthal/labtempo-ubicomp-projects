package br.uff.tempo.middleware.comm.current.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.util.Log;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;

import com.google.gson.Gson;

public class JSONHelper {

	public static String createMethodCall(String method, List<Tuple<String, Object>> params) {
		List<String> jsonparams = new ArrayList<String>();
		List<String> jsontypes = new ArrayList<String>();

		Iterator<Tuple<String, Object>> iterator = params.iterator();

		Tuple<String, Object> tp;

		try {
			while (iterator.hasNext()) {
				tp = iterator.next();
				Class type = Class.forName(tp.key);
				jsonparams.add((new Gson()).toJson(tp.value, type));
				jsontypes.add(tp.key);
			}
		} catch (ClassNotFoundException e) {
			throw new SmartAndroidRuntimeException("Exception in JSONHelper#createMethodCall", e);
		}
		String id = UUID.randomUUID().toString();
		JSONRPC jsonRpc = new JSONRPC(id, method, jsonparams, jsontypes);

		return (new Gson()).toJson(jsonRpc, JSONRPC.class);
	}

	public static Object getMessage(String jsonRPCString, Type returnType) {
		if (void.class.equals(returnType)) {
			return null;
		}
		
		Log.d("SmartAndroid", "JSON String: " + jsonRPCString);
		
		JSONRPC jsonRpc = (new Gson()).fromJson(jsonRPCString, JSONRPC.class);
		
		if (jsonRpc.getResponse() == null && jsonRpc.getResponseType() == null) {
			return null;
		}

		return new Gson().fromJson(jsonRpc.getResponse(), returnType);
	}

	public static String createReply(String id, Object msg, Class type) {
		if (void.class.equals(type)) {
			return new Gson().toJson(new JSONRPC(id), JSONRPC.class);
		}
		
		String jsonString = (new Gson()).toJson(msg, type);
		
		String ret = convertPrimitiveToObjectIfNecessary(type);
		
		JSONRPC jsonRpc = new JSONRPC(id, jsonString, ret);						
		
		String resultMsg = new Gson().toJson(jsonRpc, JSONRPC.class);

		return resultMsg;
	}


	private static String convertPrimitiveToObjectIfNecessary(Class type) {
		if (boolean.class.getName().equals(type.getName())) {
			return Boolean.class.getName();
		} else if (int.class.getName().equals(type.getName())) {
			return Integer.class.getName();
		} else if (long.class.getName().equals(type.getName())) {
			return Long.class.getName();
		} else if (double.class.getName().equals(type.getName())) {
			return Double.class.getName();
		} else if (float.class.getName().equals(type.getName())) {
			return Float.class.getName();
		} else if (byte.class.getName().equals(type.getName())) {
			return Byte.class.getName();
		} else if (char.class.getName().equals(type.getName())) {
			return Character.class.getName();
		}
		
		return type.getName();
	}

	private static List<Tuple<String, String>> getParams(JSONRPC jsonRpc) {
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
	
	private static Object[] paramsToArray(List<Tuple<String, String>> argList) {
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++){
			Tuple<String, String> tuple = argList.get(i);
			if (tuple.value != null) {
				try {
					Class type = Class.forName(tuple.key);
					args[i] = new Gson().fromJson(tuple.value.toString(), type);
				} catch (ClassNotFoundException e) {
					throw new SmartAndroidRuntimeException("Exception in JSONHelper#createMethodCall", e);
				}
			} else {
				args[i] = null;
			}
		}			
		return args;
	}

	public static Object[] getParamsArray(String jsonRPCString) {
		JSONRPC jsonRpc = (new Gson()).fromJson(jsonRPCString, JSONRPC.class);
		
		List<Tuple<String, String>> params = getParams(jsonRpc);

		Object[] paramsArray = paramsToArray(params);
		return paramsArray;
	}

	public static String getMethodName(String jsonRPCString) {
		JSONRPC jsonRpc = (new Gson()).fromJson(jsonRPCString, JSONRPC.class);
		return jsonRpc.getMethod();
	}
	
	public static String getId(String jsonRPCString) {
		JSONRPC jsonRpc = (new Gson()).fromJson(jsonRPCString, JSONRPC.class);
		return jsonRpc.getId();
	}
	
	public static String toJson(Object value) {
		return new Gson().toJson(value);
	}
	
	public static Object fromJson(String json, Type type) {
		return new Gson().fromJson(json, type);
	}
}
