package br.uff.tempo.apps.map.objects;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import br.uff.tempo.apps.map.objects.notification.INotificationBoxReceiver;
import br.uff.tempo.apps.map.objects.notification.NotificationBox;

//implementing long press by hands!
public abstract class ResourceObject extends org.andengine.entity.sprite.Sprite implements INotificationBoxReceiver {

	// min time that represents a long press event
	private static final int LONGPRESS_THRESHOLD = 500;

	private boolean mLongPressed;
	private Timer mLongPressTimer;

	private NotificationBox nbox;

	public ResourceObject(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			FontManager fontManager, TextureManager textureManager) {

		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

		nbox = new NotificationBox(this.getWidth(), this.getHeight(),
				pVertexBufferObjectManager, fontManager, textureManager);
		
		this.attachChild(nbox);
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	@Override
	public void showMessage(String message) {
	
		nbox.show(message);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {

			// Finger has touched screen.

			// starts the timer
			mLongPressTimer = new Timer();
			mLongPressTimer.schedule(new TimerTask() {
				@Override
				public void run() {

					// if passed more than LONGPRESS_THRESHOLD ms, long press
					// event occurred
					mLongPressed = true;
					ResourceObject.this.onStartLongPress(pSceneTouchEvent);
				}

			}, LONGPRESS_THRESHOLD);
		}

		if (pSceneTouchEvent.isActionMove()) {

			if (mLongPressed) {
				onLongPressMove(pSceneTouchEvent);
			}
		}

		if (pSceneTouchEvent.isActionUp()) {

			if (mLongPressed) {

				onEndLongPressMove(pSceneTouchEvent);
				
			} else {
				
				onTap(pSceneTouchEvent);
			}
			
			cancelTimer();
		}

		return true;
	}

	private void cancelTimer() {

		if (mLongPressTimer != null) {
			mLongPressTimer.cancel();
		}
		mLongPressed = false;
	}

	// ===========================================================
	// Abstract Methods
	// ===========================================================

	// fired when user performs a long touch (more than LONGPRESS_THRESHOLD ms)
	public abstract void onStartLongPress(TouchEvent pSceneTouchEvent);

	// fired when user performs a long touch and move the object (more than LONGPRESS_THRESHOLD ms)
	public abstract void onLongPressMove(TouchEvent pSceneTouchEvent);
	
	// fired when user release the object, after a long press
	public abstract void onEndLongPressMove(TouchEvent pSceneTouchEvent);

	// fired when user performs a short touch
	public abstract void onTap(TouchEvent pSceneTouchEvent);

}
