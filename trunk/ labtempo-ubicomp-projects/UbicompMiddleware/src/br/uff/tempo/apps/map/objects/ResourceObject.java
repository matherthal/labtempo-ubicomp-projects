package br.uff.tempo.apps.map.objects;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.content.Intent;

//implementing long press by hands!
public abstract class ResourceObject extends org.andengine.entity.sprite.Sprite {

	// min time that represents a long press event
	private static final int LONGPRESS_THRESHOLD = 500;

	private boolean mLongPressed;
	private Intent intent;
	private Context context;
	private Timer mLongPressTimer;

	public ResourceObject(float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	}

	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

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
				onLongPress(pSceneTouchEvent);
			}
		}

		if (pSceneTouchEvent.isActionUp()) {

			if (!mLongPressed) {

				onTap(pSceneTouchEvent);
			}
			cancelTimer();
		}

		return true;
	}

	private void cancelTimer() {

		mLongPressTimer.cancel();
		mLongPressed = false;
	}

	public void setAction(Intent i, Context v) {
		this.intent = i;
		this.context = v;

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	// ===========================================================
	// Abstract Methods
	// ===========================================================

	// fired when user performs a long touch (more than LONGPRESS_THRESHOLD ms)
	public abstract void onStartLongPress(TouchEvent pSceneTouchEvent);

	// fired when user finishes a long touch (more than LONGPRESS_THRESHOLD ms)
	public abstract void onLongPress(TouchEvent pSceneTouchEvent);/*
																 * {
																 * 
																 * this.setPosition
																 * (
																 * pSceneTouchEvent
																 * .getX() -
																 * this
																 * .getWidth() /
																 * 2,
																 * pSceneTouchEvent
																 * .getY() -
																 * this
																 * .getHeight()
																 * / 2); }
																 */

	// fired when user performs a short touch
	public abstract void onTap(TouchEvent pSceneTouchEvent); /*
															 * {
															 * 
															 * //starts an
															 * activity
															 * ("the app")
															 * this.context
															 * .startActivity
															 * (intent); }
															 */

}
