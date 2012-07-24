package br.uff.tempo.middleware.management;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Condition;

public class RuleInterpreter extends ResourceAgent {
	private String TAG = "RuleInterpreter";
	private Set<Condition> conds = new HashSet<Condition>();
	private IResourceDiscovery discovery;

	public RuleInterpreter() {
		super("Regra do Fogao", "br.uff.tempo.middleware.management.RuleInterpreter", 9);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
	}

	@ContextVariable(name = "Regra disparada")
	public void ruleTrigger() {
	}

	@Service(name = "Definir expressão")
	public void setExpression(String expr) {
		parseExpression(expr);
	}

	private void parseExpression(String expr) {
		// Condition cond = new Condition(rRS, expr, expr, cond);
	}

	@Deprecated
	public void setCondition(String rai, String cv, Object[] params, Operator op, Object value) throws Exception {
		IResourceAgent ra = new ResourceAgentStub(rai);
		ra.registerStakeholder(cv, this.getURL());
		// re discovery.search(rai).get(0);
		conds.add(new Condition(ra, cv, params, op.toString(), value, 0));
	}

	private void evaluate() {
		boolean valid = false;
		for (Condition c : conds) {
			if (c.test())
				valid = true;
			else {
				valid = false;
				break;
			}
		}
		if (valid)
			try {
				notifyStakeholders(JSONHelper.createChange(this.getURL(), "Regra disparada", true));
			} catch (JSONException e) {
				Log.e(TAG, "Error in evaluation");
				e.printStackTrace();
			}
	}

	@Override
	public void notificationHandler(String change) {
		String id = JSONHelper.getChange("id", change).toString();
		String mtd = JSONHelper.getChange("method", change).toString();
		Object val = JSONHelper.getChange("value", change);

		for (Condition c : conds) {
			if (c.ra.getURL().equals(id) && c.method.equals(mtd) && !c.value.equals(val)) {
				c.value = val;
				evaluate();
			}
		}
	}

}
