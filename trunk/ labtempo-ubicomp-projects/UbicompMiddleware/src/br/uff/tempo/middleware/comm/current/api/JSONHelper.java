package br.uff.tempo.middleware.comm.current.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONHelper {

	public static String createMethodCall(String method,
			List<Tuple<String, Object>> params) throws JSONException {

		Map<String, Object> methodCall = new HashMap<String, Object>();
		List<Object> jsonparams = new ArrayList<Object>();
		List<Object> jsontypes = new ArrayList<Object>();

		Iterator<Tuple<String, Object>> iterator = params.iterator();

		Tuple<String, Object> tp;

		try {
			while (iterator.hasNext()) {
				tp = iterator.next();
				Class type = Class.forName(tp.key);
				jsonparams.add(tp.value);
				jsontypes.add(tp.key);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		methodCall.put("jsonrpc", "2.0");
		methodCall.put("method", method);
		methodCall.put("params", jsonparams);
		methodCall.put("types", jsontypes);

		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();

		return (new Gson()).toJson(methodCall, collectionType);
	}

	// deserialize
	private static String deserialize(String result) {

		// Parse response string
		Type collectionType = new TypeToken<HashMap<String, String>>() {
		}.getType();

		String[] resultData = result.split(";");
		Map<String, String> response = (new Gson()).fromJson(resultData[0],
				collectionType);
		return response.get("result");
	}

	public static Object getMessage(String result) throws JSONException {
		// Parse response string
		Type collectionType = new TypeToken<HashMap<String, String>>() {
		}.getType();

		String[] resultData = result.split(";");
		Map<String, String> response = (new Gson()).fromJson(resultData[0],
				collectionType);
		String json = response.get("result");
		
		Object obj = new Gson().fromJson(json, Object.class);

		return obj;
	}

	public static Object getMessage(String result, Class type)
			throws JSONException {
		
		String json = deserialize(result);
		return new Gson().fromJson(json, type);
	}
	
	public static Object getMessage(String result, Type type)
			throws JSONException {
		
		String json = deserialize(result);
		return new Gson().fromJson(json, type);
	}

	public static String createReply(Object msg) throws JSONException {
		// FIXME: Can I return anything?

		String msgStr = new Gson().toJson(msg);
		Map<String, String> response = new HashMap<String, String>();
		response.put("result", msgStr);
		response.put("id", "0"); // we don't really use this so value is always
									// zero
		String resultMsg;
		try {
			resultMsg = (new Gson()).toJson(response);
		} catch (Exception e) {
			String result = (new Gson()).toJson(response.get("result"),
					View.class);
			response.put("result", result);
			resultMsg = (new Gson()).toJson(response);
		}
		return resultMsg;
		// Serialise response to JSON-encoded string
		// The response string can now be sent back to the client...
	}

	@Deprecated
	public static String createChange(String id, String method, Object value) {
		Map<String, Object> change = new HashMap<String, Object>();
		change.put("jsonobject", "2.0");
		change.put("id", id);
		change.put("method", method);
		change.put("value", value);
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		return (new Gson()).toJson(change, collectionType);
	}

	@Deprecated
	public static Object getChange(String what, String change) {
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();

		Map<String, Object> changeObj = (new Gson()).fromJson(change,
				collectionType);
		return changeObj.get(what);
	}

	/*
	 * public static JSONArray createMethodParams(JSONArray params) throws
	 * JSONException { // Default params that appear in all method calls
	 * params.put(new JSONObject().put("first", "A")); params.put(new
	 * JSONObject().put("second", "B"));
	 * 
	 * // To add custom parameters, add to the return value rather than here, //
	 * or this method becomes messy return params; }
	 */
}
