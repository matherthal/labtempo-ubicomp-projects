package br.uff.tempo.apps.rule;

import android.content.Context;
import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class RuleInterpreterTest extends ResourceAgent {
	private String TAG = "RuleInterpreterTest";
	private IResourceDiscovery discovery;

	public Context context;

	public RuleInterpreterTest(Context mContext) {
		super("Teste regra", "br.uff.tempo.apps.rule.RuleInterpreterTest", 30);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		IResourceAgent ra = new ResourceAgentStub(discovery.search("Regra do Fogao").get(0));

		// String cv = "Regra disparada";
		String cv = "ruleTrigger";
		ra.registerStakeholder(cv, this.getURL());

		this.context = mContext;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
		Log.d(TAG, "CHANGE: " + rai + " " + method + " " + value);
		
		//Intent intent = new Intent(null, NotifyDemo.class);
		//startActivity(intent);
		// Toast.makeText(getBaseContext(), "Notificacao 1. Mudança: " + change,
		// Toast.LENGTH_LONG).show();
		// Toast.makeText(context, "Notificacao 2. Mudança: " + change,
		// Toast.LENGTH_LONG).show();
	}

}
