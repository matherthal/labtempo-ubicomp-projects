package br.uff.tempo.apps.map.objects.sprite;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;

public class PeoplePathListener implements IPathModifierListener {

	private Object lock;
	
	public PeoplePathListener(Object lock) {
		this.lock = lock;
	}
	
	@Override
	public void onPathWaypointStarted(final PathModifier pPathModifier,
			final IEntity pEntity, final int pWaypointIndex) {

		final int nextWaypoint = (pWaypointIndex >= pPathModifier.getPath()
				.getLength()) ? 0 : pWaypointIndex + 1;
		final float pathX = pPathModifier.getPath().getCoordinatesX()[nextWaypoint];
		final float pathY = pPathModifier.getPath().getCoordinatesY()[nextWaypoint];
		final AnimatedSprite sprite = (AnimatedSprite) pEntity;

		if (pathX > sprite.getX()) {
			// Right
			sprite.animate(new long[] { 200, 200, 200 }, 6, 8, true);
		} else if (pathX < sprite.getX()) {
			// Left
			sprite.animate(new long[] { 200, 200, 200 }, 3, 5, true);
		} else if (pathY > sprite.getY()) {
			// Down
			sprite.animate(new long[] { 200, 200, 200 }, 0, 2, true);
		} else if (pathY < sprite.getY()) {
			// Up
			sprite.animate(new long[] { 200, 200, 200 }, 9, 11, true);
		}
	}

	@Override
	public void onPathWaypointFinished(final PathModifier pPathModifier,
			final IEntity pEntity, final int pWaypointIndex) {	}

	@Override
	public void onPathStarted(final PathModifier pPathModifier,
			final IEntity pEntity) {}

	@Override
	public void onPathFinished(final PathModifier pPathModifier,
			final IEntity pEntity) {
		((AnimatedSprite) pEntity).stopAnimation(1);
		
		synchronized (lock) {
			Log.d("MAP", "Finishing Animation, notifying the others...");
			lock.notifyAll();
		}
	}

}
