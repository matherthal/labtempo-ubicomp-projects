package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

public class InputDialog {

	private final AlertDialog.Builder alert;
	private final EditText input;
	
	public InputDialog(String title, String message, Activity act, final InputTextGetter itg) {
		alert = new AlertDialog.Builder(act);

		alert.setMessage(message);
		alert.setTitle(title);

		// Set an EditText view to get user input 
		input = new EditText(act);
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				itg.onInputText(input.getText().toString());
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		
		alert.show();
	}
}


