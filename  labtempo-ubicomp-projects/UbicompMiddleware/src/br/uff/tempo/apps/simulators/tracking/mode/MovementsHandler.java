package br.uff.tempo.apps.simulators.tracking.mode;

import java.util.Iterator;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import br.uff.tempo.apps.simulators.tracking.Avatar;

public class MovementsHandler {
	
	private boolean caught = false;
	private Avatar currentUser;
	
	public Point defineTrack(MotionEvent event) {
		
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			return new Point(x, y);
		}
		return null;
	}

	public void manualMove(MotionEvent event, List<Avatar> users) {
		
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Log.d("TrackingPanel", "Action DOWN");
			select(event, users);
			
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			Log.d("TrackingPanel", "Action MOVE");

			if (caught) {

				Log.d("TrackingPanel", "Moving " + currentUser.getName()
						+ " from [" + currentUser.getCenterX() + " "
						+ currentUser.getCenterY() + "] to [" + x + " " + y
						+ "]");

				currentUser.setCenter(x, y);
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {

			Log.d("TrackingPanel", "Action UP");

			if (currentUser != null && caught) {
				currentUser.storePosition();
			}
			caught = false;
		}
	}

	public Avatar select(MotionEvent event, List<Avatar> users) {
		
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		Iterator<Avatar> it = users.iterator();

		while (it.hasNext()) {

			Avatar c = it.next();

			if (c.contains(x, y)) {

				currentUser = c;
				caught = true;

				Log.d("TrackingPanel", c.getName() + " was caught");
				return currentUser;
			}
		}
		
		return null;
	}
}