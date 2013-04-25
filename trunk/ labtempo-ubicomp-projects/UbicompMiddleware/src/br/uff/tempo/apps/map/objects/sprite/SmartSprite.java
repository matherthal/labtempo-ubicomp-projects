package br.uff.tempo.apps.map.objects.sprite;

import java.lang.reflect.Method;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;
import br.uff.tempo.apps.map.TouchEvents;
import br.uff.tempo.apps.map.rule.ContextMenu;
import br.uff.tempo.apps.map.rule.ContextMenuItem;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.ContextVariable;

public class SmartSprite extends Sprite implements TouchEvents.ITouchEvents {

	private TouchEvents touch;
	private ISpriteController spriteController;
	private ResourceWrapper wrapper;
	private ContextMenu menu;

	public SmartSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.touch = new TouchEvents(this);
	}
	
	/**
	 * Registers an {@link ISpriteController} object to receive events
	 * from this SmartSprite. There is only one controller object that will
	 * receive this events. Calling this method, the controller set before
	 * will be replaced.
	 * 
	 * @param spriteController The Controller object that will receive the events
	 * 
	 * @see ISpriteController
	 */
	public void registerSpriteController(ISpriteController spriteController) {
		this.spriteController = spriteController;
	}
	
	private void fillContextVariables() {
		
		IResourceAgent stub = wrapper.getStub();
		
		if (stub == null) {
			String errorMessage = "Stub object from this agent is null";
			Log.e("SmartAndroid", errorMessage);
			throw new SmartAndroidRuntimeException(errorMessage);
		}
		Class<?> classes[] = stub.getClass().getInterfaces();
		
		if (classes.length == 0) {
			String errorMessage = "The agent [" + stub.getName() + "] of class [" + stub.getClass().getSimpleName() + "] doesn't implement any interfaces. It should implement at least some 'IResourceAgent' subclass";
			Log.wtf("SmartAndroid", errorMessage);
			throw new SmartAndroidRuntimeException(errorMessage);
		}
		
		//Iterate over the array of interfaces which are implemented by this agent.
		//In fact, we're looking for a IResourceAgent subclass (e.g IStove).
		//The loop is necessary because we don't know what position the desired interface is
		for (Class<?> clazz : classes) {

			Method[] methods = clazz.getMethods();
			
			for (Method m : methods){
				
				if (m.isAnnotationPresent(IResourceAgent.ContextVariable.class)) {
					
					ContextVariable cv = m.getAnnotation(IResourceAgent.ContextVariable.class);
					ContextMenuItem item = new ContextMenuItem(cv.name());
					item.setExtra(m);
					item.setRans(stub.getRANS());
					menu.addItem(item);
				}
			}

		}
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		touch.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		
		return true;
	}
	
	public void enableContextMenu(FontManager menuFontManager, TextureManager menuTextureManager) {
		if (menu == null) {
			menu = new ContextMenu(getWidth(), getHeight(), getVertexBufferObjectManager(), menuFontManager, menuTextureManager);
			fillContextVariables();
		}
		attachChild(menu);
	}
	
	public void showContextMenu(Camera camera) {
		float testX = getX() + getWidth() + menu.getBoxMenu().getWidth();
		float testY = getY() + getHeight() + menu.getBoxMenu().getHeight();
		
		if (testX > camera.getWidth()) {
			menu.setX(-menu.getWidth());
		} else {
			menu.setX(getWidth());
		}
		
		if (testY > camera.getHeight()) {
			menu.setY(-menu.getHeight());
		} else {
			menu.setY(getHeight());
		}
		
		menu.show();
	}
	
	public void hideContextMenu() {
		menu.hide();
	}
	
	public ContextMenu getContextMenu() {
		return this.menu;
	}
	
	public void setPosition(float pX, float pY) {
		super.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onStartLongPress(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onStartLongPress");
		if (spriteController != null) {
			spriteController.onSpriteStartLongPress(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onLongPressMove(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onLongPressMove");
		if (spriteController != null) {
			spriteController.onSpriteLongPressMove(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onEndLongPressMove(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onEndLongPressMove");
		if (spriteController != null) {
			spriteController.onSpriteEndLongPressMove(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onTap(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onTap");
		if (spriteController != null) {
			spriteController.onSpriteTap(this, pSceneTouchEvent);
		}
	}

	public ResourceWrapper getResourceWrapper() {
		return wrapper;
	}

	public void setResourceWrapper(ResourceWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
