package br.uff.tempo.apps.map.objects;

import java.util.LinkedList;
import java.util.List;

import br.uff.tempo.apps.ResourceData;

public class InterfaceApplicationManager {
	
	public static final int STOVE_DATA = 0;
	public static final int TV_DATA = STOVE_DATA + 1;
	public static final int BED_DATA = TV_DATA + 1;
	
	private static InterfaceApplicationManager obj = null;
	private List<ResourceData> data;
	
	private InterfaceApplicationManager() {
		
		this.data = new LinkedList<ResourceData>();
	}
	
	public static InterfaceApplicationManager getInstance() {
		
		if (obj == null)
			obj = new InterfaceApplicationManager();
		
		return obj;
	}
	
	public void addResourceData(ResourceData resourceData) {
		
		this.data.add(resourceData);
	}
	
	public List<ResourceData> getResourcesData() {
		return this.data;
	}

}
