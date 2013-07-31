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
	@ContextVariable(name = "Ligado-Desligado", description = "", type = "OnOff")
	public boolean isOn();

	/**
	 * Get burner number <code>burnerIndex</code>'s temperature
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 */
	@ContextVariable(name = "Temperatura boca", type = "BurnerTemperature")
	public float getBurnerTemperature(int burnerIndex);

	/**
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 * @return <code>true</code> if the burner number <code>burnerIndex</code>
	 *         is on; <code>false</code> otherwise.
	 */
	@ContextVariable(name = "Boca Ligada-Desligada", description = "", type = "BurnerOnOff")
	public boolean isOnBurner(int burnerIndex);

	/**
	 * Returns the temperature of the oven.
	 * 
	 * @return The actual oven temperature
	 */
	@ContextVariable(name = "Temperatura forno", type = "OvenTemperature")
	public float getOvenTemperature();

	/**
	 * Returns the amount of gas leaking
	 */
	@ContextVariable(name = "Vazamento de gás", description = "", type = "GasLeak")
	public float getGasLeak();

	/**
	 * Verify if the oven is on
	 * 
	 * @return <code>true</code> if the oven is on; <code>false</code>
	 *         otherwise.
	 */
	@ContextVariable(name = "Forno Ligado-Desligado", description = "", type = "OvenOnOff")
	public boolean isOvenOn();

	/***********
	 * SERVICES *
	 *************/

	/**
	 * Turns the burner number <code>burnerIndex</code> on.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner.
	 */
	@Service(name = "Ligar boca", type = "TurnBurnerOnOff")
	public void turnOnBurner(int burnerIndex);

	/**
	 * Turns the burner number <code>burnerIndex</code> off.
	 * 
	 * @param burnerIndex
	 *            The index of the stove burner
	 */
	@Service(name = "Desligar boca", type = "TurnBurnerOnOff")
	public void turnOffBurner(int burnerIndex);

	/**
	 * Sets the oven temperature to <code>newTemperature</code>. Notice that
	 * setting a new temperature, the oven will be turned on.
	 * 
	 * @param newTemperature
	 *            The new oven temperature.
	 */
	@Service(name = "Definir temperatuda do forno", type = "SetOvenTemperature")
	public void setOvenTemperature(float newTemperature);

	/**
	 * Turns the oven off.
	 */
	@Service(name = "Desligar forno", type = "TurnOvenOnOff")
	public void turnOffOven();

	/**
	 * Turns the oven on.
	 */
	@Service(name = "Ligar forno", type = "TurnOvenOnOff")
	public void turnOnOven();
}
