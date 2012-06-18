package br.uff.tempo.apps.map.objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.uff.tempo.apps.ResourceData;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

/**
 * This class manages resource agents, stubs
 * and other kind of data from resources in the scene.
 * It's a Singleton!
 * 
 * @author dbarreto
 *
 */
public class InterfaceApplicationManager {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final int STOVE_DATA = 0;
	public static final int TV_DATA = STOVE_DATA + 1;
	public static final int BED_DATA = TV_DATA + 1;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	//used with singleton pattern
	private static InterfaceApplicationManager obj = null;
	
	//data from resources
	private List<ResourceData> data;
	private Map<Integer, IResourceAgent> resAg;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	private InterfaceApplicationManager() {
		
		this.data = new LinkedList<ResourceData>();
		this.resAg = new HashMap<Integer, IResourceAgent>();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public static InterfaceApplicationManager getInstance() {
		
		if (obj == null)
			obj = new InterfaceApplicationManager();
		
		return obj;
	}
	
	public void addResourceData(ResourceData resourceData) {
		
		this.data.add(resourceData);
	}
	
	public void addResourceAgent(int id, IResourceAgent ra) {
		
		this.resAg.put(id, ra);
	}
	
	public IResourceAgent getResourceAgent(int id) {
		
		return this.resAg.get(id);
	}
	
	public List<ResourceData> getResourcesData() {
		return this.data;
	}

}
