package br.uff.tempo.apps.map.utils;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;

public class Transformations {

	public static void applyAlphaModifier(final IEntity entity, final float fromAlpha, final float toAlpha) {
		entity.registerEntityModifier(new AlphaModifier(2, fromAlpha, toAlpha));
	}
	
	public static void applyAlphaModifierInChindren(final IEntity entity, final float fromAlpha, final float toAlpha) {
		for (int i = 0; i < entity.getChildCount(); i++) {
			IEntity child = entity.getChildByIndex(i);
			applyAlphaModifier(child, fromAlpha, toAlpha);
		}
	}
	
	public static void applyAlphaModifierInFatherAndChindren(final IEntity entity, final float fromAlpha, final float toAlpha) {
		applyAlphaModifier(entity, fromAlpha, toAlpha);
		applyAlphaModifierInChindren(entity, fromAlpha, toAlpha);
	}
}
