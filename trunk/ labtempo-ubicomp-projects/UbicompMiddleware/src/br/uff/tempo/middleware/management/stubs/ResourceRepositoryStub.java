package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;

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

	public boolean add(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));

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

	public boolean update(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));

		return (Boolean) makeCall("update", params, Boolean.class);
	}

	public ArrayList<String> getSubList(String url) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));
		
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();

		return (ArrayList<String>) makeCall("getSubList", params, type);
	}

}
