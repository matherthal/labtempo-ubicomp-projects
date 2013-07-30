package br.uff.tempo.apps;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.interfaces.ILamp;
import br.uff.tempo.middleware.resources.stubs.LampStub;

public class AppLampController extends Activity {
	private static final String TAG = "AppLampController";
	private IResourceDiscovery discovery;
	private ILamp lamp;
	private int count = 0;
	private Generic lampStakeholder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lamp_controller);

		if (savedInstanceState == null) {
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			List<ResourceData> lamps = discovery.search(ResourceData.TYPE, ResourceAgent.type(Lamp.class));
			ResourceData rdLamp = lamps.get(0);
			Toast.makeText(this, "Lâmpada encontrada", Toast.LENGTH_SHORT).show();
			lamp = new LampStub(rdLamp.getRans());

			// Subscription
			lampStakeholder = new Generic("Controlador de Lampada", "Controlador de Lampada", lamp, "isOn") {
				
				private static final long serialVersionUID = 1L;
				
				boolean lastVal = false;

				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
					
					boolean val = Boolean.valueOf(value.toString());
					// If it's really the lamp
					if (rai.equals(lamp.getRANS()) && method.equals("isOn"))
						// If value has changed
						if (count == 0 || lastVal != val) {
							lastVal = val;
							incCount();
						}
				}
			};
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_app_lamp_controller, menu);
		return true;
	}

	/*
	 * Button turn lamp on
	 */
	public void buttonTurnLampOn_Clicked(View view) {
		Log.i(TAG, "Turning lamp on");
		lamp.turnOn();
		Log.i(TAG, "Lamp turned on");
		Toast.makeText(this, "Lâmpada ligada", Toast.LENGTH_SHORT).show();
	}

	/*
	 * Button turn lamp off
	 */
	public void buttonTurnLampOff_Clicked(View view) {
		Log.i(TAG, "Turning lamp off");
		lamp.turnOff();
		Log.i(TAG, "Lamp turned off");
		Toast.makeText(this, "Lâmpada desligada", Toast.LENGTH_SHORT).show();
	}

	private Activity a = this;

	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			Toast.makeText(a, "Contador incrementado para " + count, Toast.LENGTH_LONG).show();
		}
	};

	private void incCount() {
		count++;
		Log.i(TAG, "Count inc to: " + count);
		// Toast.makeText(AppLampController.this, "Contador incrementado para "
		// + count, Toast.LENGTH_LONG).show();
		// TextView tv = (TextView) findViewById(R.id.textViewStatus);
		// tv.setText("Contador incrementado para " + count);
		mHandler.post(mUpdateResults);
	}
}
