package br.uff.tempo.apps.map.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View.OnClickListener;
import br.uff.tempo.apps.map.MapActivity;

public abstract class MapDialog implements OnCancelListener, OnClickListener {

	protected Dialog dialog;
	protected MapActivity activity;
	protected boolean finished = false;

	public MapDialog(final Activity activity, final int layout, final int titleString) {

		this.activity = (MapActivity) activity;

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				dialog = new Dialog(activity);

				dialog.setContentView(layout);
				dialog.setTitle(titleString);

				dialog.setOnCancelListener(MapDialog.this);
			}
		});
	}

	public void showDialog() {

		dialog.show();
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		this.finished = true;
	}

	public boolean isFinished() {
		return this.finished;
	}

}
