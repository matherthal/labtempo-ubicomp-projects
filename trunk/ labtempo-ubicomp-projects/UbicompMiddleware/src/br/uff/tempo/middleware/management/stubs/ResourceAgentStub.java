package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Tuple;
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
		params.add(new Tuple<String, Object>("change", change));

		makeCall("notificationHandler", params);

	}

	public String getResourceClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerStakeholder(String method, String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("method", method));
		params.add(new Tuple<String, Object>("url", url));

		makeCall("registerStakeholder", params);
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public void setURL(String uRL) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean identify() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyStakeholders(String change) throws JSONException {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public boolean registerStakeholder(String method, ResourceAgent rA) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
