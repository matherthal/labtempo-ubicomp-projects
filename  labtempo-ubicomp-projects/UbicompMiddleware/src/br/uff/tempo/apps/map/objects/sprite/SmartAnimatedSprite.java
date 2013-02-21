package br.uff.tempo.apps.map.objects;

import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import br.uff.tempo.apps.simulators.utils.ResourceWrapper;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class SmartAnimatedSprite extends AnimatedSprite {
	
	// default duration of animation (ms)
	private static final float DEFAULT_DURATION = 5;

	private Space space;
	private IPathModifierListener pathListener;
	private ResourceWrapper wrapper;

	public SmartAnimatedSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, (ITiledTextureRegion) pTextureRegion, pVertexBufferObjectManager);
		this.pathListener = new PeoplePathListener();
	}
	
	public void setSpace(Space space) {
		this.space = space;
	}
	
	public void updatePosition(Position p) {

		float x = p.getX();
		float y = p.getY();

		updatePosition(x, y);
	}
	
	public synchronized void updatePosition(float x, float y) {

		int pX = space.metersToPixel(x);
		int pY = space.metersToPixel(space.invertYcoordinate(y));

		pX -= this.getWidth() / 2;
		pY -= this.getHeight();

		final Path path = new Path(2).to(getX(), getY()).to(pX, pY);

		this.registerEntityModifier(new PathModifier(DEFAULT_DURATION, path,
				null, this.pathListener));
	}
	
	public ResourceWrapper getResourceWrapper() {
		return wrapper;
	}

	public void setResourceWrapper(ResourceWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
