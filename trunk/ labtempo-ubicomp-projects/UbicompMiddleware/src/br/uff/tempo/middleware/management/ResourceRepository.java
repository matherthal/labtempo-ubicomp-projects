package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceRepository;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	

	ArrayList<String> repository;
	private static ResourceRepository instance;
	
	private ResourceRepository()
	{
		setId(0);
		try{
			InetAddress addr = InetAddress.getLocalHost();
			setURL("rai:"+addr.getHostAddress()+"//br.uff.tempo.middleware.management.ResourceRepository:ResourceRepository");
		}catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		setName("ResourceRepository");
		setType("management");	
		
		ResourceContainer container = ResourceContainer.getInstance();
		ResourceRepository rR = this;
		ResourceDiscovery rDS = ResourceDiscovery.getInstance();
		ResourceRegister rRS = ResourceRegister.getInstance();
		ResourceLocation rLS = ResourceLocation.getInstance();
		container.add(rR);
		container.add(rDS);
		container.add(rRS);
		container.add(rLS);
		
		repository = new ArrayList<String>();
		repository.add(rDS.getURL());
		repository.add(rRS.getURL());
		repository.add(rLS.getURL());
		repository.add(rR.getURL());
	}
	
	public static ResourceRepository getInstance()
	{
		if (instance == null)
			instance = new ResourceRepository();
		return instance;
	}


	public String get(String url) {
		for (int i = 0; i< repository.size(); i++)
			if (repository.get(i).contains(url))
					return repository.get(i);
		return null;	
	}


	public ArrayList<String> getList(){
		return  repository;
	}
	
	
	
	public boolean add(String url) {
		repository.add(url);
		return true;
	}

	public boolean remove(String url) {
		repository.remove(url);
		return true;
	}


	public boolean update(String url)
	{

		repository.add(url);
		return true;
	}

	@Override
	public void notificationHandler(String change) throws JSONException {
		//ResourceAgent rA = (ResourceAgent)new JSONObject(change).get("value");
		//update(rA);
	}

	public ArrayList<String> getSubList(String url) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i< repository.size(); i++)
			if (repository.get(i).contains(url))
					 result.add(repository.get(i));
		if (result.size() == 0)
			return null;
		else
			return result;
	}

	
	

	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		/*List<Tuple<String, Method>> attribs = new ArrayList<Tuple<String,Method>>();
		attribs.add(new Tuple<String, Method>("Obter Agente de Recurso", this.getClass().getMethod("get", String.class)));
		attribs.add(new Tuple<String, Method>("Obter lista de Agentes de Recurso", this.getClass().getMethod("getList")));
		attribs.add(new Tuple<String, Method>("Adicionar Agente de Recurso", this.getClass().getMethod("add", ResourceAgent.class)));
		attribs.add(new Tuple<String, Method>("Remover Agente de Recurso", this.getClass().getMethod("remove", String.class)));
		attribs.add(new Tuple<String, Method>("Atualizar Agente de Recurso", this.getClass().getMethod("update", ResourceAgent.class)));
		return attribs;*/
		return null;
	}
}
