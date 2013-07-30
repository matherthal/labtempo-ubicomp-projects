package br.uff.tempo.apps.map.objects.sprite;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import br.uff.tempo.middleware.e.SmartAndroidException;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;

public class ResourceTextureFactory {

	private BaseGameActivity baseGameActivity;
	private BuildableBitmapTextureAtlas buildableTextureAtlas;
	private Map<String, ITextureRegion> resourceTextures;

	public ResourceTextureFactory(BaseGameActivity baseGameActivity)
			throws SmartAndroidException {

		this.baseGameActivity = baseGameActivity;
		this.buildableTextureAtlas = new BuildableBitmapTextureAtlas(
				baseGameActivity.getTextureManager(), 512, 512,
				TextureOptions.DEFAULT);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		setupTextures();

		try {
			this.buildableTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.buildableTextureAtlas.load();

		} catch (TextureAtlasBuilderException e) {
			throw new SmartAndroidException("Error building the Textures", e);
		}
	}

	private void setupTextures() {

		this.resourceTextures = new HashMap<String, ITextureRegion>();

		// Stove
		this.resourceTextures.put("Stove", createTextureRegion("stove_small.png"));
		// TV
		this.resourceTextures.put("Television", createTextureRegion("tv_small.png"));
		// Lamp
		this.resourceTextures.put("Lamp", createTextureRegion("lamp_inactive.png"));
		// Bed
		this.resourceTextures.put("Bed", createTextureRegion("bed_small.png"));
		// Air conditioner
		this.resourceTextures.put("AirConditioner", createTextureRegion("air_cond.png"));
		// People
		this.resourceTextures.put("Person", BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.buildableTextureAtlas,this.baseGameActivity,
						"man_bald_big.png", 3, 4));
	}

	public TextureRegion createTextureRegion(String resName) {
		return BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.buildableTextureAtlas, this.baseGameActivity, resName);
	}

	public ITextureRegion getResourceTextureRegion(String resource) {

		ITextureRegion texture = this.resourceTextures.get(resource);
		
		if (texture != null) {
			return texture;
		}
		
		throw new SmartAndroidRuntimeException(
				"There is no Texture for the resource [" + resource + "]");
	}
}
