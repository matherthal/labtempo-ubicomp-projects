package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;

public class ResourceDiscovery extends ResourceAgent implements
		IResourceDiscovery {

	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		setId(2);
		setName("ResourceDiscovery");
		setType("management");
	}

	public ArrayList<ResourceAgent> search(String query) {
		ResourceRepository rR = ResourceRepository.getInstance();
		if (query == "")
			return rR.getList();
		return null;
	}

	private ArrayList<ResourceAgent> queryByLocal(String query) {
		return null;
	}

	private ArrayList<ResourceAgent> queryByProximity(String query) {
		return null;
	}

	public static ResourceDiscovery getInstance() {
		if (instance == null)
			instance = new ResourceDiscovery();
		return instance;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		/*List<Tuple<String, Method>> attribs = new ArrayList<Tuple<String,Method>>();
		attribs.add(new Tuple<String, Method>("Busca", this.getClass().getMethod("search", String.class)));
		attribs.add(new Tuple<String, Method>("Busca por local", this.getClass().getMethod("queryByLocal", String.class)));
		attribs.add(new Tuple<String, Method>("Busca por proximidade", this.getClass().getMethod("queryByProximity", String.class)));
		return attribs;*/
		return null;
	}

}
