package br.uff.tempo.apps.map.objects.sprite;

import org.andengine.input.touch.TouchEvent;

public interface ISpriteController {

	void onSpriteStartLongPress(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteEndLongPressMove(SmartSprite sprite, TouchEvent pSceneTouchEvent);
	void onSpriteTap(SmartSprite sprite, TouchEvent pSceneTouchEvent);
}
