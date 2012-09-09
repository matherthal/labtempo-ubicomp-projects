package br.uff.tempo.apps.map.objects;

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

	// used with singleton pattern
	private static InterfaceApplicationManager obj = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	private InterfaceApplicationManager() {

		super("InterfaceManager",
				"br.uff.tempo.apps.map.objects.InterfaceApplicationManager", 37);
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

	// ===========================================================
	// Inherited Methods
	// ===========================================================

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.d("InterfaceManager", "Notification received from "
				+ rai + ". Context variable modified: "
				+ method + " current value: " + value);
	}
}
