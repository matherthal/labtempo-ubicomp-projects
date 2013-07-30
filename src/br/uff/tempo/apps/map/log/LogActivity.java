package br.uff.tempo.apps.map.log;

import android.app.Activity;
import android.os.Bundle;

public class LogActivity extends Activity {

	private LogAgent agent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Create and register the log Agent
		agent = LogAgent.getInstance();
		agent.identify();
		
		
	}
}
