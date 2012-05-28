package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import br.uff.tempo.middleware.comm.Tuple;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	HashMap<String,ResourceAgent> repository;
	private static ResourceRepository instance;
	
	private ResourceRepository()
	{
		setId(0);
		setName("ResourceDiscovery");
		setType("management");	
		repository = new HashMap<String,ResourceAgent>();
	}
	
	public static ResourceRepository getInstance()
	{
		if (instance == null)
			instance = new ResourceRepository();
		return instance;
	}

	public ResourceAgent get(String url) {
		return repository.get(url);
	}

	public ArrayList<ResourceAgent> getList(){
		return  new ArrayList<ResourceAgent>(repository.values());
	}
	
	public boolean add(ResourceAgent rA) {
		repository.put(rA.getURL(), rA);
		rA.registerStakeholder("all", this);//all methods of IAR are stakeholders
		return true;
	}

	public boolean remove(String url) {
		repository.remove(url);
		return true;
	}

	public boolean update(ResourceAgent rA)
	{
		repository.put(rA.getURL(),rA);
		return true;
	}

	@Override
	public void notificationHandler(String change) throws JSONException {
		//ResourceAgent rA = (ResourceAgent)new JSONObject(change).get("value");
		//update(rA);
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
