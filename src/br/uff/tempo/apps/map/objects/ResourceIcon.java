package br.uff.tempo.apps.map.objects;

import org.andengine.entity.sprite.Sprite;

import br.uff.tempo.apps.map.objects.notification.INotificationBoxReceiver;
import br.uff.tempo.apps.map.objects.notification.NotificationBox;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;

public class ResourceIcon implements INotificationBoxReceiver {

	private Sprite resourceSprite;
	private NotificationBox box;
	private ResourceWrapper resourceWrapper;
	
	public ResourceIcon(Sprite resourceSprite, NotificationBox box, ResourceWrapper resourceWrapper) {
		
		this.box = box;
		this.resourceSprite = resourceSprite;
		this.resourceWrapper = resourceWrapper;
		
		this.resourceSprite.attachChild(box);
	}
	
	// ===========================================================
	// Overridden Methods
	// ===========================================================
	
	@Override
	public void showMessage(String message) {
		this.box.show(message);
	}
	
	// ===========================================================
	// Getters and Setters
	// ===========================================================

	public Sprite getResourceSprite() {
		return resourceSprite;
	}

	public void setResourceSprite(Sprite resourceSprite) {
		this.resourceSprite = resourceSprite;
	}

	public NotificationBox getBox() {
		return box;
	}

	public void setBox(NotificationBox box) {
		this.box = box;
	}

	public ResourceWrapper getResourceWrapper() {
		return resourceWrapper;
	}

	public void setResourceWrapper(ResourceWrapper resourceWrapper) {
		this.resourceWrapper = resourceWrapper;
	}
}
