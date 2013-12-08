package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Sorter;
import br.uff.tempo.middleware.management.utils.Space;

/**
 * Class representing Resource Location Service.
 * 
 * This class is responsible for index and query from a ambient loaded Map
 */
public class ResourceLocation extends ResourceAgent implements IResourceLocation {

	private static final long serialVersionUID = 1L;
	
	private Space currentSpace;

	private static ResourceLocation instance;
	
	//<Place name, Area>
	HashMap<String, Place> map;
	//<RANS, position>
	HashMap<String, Position> resources;
	
	//<Place name, <RANS, position>>
	HashMap<String, HashMap<String, Position>> baseIndexer;
	
	
	private ResourceLocation() {
		super("ResourceLocation", ResourceLocation.class.getName(), IResourceLocation.rans);
		
		currentSpace = new Space();
		resources = new HashMap<String, Position>();
		baseIndexer = new HashMap<String, HashMap<String, Position>>();
	}
	
	/**
	 * Singleton instance that only SmartServer components can use
	 * @return reference to ResourceLocation
	 */
	public static ResourceLocation getInstance() {
		if (instance == null)
			instance = new ResourceLocation();
		return instance;
	}

	@Override
	public boolean identify() {
		String ip = SmartAndroid.getLocalIpAddress();
		int prefix = SmartAndroid.getLocalPrefix();
		
		ResourceAgentNS raNS = new ResourceAgentNS(this.getRANS(), ip, prefix);
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(raNS);
		ResourceRepository.getInstance().add(new ResourceData(this.getRANS(), this.getName(), this.getType(), null, null, raNS));	
		
		return true;
	}

	/**
	 * Add place to map structure
	 * @param name
	 * @param lower area position 
	 * @param upper area position
	 */
	public void addPlace(String name, Position lower, Position upper) {
		Place place = new Place(name, lower, upper);
		addPlace(place);
	}
	
	/**
	 * Add defined place to map structure
	 * @param place
	 */
	public void addPlace(Place place) {	
		currentSpace.addPlace(place);
		
		Log.i("SmartAndroid", "New place added: " + place.getName() + ". Bottom-left corner at = [" + place.getLower().getX() + " , " + place.getLower().getY() + "] and top-right corner at [" + place.getUpper().getX() + " , " + place.getUpper().getY() + "]");
	}

	/**
	 * Search for ARs in terms of Place
	 * @param query
	 * @return reference list of ARs
	 */
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
	
	/**
	 * Get a set of place references from loaded map
	 * @return set of place references from loaded map
	 */
	public Set<String> getPlacesNames() {
		return currentSpace.getPlacesNames();
	}

	@Override
	public Position getPosition(String place, String rai) {
		return baseIndexer.get(place).get(rai);
	}
	
	/**
	 * Get a Place instance of referred name
	 * @param name reference of a Place instance
	 * @return Place instance
	 */
	public Place getPlace(String name) {
		return currentSpace.getPlace(name);
	}

	@Override
	public Collection<Place> getAllPlaces() {
		return currentSpace.getAllPlaces();
	}

	/**
	 * Get a Place instance from indexed position
	 * @param position
	 * @return Place instance from indexed position
	 */
	public Place getLocal(Position position) {
		if (position != null) {
			for (Place local : currentSpace.getAllPlaces()) {
				if (local.contains(position)) {
					return local;
				}
			}
		}
		
		String msg = "Position [" + position + "] doesn't fit in any place!";
		Log.e("SmartAndroid", msg);
		//throw new SmartAndroidRuntimeException(msg);
		return null;
	}
	
	/**
	 * Get a sorted subset of Place instances from localList representing the neighbors from current Place instance
	 * @param current target of search for neighbors
	 * @param localList subset of Place instances to search
	 * @param result auxiliary subset to add sorted Place instances
	 * @return sorted subset of Place instances from localList representing the neighbors
	 */
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
	
	/**
	 * Get acces to Space map instance
	 * @return Space map instance;
	 */
	public Space getMap() {
		return this.currentSpace;
	}
	
	/**
	 * Insert newMap as Space map reference for this ResourceLocation instance
	 * @param newMap new Space map
	 */
	public void setMap(Space newMap) {
		this.currentSpace = newMap;
		
		for (Place place : currentSpace.getAllPlaces()) {
			baseIndexer.put(place.getName(), new HashMap<String, Position>());
		}
	}

	public void registerInPlace(String url, Position position) {
		Place place = getLocal(position);
		
		registerResource(url,place,position);
	}

	public void registerInPlaceRelative(String url, Place place, Position position) {
		float x = place.getLower().getX() + position.getX();
		float y = place.getLower().getY() + position.getY();

		Position rPos = new Position(x, y);
		
		registerResource(url, place, rPos);
	}

	public void registerInPlaceMiddlePos(String url, Place place) {
		float x = Float.valueOf((place.getLower().getX() + place.getUpper().getX() / 2));
		float y = Float.valueOf((place.getLower().getY() + place.getUpper().getY() / 2));
		Position position = new Position(x, y);
		
		registerResource(url, place, position);
	}
	
	private void registerResource(String url, Place place, Position position) {
		//get place entry to register new RA
		HashMap<String, Position> rAMap = baseIndexer.get(place.getName());
		rAMap.put(url, position);
		resources.put(url, position);
	}

	private void removeResource(String rans, Place place) {
		if (place != null) {
			HashMap<String, Position> rAMap = baseIndexer.get(place.getName());
			rAMap.remove(rans);
		}
	}
	
	private void moveResource(String rans, Place old, Place current, Position position){
		registerResource(rans, current, position);
		removeResource(rans, old);
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
	
	public void updateLocation(ResourceData resource){
		ResourceData oldResource = ResourceDirectory.getInstance().read(ResourceData.RANS, resource.getRans()).get(0);
		ResourceDirectory.getInstance().update(resource);
		Place oldPlace = oldResource.getPlace();
		Place newPlace = resource.getPlace();
		if (oldPlace != newPlace) {
			moveResource(resource.getRans(), oldPlace, newPlace, resource.getPosition());
			
			//It can be used by IPGAP to update MAP interface
			if (oldPlace != null) {
				notifyStakeholders("exit("+resource.getType()+")", oldResource.getPlace());
			}
			notifyStakeholders("enter("+resource.getType()+")", resource.getPlace());	
		}
		
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getLocalReference(Position position) {
		Place place = getLocal(position);
		return place.getName();
	}
}
                                                                                                                                                                                                                                                                                                                                                                 