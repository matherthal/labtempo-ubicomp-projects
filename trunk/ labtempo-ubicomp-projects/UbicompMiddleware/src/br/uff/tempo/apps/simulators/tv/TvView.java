package br.uff.tempo.apps.simulators.tv;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TvView extends AbstractView implements OnClickListener {

	private static final String TAG = "TvView";
	private ITelevision agent;
	private static int id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tv);

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

			agent.incChannel();
			break;

		case R.id.btnChMinus:

			agent.decChannel();
			break;

		case R.id.btnVolPlus:

			agent.incVolume(10);
			break;

		case R.id.btnVolMinus:

			agent.decVolume(10);
			break;

		case R.id.btnTurnOnOff:

			boolean on = agent.isOn();
			agent.setOn(!on);

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

	@Override
	public IResourceAgent createNewResourceAgent() {
		return new Television("SomeTV");		
	}

}