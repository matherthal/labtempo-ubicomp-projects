package br.uff.tempo.apps.baseview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import br.uff.tempo.R;

public class BaseView extends Activity {

	TextView tv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.base);

		tv = (TextView) findViewById(R.id.textView2);
		(new BaseListener(tv, this)).start();
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
		// stove.unbindService(mConnection);
	}

	// TODO: Improving the input method (an alternative to SeekBar)...

}
