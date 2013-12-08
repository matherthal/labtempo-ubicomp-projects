package br.uff.tempo.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import br.uff.tempo.apps.simulators.tracking.TrackingView;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IPerson;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.PersonStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;
import br.uff.tempo.middleware.management.utils.Stakeholder;
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

public class AppLampControlSystemTracking extends TrackingView {
	private static final String TAG = "AppLampController";
	private IResourceDiscovery discovery;
	private IResourceLocation location;
	private Map<String, String> psDictionary = new HashMap<String, String>();
	private Map<String, Tuple<String, Object>> lampDictionary = new HashMap<String, Tuple<String, Object>>();
	private IPresenceSensor currPS = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_app_lamp_control_system);
		
		// Add first person
		super.panel.addPerson();
		
		if (savedInstanceState == null) {
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			location = new ResourceLocationStub(IResourceLocation.rans);

			createEnvironment();
			//populateSpinnerPresence();
			//populateSpinnerBlockLamp();
		}
	}

	private void createEnvironment() {
		// Create a day light sensor
		DayLightSensor dl = new DayLightSensor("Sensor de Luminosidade", "Sensor de Luminosidade");
		dl.identify();
		Log.i("DayLightSensor", "New DayLightSensor: " + dl.getRANS());
		// Set night
		dl.setDay(false);
		
		// For each place, create a Lamp and a Presence Sensor
		Set<String> places = location.getPlacesNames();

		// Pessoa
		//Person person = new Person("Matheus", "Matheus");
		//person.identifyInPlace(places.iterator().next(), null);
		Iterator<ResourceData> itperson = discovery.search(ResourceData.TYPE, "Person").iterator();
		IPerson person;
		if (itperson.hasNext())
			person = new PersonStub(itperson.next().getRans());
		else {
			Log.i(TAG, "Nenhuma pessoa registrada no ambiente");
			return;
		}
		
		if (places != null) {
			BufferedReader in = null;
			String roomFullRule;
			String roomEmptyRule;
			try {
				roomFullRule = readFile(this.getAssets().open("interpreter_smartlic_roomfull.json"));
				roomEmptyRule = readFile(this.getAssets().open("interpreter_smartlic_roomempty.json"));
			} catch (IOException e1) {
				Log.e(TAG, "File with rules could not be found or opened");
				e1.printStackTrace();
				return;
			}
						
			for (String place : places) {
				// Create a lamp for this place
				String lampName = "Lâmpada " + place;
				Lamp l = new Lamp(lampName, lampName);
				// l.identify();
				l.identifyInPlace(place, null);
				final String lRAI = l.getRANS();
				Log.i("Lamp", "New Lamp: " + lRAI);

				// Put lamp in dictionary to populate the spinner
				while (lampDictionary.containsKey(lampName))
					// To avoid problems with names in dictionary
					lampName = lampName + ".";
				lampDictionary.put(lampName, new Tuple<String, Object>(lRAI, false));

				// Create a presence sensor for this place
				String psName = "Sensor de presença " + place;
				PresenceSensor ps = new PresenceSensor(psName, psName);
				// ps.identify();
				ps.identifyInPlace(place, null);
				// subscribe the person to the presence sensor simulator
				person.registerStakeholder("updateLocation", ps.getRANS());
				Log.i("PresenceSensor", "New PresenceSensor: " + ps.getRANS());

				// Put presence sensor in dictionary to populate the spinner
				while (psDictionary.containsKey(psName))
					// To avoid problems with names in dictionary
					psName = psName + ".";
				psDictionary.put(psName, ps.getRANS());

				// Create a rule that detects if the room has somebody
				RuleInterpreter riRoomFull = new RuleInterpreter("Detecta Sala Cheia - " + place, "Detecta Sala Cheia - " + place);
				// Create a rule that detects if somebody has left the room and
				// 10 seconds has passed
				RuleInterpreter riRoomEmpty = new RuleInterpreter("Detecta Sala Vazia por 10seg - " + place, "Detecta Sala Vazia por 10seg - " + place);
				try {
					riRoomFull.setExpression(roomFullRule.replace("_PRESENCE_", ps.getRANS()).replace("_DAYLIGHT_", dl.getRANS()));
					riRoomEmpty.setExpression(roomEmptyRule.replace("_PRESENCE_", ps.getRANS()));
					
					riRoomFull.identify();
					riRoomEmpty.identify();
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Error in setting condition to the rule");
				}

				// Rules' RAIs to be used by the action
				final String roomFullRAI = riRoomFull.getRANS();
				final String roomEmptyRAI = riRoomEmpty.getRANS();

				// Create action to subscribe to the rule interpreter
				Generic action = new Generic("Iluminador de caminho " + place, "Iluminador de caminho " + place) {

					private static final long serialVersionUID = 1L;

					private ILamp lamp;

					@Override
					public void notificationHandler(String rai, String method, Object value) {
						Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);

						// Get lamp to turn on or off
						lamp = new LampStub(discovery.search(ResourceData.RANS, lRAI).get(0).getRans());
						// Verify if lamp is blocked
						String lName = lamp.getName();
						Tuple tp = lampDictionary.get(lName);
						if ((Boolean) tp.value) {
							Log.i(TAG, "Lamp " + lName + " is blocked");
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
					}
					
				};
				
				action.identify();
				// Subscribing this action to both rules
				riRoomFull.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getRANS());
				riRoomEmpty.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, action.getRANS());
			}
		}
	}

	private String readFile(InputStream stream) {
		String fileStr = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(stream));

			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');

			fileStr = buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileStr;
	}
}
