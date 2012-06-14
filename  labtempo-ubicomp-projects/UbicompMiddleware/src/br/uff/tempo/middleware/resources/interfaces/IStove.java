package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IStove extends IResourceAgent {

	/**
	 * Verifies if the stove burners or oven or any stove function is on.
	 * 
	 * @return <code>true</code> if the stove burners or oven or any stove
	 *         function is on; <code>false</code> otherwise.
	 */

	public boolean isOn();

	/**
	 * Turns the burner number <code>burnerIndex</code> on.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner.
	 */

	public void turnOnBurner(int burnerIndex);

	/**
	 * Turns the burner number <code>burnerIndex</code> off.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 */

	public void turnOffBurner(int burnerIndex);

	/**
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 * @return <code>true</code> if the burner number <code>burnerIndex</code>
	 *         is on; <code>false</code> otherwise.
	 */

	public boolean isOnBurner(int burnerIndex);

	/**
	 * Sets the oven temperature to <code>newTemperature</code>. Notice that
	 * setting a new temperature, the oven will be turned on.
	 * 
	 * @param newTemperature
	 *            The new oven temperature.
	 */

	public void setOvenTemperature(float newTemperature);

	/**
	 * Returns the temperature of the oven.
	 * 
	 * @return The actual oven temperature
	 */

	public float getOvenTemperature();

	/**
	 * Turns the oven off.
	 */

	public void turnOffOven();

}
