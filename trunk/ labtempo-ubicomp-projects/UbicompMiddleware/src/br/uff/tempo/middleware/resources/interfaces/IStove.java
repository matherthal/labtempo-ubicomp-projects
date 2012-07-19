package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

/**
 * This interface contains the common operations of a Stove
 * 
 * @author dbarreto, dmareli, merthal
 * 
 */
public interface IStove extends IResourceAgent {

	/********************
	 * CONTEXT VARIABLES *
	 **********************/

	/**
	 * Verifies if the stove burners or oven or any stove function is on.
	 * 
	 * @return <code>true</code> if the stove burners or oven or any stove
	 *         function is on; <code>false</code> otherwise.
	 */
	public boolean isOn();

	/**
	 * Get burner number <code>burnerIndex</code>'s temperature
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 */
	@ContextVariable(name = "Temperatura boca", type = CVType.Temperature)
	public float getBurnerTemperature(int burnerIndex);

	/**
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 * @return <code>true</code> if the burner number <code>burnerIndex</code>
	 *         is on; <code>false</code> otherwise.
	 */
	@ContextVariable(name = "Boca Ligada", description = "", type = CVType.On)
	public boolean isOnBurner(int burnerIndex);

	/**
	 * Returns the temperature of the oven.
	 * 
	 * @return The actual oven temperature
	 */
	@ContextVariable(name = "Temperatura forno", type = CVType.Temperature)
	public float getOvenTemperature();

	/**
	 * Returns if the oven is on or not
	 * 
	 * @return <code>true</code> if the ove is on; <code>false</code> otherwise.
	 */
	@ContextVariable(name = "Boca Ligada", description = "", type = CVType.On)
	public boolean isOnOven();

	/**
	 * Returns the amount of gas leaking
	 */
	@ContextVariable(name = "Vazamento de gás", description = "", type = CVType.On)
	public float getGasLeak();

	/***********
	 * SERVICES *
	 *************/

	/**
	 * Turns the burner number <code>burnerIndex</code> on.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner.
	 */
	@Service(name = "Ligar boca")
	public void turnOnBurner(int burnerIndex);

	/**
	 * Turns the burner number <code>burnerIndex</code> off.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 */
	@Service(name = "Desligar boca")
	public void turnOffBurner(int burnerIndex);

	/**
	 * Verify if the oven is on
	 * 
	 * @return <code>true</code> if the oven is on; <code>false</code>
	 *         otherwise.
	 */
	public boolean isOvenOn();

	/**
	 * Sets the oven temperature to <code>newTemperature</code>. Notice that
	 * setting a new temperature, the oven will be turned on.
	 * 
	 * @param newTemperature
	 *            The new oven temperature.
	 */
	@Service(name = "Definir temperatuda do forno")
	public void setOvenTemperature(float newTemperature);

	/**
	 * Turns the oven off.
	 */
	@Service(name = "Deligar forno")
	public void turnOffOven();
}
