package br.uff.tempo.apps.map.objects;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;

/**
 * This class manages resource agents, stubs and other kind of data from
 * resources in the scene. It's a Singleton!
 * 
 * @author dbarreto
 * 
 */
public class InterfaceApplicationManager extends ResourceAgent {
	
	private static final long serialVersionUID = 1L;

	// ===========================================================
	// Constants
	// ===========================================================

	public static final int STOVE_DATA = 0;
	public static final int TV_DATA = STOVE_DATA + 1;
	public static final int BED_DATA = TV_DATA + 1;
	public static final int LAMP_DATA = BED_DATA + 1;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private Map<String, ResourceObject> sceneObjects;

	// used with singleton pattern
	private static InterfaceApplicationManager obj = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	private InterfaceApplicationManager() {

		super("InterfaceManager",
				"br.uff.tempo.apps.map.objects.InterfaceApplicationManager", 37);
		
		sceneObjects = new HashMap<String, ResourceObject>();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static InterfaceApplicationManager getInstance() {

		if (obj == null) {
			obj = new InterfaceApplicationManager();
			obj.identify();
		}

		return obj;
	}
	
	public void addResource(String rai, ResourceObject obj) {
		
		sceneObjects.put(rai, obj);
	}
	
	public ResourceObject getResource(String rai) {
		
		return sceneObjects.get(rai);
	}

	// ===========================================================
	// Inherited Methods
	// ===========================================================

	// Called when a resource change the value of a context variable
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		
		Log.d("InterfaceManager", "Notification received from "
				+ rai + ". Context variable modified: "
				+ method + " current value: " + value);
		
		// Verify which resource in the scene has caused the notification
		// For each resource in the map...
		for (Map.Entry<String, ResourceObject> entry : sceneObjects.entrySet()) {
			
			// Get the RAI
			String key = entry.getKey();
			
			if (rai.equals(key)) {
				
				// Get the resource object associated to that RAI
				ResourceObject obj = entry.getValue();
				
				// Create a pop up message showing the context variable changed and its current value
				obj.showMessage(method + " = " + value.toString());
				
				break;
			}
		}
	}
}
