package br.uff.tempo.apps.map.objects.sprite;

import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;
import br.uff.tempo.apps.simulators.utils.ResourceWrapper;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class SmartAnimatedSprite extends AnimatedSprite {
	
	// default duration of animation (ms)
	private static final float DEFAULT_DURATION = 5;
	private Object lock = new Object();

	private Space space;
	private IPathModifierListener pathListener;
	private PathModifier modifier;
	private ResourceWrapper wrapper;

	public SmartAnimatedSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(pX, pY, (ITiledTextureRegion) pTextureRegion, pVertexBufferObjectManager);
		this.pathListener = new PeoplePathListener(lock);
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

		synchronized (lock) {
			
			Log.i("MAP", "Calling updatePosition");
			
			if (modifier != null && !modifier.isFinished()) {
				Log.i("MAP", "Animation is running, wait a little bit...");
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			int pX = space.metersToPixel(x);
			int pY = space.metersToPixel(space.invertYcoordinate(y));
	
			pX -= this.getWidth() / 2;
			pY -= this.getHeight();
				
			Log.d("MAP", "Moving from [" + getX() + ", " + getY()  + "] to [" + pX + ", " + pY + "]");
	
			final Path path = new Path(2).to(getX(), getY()).to(pX, pY);
	
			modifier = new PathModifier(DEFAULT_DURATION, path, null, this.pathListener);
			this.registerEntityModifier(modifier);
		}
	}
	
	public ResourceWrapper getResourceWrapper() {
		return wrapper;
	}

	public void setResourceWrapper(ResourceWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
