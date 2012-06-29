package br.uff.tempo.middleware.comm;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONHelper {

	public static String createMethodCall(String method, List<Tuple> params)
			throws JSONException {

		Map<String, Object> methodCall = new HashMap<String, Object>();
		Map<String, Object> jsonparams = new HashMap<String, Object>();

		Iterator<Tuple> iterator = params.iterator();

		Tuple<String, Object> tp;

		while (iterator.hasNext()) {
			tp = iterator.next();
			jsonparams.put(tp.key, tp.value);
		}

		methodCall.put("jsonrpc", "2.0");
		methodCall.put("method", method);
		methodCall.put("params", jsonparams);

		return (new Gson()).toJson(methodCall);
	}

	public static Object getMessage(String result) throws JSONException {
		// Parse response string
		Type collectionType = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		
		Log.d("JSONHelper", "result = " + result);
		
		Map<String, Object> response = (new Gson()).fromJson(result,
				collectionType);
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
