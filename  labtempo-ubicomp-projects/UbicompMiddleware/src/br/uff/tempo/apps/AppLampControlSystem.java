package br.uff.tempo.apps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.resources.DayLightSensor;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.PresenceSensor;
import br.uff.tempo.middleware.resources.interfaces.IDayLightSensor;
import br.uff.tempo.middleware.resources.interfaces.ILamp;
import br.uff.tempo.middleware.resources.interfaces.IPresenceSensor;
import br.uff.tempo.middleware.resources.stubs.DayLightSensorStub;
import br.uff.tempo.middleware.resources.stubs.LampStub;
import br.uff.tempo.middleware.resources.stubs.PresenceSensorStub;

public class AppLampControlSystem extends Activity {
	private static final String TAG = "AppLampController";
	private IResourceDiscovery discovery;
	private IResourceLocation location;
	private Map<String, String> psDictionary = new HashMap<String, String>();
	private IPresenceSensor currPS = null;

	// private Set<Lamp> lampSet = new HashSet<Lamp>();
	// private Set<PresenceSensor> psSet = new HashSet<PresenceSensor>();
	// private Set<Generic> actionSet = new HashSet<Generic>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lamp_control_system);

		if (savedInstanceState == null) {
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
			location = new ResourceLocationStub(discovery.search("ResourceLocation").get(0));

			createEnvironment();
			populateSpinner();
		}
	}

	private void createEnvironment() {
		// Create a day light sensor
		DayLightSensor dl = new DayLightSensor("Sensor de Luminosidade");
		dl.identify();
		Log.i("DayLightSensor", "New DayLightSensor: " + dl.getURL());
		TextView tv = (TextView) this.findViewById(R.id.textViewStatus);
		if (dl.isDay())
			tv.setText("É dia");
		else
			tv.setText("É noite");

		// For each place, create a Lamp and a Presence Sensor
		Set<String> places = location.listLocations();
		if (places != null)
			for (String place : places) {
				// Create a lamp for this place
				Lamp l = new Lamp("Lâmpada " + place);
				// l.identify();
				l.identifyInPlace(place, null);
				final String lRAI = l.getURL();
				Log.i("Lamp", "New Lamp: " + lRAI);

				// Put lamp in set to keep link
				// lampSet.add(l);

				// Create a presence sensor for this place
				String psName = "Sensor de presença " + place;
				PresenceSensor ps = new PresenceSensor(psName);
				// ps.identify();
				ps.identifyInPlace(place, null);
				Log.i("PresenceSensor", "New PresenceSensor: " + ps.getURL());

				// Put presence sensor in set to keep link
				// psSet.add(ps);

				// Put presence sensor in dicionary to populate the spinner
				while (psDictionary.containsKey(psName))
					// To avoid problema with nomes in dictionary
					psName = psName + ".";
				psDictionary.put(psName, ps.getURL());

				// Create a rule that detects if the room has somebody
				RuleInterpreter riRoomFull = new RuleInterpreter("Detecta Sala Cheia - " + place);
				// Create a rule that detects if somebody has left the room and
				// 10 seconds has passed
				RuleInterpreter riRoomEmpty = new RuleInterpreter("Detecta Sala Vazia por 10seg - " + place);
				try {
					riRoomFull.setCondition(ps.getURL(), PresenceSensor.CV_GETPRESENCE, null, Operator.Equal, true);
					riRoomFull.setCondition(dl.getURL(), DayLightSensor.CV_ISDAY, null, Operator.Equal, false);

					riRoomEmpty.setCondition(ps.getURL(), PresenceSensor.CV_GETPRESENCE, null, Operator.Equal, false);
					riRoomEmpty.setTimeout(10);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Error in setting condition to the rule");
				}
				riRoomFull.identify();
				riRoomEmpty.identify();

				// Rules' RAIs to be used by the action
				final String roomFullRAI = riRoomFull.getURL();
				final String roomEmptyRAI = riRoomEmpty.getURL();

				// Create action to subscribe to the rule interpreter
				Generic action = new Generic("Iluminador de caminho " + place) {
					private ILamp lamp;

					@Override
					public void notificationHandler(String rai, String method, Object value) {
						Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
						
						// Get lamp to turn on or off
						lamp = new LampStub(discovery.search(lRAI).get(0));
						// Test rule's identification
						if (rai.equals(roomFullRAI))
							lamp.turnOn();
						else if (rai.equals(roomEmptyRAI))
							lamp.turnOff();
						else
							return;
						Log.i("Lamp", "RAI: " + lRAI + " / Is on: " + lamp.isOn());
						if (lamp.isOn())
							toastMessage("Lâmpada " + lRAI + " ligada");
						else
							toastMessage("Lâmpada " + lRAI + " desligada");
					}
				};
				action.identify();
				// Subscribing this action to both rules
				riRoomFull.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getURL());
				riRoomEmpty.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getURL());
				// actionSet.add(action);
			}
		// for (String l : discovery.search("Lamp"))
		// Log.i("LAMP", l);
		// for (String p : discovery.search("PresenceSensor"))
		// Log.i("PRESENCESENSOR", p);
		// for (String g : discovery.search("Generic"))
		// Log.i("GENERIC", g);
		// for (String r : discovery.search("RuleInterpreter"))
		// Log.i("RULE", r);
	}

	private void populateSpinner() {
		Spinner s = (Spinner) findViewById(R.id.spinnerPresenceSensors);
		String[] array_spinner = new String[psDictionary.size()];
		int counter = 0;
		for (String ps : psDictionary.keySet()) {
			array_spinner[counter] = ps;
			counter++;
		}
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
		s.setAdapter(adapter);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String psRAI = psDictionary.get(parentView.getItemAtPosition(position).toString());

				if (currPS != null) {
					currPS.setPresence(false);
					Log.i("Spinner", "Set presence sensor " + currPS.getName() + " to false. Presence = " + currPS.getPresence());
				}
				currPS = new PresenceSensorStub(psRAI);
				currPS.setPresence(true);
				Log.i("Spinner", "Set presence sensor " + currPS.getName() + " to true. Presence = " + currPS.getPresence());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
	}

	/*
	 * Button toggle between day and night
	 */
	public void buttonToggleDayNight_Clicked(View view) {
		String rai = discovery.search("DayLightSensor").get(0);
		IDayLightSensor dl = new DayLightSensorStub(rai);
		if (dl.isDay())
			dl.setDay(false);
		else
			dl.setDay(true);
		TextView tv = (TextView) this.findViewById(R.id.textViewStatus);
		if (dl.isDay())
			tv.setText("É dia");
		else
			tv.setText("É noite");
	}

	private void toastMessage(String strMsg) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString("toastMessage", strMsg);
		msg.setData(b);
		// send message to the handler with the current message handler
		handler.sendMessage(msg);
	}

	private Activity a = this;
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String strMsg = b.getString("My Key");
			Toast.makeText(a, strMsg, Toast.LENGTH_LONG).show();
		}
	};
}
