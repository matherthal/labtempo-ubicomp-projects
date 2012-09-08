package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class RuleInterpreter extends ResourceAgent {
	private String TAG = "RuleInterpreter";

	public static final String RULE_TRIGGERED = "ruleTrigger";

	private Set<ComparisonNode> cNSet = new HashSet<ComparisonNode>();
	private IResourceDiscovery discovery;
	private boolean valid = false;
	private Timer timer = null;
	private TimeoutTask timeTask = null;
	private List<Integer> timeoutList = new ArrayList();
	// FIXME: this will not belong to the rule, but to the comparison node
	private Integer timeout = 0;

	public RuleInterpreter(String name) {
		super(name, "br.uff.tempo.middleware.management.RuleInterpreter", 9);
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

	/**
	 * FIXME: this will not belong to the rule, but to the comparison node
	 * 
	 * @param timeoutInSec
	 */
	public void setTimeout(Integer timeoutInSec) {
		this.timeout = timeoutInSec;
	}

	@Deprecated
	public void setCondition(String rai, String cv, Object[] params, Operator op, Object value) throws Exception {
		IResourceAgent ra = new ResourceAgentStub(rai);
		ra.registerStakeholder(cv, this.getURL());
		// re discovery.search(rai).get(0);
		cNSet.add(new ComparisonNode(rai, cv, params, op, value, 0));
	}

	// @Deprecated
	// public void setTimedCondition(String rai, String cv, Object[] params,
	// Operator op, Object value, Integer timeInSec)
	// throws Exception {
	// IResourceAgent ra = new ResourceAgentStub(rai);
	// ra.registerStakeholder(cv, this.getURL());
	// // re discovery.search(rai).get(0);
	// cNSet.add(new ComparisonNode(rai, cv, params, op, value, timeInSec));
	// timeoutList.add(timeInSec);
	// }

	/**
	 * Get the data structure that stores the Comparison Nodes and evaluates
	 * this.
	 */
	private boolean evaluateExpr() {
		// boolean prevValid = valid;
		valid = true; // Temporary. If it's false, it'll turn again to false
		for (ComparisonNode cn : cNSet) {
			if (!cn.evaluate()) {
				valid = false;
				break;
			}
		}
		// if (valid)
		// if (timeout > 0 && !prevValid) {
		// Log.i("RuleInterpreter", "Timer reset!");
		// timerReset();
		// } else
		// timerStop();
		return valid;
	}

	private void notifyActionPerformers() {
		notifyStakeholders(RULE_TRIGGERED, true);
	}

	@Override
	public void notificationHandler(String change) {
		Log.i(TAG, "Notification received");
		String id = JSONHelper.getChange("id", change).toString();
		String mtd = JSONHelper.getChange("method", change).toString();
		Object val = JSONHelper.getChange("value", change);

		for (ComparisonNode cn : cNSet) {
			// If the change comes from the correct agent AND the context
			// variable (method) is the same AND the value is different,
			// then evaluate
			if (cn.getRai().equals(id) && cn.getMethod().equals(mtd)) {
				boolean prevValid = cn.isValid();

				cn.setValueCache(val);
				// The expression only will be evaluated if the current
				// validation of the rule and the context variable are false.
				// Otherwise it will be assumed that the expression didn't came
				// from a change. This avoids the problem of overflow the system
				// with messages while the rule keeps with valid value as true
				// Log.i("EVALUATE CN ", cn.getRai());
				// if (cn.evaluate()) {
				// Log.i("EVALUATE CN ", "TRUE");
				// If the node contains a timer and it's validation has
				// changed from false to true, reset timer
				// if (cn.getTimeout() > 0 && !prevValid) {
				// Log.i("RuleInterpreter", "Timer reset!");
				// timerReset();
				// }

				valid = evaluateExpr();

				if (valid) {
					if (timeout <= 0) {
						notifyActionPerformers();
					} else {
						if (!prevValid) {
							// It means that the rule wasn't correct,
							// but now it's
							Log.i("RuleInterpreter", "Timer start!");
							timerReset();
						}
						// If the rule correctness didn't change
						// do nothing
					}
				} else {
					// If the evaluation of the context variable returns
					// false, the timer (if it exists) must be stopped
					// if (cn.getTimeout() > 0)
					timerStop();
				}

			}
		}
	}

	private void timerReset() {
		timerStop();
		Log.i("Timer", "Start");
		this.timer = new Timer();
		timeTask = new TimeoutTask();

		Log.i("TIME", new Date().toGMTString());

		long t = this.timeout * 1000;
		timer.schedule(timeTask, t);
	}

	private void timerStop() {
		if (timeTask != null) {
			Log.i("Timer", "Stop");
			timeTask.cancel();
			timeTask = null;
		}
	}

	class TimeoutTask extends TimerTask {

		public void run() {
			Log.i("TimeoutTask", "Timeout went off!");
			Log.i("TIME", new Date().toGMTString());
			if (evaluateExpr())
				notifyActionPerformers();
		}
	}
}
