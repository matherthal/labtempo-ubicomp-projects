package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceDiscovery extends ResourceAgent implements
		IResourceDiscovery {

	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		setId(2);
		setName("ResourceDiscovery");
		setType("br.uff.tempo.middleware.management.ResourceDiscovery");
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			setURL(ResourceAgentIdentifier.generateRAI(addr.getHostAddress(), "br.uff.tempo.middleware.management.ResourceDiscovery", "ResourceDiscovery"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResourceContainer.getInstance().add(this);
	}


	public ArrayList<String> search(String query) {
		ResourceRepository rR = ResourceRepository.getInstance();
		if (query == "")
			return rR.getList();
		else
			return rR.getSubList(query);
		
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


	public ResourceAgent get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}