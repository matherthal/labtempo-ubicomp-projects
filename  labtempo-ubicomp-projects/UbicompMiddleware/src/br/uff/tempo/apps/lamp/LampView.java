package br.uff.tempo.apps.lamp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.Lamp;

public class LampView extends Activity /* implements Observer */{

	private static final String TAG = "LampView";
	private Lamp lamp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.lamp = new Lamp("Lampada Quarto");
		this.lamp.identify();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.lamp);
	}

	/*
	 * public void onBurnerIntensityChanged (int burner, int intensity) {
	 * //this.bedData.setBurnerIntensity(burner, intensity); }
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onRegisterClick(View target) {
		Toast.makeText(LampView.this, "Register Called", Toast.LENGTH_LONG).show();
	}

	public Lamp getLampState() {
		return lamp;
	}

	/*
	 * //Called when external representation of stove change its state
	 * //@Override public void update(Observable observable, Object data) {
	 * 
	 * //received a change in the stove burners state from external stove if
	 * (data instanceof BedData) { BedData sd = (BedData) data;
	 * 
	 * Toast.makeText(BedView.this, "Received a change state!",
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * for (int i=0; i < sd.getBurners().length; i++) {
	 * onBurnerIntensityChanged(i, sd.getBurnerIntensity(i)); } } }
	 */
}