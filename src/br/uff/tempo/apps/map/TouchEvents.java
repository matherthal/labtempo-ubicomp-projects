package br.uff.tempo.apps.map;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.input.touch.TouchEvent;

public class TouchEvents {

	// minimum time that represents a long press event
	private static final int LONGPRESS_THRESHOLD = 500;

	private Timer mLongPressTimer;
	private ITouchEvents touch;
	private boolean mLongPressed;

	public TouchEvents(ITouchEvents touch) {
		this.touch = touch;
	}

	public void onAreaTouched(final TouchEvent pSceneTouchEvent,
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
					TouchEvents.this.touch.onStartLongPress(pSceneTouchEvent);
				}
			}, LONGPRESS_THRESHOLD);
		}

		if (pSceneTouchEvent.isActionMove()) {

			if (mLongPressed) {
				this.touch.onLongPressMove(pSceneTouchEvent);
			}
		}

		if (pSceneTouchEvent.isActionUp()) {

			if (mLongPressed) {
				this.touch.onEndLongPressMove(pSceneTouchEvent);
			} else {
				this.touch.onTap(pSceneTouchEvent);
			}
			cancelTimer();
		}
	}

	private void cancelTimer() {

		if (mLongPressTimer != null) {
			mLongPressTimer.cancel();
		}
		mLongPressed = false;
	}

	public static interface ITouchEvents {

		/**
		 * Fired when user performs a long touch (more than LONGPRESS_THRESHOLD
		 * ms)
		 * 
		 * @param pSceneTouchEvent
		 */
		void onStartLongPress(TouchEvent pSceneTouchEvent);

		/**
		 * Fired when user performs a long touch and move the object (more than
		 * LONGPRESS_THRESHOLD ms)
		 * 
		 * @param pSceneTouchEvent
		 */
		void onLongPressMove(TouchEvent pSceneTouchEvent);

		/**
		 * Fired when user release the object, after a long press
		 * @param pSceneTouchEvent
		 */
		void onEndLongPressMove(TouchEvent pSceneTouchEvent);

		/**
		 * Fired when user performs a short touch
		 * @param pSceneTouchEvent
		 */
		void onTap(TouchEvent pSceneTouchEvent);
	}
}