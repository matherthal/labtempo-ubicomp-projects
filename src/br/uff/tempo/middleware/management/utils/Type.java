package br.uff.tempo.middleware.management.utils;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.ResourceData;

public class Type {
	
	private String name;
	private List<Type> subTypes;
	private List<ResourceData> resources;

	public Type(List<String> list, ResourceData resource) {
		this();
		if (list != null){
			this.name = list.get(0);
			initLists(list,resource);
		} else {
			resources.add(resource);
		}
	}

	public Type(String name) {
		this.name = name;
		subTypes = new ArrayList<Type>();
		resources = new ArrayList<ResourceData>();
	}
	
	private Type() {
		subTypes = new ArrayList<Type>();
		resources = new ArrayList<ResourceData>();
	}

	private void initLists(List<String> list, ResourceData resource) {
		List<Type> subTypesTemp = subTypes;
		Type aux = this;
		for (int i = 1; i < list.size(); i++){
			aux = new Type(list.get(i));
			subTypesTemp.add(aux);
			subTypesTemp = aux.subTypes;
		}
		aux.resources.add(resource);
	}

	public List<Type> getSubTypeList() {
		return subTypes;
	}

	public void add(ArrayList<String> list, ResourceData resource, int begin) {
		List<Type> subTypesTemp = this.subTypes;
		Type aux = this;
		for (int i = begin; i< list.size(); i++){
			aux = new Type(list.get(i));
			subTypesTemp.add(aux);
			subTypesTemp = aux.subTypes;
		}
		aux.resources.add(resource);
	}

	public String getName() {
		return name;
	}

	public List<ResourceData> getResources() {
		return resources;
	}

	public void setResources(List<ResourceData> resources) {
		this.resources = resources;
	}
	
	

}
