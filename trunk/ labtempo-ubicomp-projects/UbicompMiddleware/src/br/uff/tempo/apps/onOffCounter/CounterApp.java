/**
 * @author dbarreto
 */

package br.uff.tempo.apps.onOffCounter;

import br.uff.tempo.R;
import br.uff.tempo.apps.map.dialogs.ChooseExternalResource;
import br.uff.tempo.apps.map.dialogs.IResourceChooser;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.interfaces.ILamp;
import br.uff.tempo.middleware.resources.stubs.LampStub;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CounterApp extends Activity implements IResourceChooser {


	//My Counter Agent
	private CounterAg counter;
	
	//GUI components
	private ToggleButton onOff;
	private Button choose;
	private TextView count;
	
	//My Dialog
	private ChooseExternalResource dialog;
	
	//Resource Discovery Service
	private IResourceDiscovery rds;
	
	//My Lamp
	private ILamp lamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//instantiate the dialog list 
		dialog = new ChooseExternalResource(this);
		
		//get an reference to the Resource Discovery Service
		rds = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		
		//use the specified GUI layout
		setContentView(R.layout.onoffcounter);
		
		//instantiate my agent
		counter = new CounterAg("CounterByDavid");
		
		//find the GUI components references
		onOff  = (ToggleButton) findViewById(R.id.toggleButton1);
		choose = (Button) findViewById(R.id.btnChoose);
		count  = (TextView) findViewById(R.id.tvCount);
	}
	
	public void chooseClick (View v) {
		
		dialog.showDialog(rds.search("Lamp"));
	}
	
	public void onOffclick (View v) {
		
		//TODO Create a 'remote control'
	}

	@Override
	public void onRegisteredResourceChoosed(String resourceRAI) {
		
		lamp = new LampStub(resourceRAI);
		
		counter.registerStakeholder("isOn", resourceRAI);
		
		boolean on = lamp.isOn();
		
		onOff.setChecked(on);
	}
}
