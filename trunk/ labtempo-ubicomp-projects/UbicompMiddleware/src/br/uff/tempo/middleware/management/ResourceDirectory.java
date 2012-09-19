package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceDirectory {

	public static final int NAME = 0;  
	public static final int TYPE = 1;
	public static final int POSITION = 2;
	public static final int PLACE = 3;
	
	Type directory;
	
	ResourceDirectory instance;
	
	private ResourceDirectory() {
		directory = new Type("\\");
	}
	
	public ResourceDirectory getInstance() {
		if (instance == null)
			return instance = new ResourceDirectory();
		return instance;
	}
	
	public void create(ResourceData resource) {
		ArrayList<String> list = extractType(resource.getType());		
		Type typeBase = directory;
		int i = 0;
		if (list != null){
			if (typeBase == null){
				typeBase = new Type(list,resource);
			}
			else {	
				Type typeTemp = typeBase;
				boolean isInserted = false;
				while (typeTemp != null && !isInserted){
					Type typeSubTemp = search(list.get(++i),typeTemp.getSubTypeList());
					if (typeSubTemp != null){
						typeTemp = typeSubTemp;
					} else{
						if (list.size()-i>0){
							typeTemp.add(list,resource,i);
						} else {
							typeTemp.resources.add(resource);
						}
						isInserted = true;
					}
				}
			}		
		}
	}
	/**
	 * Part of CRUD
	 * @param attribute is value of target variable of query
	 * @param query is specific value of attribute that contains in some resources
	 * @return list of resources that respond query
	 */
	public List<ResourceData> read(int attribute, String query){
		List<ResourceData> result = new ArrayList<ResourceData>();
		switch (attribute) {
			case NAME:
				result = searchByName(query);
				break;
			case TYPE:
				result = searchByType(query);
				break;
			case PLACE:
				result = searchByPlace(query);
				break;
		}
		return result;
	}
	

	
	private List<ResourceData> searchByPlace(String query) {
		ResourceLocation rL = ResourceLocation.getInstance();
		List<String> raiList = rL.search(query);
		if (raiList != null){
			List<ResourceData> result = new ArrayList<ResourceData>();
			for (String raiStr : raiList){
				ResourceAgentIdentifier rai = new ResourceAgentIdentifier(raiStr);
				if ((result = searchByName(rai.getName()))!=null){
					result.addAll(result);
				}
			}
			return result;
		}
		return null;
	}

	private List<ResourceData> searchByType(String query) {
		return typeTreeSearch(query, directory);	
	}

	private List<ResourceData> typeTreeSearch(String query, Type type) {
		if (type.getName().equals(query)){
			return getResources(type);		
		} else {
			List<Type> subTypes = type.getSubTypeList();
			if (subTypes != null){
				List<ResourceData> result = new ArrayList<ResourceData>();
				for (Type subType : subTypes){
					if ((result = typeTreeSearch(query,subType)) != null ){
						return result;
					}
				}
			}
			return null;
		}
	}

	private List<ResourceData> getResources(Type type) {
		List<ResourceData> result = new ArrayList<ResourceData>();
		result.addAll(type.resources);
		for (Type current : type.getSubTypeList()){
			result.addAll(getResources(current));
		}
		return result;
	}

	private List<ResourceData> searchByName(String query) {
		return nameTreeSearch(query,directory);
	}

	private List<ResourceData> nameTreeSearch(String query, Type type) {
		List<ResourceData> result = new ArrayList<ResourceData>();
		ResourceData resource = null;
		if ((resource = get(query, type.resources)) != null){
			result.add(resource);
			return result;
		} else {
			List<Type> subTypes = type.getSubTypeList();
			if (subTypes != null){
				for (Type subType : subTypes){
					if ((result = nameTreeSearch(query,subType)) != null ){
						return result;
					}
				}
			}
			return null;
		}
	}

	private ResourceData get(String query, List<ResourceData> resources) {
		for (ResourceData resource : resources){
			if (resource.getName().equals(query)){
				return resource;
			}
		}
		return null;
	}

	private Type search(String type, List<Type> typeList) {
		for (Type obj : typeList) {
			if (obj.getName().equals(type)){
				return obj;
			}
		}
		return null;
	}

	private ArrayList<String> extractType(String type) {
		ArrayList<String> result = new ArrayList<String>();
		String[] path = type.split("/");
		for (String folder : path){
			result.add(folder);
		}
		return result;
	}
	
}
