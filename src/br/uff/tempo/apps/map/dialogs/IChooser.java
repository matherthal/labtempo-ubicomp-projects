package br.uff.tempo.apps.map.dialogs;

/**
 * Interface IChooser used to handle the operation of choosing an item in the dialog
 * @author dbarreto
 */
public interface IChooser {

	/**
	 * Handle what happen when an item is selected from a Dialog
	 * @param chosenData
	 */
	void onResourceChosen(ChosenData chosenData);
}
