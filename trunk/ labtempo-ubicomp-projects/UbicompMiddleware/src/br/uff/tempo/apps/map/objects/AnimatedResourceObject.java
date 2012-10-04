package br.uff.tempo.apps.map.objects;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;

import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class AnimatedResourceObject extends AnimatedSprite implements
		INotificationBoxReceiver {

	private static final float DEFAULT_DURATION = 10;

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

	public void updatePosition(float x, float y) {

		int pX = space.metersToPixel(x);
		int pY = space.metersToPixel(space.invertYcoordinate(y));

		final Path path = new Path(1).to(x, y);

		this.registerEntityModifier(new PathModifier(DEFAULT_DURATION, path,
				null, new IPathModifierListener() {

					@Override
					public void onPathWaypointStarted(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {

						switch (pWaypointIndex) {
						case 0:
							AnimatedResourceObject.this.animate(new long[] {
									200, 200, 200 }, 6, 8, true);
							break;

						case 1:

							AnimatedResourceObject.this.animate(new long[] {
									200, 200, 200 }, 3, 5, true);
							break;

						case 2:

							AnimatedResourceObject.this.animate(new long[] {
									200, 200, 200 }, 0, 2, true);
							break;

						case 3:

							AnimatedResourceObject.this.animate(new long[] {
									200, 200, 200 }, 9, 11, true);
							break;
						}
					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						// TODO Auto-generated method stub

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
						// TODO Auto-generated method stub

					}
				}));
	}

	@Override
	public void showMessage(String message) {
		nbox.show(message);
	}

	@Override
	public void setNotificationBoxColor(Color color) {
		nbox.setColor(color);
	}

	@Override
	public void setNotificationBoxVisible(boolean visible) {
		nbox.setVisible(visible);
	}

	public void setSpace(Space space) {
		this.space = space;
	}

}
