package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceLocation extends ResourceAgent implements IResourceLocation {

	private ResourceRepository rR;

	private static ResourceLocation instance;

	HashMap<String, Place> map;
	HashMap<String, HashMap<String, Position>> base;
		
	private ResourceLocation() {
		setId(3);
		setURL("br.uff.tempo.middleware.management.ResourceLocation");
		setName("ResourceLocation");
		setType("management");

		setURL(ResourceAgentIdentifier.generateRAI(ResourceAgentIdentifier.getLocalIpAddress(), "br.uff.tempo.middleware.management.ResourceLocation", "ResourceLocation"));

		map = new HashMap<String, Place>();
		base = new HashMap<String, HashMap<String, Position>>();
		ResourceContainer.getInstance().add(this);
		loadBase();
	}
	
	
	//
	//
	/**
	 * carrega mapa com lugares baseados na interface do MapaDaCasa
	 * áreas da casa e dos cômodos foram estimadas
	 * Área da casa: 100x50
	 */
	public void loadBase()
	{
		addPlace("Hall", new Position(0,0), new Position(50,25));		
		addPlace("Bathroom", new Position(50,0), new Position(70,20));
		addPlace("Storeroom", new Position(70,0), new Position(100,20));
		addPlace("Corridor", new Position(50,20), new Position(100,25));
		addPlace("Bedroom1", new Position(0,25), new Position(30,50));
		addPlace("Bedroom2", new Position(30,25), new Position(60,50));
		addPlace("Kitchen", new Position(60,25), new Position(100,50));
	}
	
	public void addPlace(String name, Position lower, Position upper) {
		Place place = new Place(name, lower, upper);
		//place.identify();
		map.put(place.getName(), place);
		base.put(place.getName(), new HashMap<String, Position>());
	}
	

	public static ResourceLocation getInstance() {
		if (instance == null)
			instance = new ResourceLocation();
		return instance;
	}

//	public ArrayList<Place> search(ResourceAgent rA) {
//		ArrayList<Place> result = new ArrayList<Place>();
//		Place current = get(rA);
//		result.add(0, current);// current local is the nearest
//		ArrayList<Place> localList = (ArrayList<Place>) map.values();
//		result = getNeighbors(current, localList, result);
//		return result;
//	}
	
	public ArrayList<String> search(String query) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Set<String>> setList = new ArrayList<Set<String>>();
		
		if (query.equals("")) {
			Set<String> setMap = base.keySet();
			Iterator<String> itSet = setMap.iterator();
			while(itSet.hasNext()) {
				Map<String,Position> raMap = base.get(itSet.next());
				setList.add(raMap.keySet());
			}
		} else {
			Map<String,Position> raMap = base.get(query);
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

	private ArrayList<Place> getNeighbors(Place current, ArrayList<Place> localList, ArrayList<Place> result) {
		ArrayList<Place> neighborList = new ArrayList<Place>();
		if (localList.size() != 0) {
			for (Place local : localList) {
				if (current.equalCorners(local) || current.equalSide(local) || current.equalHeight(local)) {
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

	public Place get(ResourceAgent rA) {
		// TODO Auto-generated method stub
		return null;
	}

	public Place getLocal(Position position) {
		
		for (Place local : map.values())
		{
			if (local.contains(position)){
				return local;
			}
		}
		return null;
	}
	
	public void setMap(HashMap<String, Place> map) {
		this.map = map;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}
	
	

	public void registerInPlace(String url, Position position) {
		Place place = getLocal(position);
		HashMap<String, Position> rAMap = base.get(place.getName());
		rAMap.put(url, position);
	}
	
	public Set<String> listLocations()
	{
		return map.keySet();
	}


	@Override
	public Position getPosition(String place, String rai) {
		return base.get(place).get(rai);
	}
	
	
}
