package br.uff.tempo.apps.map;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.widget.Toast;
import br.uff.tempo.apps.map.objects.InterfaceApplicationManager;
import br.uff.tempo.apps.map.objects.ResourceIcon;
import br.uff.tempo.apps.map.objects.notification.NotificationBox;
import br.uff.tempo.apps.map.objects.sprite.ISpriteController;
import br.uff.tempo.apps.map.objects.sprite.SmartSprite;
import br.uff.tempo.apps.map.objects.sprite.SmartSpriteFactory;
import br.uff.tempo.apps.map.rule.ContextMenu;
import br.uff.tempo.apps.map.rule.ContextMenuItem;
import br.uff.tempo.apps.map.rule.IContextMenuAction;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;

public class SmartMapActivity extends SmartAndroidMap implements ISpriteController, IContextMenuAction {

	// Called when a resource is just created by the Menu
	@Override
	public void onResourceCreationFinished(ResourceWrapper wrapper) {
		//Get the appropriated texture
		ITextureRegion resTexture = getTextureFactory().getResourceTextureRegion(wrapper.getId());
		ResourceIcon icon = createSprite(resTexture, wrapper);
		
		InterfaceApplicationManager manager = getInterfaceManager();
		manager.addResource(wrapper.getStub().getRANS(), icon);
		
		// Register interest in all context variables
		wrapper.getStub().registerStakeholder("all", manager.getRANS());
	}

	public ResourceIcon createSprite(ITextureRegion textureRegion, final ResourceWrapper wrapper) {
		
		// It's initially positioned at (x,y) position (pixels)
		float x = getMap().metersToPixel(wrapper.getStub().getPosition().getX());
		float y = getMap().metersToPixel(getMap().invertYcoordinate(wrapper.getStub().getPosition().getY()));

		VertexBufferObjectManager vertexBufferObj = this.getVertexBufferObjectManager();
		Sprite sprite = SmartSpriteFactory.createSprite(wrapper.getId(), x, y, textureRegion, vertexBufferObj, this, wrapper, getMap(), this);

		// Creates a box that will show notification texts near the sprite
		NotificationBox box = new NotificationBox(sprite.getWidth(),
				sprite.getHeight(), vertexBufferObj, this.getFontManager(), this.getTextureManager());

		getScene().registerTouchArea(sprite);
		getScene().attachChild(sprite);
		
		return new ResourceIcon(sprite, box, wrapper);
	}
	
	@Override
	public void onSpriteStartLongPress(SmartSprite sprite,	TouchEvent pSceneTouchEvent) {
		// Vibrate the device for a while 
		this.mEngine.vibrate(VIBRATE_TIME);
	}

	@Override
	public void onSpriteLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent) {
		//Move the sprite
		sprite.setPosition(pSceneTouchEvent.getX() - sprite.getWidth() / 2, pSceneTouchEvent.getY() - sprite.getHeight() / 2);
	}

	@Override
	public void onSpriteEndLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent) {
	}

	@Override
	public void onSpriteTap(SmartSprite sprite, TouchEvent pSceneTouchEvent) {
		//getResourceCreator().callSimulator(sprite.getResourceWrapper().getStub(), sprite.getResourceWrapper().getSimulator());
		sprite.showContextMenu(getCamera());
	}

	@Override
	public void onContextMenuAction(ContextMenu menu,
			ContextMenuItem itemSelected) {
		toastOnUIThread(itemSelected.getLabel(), Toast.LENGTH_LONG);
	}
}
