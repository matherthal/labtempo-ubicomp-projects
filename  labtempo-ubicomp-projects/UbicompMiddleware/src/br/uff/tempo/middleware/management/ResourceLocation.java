package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Local;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

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
		
		setURL(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceLocation", "ResourceLocation"));
		
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
}
