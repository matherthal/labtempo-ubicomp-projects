package br.uff.tempo.apps.map.dialogs;

import android.app.Dialog;

/**
 * Interface to handle a dialog finishing
 * @author dbarreto
 */
public interface IDialogFinishHandler {

	/**
	 * Actions that will be executed when the dialog finish
	 * @param dialog The dialog that was finished
	 */
	void onDialogFinished(Dialog dialog);
}
