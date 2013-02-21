package br.uff.tempo.apps.map.objects.sprite;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;
import br.uff.tempo.apps.map.TouchEvents;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;

public class SmartSprite extends Sprite implements TouchEvents.ITouchEvents {

	private TouchEvents touch;
	private ISpriteControler spriteControler;
	private ResourceWrapper wrapper;

	public SmartSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		
		this.touch = new TouchEvents(this);
	}
	
	public void registerSpriteControler(ISpriteControler spriteControler) {
		this.spriteControler = spriteControler;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		touch.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		
		return true;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onStartLongPress(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onStartLongPress");
		if (spriteControler != null) {
			spriteControler.onSpriteStartLongPress(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onLongPressMove(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onLongPressMove");
		if (spriteControler != null) {
			spriteControler.onSpriteLongPressMove(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onEndLongPressMove(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onEndLongPressMove");
		if (spriteControler != null) {
			spriteControler.onSpriteEndLongPressMove(this, pSceneTouchEvent);
		}
	}

	@Override
	public void onTap(TouchEvent pSceneTouchEvent) {
		Log.i("SmartAndroid", "onTap");
		if (spriteControler != null) {
			spriteControler.onSpriteTap(this, pSceneTouchEvent);
		}
	}

	public ResourceWrapper getResourceWrapper() {
		return wrapper;
	}

	public void setResourceWrapper(ResourceWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
