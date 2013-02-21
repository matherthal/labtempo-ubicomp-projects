package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;

import com.google.gson.reflect.TypeToken;

public class ResourceRepositoryStub extends ResourceAgentStub implements IResourceRepository {
	
	private static final long serialVersionUID = 1L;

	public ResourceRepositoryStub(String calleeID) {
		super(calleeID);
	}

//	public String get(String url) {
//
//		// Set params
//		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
//		params.add(new Tuple<String, Object>(String.class.getName(), url));
//
//		return (String) makeCall("get", params, String.class);
//
//	}

	public boolean add(ResourceData resourceData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), resourceData));

		return (Boolean) makeCall("add", params, Boolean.class);
	}

	public boolean remove(String rans) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));

		return (Boolean) makeCall("remove", params, Boolean.class);

	}

	public ArrayList<String> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceAgentNS getRANS(String rans) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		
		return (ResourceAgentNS) makeCall("getRANS", params, ResourceAgentNS.class);
	}

	@Override
	public ResourceData get(String rans) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));

		return (ResourceData) makeCall("get", params, ResourceData.class);
	}

}
