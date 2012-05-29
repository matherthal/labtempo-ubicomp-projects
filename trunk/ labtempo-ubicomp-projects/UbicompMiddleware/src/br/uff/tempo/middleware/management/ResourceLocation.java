package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Local;

public class ResourceLocation extends ResourceAgent implements IResourceLocation{

	private ResourceRepository rR;
	
	private static ResourceLocation instance;
	
	HashMap<String,Local> map;
	
	private ResourceLocation()
	{
		setId(3);
		setURL("br.uff.tempo.middleware.management.ResourceLocation");
		setName("ResourceLocation");
		setType("management");
		map = new HashMap<String,Local>();
		ResourceContainer.getInstance().add(this);
	}
	
	public static ResourceLocation getInstance()
	{
		if (instance == null)
			instance = new ResourceLocation();
		return instance;
	}
	
	public ArrayList<Local> search(ResourceAgent rA) {
		ArrayList<Local> result = new ArrayList<Local>();
		Local current = get(rA);
		result.add(0, current);//current local is the nearest
		ArrayList<Local> localList = (ArrayList<Local>) map.values();
		result = getNeighbors(current,localList,result);
		return result;
	}

	private ArrayList<Local> getNeighbors(Local current, ArrayList<Local> localList, ArrayList<Local> result) {
		ArrayList<Local> neighborList = new ArrayList<Local>();
		if (localList.size()!=0)
		{
			for (Local local : localList)
			{
				if (current.equalCorners(local)||current.equalSide(local)||current.equalHeight(local))
				{   
					result.add(local);
					neighborList.add(local);
				}
			}
			localList.remove(current);
			for (Local local : neighborList){
				result = getNeighbors(local,localList,result);
			}
		}
		return result;
	}

	public Local get(ResourceAgent rA) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setMap(HashMap<String, Local> map)
	{
		this.map = map;
	}


	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		/*List<Tuple<String, Method>> attribs = new ArrayList<Tuple<String,Method>>();
		attribs.add(new Tuple<String, Method>("Busca Agente de Recurso", this.getClass().getMethod("search", ResourceAgent.class)));
		attribs.add(new Tuple<String, Method>("Busca de vizinhos", this.getClass().getMethod("getNeighbors", String.class)));
		attribs.add(new Tuple<String, Method>("Busca por proximidade", this.getClass().getMethod("queryByProximity", Local.class, ArrayList.class, ArrayList.class)));
		return attribs;*/
		return null;
	}

}
