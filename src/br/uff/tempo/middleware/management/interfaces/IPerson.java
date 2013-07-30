package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.SmartObject;
import br.uff.tempo.middleware.management.utils.Position;

/**
 * This interface contains Person Agent methods
 * <br /><br />
 * Person agent type contains methods that manage mobility and aggregate body sensors 
 */
public interface IPerson extends IResourceAgent {
	
	/**
	 * Set a body sensor in person sensor list
	 * @param i index in list
	 * @param sensor instance of body sensor
	 * @return added sensor
	 */
	public SmartObject setSensor(int i, SmartObject sensor);

	/**
	 * Add a body sensor in person sensor list
	 * @param sensor instance of body sensor
	 */
	public void addSensor(SmartObject sensor);

	/**
	 * Remove a body sensor in person sensor list
	 * @param sensor instance of body sensor
	 * @return removed body sensor
	 */
	public boolean removeSensor(SmartObject sensor);
	
	/**
	 * @return current instance position
	 */
	public Position getPosition();
	
	/**
	 * Get a position in recent positions list 
	 * @param i index in recent positions list
	 * @return i-th position in list
	 */
	public Position getPositionIndex(int i);

	/**
	 * @return current instance position
	 */
	public Position getCurrentPosition();
	
	/**
	 * Append a position in recent positions list
	 * @param position new position
	 */
	public void addPosition(Position position);
	
	/**
	 * Remove a specified position in list
	 * @param position specified position
	 * @return removed position
	 */
	public boolean removePosition(Position position);

}
