package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.LogOpenHelper.LogObject;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

import com.google.gson.reflect.TypeToken;

public class ResourceAgentStub extends Stub implements IResourceAgent {

	private static final long serialVersionUID = 1L;

	private String rans;

	public ResourceAgentStub(String rans) {
		super(rans);
		this.rans = rans;
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

		// return (List<Stakeholder>) makeCall("getStakeholders", params, new
		// TypeToken<List<Stakeholder>>() {}.getType());
		return (List<Stakeholder>) makeCall("getStakeholders", params, (new ArrayList<Stakeholder>()).getClass());
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
	public String getRANS() {
		// List<Tuple<String, Object>> params = new ArrayList<Tuple<String,
		// Object>>();
		//
		// return makeCall("getRAI", params).toString();

		return rans;
	}

	@Override
	public void setRANS(String rai) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		makeCall("setRANS", params, void.class);
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
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (ArrayList<ResourceAgent>) makeCall("getStakeholders", params,
				(new ArrayList<ResourceAgent>()).getClass());
	}

	@Override
	public void setInterests(ArrayList<ResourceAgent> interests) {
		// TODO Auto-generated method stub

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

	public boolean identifyPosition(Position position){
		return false;
	}
	
	public boolean identifyInPlace(String placeName, Position position) {
		return false;
	}
	
	public void setPosition(Position pos) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), pos));

		makeCall("setPosition", params, void.class);		
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

	@Override
	public void updateLocation(Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		makeCall("updateLocation", params, void.class);
	}

	@Override
	public LogObject getLog(Date date) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Date.class.getName(), date));

		return (LogObject) makeCall("getLog", params, LogObject.class);
	}

	@Override
	public void log(String record) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), record));
		
		makeCall("log", params, void.class);
	}

	@Override
	public Place getPlace() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Place) makeCall("getPlace", params, Place.class);
	}
}
