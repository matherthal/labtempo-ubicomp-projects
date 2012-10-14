package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import br.uff.tempo.middleware.management.interfaces.IPerson;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;

public class Person extends ResourceAgent implements IPerson {

	public static final long DEFAULT_PERIOD = 1000;
	public static final float DEFAULT_THRESHOLD = 1.0f;
	public static final float EPSLON = 0.01f;
	public static final int DEFAUT_WINDOW_SIZE = 20;
	private static final long serialVersionUID = 1L;
	public static final String CV_POSITION = "position";

	private Timer timer;

	private long period; // milliseconds
	private int windowSize;

	Position lastPos = new Position(0f,0f);

	List<SmartObject> objects;
	List<Place> recentLocal;
	LinkedList<Position> recentPositions;
	IResourceLocation rLS;

	public Person(String name) {
		this(name, Person.class.getName(), DEFAULT_PERIOD);
	}

	public Person(String name, String type, long period) {
		this(new ArrayList<SmartObject>(), new LinkedList<Position>(), name,
				type, 4312, period);

	}

	public Person(List<SmartObject> sensors,
			LinkedList<Position> recentPositions, String name, String type,
			int id, long period) {
		super(name, type, id);
		this.objects = sensors;
		this.recentPositions = recentPositions;

		this.period = period;

		this.windowSize = DEFAUT_WINDOW_SIZE;

//		this.timer = new Timer();
//
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//
//				while (true) {
//					if (lastPos != null) {
//						notifyStakeholders(CV_POSITION, lastPos);
//						lastPos = null;
//					}
//				}
//			}
//		}, this.period);
	}

	public void updateRecentLocal() {
		rLS = new ResourceLocationStub(this.getRDS().search("ResourceLocation")
				.get(0));
		for (Position position : recentPositions) {
			recentLocal.add(rLS.getLocal(position));
		}
	}

	// list is in reverse order
	public SmartObject getSensor(int i) {
		return objects.get(objects.size() - (i + 1));
	}

	// return preview sensor
	public SmartObject setSensor(int i, SmartObject sensor) {
		return objects.set(objects.size() - (i + 1), sensor);
	}

	public void addSensor(SmartObject sensor) {
		objects.add(sensor);
	}

	public boolean removeSensor(SmartObject sensor) {
		return objects.remove(sensor);
	}

	public Position getPosition(int i) {
		return recentPositions.get(objects.size() - (i + 1));
	}

	public Position getCurrentPosition() {
		return recentPositions.getLast();
	}

	// //return preview sensor
	// public Position setPosition(int i, Position position){
	// return recentPositions.set(recentPositions.size()-(i+1), position);
	// }

	public void addPosition(Position position) {
		if (recentPositions.size() >= this.windowSize) {
			recentPositions.removeFirst();
		}

		recentPositions.add(position);

		// float point number comparison is error probe. Using an epslon to avoid rounding errors
		if (Math.abs(position.getDistance(lastPos) - DEFAULT_THRESHOLD) >= EPSLON) {
			notifyStakeholders(CV_POSITION, position);
		}
		
		lastPos = position;
	}

	public boolean removePosition(Position position) {
		return recentPositions.remove(position);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
