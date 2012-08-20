package br.uff.tempo.middleware.management;

import java.util.List;

import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;

public class Person extends ResourceAgent{

	List<SmartObject> sensors;
	List<Local> recentLocal;
	List<Position> recentPositions;
	IResourceLocation rLS;
	public Person(List<SmartObject> sensors, List<Position> recentPositions, String name, String type, int id){
		super(name, type, id);
		this.sensors = sensors;
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
		return sensors.get(sensors.size()-(i+1));
	}
	
	//return preview sensor
	public SmartObject setSensor(int i, SmartObject sensor){
		return sensors.set(sensors.size()-(i+1), sensor);
	}
	
	public void addSensor(SmartObject sensor)
	{
		sensors.add(sensor);
	}
	
	public boolean removeSensor(SmartObject sensor)
	{
		return sensors.remove(sensor);
	}
	
	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

}
