package br.uff.tempo.apps.map.objects.sprite;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import br.uff.tempo.apps.map.SmartMapActivity;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;
import br.uff.tempo.middleware.management.utils.Space;

/**
 * Creates one of a set of Sprites, according to the Type passed 
 * @author dbarreto
 */
public final class SmartSpriteFactory {
	
	private SmartSpriteFactory() {}
	
	/**
	 * Creates a new Sprite.
	 * @param type The Sprite Type (either SmartSprite or SmartAnimatedSprite)
	 * @param x Position X (scene coordinates)
	 * @param y Position Y (scene coordinates)
	 * @param texture The texture that represents the Sprite graphically
	 * @param vertex
	 * @return The new Sprite created
	 */
	public static Sprite createSprite(String type, float x, float y, ITextureRegion texture, VertexBufferObjectManager vertex) {
		return createSprite(type, x, y, texture, vertex, null, null, null, null);
	}
	
	/**
	 * Creates a new Sprite and register in it to listen events, if its applicable.
	 * @param type The Sprite Type (either SmartSprite or SmartAnimatedSprite)
	 * @param x Position X (scene coordinates)
	 * @param y Position Y (scene coordinates)
	 * @param texture The texture that represents the Sprite graphically
	 * @param vertex
	 * @param controler The object that will listen Sprite events
	 * @param wrapper The object that stores information about the resource agent
	 * @param map The map
	 * @return The new Sprite createdbaseGameActivity.getFontManager()
	 */
	public static Sprite createSprite(String type, float x, float y, ITextureRegion texture, VertexBufferObjectManager vertex, ISpriteController controler, ResourceWrapper wrapper, Space map, SmartMapActivity activity) {
		
		if (type.equals("Person")) {
			SmartAnimatedSprite sprite = new SmartAnimatedSprite(x, y, (ITiledTextureRegion) texture, vertex);
			sprite.setResourceWrapper(wrapper);
			sprite.setSpace(map);
			return sprite;
		} else {
			SmartSprite sprite = new SmartSprite(x, y, texture, vertex);
			sprite.registerSpriteController(controler);
			sprite.setResourceWrapper(wrapper);
			sprite.enableContextMenu(activity.getFontManager(), activity.getTextureManager());
			sprite.getOpMenu().registerActionListener(activity);
			activity.getScene().registerTouchArea(sprite.getOpMenu().getBoxMenu());
			sprite.getContextMenu().registerActionListener(activity);
			activity.getScene().registerTouchArea(sprite.getContextMenu().getBoxMenu());
			return sprite;
		}
	}
}
