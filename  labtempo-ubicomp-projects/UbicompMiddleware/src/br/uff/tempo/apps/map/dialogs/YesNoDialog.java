package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class YesNoDialog {

	private final AlertDialog.Builder alert;
	
	public YesNoDialog(String title, String message, String btnYes, String btnNo, Activity act, final YesNoGetter yng) {
		alert = new AlertDialog.Builder(act);

		alert.setMessage(message);
		alert.setTitle(title);

		alert.setPositiveButton(btnYes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				yng.onYesPressed();
			}
		});

		alert.setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				yng.onNoPressed();
			}
		});
		
		alert.show();
	}
}


