package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;

public class ResourceRepositoryStub extends ResourceAgentStub implements IResourceRepository {

	public ResourceRepositoryStub(String calleeID) {
		super(calleeID);
	}

	public String get(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (String) makeCall("get", params);

	}

	public boolean add(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (Boolean) makeCall("add", params);

	}

	public boolean remove(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (Boolean) makeCall("remove", params);

	}

	public ArrayList<String> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean update(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (Boolean) makeCall("update", params);
	}

	public ArrayList<String> getSubList(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (ArrayList<String>) makeCall("getSubList", params);
	}

}
