/**
 * @author dbarreto
 */

package br.uff.tempo.apps.onOffCounter.copyToTestCommAPI;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.apps.map.dialogs.IResourceListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.interfaces.ILamp;
import br.uff.tempo.middleware.resources.stubs.LampStub;

public class CounterApp2 extends Activity implements IResourceChooser, IResourceListGetter {
	
	// My Counter Agent
	private CounterAg2 counterAg;

	//Counter variable
	int counter = 0;
	// GUI components
	private ToggleButton onOff;
	private Button choose;
	private TextView tvCount;
	private TextView tvName;

	// My Dialog
	private ChooseResource dialog;

	// Resource Discovery Service
	private IResourceDiscovery rds;

	// My Lamp
	private ILamp lamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
        InterestAPI ia = InterestAPIImpl.getInstance();
        String msg = "rai:192.168.1.105//br.uff.tempo.middleware.resources.Lamp:GeneralLamp;{\"types\":[],\"jsonrpc\":\"2.0\",\"method\":\"isOn\",\"params\":[]}";
        try {
            ia.sendMessage("rai:192.168.1.105//br.uff.tempo.middleware.resources.Lamp:GeneralLamp://ContextVariable", msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

		// instantiate the dialog list
		dialog = new ChooseResource(this);

		// get an reference to the Resource Discovery Service
		rds = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);

		// use the specified GUI layout
		setContentView(R.layout.onoffcounter);
		
		//instantiate my counter agent
		counterAg = new CounterAg2("CounterByDavid", this);
		
		//register
		counterAg.identify();
		
		Log.d("CounterLamp", "My counter was registered");

		// find the GUI components references
		onOff   = (ToggleButton) findViewById(R.id.toggleButton1);
		choose  = (Button) findViewById(R.id.btnChoose);
		tvCount = (TextView) findViewById(R.id.tvCount);
		tvName  = (TextView) findViewById(R.id.tvName);
	}
	
	public void inc() {
		
		counter++;
		update();
	}
	
	public void reset() {
		
		counter = 0;
		update();
	}
	
	public void turnOnOff(final boolean b) {
		
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				onOff.setChecked(b);
			}
		});
	}
	
	public boolean getCurrentState() {
		
		return onOff.isChecked();
	}
	
	public void update() {
		
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				tvCount.setText(Integer.toString(counter));
				Log.d("CounterLamp", "tvCount = " + tvCount.getText());
			}
		});
		
	}
	
	public String getCurrentRAI() {
		
		return lamp.getRAI();
	}
	
	public void chooseClick(View v) {

		MiddlewareOperation m = new MiddlewareOperation(this, "Lamp");
		m.execute(null);
		
	}
	
	@Override
	public void onGetResourceList(List<String> result) {
		
		dialog.showDialog(result);
	}
	
	@Override
	public void onRegisteredResourceChoosed(String resourceRAI) {
		
		//instantiate the stub (proxy) from lamp 
		lamp = new LampStub(resourceRAI);
		
		//register my counter agent in "isOn" method in the lamp agent
		lamp.registerStakeholder("isOn", counterAg.getRAI());
		
		boolean on = lamp.isOn();	
		onOff.setChecked(on);
		tvName.setText(resourceRAI);	
		
		Log.d("CounterLamp", "Lamp choosed...");
	}

	@Override
	public void onDialogFinished(Dialog dialog) {
		// TODO Auto-generated method stub
		
	}

	public void onOffclick(View v) {

		boolean on = onOff.isChecked();
		
		onOff.setChecked(!on);
		
		if (on) {
			lamp.turnOn();
		}
		else {
			lamp.turnOff();
		}
			
	}
	
	public void resetCounterClick(View v) {
		
		reset();
	}
}
