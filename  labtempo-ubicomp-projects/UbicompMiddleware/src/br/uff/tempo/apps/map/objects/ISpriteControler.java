package br.uff.tempo.apps.map.objects;

import org.andengine.input.touch.TouchEvent;

public interface ISpriteControler {

	void onSpriteStartLongPress(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteEndLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteTap(SmartSprite sprite, TouchEvent pSceneTouchEvent);
}
