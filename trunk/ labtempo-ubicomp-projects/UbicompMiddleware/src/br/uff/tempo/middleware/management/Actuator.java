package br.uff.tempo.middleware.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IActuator;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.Stub;

public class Actuator extends ResourceAgent implements IActuator {
	protected static final String TAG = "Actuator";
	protected IResourceDiscovery discovery;
	protected List<Action> actions = new ArrayList<Action>();
	
	public Actuator(String name, String rans) {
		super(name, "br.uff.tempo.middleware.management.Actuator", rans);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.i(TAG, "Notification received");
		// Call all actions
		if (actions != null) {
			Iterator<Action> it = actions.iterator();
			while (it.hasNext())
				it.next().exec();
		}
	}

	@Service(name = "Definir expressão", type = "SetExpression")
	public void setExpression(String expr) throws Exception {
		parseExpression(expr.replace('\t', ' ').replace('\n', ' ').trim());
	}

	@Service(name = "Definir expressão", type = "SetExpression")
	public void setExpression(InputStream stream) throws Exception {
		parseExpression(inputStreamToString(stream));
	}
	
	public String inputStreamToString(InputStream stream) {
		BufferedReader in = null;
		String expr = "";
		try {
			in = new BufferedReader(new InputStreamReader(stream));

			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');

			expr = buffer.toString().replace('\t', ' ').replace('\n', ' ').trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return expr;
	}

	protected void parseExpression(String expr) throws Exception {
		JSONArray act = new JSONObject(expr).getJSONArray("actuator");
		if (act == null)
			throw new Exception("Actuator not found while parsing JSON rule");
		
		JSONObject obj;
		for (int i = 0; i < act.length(); ++i)
		{
			obj = (JSONObject) act.get(i);
			String key = obj.keys().next().toString();
			Object val = obj.get(key);
			
			if ("action".equals(key)) {
				JSONObject action = (JSONObject) val;
				// Get rai
				String rai = action.getString("rai");
				// Get service
				String serv = action.getString("service");
				// Get params (optional)
				Object[] params = new Object[0];
				if (action.has("params")) {
					String s_params = action.getString("params");
					params = s_params.split(",");
				}
				addAction(rai, serv, params);
			} else if ("name".equals(key)) {
				String name = val.toString();
				super.setName(name);
				Log.i(TAG, val.toString());
			}
		}
	}
	
	protected void addAction(String rai, String serv, Object[] params) {
		actions.add(new Action(rai, serv, params));
	}

	protected class Action {
		private String rai;
		private String serv;
		private List<Tuple<String, Object>> params;
		
		Action(String rai, String serv, Object[] params) {
			this.rai = rai;
			this.serv = serv;
			this.params = new ArrayList<Tuple<String, Object>>();

			if (params != null && params.length != 0)
				for (Object o : params) 
					this.params.add(new Tuple<String, Object>(o.getClass().getName(), o));
		}
		
		public void exec() {
			Stub s = new Stub(rai);
			s.makeCall(serv, params, void.class);
			log("CALL " + rai + ": " + serv + params.get(0).value);
			getBaseContext();
		}
		
		public String getRai() {
			return rai;
		}

		public String getService() {
			return serv;
		}

		public List<Tuple<String, Object>> getParams() {
			return params;
		}

	}
	
	public List<Action> getActions() {
		return this.actions;
	}
}
