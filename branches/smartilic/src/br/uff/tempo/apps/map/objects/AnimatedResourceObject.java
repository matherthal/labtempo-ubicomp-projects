package br.uff.tempo.apps.map.objects;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import br.uff.tempo.apps.map.objects.notification.INotificationBoxReceiver;
import br.uff.tempo.apps.map.objects.notification.NotificationBox;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class AnimatedResourceObject extends AnimatedSprite implements
		INotificationBoxReceiver {

	private static final float DEFAULT_DURATION = 5;

	private NotificationBox nbox;
	private Space space;

	public AnimatedResourceObject(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			FontManager fontManager, TextureManager textureManager) {

		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

		nbox = new NotificationBox(this.getWidth(), this.getHeight(),
				pVertexBufferObjectManager, fontManager, textureManager);

		this.attachChild(nbox);
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
				null, new IPathModifierListener() {

					@Override
					public void onPathWaypointStarted(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {

						final int nextWaypoint = (pWaypointIndex >= pPathModifier.getPath().getLength()) ? 0 : pWaypointIndex + 1;
						final float pathX = pPathModifier.getPath().getCoordinatesX()[nextWaypoint];
						final float pathY = pPathModifier.getPath().getCoordinatesY()[nextWaypoint];
						final AnimatedSprite sprite = (AnimatedSprite)pEntity;
						
						if (pathX > sprite.getX()){
							// Right
							sprite.animate(new long[]{200, 200, 200}, 6, 8, true);
						}else if (pathX < sprite.getX()){
							// Left
							sprite.animate(new long[]{200, 200, 200}, 3, 5, true);
						}else if (pathY > sprite.getY()){
							// Down
							sprite.animate(new long[]{200, 200, 200}, 0, 2, true);
						}else if (pathY < sprite.getY()){
							// Up
							sprite.animate(new long[]{200, 200, 200}, 9, 11, true);
						}
					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						

					}

					@Override
					public void onPathStarted(final PathModifier pPathModifier,
							final IEntity pEntity) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onPathFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity) {
						
						((AnimatedSprite)pEntity).stopAnimation(1);
					}
				}));
	}

	@Override
	public void showMessage(String message) {
		nbox.show(message);
	}

	public void setSpace(Space space) {
		this.space = space;
	}

}
