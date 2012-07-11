package br.uff.tempo.apps.map.objects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.ResourceData;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;

/**
 * This class manages resource agents, stubs
 * and other kind of data from resources in the scene.
 * It's a Singleton!
 * 
 * @author dbarreto
 *
 */
public class InterfaceApplicationManager extends ResourceAgent {
	
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
	
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	private InterfaceApplicationManager() {
		
		super("InterfaceManager", "br.uff.tempo.apps.map.objects.InterfaceApplicationManager", 37); 
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public static InterfaceApplicationManager getInstance() {
		
		if (obj == null)
			obj = new InterfaceApplicationManager();
		
		return obj;
	}
	
	
	// ===========================================================
	// Inherited Methods
	// ===========================================================
	
	@Override
	public void notificationHandler(String change) {
		
		String raiFromExternal = (String) JSONHelper.getChange("id", change);
		String methodChanged = (String) JSONHelper.getChange("method", change);
		Object valueChanged = JSONHelper.getChange("value", change);
	}

}