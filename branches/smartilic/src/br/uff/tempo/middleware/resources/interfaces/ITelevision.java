package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ITelevision extends IResourceAgent {

	/**
	 * Shows a message in the TV screen
	 * 
	 * @param text
	 *            The message that will be showed in the screen
	 */
	public void showMessage(String text);
	
	/**
	 * Gets the last TV message
	 * @return
	 */
	public String getMessage();

	/**
	 * Gets the current TV channel
	 * 
	 * @return An integer that represents the channel
	 */
	public int getChannel();

	/**
	 * Changes the current TV channel to <code>channel</code>
	 * 
	 * @param channel
	 *            An integer that represents the new channel
	 */
	public void setChannel(int channel);

	/**
	 * Increases the current TV channel by one.
	 */
	public void incChannel();

	/**
	 * Decreases the current TV channel by one.
	 */
	public void decChannel();

	/**
	 * Tell us if the TV is on or not
	 * 
	 * @return <code>true</code> if the TV is on; <code>false</code> otherwise.
	 */
	public boolean isOn();

	/**
	 * Turns the TV on or off, according to the parameter
	 * 
	 * @param on
	 *            <code>true</code> to turn the TV on; <code>false</code> to
	 *            turn the TV off
	 */
	public void setOn(boolean on);

	/**
	 * Get the current TV volume
	 * 
	 * @return An int that represents the current volume
	 */
	public int getVolume();

	/**
	 * Define a new volume to TV
	 * 
	 * @param volume
	 *            The new volume
	 */
	public void setVolume(int volume);

	/**
	 * Increases the TV volume in <code>inc</code> units
	 * 
	 * @param inc
	 *            The unit that the TV volume will be increased
	 */
	public void incVolume(int inc);

	/**
	 * Decreases the TV volume in <code>dec</code> units
	 * 
	 * @param dec
	 *            The unit that the TV volume will be decreased
	 */
	public void decVolume(int dec);
}
