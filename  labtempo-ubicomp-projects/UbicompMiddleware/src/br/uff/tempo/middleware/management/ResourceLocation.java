package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceLocation extends ResourceAgent implements IResourceLocation{

	private ResourceRepository rR;
	
	HashMap<String,Local> map;
	
	public ResourceLocation()
	{
		setId(3);
		setName("ResourceLocation");
		setType("management");
		map = new HashMap<String,Local>();
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
