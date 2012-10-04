package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.SmartObject;
import br.uff.tempo.middleware.management.utils.Position;

public interface IPerson extends IResourceAgent {
	
	public SmartObject setSensor(int i, SmartObject sensor);

	public void addSensor(SmartObject sensor);

	public boolean removeSensor(SmartObject sensor);

	public Position getPosition(int i);

	public Position getCurrentPosition();

	public void addPosition(Position position);
	
	public boolean removePosition(Position position);

}
