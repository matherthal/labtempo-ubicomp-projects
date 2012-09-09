package br.uff.tempo.middleware.management;

import java.util.List;

import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;

public class Person extends ResourceAgent {

	List<SmartObject> objects;
	List<Place> recentLocal;
	List<Position> recentPositions;
	IResourceLocation rLS;
	public Person(List<SmartObject> sensors, List<Position> recentPositions, String name, String type, int id){
		super(name, type, id);
		this.objects = sensors;
		this.recentPositions = recentPositions;		
	}
	
	public void updateRecentLocal()
	{
		rLS = new ResourceLocationStub(this.getRDS().search("ResourceLocation").get(0));
		for (Position position : recentPositions)
		{
			recentLocal.add(rLS.getLocal(position));
		}
	}
	
	//list is in reverse order
	public SmartObject getSensor(int i)
	{
		return objects.get(objects.size()-(i+1));
	}
	
	//return preview sensor
	public SmartObject setSensor(int i, SmartObject sensor){
		return objects.set(objects.size()-(i+1), sensor);
	}
	
	public void addSensor(SmartObject sensor)
	{
		objects.add(sensor);
	}
	
	public boolean removeSensor(SmartObject sensor)
	{
		return objects.remove(sensor);
	}
	
	public Position getPosition(int i)
	{
		return recentPositions.get(objects.size()-(i+1));
	}
	
	public Position getCurrentPosition()
	{
		return getPosition(0);
	}
	
	//return preview sensor
	public Position setPosition(int i, Position position){
		return recentPositions.set(recentPositions.size()-(i+1), position);
	}
	
	public void addPosition(Position position)
	{
		recentPositions.add(position);
	}
	
	public boolean removePosition(Position position)
	{
		return recentPositions.remove(position);
	}	
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}
}
