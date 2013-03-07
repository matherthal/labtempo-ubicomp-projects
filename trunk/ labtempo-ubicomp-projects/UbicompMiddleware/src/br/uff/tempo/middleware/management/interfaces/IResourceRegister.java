package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.utils.Position;

/**
 * Resource Register Service (RRS) interface
 * <br /><br />
 * It enables use of RRS in SmartServer  
 */
public interface IResourceRegister {
	
	/**
	 * Unique system rans of RLS
	 */
	public static final String rans = "resourceregister.ra";
	
	/**
	 * Register representative data of Resource Agent
	 * @param resourceData representative data of Resource Agent
	 * @return if it is succesful
	 */
	public boolean register(ResourceData resourceData);
	
	/**
	 * Register representative data of Resource Agent with known position
	 * @param position known position
	 * @param resourceData representative data of Resource Agent
	 * @return if it is succesful
	 */
	public boolean registerLocation(Position position, ResourceData resourceData);
	
	/**
	 * Register representative data of Resource Agent with known place name
	 * @param placeName known place name
	 * @param position known position (can be null)
	 * @param resourceData representative data of Resource Agent
	 * @return if it is succesful
	 */
	public boolean registerInPlace(String placeName, Position position, ResourceData resourceData);
	
	/**
	 * Remove representative data of ResourceAgent referenced by specified rans
	 * @param rans reference of ResourceAgent RANS 
	 * @return if it is succesful
	 */
	public boolean unregister(String rans);

}
