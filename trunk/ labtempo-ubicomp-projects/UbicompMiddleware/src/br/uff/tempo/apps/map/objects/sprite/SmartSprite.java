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
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.Service;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class SmartSprite extends Sprite implements TouchEvents.ITouchEvents {

	private TouchEvents touch;
	private ISpriteController spriteController;
	private ResourceWrapper wrapper;
	private ContextMenu menuIc;
	private ContextMenu menuOp;

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
					item.setMethodName(m.getName());
					menuIc.addItem(item);
				} else if (m.isAnnotationPresent(IResourceAgent.Service.class)) {
					Service service = m.getAnnotation(IResourceAgent.Service.class);
					ContextMenuItem item = new ContextMenuItem(service.name());
					item.setExtra(m);
					item.setRans(stub.getRANS());
					item.setMethodName(m.getName());
					menuOp.addItem(item);
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
		if (menuIc == null && menuOp == null) {
			menuIc = new ContextMenu(getWidth(), getHeight(), getVertexBufferObjectManager(), menuFontManager, menuTextureManager);
			menuOp = new ContextMenu(getWidth(), getHeight(), getVertexBufferObjectManager(), menuFontManager, menuTextureManager);
			fillContextVariables();
		}
		attachChild(menuIc);
		attachChild(menuOp);
	}
	
	public void showMenu(Camera camera, ContextMenu m) {
		float testX = getX() + getWidth() + m.getBoxMenu().getWidth();
		float testY = getY() + getHeight() + m.getBoxMenu().getHeight();
		
		if (testX > camera.getWidth()) {
			m.setX(-m.getWidth());
		} else {
			m.setX(getWidth());
		}
		
		if (testY > camera.getHeight()) {
			m.setY(-m.getHeight());
		} else {
			m.setY(getHeight());
		}
		
		m.show();
	}
	
	public void showContextMenu(Camera camera) {
		showMenu(camera, menuIc);
	}
	
	public void showOperationsMenu(Camera camera) {
		showMenu(camera, menuOp);
	}
	
	public void hideContextMenu() {
		menuIc.hide();
	}
	
	public void hideOpMenu() {
		menuOp.hide();
	}
	
	public ContextMenu getContextMenu() {
		return this.menuIc;
	}
	
	public ContextMenu getOpMenu() {
		return this.menuOp;
	}	
	
	public void setPosition(float pX, float pY) {
		super.setPosition(pX, pY);
		pX = Space.pixelToMeters((int)pX, Space.PIXEL_PER_METER);
		pY = Space.pixelToMeters((int)pY, Space.PIXEL_PER_METER);
		wrapper.getStub().setPosition(new Position(pX, pY));
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
