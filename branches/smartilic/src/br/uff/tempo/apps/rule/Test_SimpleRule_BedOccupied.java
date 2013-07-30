package br.uff.tempo.apps.rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Generic;

public class Test_SimpleRule_BedOccupied extends Activity {
	private IResourceDiscovery discovery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Toast.makeText(this, "Regra Inicializada", Toast.LENGTH_SHORT).show();

			// Subscription Test
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
			String raiB = discovery.search(ResourceData.TYPE, ResourceAgent.type(Bed.class))
					.get(0).getRans();
			IResourceAgent ra = new ResourceAgentStub(raiB);
			new Generic("SubscTest", "SubscTest", ra, "occupied") {
				private static final String TAG = "SubscTest";

				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.i(this.TAG, "SUBSCRIÇÃO FUNCIONOU!!!");
				}
			};

			RuleInterpreter ri = new RuleInterpreter("RegraCamaOcupada", "RegraCamaOcupada");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(this.getAssets().open("interpreter-cama.json")));

				String line;
				StringBuilder buffer = new StringBuilder();
				while ((line = in.readLine()) != null)
					buffer.append(line).append('\n');

				ri.setExpression(buffer.toString());
				ri.identify();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			new Generic("Bed Occupied Action", "Bed Occupied Action", (IResourceAgent) ri,
					RuleInterpreter.RULE_TRIGGERED) {
				private static final long serialVersionUID = 1L;
				private static final String TAG = "Action Stove";
				private int counter = 0;

				@Override
				public void notificationHandler(String rai, String method, Object value) {
					Log.i(TAG, "CHANGE: " + rai + " " + method + " " + value);
				}
			};
		}
	}
}
