package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public class ResourceAgentStub extends Stub implements IResourceAgent {
	private String url;

	public ResourceAgentStub(String url) {
		super(url);
		this.url = url;
	}

	public void notificationHandler(String change) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("java.lang.String", change));

		makeCall("notificationHandler", params);
	}

	public String getResourceClassName() {
		List<Tuple> params = new ArrayList<Tuple>();

		return makeCall("getResourceClassName", params).toString();
	}

	@Override
	public void registerStakeholder(String method, String url) {
		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("java.lang.String", method));
		params.add(new Tuple<String, Object>("java.lang.String", url));

		makeCall("registerStakeholder", params);
	}

	@Override
	public int getId() {
		List<Tuple> params = new ArrayList<Tuple>();

		return (Integer) makeCall("getName", params);
	}

	@Override
	public void setId(int id) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), id));

		makeCall("setId", params);
	}

	@Override
	public String getName() {
		List<Tuple> params = new ArrayList<Tuple>();

		return makeCall("getName", params).toString();
	}

	@Override
	public void setName(String name) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), name));

		makeCall("setName", params);
	}

	@Override
	public String getURL() {
		List<Tuple> params = new ArrayList<Tuple>();

		return makeCall("getURL", params).toString();
	}

	@Override
	public void setURL(String uRL) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), uRL));

		makeCall("setURL", params);
	}

	@Override
	public String getType() {
		List<Tuple> params = new ArrayList<Tuple>();
		return makeCall("getType", params).toString();
	}

	@Override
	public void setType(String type) {
		List<Tuple> params = new ArrayList<Tuple>();
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
		List<Tuple> params = new ArrayList<Tuple>();

		return (Boolean) makeCall("isRegistered", params);
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

}
