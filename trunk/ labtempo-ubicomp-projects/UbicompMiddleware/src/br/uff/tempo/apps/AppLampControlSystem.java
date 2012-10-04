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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.comm.current.api.Tuple;
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
	private Map<String, Tuple<String, Object>> lampDictionary = new HashMap<String, Tuple<String, Object>>();
	private IPresenceSensor currPS = null;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String strMsg = b.getString("toastMessage");
			Toast.makeText(AppLampControlSystem.this, strMsg, Toast.LENGTH_LONG).show();
			// EditText et = (EditText) findViewById(R.id.editTextAboutLamps);
			// et.setText(et.getText() + "\n" + strMsg);
		}
	};
	
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
			populateSpinnerPresence();
			populateSpinnerBlockLamp();
		}
	}

	private void createEnvironment() {
		// Create a day light sensor
		DayLightSensor dl = new DayLightSensor("Sensor de Luminosidade");
		dl.identify();
		Log.i("DayLightSensor", "New DayLightSensor: " + dl.getRAI());
		TextView tv = (TextView) this.findViewById(R.id.textViewStatus);
		if (dl.isDay())
			tv.setText("É dia");
		else
			tv.setText("É noite");

		// For each place, create a Lamp and a Presence Sensor
		Set<String> places = location.getPlacesNames();
		if (places != null)
			for (String place : places) {
				// Create a lamp for this place
				String lampName = "Lâmpada " + place;
				Lamp l = new Lamp(lampName);
				// l.identify();
				l.identifyInPlace(place, null);
				final String lRAI = l.getRAI();
				Log.i("Lamp", "New Lamp: " + lRAI);

				// Put lamp in dictionary to populate the spinner
				while (lampDictionary.containsKey(lampName))
					// To avoid problems with names in dictionary
					lampName = lampName + ".";
				lampDictionary.put(lampName, new Tuple<String, Object>(lRAI, false));

				// Create a presence sensor for this place
				String psName = "Sensor de presença " + place;
				PresenceSensor ps = new PresenceSensor(psName);
				// ps.identify();
				ps.identifyInPlace(place, null);
				Log.i("PresenceSensor", "New PresenceSensor: " + ps.getRAI());

				// Put presence sensor in dictionary to populate the spinner
				while (psDictionary.containsKey(psName))
					// To avoid problems with names in dictionary
					psName = psName + ".";
				psDictionary.put(psName, ps.getRAI());

				// Create a rule that detects if the room has somebody
				RuleInterpreter riRoomFull = new RuleInterpreter("Detecta Sala Cheia - " + place);
				// Create a rule that detects if somebody has left the room and
				// 10 seconds has passed
				RuleInterpreter riRoomEmpty = new RuleInterpreter("Detecta Sala Vazia por 10seg - " + place);
				try {
					riRoomFull.setCondition(ps.getRAI(), PresenceSensor.CV_GETPRESENCE, null, Operator.Equal, true);
					riRoomFull.setCondition(dl.getRAI(), DayLightSensor.CV_ISDAY, null, Operator.Equal, false);

					riRoomEmpty.setCondition(ps.getRAI(), PresenceSensor.CV_GETPRESENCE, null, Operator.Equal, false);
					riRoomEmpty.setTimeout(10);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Error in setting condition to the rule");
				}
				riRoomFull.identify();
				riRoomEmpty.identify();

				// Rules' RAIs to be used by the action
				final String roomFullRAI = riRoomFull.getRAI();
				final String roomEmptyRAI = riRoomEmpty.getRAI();

				// Create action to subscribe to the rule interpreter
				Generic action = new Generic("Iluminador de caminho " + place) {
					
					private static final long serialVersionUID = 1L;
					
					private ILamp lamp;

					@Override
					public void notificationHandler(String rai, String method, Object value) {
						Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
						
						// Get lamp to turn on or off
						lamp = new LampStub(discovery.search(lRAI).get(0));
						//Verify if lamp is blocked
						String lName = lamp.getName();
						Tuple tp = lampDictionary.get(lName);
						if ((Boolean) tp.value) {
							Log.i(TAG, "Lamp " + lName + " is blocked");
							toastMessage("Lamp " + lName + " is blocked");
							return;
						}
						// Test rule's identification
						if (rai.equals(roomFullRAI))
							lamp.turnOn();
						else if (rai.equals(roomEmptyRAI))
							lamp.turnOff();
						else
							return;
						Log.i("Lamp", "RAI: " + lRAI + " / Is on: " + lamp.isOn());
						if (lamp.isOn())
							toastMessage("Lâmpada " + lamp.getName() + " ligada");
						else
							toastMessage("Lâmpada " + lamp.getName() + " desligada");
					}
				};
				action.identify();
				// Subscribing this action to both rules
				riRoomFull.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getRAI());
				riRoomEmpty.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getRAI());
			}
	}

	private void populateSpinnerPresence() {
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

	private void populateSpinnerBlockLamp() {
		Spinner s = (Spinner) findViewById(R.id.spinnerBlockLamp);
		String[] array_spinner = new String[lampDictionary.size()];
		int counter = 0;
		for (String ps : lampDictionary.keySet()) {
			array_spinner[counter] = ps;
			counter++;
		}
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
		s.setAdapter(adapter);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String lName = parentView.getItemAtPosition(position).toString();
				Tuple tp = lampDictionary.get(lName);
				String lRAI = tp.key;
				boolean blocked = (Boolean) tp.value;

				String msg = "";
				if (!blocked) {
					lampDictionary.remove(lName);
					tp.value = true;
					lampDictionary.put(lName, tp);
					msg = "Lamp " + lName + " is blocked";
					Log.i("Spinner", msg);
					toastMessage(msg);
				} else {
					lampDictionary.remove(lName);
					tp.value = false;
					lampDictionary.put(lName, tp);
					msg = "Lamp " + lName + " is NOT blocked";
					Log.i("Spinner", msg);
					toastMessage(msg);
				}
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
}