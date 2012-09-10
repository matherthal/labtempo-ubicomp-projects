package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

public class Type {
	
	String name;
	List<Type> subTypes;
	List<String> resourcesName;

	public Type(List<String> list) {
		this();
		if (list != null){
			if (list.size()>1){
				this.name = list.get(0);
				initLists(list);
			} else {
				resourcesName.add(list.get(0));
			}
			
		}
	}

	private Type(String name) {
		this.name = name;
		subTypes = new ArrayList<Type>();
		resourcesName = new ArrayList<String>();
	}
	
	public Type() {
		subTypes = new ArrayList<Type>();
		resourcesName = new ArrayList<String>();
	}

	private void initLists(List<String> list) {
		List<Type> subTypesTemp = subTypes;
		Type aux = this;
		for (int i = 1; i < list.size()-1; i++){
			aux = new Type(list.get(i));
			subTypesTemp.add(aux);
			subTypesTemp = aux.subTypes;
		}
		aux.resourcesName.add(list.get(list.size()-1));
	}

	public List<Type> getSubTypeList() {
		return subTypes;
	}

	public void add(ArrayList<String> list, int begin) {
		List<Type> subTypesTemp = this.subTypes;
		Type aux = this;
		for (int i = begin; i< list.size()-1; i++){
			aux = new Type(list.get(i));
			subTypesTemp.add(aux);
			subTypesTemp = aux.subTypes;
		}
		aux.resourcesName.add(list.get(list.size()-1));
	}

	public String getName() {
		return name;
	}

}
