package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceData;


/**
 * This interface contains Place Agent methods
 * <br /><br />
 * This element contains methods that indicates access by agents in Place
 */
public interface IPlace {
	
	/**
	 * It indicates that an agent enters in this
	 * @param raData ResourceAgent data that enters
	 */
	public void enter(ResourceData raData);
	
	/**
	 * It indicates that an agent exits in this
	 * @param raData ResourceAgent data that exits
	 */
	public void exit(ResourceData raData);

}
