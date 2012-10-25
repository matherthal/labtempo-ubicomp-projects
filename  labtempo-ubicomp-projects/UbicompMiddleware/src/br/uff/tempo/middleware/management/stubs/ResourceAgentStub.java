package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

import com.google.gson.reflect.TypeToken;

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

		makeCall("notificationHandler", params, void.class);
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

		makeCall("registerStakeholder", params, void.class);
	}

	@Override
	public List<Stakeholder> getStakeholders() {
		
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (List<Stakeholder>) makeCall("getStakeholders", params, new TypeToken<List<Stakeholder>>() {}.getType());		
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

		makeCall("setId", params, void.class);
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

		makeCall("setName", params, void.class);
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

		makeCall("setRAI", params, void.class);
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

		makeCall("setType", params, void.class);
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
	
	public boolean unregister() {
		return false;
	}
	
	public Position getPosition() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Position) makeCall("getPosition", params, Position.class);
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

		makeCall("removeStakeholder", params, void.class);	
	}

}
