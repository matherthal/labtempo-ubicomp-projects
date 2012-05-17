package br.uff.tempo.middleware.comm;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {
	public static String createMethodCall(String method, List<Tuple> params)
			throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsonparams = new JSONArray();

		Iterator<Tuple> iterator = params.iterator();
		Tuple<String, Object> tp;
		while (iterator.hasNext()) {
			tp = iterator.next();
			jsonparams.put(new JSONObject().put(tp.key, tp.value));
		}

		json.put("jsonrpc", "2.0");
		json.put("method", method);
		json.put("params", jsonparams);

		return json.toString();
	}

	public static Object getMessage(String result) throws JSONException {
		// Parse response string
		JSONObject respIn = new JSONObject(result);
		return respIn.get("result");
	}


	public static String createReply(Object msg) throws JSONException {
		String result = msg.toString(); // FIXME: Can I return anything?
		JSONObject respOut = new JSONObject();
		respOut.put("result", result);
		respOut.put("id", "0"); // we don't really use this so value is always zero
		return respOut.toString();
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
