package br.uff.tempo.apps.map.objects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import br.uff.tempo.apps.map.objects.notification.INotificationBoxReceiver;
import br.uff.tempo.apps.map.objects.sprite.SmartAnimatedSprite;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;

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
	// Fields
	// ===========================================================
	
	private Map<String, INotificationBoxReceiver> sceneObjects;

	// used with singleton pattern
	private static InterfaceApplicationManager obj = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	private InterfaceApplicationManager() {
		super("InterfaceManager", "br.uff.tempo.apps.map.objects.InterfaceApplicationManager", "InterfaceManager" + SmartAndroid.DEVICE_ID);
		sceneObjects = new HashMap<String, INotificationBoxReceiver>();
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
	
	public void addResource(String rai, INotificationBoxReceiver obj) {
		sceneObjects.put(rai, obj);
	}
	
	public INotificationBoxReceiver getResource(String rai) {
		return sceneObjects.get(rai);
	}
	
	public Collection<INotificationBoxReceiver> getAllResources() {
		return sceneObjects.values();
	}

	// ===========================================================
	// Inherited Methods
	// ===========================================================

	// Called when any resource change the value of a context variable
	@Override
	public void notificationHandler(String raiFromEvent, String method, Object value) {
		
		Log.d("InterfaceManager", "Notification received from "
				+ raiFromEvent + ". Context variable modified: "
				+ method + " current value: " + value);
		
		INotificationBoxReceiver receiver = sceneObjects.get(raiFromEvent);
		
		if (receiver != null) {
			
			// Create a pop up message showing the context variable changed and its current value
			receiver.showMessage(method + " = " + value.toString());
			
			if (method.equals(Person.CV_POSITION)) {
				
				SmartAnimatedSprite anim = (SmartAnimatedSprite) ((ResourceIcon) receiver).getResourceSprite();
				anim.updatePosition((Position) value);
			}
		}
	}
}
