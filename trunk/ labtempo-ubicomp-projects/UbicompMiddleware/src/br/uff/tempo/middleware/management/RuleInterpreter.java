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

public class RuleInterpreter extends ResourceAgent {
	private String TAG = "RuleInterpreter";

	public final String RULE_TRIGGERED = "RULE_TRIGGERED";

	private Set<ComparisonNode> cNSet = new HashSet<ComparisonNode>();
	private IResourceDiscovery discovery;
	private boolean valid = false;

	public RuleInterpreter() {
		super("Regra do Fogao", "br.uff.tempo.middleware.management.RuleInterpreter", 9);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
	}

	@ContextVariable(name = "Regra disparada")
	public boolean ruleTrigger() {
		return valid;
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
		cNSet.add(new ComparisonNode(rai, cv, params, op, value, 0));
	}

	@Deprecated
	public void setTimedCondition(String rai, String cv, Object[] params, Operator op, Object value, Integer timeInSec)
			throws Exception {
		IResourceAgent ra = new ResourceAgentStub(rai);
		ra.registerStakeholder(cv, this.getURL());
		// re discovery.search(rai).get(0);
		cNSet.add(new ComparisonNode(rai, cv, params, op, value, timeInSec));
	}

	/**
	 * Get the data structure that stores the Comparison Nodes and evaluates
	 * this.
	 */
	private void evaluateExpr() {
		boolean v = true;
		for (ComparisonNode cn : cNSet) {
			if (!cn.evaluate()) {
				v = false;
				break;
			}
		}

		if (v)
			valid = v;
			try {
			notifyStakeholders(JSONHelper.createChange(this.getURL(), RULE_TRIGGERED, true));
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

		for (ComparisonNode cn : cNSet) {
			// If the change comes from the correct agent AND the context
			// variable (method) is the same AND the value is different,
			// then evaluate
			// && !cn.getValueCache().equals(val)
			if (cn.getRai().equals(id) && cn.getMethod().equals(mtd)) {
				cn.setValueCache(val);
				// The expression only will be evaluated if the current
				// validation of the rule is false. Otherwise it will be assumed
				// that the expression didn't came from a change. This avoids
				// the problem of overflow the system with messages while the
				// rule keeps with valid value as true
				if (!valid) {
					Log.i("EVALUATE CN ", "?");
					if (cn.evaluate()) {
						Log.i("EVALUATE CN ", "TRUE");
						evaluateExpr();
					}
				}
			}
		}
	}

	public void ConditionTimeout() {

	}
}
