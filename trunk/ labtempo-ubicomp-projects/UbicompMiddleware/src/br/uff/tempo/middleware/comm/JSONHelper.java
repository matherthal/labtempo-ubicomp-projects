package br.uff.tempo.middleware.comm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONHelper {

	public static String createMethodCall(String method, List<Tuple> params) throws JSONException {

		Map<String, Object> methodCall = new HashMap<String, Object>();
		List<Object> jsonparams = new ArrayList<Object>();

		Iterator<Tuple> iterator = params.iterator();

		Tuple<String, Object> tp;

		while (iterator.hasNext()) {
			tp = iterator.next();
			jsonparams.add(tp.value);
		}

		methodCall.put("jsonrpc", "2.0");
		methodCall.put("method", method);
		methodCall.put("params", jsonparams);

		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();

		return (new Gson()).toJson(methodCall, collectionType);
	}

	public static Object getMessage(String result) throws JSONException {
		// Parse response string
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();

		Log.d("JSONHelper", "result = " + result);
		String[] resultData = result.split(";");
		Map<String, Object> response = (new Gson()).fromJson(resultData[0], collectionType);
		Object obj = response.get("result");
		return obj;
	}

	public static String createReply(Object msg) throws JSONException {
		// FIXME: Can I return anything?

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("result", msg);
		response.put("id", "0"); // we don't really use this so value is always
									// zero
		String resultMsg = (new Gson()).toJson(response);

		return resultMsg;
		// Serialise response to JSON-encoded string
		// The response string can now be sent back to the client...
	}

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

	public static Object getChange(String what, String change) {
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();

		Map<String, Object> changeObj = (new Gson()).fromJson(change, collectionType);
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
