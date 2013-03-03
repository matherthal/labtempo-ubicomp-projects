package br.uff.tempo.apps.simulators.tracking;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import br.uff.tempo.apps.simulators.tracking.mode.TrackingMode;
import br.uff.tempo.apps.simulators.tracking.surface.AbstractTrackingPanel;

public class TrackingPanel extends AbstractTrackingPanel {
	
	private int[] colors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.RED, Color.YELLOW };
	private String[] names = {"Andre", "David", "Douglas", "Lucas", "Matheus", "Orlando", "Pedro"};
	
	private Random random = new Random();
	private Avatar selectedAvatar;
	private Iterator<Point> itPath;
	
	public TrackingPanel(Context context) {
		super(context);
	}
	
	public TrackingPanel(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public void stepPath(final int index) {
		
		if (selectedAvatar == null) return;
			
		if (itPath.hasNext()) {
			new Thread() {
				public void run() {
					try {
						sleep(STEP_TIME * index);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Point p = itPath.next();
					Log.d("TrackingPanel", "Changing positions to [" + p.x + ", " + p.y + "]");
					selectedAvatar.setAndStorePosition(p.x, p.y);
				}
			}.start();
			
		} else {
			itPath = getPathPoints().iterator();
		}
	}
	
	public void playPath() {
		
		if (selectedAvatar == null) return;

		itPath = getPathPoints().iterator();
		for (int i = 0; i < getPathPoints().size(); i++){
			stepPath(i);
		}
	}
	
	public void applyPath() {
		//Choose witch User will be controlled
		Toast.makeText(getContext(), "Select an object in the screen", Toast.LENGTH_SHORT).show();
		setMode(TrackingMode.SELECT_OBJECT);
	}

	public void addPerson() {

		int index = random.nextInt(names.length);
		String name = names[index];
		
		Paint p = new Paint();
		p.setColor(colors[index]);

		Avatar usr = new Avatar(name, getScreenCenterX(), getScreenCenterY(),
				dpTopixel(RADIUS), p);
		usr.storePosition();
		getUsers().add(usr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (getMode()) {
		
			case MANUAL_MOVE:
				
				getMovementsHandler().manualMove(event, getUsers());
				break;
				
			case DEFINE_TRACK:
				
				Point point = getMovementsHandler().defineTrack(event);
				if (point != null) {
					getPathPoints().add(point);
				}
				break;
				
			case PLAY:
				
				break;
				
			case SELECT_OBJECT:
				
				Avatar a = getMovementsHandler().select(event, getUsers());
				if (a != null) {
					selectedAvatar = a;
				}
				LinkedList<Point> pathPoints = getPathPoints();
				
				if (selectedAvatar != null && pathPoints != null && !pathPoints.isEmpty()) {
					selectedAvatar.setCenter(pathPoints.getFirst().x, pathPoints.getFirst().y);
				}
				break;
				
			case STEP:
				break;
				
			default:
				break;
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.BLACK);

		//Draw the rooms
		for (Rect rect : getRooms().values()) {
			canvas.drawRect(rect, getPaintRooms());
		}
		
		if (getMode() != TrackingMode.MANUAL_MOVE) {
			
			LinkedList<Point> pathPoints = (LinkedList<Point>) getPathPoints().clone();
			if (pathPoints != null && !pathPoints.isEmpty()) {
				Point currentPoint = pathPoints.getFirst();
				for (Point p : pathPoints) {
					canvas.drawPoint(p.x, p.y, getPaintRooms());
					canvas.drawLine(currentPoint.x, currentPoint.y, p.x, p.y, getPaintPath());
					currentPoint = p;
				}
			}
		}

		//Draw the circles (people)
		for (Avatar circ : getUsers()) {
			canvas.drawCircle(circ.getCenterX(), circ.getCenterY(),	
					circ.getRadius(), circ.getPaint());
		}
	}
}