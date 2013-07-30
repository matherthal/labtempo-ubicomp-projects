package br.uff.tempo.apps.counter;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Counter;
import br.uff.tempo.middleware.resources.OnOff;
import br.uff.tempo.middleware.resources.interfaces.IOnOff;
import br.uff.tempo.middleware.resources.stubs.OnOffStub;

public class CounterView extends Activity{

	private EditText editName;
	
	private Button register;
	private Button start;
	
	private Spinner onOffSelector;
	
	private TextView countView;
	
	private Counter counter;
	private IOnOff iOnOff;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.counter);
		
		editName = (EditText) findViewById(R.id.nameEditText);
		
		register = (Button) findViewById(R.id.registerButton);
		start = (Button) findViewById(R.id.startButton);
		
		onOffSelector = (Spinner) findViewById(R.id.spinner1);
		
		countView = (TextView) findViewById(R.id.countTextView);
		
		register.setOnClickListener(registerClick);
		start.setOnClickListener(startClick);
		initList();
	}
	
	private void initList()
	{
		IResourceDiscovery rD = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		List<ResourceData> rdList = rD.search(ResourceData.TYPE, ResourceAgent.type(OnOff.class));
		if (rdList!= null){
			String[] array_spinner = new String[rdList.size()];
			int i = 0;
			for (ResourceData rData : rdList) {
				array_spinner[i++]=rData.getRans();				
			}
			ArrayAdapter adapter = new ArrayAdapter(this,
			android.R.layout.simple_spinner_item, array_spinner);
			onOffSelector.setAdapter(adapter);
		}
	}
	
	private OnClickListener registerClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			counter = new Counter(editName.getText().toString(), editName.getText().toString());
			counter.identify();
		}		
	};
	
	private OnClickListener startClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String selected = onOffSelector.getSelectedItem().toString();
			countView.setText(selected);
			iOnOff = new OnOffStub(onOffSelector.getSelectedItem().toString());
			iOnOff.registerStakeholder("ligaDesliga", counter.getRANS());
			countView.setText(counter.getCount()+"");
			(new Listener()).start();
		}
		
	};
	
	private CounterView act = this;
	
	public class Listener extends Thread {
		public void run(){
			while (true) {
				
				act.runOnUiThread(new Runnable() {
	    			public void run() {
	    				countView.setText(counter.getCount()+"");
	    			}
	    		});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
