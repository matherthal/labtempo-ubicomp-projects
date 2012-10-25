package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.management.utils.Sorter;
import br.uff.tempo.middleware.management.utils.Space;

public class ResourceLocation extends ResourceAgent implements IResourceLocation {

	private static final long serialVersionUID = 1L;
	
	private Space currentSpace;

	private static ResourceLocation instance;

	HashMap<String, Place> map;
	HashMap<String, Position> resources;
	HashMap<String, HashMap<String, Position>> baseIndexer;

	private ResourceLocation() {
		super("ResourceLocation", ResourceLocation.class.getName(), 3);
		
		currentSpace = new Space();
		resources = new HashMap<String, Position>();
		baseIndexer = new HashMap<String, HashMap<String, Position>>();
		loadBase();
	}
	
	public static ResourceLocation getInstance() {
		if (instance == null)
			instance = new ResourceLocation();
		return instance;
	}

	@Override
	public boolean identify() {
		String ip = ResourceAgentIdentifier.getLocalIpAddress();
		int prefix = ResourceAgentIdentifier.getLocalPrefix();
		
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.getRAI(), ip, prefix));
		ResourceRepository.getInstance().add(this.getRAI(), ip, prefix, this.getRAI());
		
		return true;
	}
	
	/**
	 * carrega mapa com lugares baseados na interface do MapaDaCasa areas da
	 * casa e dos comodos foram estimadas area da casa: 100x50
	 */
	public void loadBase() {
		addPlace("Hall", new Position(0, 0), new Position(50, 25));
		addPlace("Bathroom", new Position(50, 0), new Position(70, 20));
		addPlace("Storeroom", new Position(70, 0), new Position(100, 20));
		addPlace("Corridor", new Position(50, 20), new Position(100, 25));
		addPlace("Bedroom1", new Position(0, 25), new Position(30, 50));
		addPlace("Bedroom2", new Position(30, 25), new Position(60, 50));
		addPlace("Kitchen", new Position(60, 25), new Position(100, 50));
	}

	public void addPlace(String name, Position lower, Position upper) {
		Place place = new Place(name, lower, upper);
		addPlace(place);
	}
	
	public void addPlace(Place place) {	
		currentSpace.addPlace(place);
		baseIndexer.put(place.getName(), new HashMap<String, Position>());
		
		Log.i("SmartAndroid", "New place added: " + place.getName() + ". Bottom-left corner at = [" + place.getLower().getX() + " , " + place.getLower().getY() + "] and top-right corner at [" + place.getUpper().getX() + " , " + place.getUpper().getY() + "]");
	}

	// public ArrayList<Place> search(ResourceAgent rA) {
	// ArrayList<Place> result = new ArrayList<Place>();
	// Place current = get(rA);
	// result.add(0, current);// current local is the nearest
	// ArrayList<Place> localList = (ArrayList<Place>) map.values();
	// result = getNeighbors(current, localList, result);
	// return result;
	// }

	public ArrayList<String> search(String query) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Set<String>> setList = new ArrayList<Set<String>>();

		if (query.equals("")) {
			Set<String> setMap = baseIndexer.keySet();
			Iterator<String> itSet = setMap.iterator();
			while (itSet.hasNext()) {
				Map<String, Position> raMap = baseIndexer.get(itSet.next());
				setList.add(raMap.keySet());
			}
		} else {
			Map<String, Position> raMap = baseIndexer.get(query);
			setList.add(raMap.keySet());
		}
		for (Set<String> set : setList) {
			Iterator<String> itRA = set.iterator();
			while (itRA.hasNext()) {
				result.add(itRA.next());
			}
		}

		return result;
	}
	
	public Set<String> getPlacesNames() {
		return currentSpace.getPlacesNames();
	}

	@Override
	public Position getPosition(String place, String rai) {
		return baseIndexer.get(place).get(rai);
	}
	
	public Place getPlace(String name) {
		return currentSpace.getPlace(name);
	}

	@Override
	public Collection<Place> getAllPlaces() {
		return currentSpace.getAllPlaces();
	}
	
	public Place get(ResourceAgent rA) {
		// TODO Auto-generated method stub
		return null;
	}

	public Place getLocal(Position position) {

		for (Place local : currentSpace.getAllPlaces()) {
			if (local.contains(position)) {
				return local;
			}
		}
		return null;
	}
	
	private ArrayList<Place> getNeighbors(Place current,
			ArrayList<Place> localList, ArrayList<Place> result) {
		ArrayList<Place> neighborList = new ArrayList<Place>();
		if (localList.size() != 0) {
			for (Place local : localList) {
				if (current.equalCorners(local) || current.equalSide(local)
						|| current.equalHeight(local)) {
					result.add(local);
					neighborList.add(local);
				}
			}
			localList.remove(current);
			for (Place local : neighborList) {
				result = getNeighbors(local, localList, result);
			}
		}
		return result;
	}
	
	public Space getMap() {
		return this.currentSpace;
	}

	public void insertMap(Space newSpace) {
		this.currentSpace = newSpace;
		
		Log.i("SmartAndroid", "New map setted. Height = " + newSpace.getHeight() + " Width = " + newSpace.getWidth());
	}
	
	public void setMap(Map<String, Place> newMap) {
		this.currentSpace.setPlaceMap(newMap);
	}

	public void registerInPlace(String url, Position position) {

		Place place = getLocal(position);
		HashMap<String, Position> rAMap = baseIndexer.get(place.getName());

		rAMap.put(url, position);
		resources.put(url, position);
	}

	public void registerInPlaceRelative(String url, Place place,
			Position position) {

		HashMap<String, Position> rAMap = baseIndexer.get(place.getName());
		float x = place.getLower().getX() + position.getX();
		float y = place.getLower().getY() + position.getY();

		Position rPos = new Position(x, y);
		rAMap.put(url, rPos);
		resources.put(url, rPos);
	}

	public void registerInPlaceMiddlePos(String url, Place place) {
		float x = Float.valueOf((place.getLower().getX() + place.getUpper().getX() / 2));
		float y = Float.valueOf((place.getLower().getY() + place.getUpper().getY() / 2));
		Position position = new Position(x, y);
		HashMap<String, Position> rAMap = baseIndexer.get(place.getName());
		rAMap.put(url, position);
		resources.put(url, position);
	}


	public ArrayList<String> queryByLocal(Position position) {
		List<Tuple<String, Position>> raList = new ArrayList<Tuple<String, Position>>();
		Set<String> setRai = resources.keySet();
		Iterator<String> itRai = setRai.iterator();
		while (itRai.hasNext()) {
			String rai = itRai.next();
			raList.add(new Tuple(rai, resources.get(rai)));
		}

		Sorter<Position> sorter = new Sorter<Position>(raList, position);
		return sorter.sort();
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
