package br.uff.tempo.apps.onoffsample;

import java.util.ArrayList;

import br.uff.tempo.R;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.resources.OnOff;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OnOffView extends Activity{
	
	private ToggleButton onOff;
	private Button register;

	private TextView device;
	
	private EditText editName;
	private EditText editX;
	private EditText editY;
	
	private OnOff agent;
	
	private IResourceDiscovery discovery;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onoff);
		
		onOff = (ToggleButton) findViewById(R.id.toggleButton1);	
		register = (Button) findViewById(R.id.registerButton);
			
		device = (TextView) findViewById(R.id.statusTextView);
		
		editName = (EditText) findViewById(R.id.nameEditText);
		editX = (EditText) findViewById(R.id.xEditText);
		editY = (EditText) findViewById(R.id.yEditText);
		
		onOff.setOnClickListener(onOffClick);
		register.setOnClickListener(registerClick);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
	}
	
	private OnClickListener registerClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String name = editName.getText().toString();
			String xStr = editX.getText().toString();
			String yStr = editY.getText().toString();
			
			ArrayList<String> list = discovery.search(name);
			if (list == null) {
				agent = getAgent(name,xStr,yStr);
			} else {
				String rai = list.get(0);
				ArrayList<String> type = new ResourceAgentIdentifier(rai).getType();
				String strType = type.get(type.size()-1);
				
				if (strType.contains("OnOff")){//connecting to a existent agent
					agent = getAgent(name+"Observer",xStr,yStr);
					//IResourceAgent observableAgent = new ResourceAgentStub(rai);
					//observableAgent.registerStakeholder("ligaDesliga", agent.getURL());
					agent.registerStakeholder("ligaDesliga", rai);
				} else {
					agent = getAgent(name,xStr,yStr);
				}
			}
			agent.identify();
			device.setText("Registered!");
			(new Listener()).start();			
		}
    };
    
    private OnOff getAgent(String name, String xStr, String yStr)
    {
    	if (xStr.equals("X")||yStr.equals("Y")){
			return new OnOff(name, onOff);
		}
		int x = Integer.valueOf(xStr);
		int y = Integer.valueOf(yStr);
		return new OnOff(name, new Position(x,y), onOff);	
    }
    
    private OnClickListener onOffClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			boolean isOn = onOff.isChecked()? true:false;
			agent.setStatus(isOn);			
		}
    };
    
    public OnOffView act = this;
    

    public class Listener extends Thread {
    	
    	public void run(){		        
	    	while (true) {
	    		act.runOnUiThread(new Runnable() {
	    			public void run() {
			    		if (agent.isOn()) {
							onOff.setChecked(true);
						} else {
							onOff.setChecked(false);
						}
	    			}
	    		});
	    		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		   
    	}
    }
    
    

}