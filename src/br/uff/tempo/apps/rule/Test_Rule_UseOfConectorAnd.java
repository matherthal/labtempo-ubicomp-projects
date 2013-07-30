package br.uff.tempo.apps.rule;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.resources.Generic;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;
import br.uff.tempo.middleware.resources.stubs.StoveStub;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class Test_Rule_UseOfConectorAnd extends Activity {
	private static final String TAG = "Test_Rule_UseOfConectorAnd";
	private IResourceDiscovery discovery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Toast.makeText(this, "Regra Inicializada", Toast.LENGTH_SHORT).show();

			// Create ARs
			// TVs 1 and 2
			new Television("Television 1", "tv1").identify();
			new Television("Television 2", "tv2").identify();
			// Stove stv1
			new Stove("Stove 1", "stv1").identify();
			// Bed must be created on the screen...

			RuleInterpreter ri = new RuleInterpreter("Regra_ConectoresAND", "Regra_ConectoresAND");
			try {
				ri.setExpression(this.getAssets().open("interpreter2.json"));
				ri.identify();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Error in setting the expression to the Rule Interpreter");
			}

			// Recover ResourceAgents' stubs 
			ITelevision tv1 = new TelevisionStub("tv1");
			ITelevision tv2 = new TelevisionStub("tv2");
			IStove stv = new StoveStub("stv1");
			
			// Update values
			tv1.setChannel(4);
			tv2.setChannel(4);
			stv.setOvenTemperature(100f);
			// Value of bed1.occupied must be set on the screen

			new Generic("Regra_ConectoresAND Action", "Regra_ConectoresAND Action", (IResourceAgent) ri,
					RuleInterpreter.RULE_TRIGGERED) {
				private static final long serialVersionUID = 1L;
				private static final String TAG = "Regra_ConectoresAND Action";

				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.i(TAG, "Rule is valid!");
				}
			};
		}
	}
}
