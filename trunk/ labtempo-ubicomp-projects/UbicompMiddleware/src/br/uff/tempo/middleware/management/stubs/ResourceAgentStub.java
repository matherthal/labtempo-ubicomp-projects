package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.google.gson.reflect.TypeToken;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public class ResourceAgentStub extends Stub implements IResourceAgent {
	
	private static final long serialVersionUID = 1L;
	
	private String rai;

	public ResourceAgentStub(String rai) {
		super(rai);
		this.rai = rai;
	}

	public void notificationHandler(String rai, String method, Object value) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rai));
		params.add(new Tuple<String, Object>(String.class.getName(), method));
		params.add(new Tuple<String, Object>(value.getClass().getName(), value));

		makeCall("notificationHandler", params);
	}

	public String getResourceClassName() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (String) makeCall("getResourceClassName", params, String.class);
	}

	@Override
	public void registerStakeholder(String method, String rai) {
		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), method));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		makeCall("registerStakeholder", params);
	}

	@Override
	public List<Stakeholder> getStakeholders() {
		
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		Type type = new TypeToken<List<Stakeholder>>() {
		}.getType();

		return (List<Stakeholder>) makeCall("getStakeholders", params, type);		
	}
	
	@Override
	public int getId() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Integer) makeCall("getName", params, int.class);
	}

	@Override
	public void setId(int id) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), id));

		makeCall("setId", params);
	}

	@Override
	public String getName() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (String) makeCall("getName", params, String.class);
	}

	@Override
	public void setName(String name) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), name));

		makeCall("setName", params);
	}

	@Override
	public String getRAI() {
//		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
//
//		return makeCall("getRAI", params).toString();
		
		return rai;
	}

	@Override
	public void setRAI(String rai) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		makeCall("setRAI", params);
	}

	@Override
	public String getType() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (String) makeCall("getType", params, String.class);
	}

	@Override
	public void setType(String type) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), type));

		makeCall("setType", params);
	}

	@Override
	public ArrayList<ResourceAgent> getInterests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInterests(ArrayList<ResourceAgent> interests) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> getRegisteredList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRegistered() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Boolean) makeCall("isRegistered", params, Boolean.class);
	}

	@Override
	public boolean identify() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Deprecated()
	public void notifyStakeholders(String change) throws JSONException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void notifyStakeholders(String method, Object value) {
		// TODO Auto-generated method stub

	}
	// @Override
	// public boolean registerStakeholder(String method, ResourceAgent rA) {
	// // TODO Auto-generated method stub
	// return false;
	// }

	@Override
	public void removeStakeholder(String method, String rai) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), method));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		makeCall("removeStakeholder", params);	
	}

}
