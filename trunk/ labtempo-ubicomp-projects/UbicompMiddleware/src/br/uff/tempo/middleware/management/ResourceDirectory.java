package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

public class ResourceDirectory {

	List<Type> directory;
	ResourceDirectory instance;
	
	private ResourceDirectory() {
		directory = new ArrayList<Type>();
	}
	
	public ResourceDirectory getInstance() {
		if (instance == null)
			return instance = new ResourceDirectory();
		return instance;
	}
	
	public void create(String type) {
		ArrayList<String> list = extractType(type);
		List<Type> typeList = directory;
		int i = 0;
		if (list != null){
			Type typeBase = search(list.get(i++),typeList);
			if (typeBase == null){
				typeBase = new Type(list);
				directory.add(typeBase);
			}
			else {	
				Type typeTemp = typeBase;
				boolean isInserted = false;
				while (typeTemp != null && !isInserted){
					Type typeSubTemp = search(list.get(++i),typeTemp.getSubTypeList());
					if (typeSubTemp != null){
						typeTemp = typeSubTemp;
					} else{
						if (list.size()-i>1){
							typeTemp.add(list,i);
						} else {
							typeTemp.resourcesName.add(list.get(i));
						}
						isInserted = true;
					}
				}
			}		
		}
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
		int pointer = 0;
		int len = type.length();
		while (pointer < len) {
			pointer++;
			int begin = pointer;
			while (type.charAt(pointer) != '/' && type.charAt(pointer) != ':' && pointer < len)
				pointer++;
			result.add(type.substring(begin, pointer));
		}
		return result;
	}
	
}
