package br.uff.tempo.apps.map.dialogs;

import android.app.Dialog;

public interface IResourceChooser {

	void onRegisteredResourceChoosed(String resourceRAI);
	void onDialogFinished(Dialog dialog);
}
