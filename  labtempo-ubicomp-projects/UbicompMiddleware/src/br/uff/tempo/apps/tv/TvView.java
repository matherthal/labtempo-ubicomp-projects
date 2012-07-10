package br.uff.tempo.apps.tv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class TvView extends Activity implements OnClickListener {

	private static final String TAG = "TvView";
	private TvData tvData;
	private ITelevision tvAgent;
	private static int id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.tvData = new TvData();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.tv);

		Intent i = getIntent();

		// A default name...
		String name = "TV";

		// if the object comes from someone (like the house map)
		if (i.getExtras() != null) {

			String type = i.getExtras().getString("type");
			name = i.getExtras().getString("name");

			if (type.equals("agent")) {
				Television t = new Television(name);
				t.identify();
				tvAgent = t;
			} else {
				tvAgent = new TelevisionStub(name);
			}
		} else {

			// or create one (nobody sent an agent)
			Television t = new Television(name + (++id));

			// identify the resource to the system (it calls
			// ResourceRegister.register())
			t.identify();
			tvAgent = t;
		}
		
		Button chPlus = (Button) findViewById(R.id.btnChPlus);
		Button chMinus = (Button) findViewById(R.id.btnChMinus);
		Button volPlus = (Button) findViewById(R.id.btnVolPlus);
		Button volMinus = (Button) findViewById(R.id.btnVolMinus);
		Button turnOnOff = (Button) findViewById(R.id.btnTurnOnOff);
		
		chPlus.setOnClickListener(this);
		chMinus.setOnClickListener(this);
		volPlus.setOnClickListener(this);
		volMinus.setOnClickListener(this);
		turnOnOff.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		

		switch (v.getId()) {
		
		case R.id.btnChPlus:
			
			tvAgent.incChannel();
			break;
		
		case R.id.btnChMinus:
			
			tvAgent.decChannel();
			break;

		case R.id.btnVolPlus:
			
			tvAgent.incVolume(10);
			break;
			
		case R.id.btnVolMinus:
			
			tvAgent.decVolume(10);
			break;
			
		case R.id.btnTurnOnOff:
			
			boolean on = tvAgent.isOn();
			tvAgent.setOn(!on);
			
			break;
			
		default:
			break;
		}
	}

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

	public ITelevision getTvState() {
		return tvAgent;
	}

}