package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;

import com.google.gson.reflect.TypeToken;

public class ResourceRepositoryStub extends ResourceAgentStub implements IResourceRepository {
	
	private static final long serialVersionUID = 1L;

	public ResourceRepositoryStub(String calleeID) {
		super(calleeID);
	}

	public String get(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));

		return (String) makeCall("get", params, String.class);

	}

	public boolean add(String rans, String ip, int prefix, String rai) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		params.add(new Tuple<String, Object>(String.class.getName(), ip));
		params.add(new Tuple<String, Object>(Integer.class.getName(), prefix));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		return (Boolean) makeCall("add", params, Boolean.class);

	}

	public boolean remove(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));

		return (Boolean) makeCall("remove", params, Boolean.class);

	}

	public ArrayList<String> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getSubList(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));
		
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();

		return (ArrayList<String>) makeCall("getSubList", params, type);
	}

	@Override
	public ResourceAgentNS getRANS(String rans) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		
		return (ResourceAgentNS) makeCall("getRANS", params, ResourceAgentNS.class);
	}

}
