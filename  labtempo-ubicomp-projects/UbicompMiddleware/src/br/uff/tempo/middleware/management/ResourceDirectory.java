package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class ResourceDirectory {
	private static final String TAG = ResourceDirectory.class.getSimpleName();
	
	Type directory;
	
	Map<String,String> pathList;//Map type easy the create type process
	
	private static ResourceDirectory instance;
	
	private ResourceDirectory() {
		pathList = new HashMap<String,String>();
		directory = new Type("//");
		pathList.put(directory.getName(),directory.getName());
	}
	
	public static synchronized ResourceDirectory getInstance() {
		if (instance == null)
			return instance = new ResourceDirectory();
		return instance;
	}
	
	/**
	 * Part of CRUD
	 * @param resource is a ResourceAgent data
	 */
	public void create(ResourceData resource) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(directory.getName());
		list.addAll(extractType(resource.getType()));		
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
					Type typeSubTemp = null;
					++i;
					if (i < list.size()){ 
						typeSubTemp = this.search(list.get(i),typeTemp.getSubTypeList());
					}
					if (typeSubTemp != null){
						typeTemp = typeSubTemp;
					} else{
						if (list.size()>i){//it has more types
							typeTemp.add(list,resource,i);
						} else {//end of list
							if (!contains(typeTemp.resources,resource)){
								typeTemp.resources.add(resource);
							}
						}
						isInserted = true;
						pathList.put(resource.getType(),resource.getType());// if type already exist subscribes it
					}
				}
			}		
		}
		Log.d(TAG, "Created");
	}

	private boolean contains(List<ResourceData> resources, ResourceData resource) {
		for (ResourceData resourceData : resources) {
			if (resourceData.getName().equals(resource.getName())) {
				return true;
			}
		}
		return false;
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
			case ResourceData.RAI:
				result = getByRai(query);
				break;
			case ResourceData.NAME:
				result = searchByName(query);
				break;
			case ResourceData.TYPE:
				result = searchByType(query);
				break;
			case ResourceData.PLACE:
				result = searchByPlace(query);
				break;
		}
		Log.d(TAG, "Read");
		return result;
	}
	
	private List<ResourceData> getByRai(String query) {
		Log.d(TAG, "getByRai");
		return raiTreeSearch(query, directory);
	}

	private List<ResourceData> raiTreeSearch(String rai, Type type) {
		List<ResourceData> result = new ArrayList<ResourceData>();
		ResourceData resource = null;
		if ((resource = getRai(rai, type.resources)) != null){
			result.add(resource);
			return result;
		} else {
			List<Type> subTypes = type.getSubTypeList();
			if (subTypes != null){
				for (Type subType : subTypes){
					if ((result = raiTreeSearch(rai,subType)) != null ){
						return result;
					}
				}
			}
			return null;
		}
	}

	private ResourceData getRai(String rai, List<ResourceData> resources) {
		for (ResourceData resource : resources){
			if (resource.getRai().equals(rai)){
				return resource;
			}
		}
		return null;
	}

	private List<ResourceData> searchByPlace(String query) {
		ResourceLocation rL = ResourceLocation.getInstance();
		List<String> raiList = rL.search(query);
		if (raiList != null){
			List<ResourceData> result = new ArrayList<ResourceData>();
			for (String raiStr : raiList){
				List<ResourceData> tmpResult = getByRai(raiStr);
				if (tmpResult!=null){
					result.addAll(tmpResult);
				}
			}
			Log.d(TAG, "searchByPlace");
			return result;
		}
		Log.d(TAG, "searchByPlace: null");
		return null;
	}

	private List<ResourceData> searchByType(String query) {
		Log.d(TAG, "searchByType");
		if (query.equals("")){
			return getResources(directory);
		} 
		String path = pathList.get(query);
		if (path != null && path != "//"){
			String[] list = path.split("/");
			Type typeTemp = directory;
			for (String strPath : list) {
				Type typeSubTemp = this.search(strPath,typeTemp.getSubTypeList());
				typeTemp = typeSubTemp;
			}
			return typeTemp.resources;
		}
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
		Log.d(TAG, "searchByName");
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
		if (typeList != null) {
			for (Type obj : typeList) {
				if (obj.getName().equals(type)){
					return obj;
				}
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
	
	/**
	 * Part of CRUD
	 * @param resource is a ResourceAgent new data
	 */
	public void update(ResourceData resource){
		List<ResourceData> result;
		if ((result = read(ResourceData.RAI, resource.getRai()))!= null){
			result.set(0, resource);
		}
		Log.d(TAG, "Updated");
	}
	
	/**
	 * Part of CRUD
	 * @param resource is ResourceAgent data to be removed
	 */
	public void delete(ResourceData resource){
		List<ResourceData> result;
		if ((result = read(ResourceData.RAI, resource.getRai()))!= null){
			result.set(0,null);
		}
		Log.d(TAG, "Deleted");
	}
	
	
}
