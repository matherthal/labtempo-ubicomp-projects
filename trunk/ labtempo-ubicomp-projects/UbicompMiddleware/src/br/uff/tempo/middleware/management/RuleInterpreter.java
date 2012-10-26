package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class RuleInterpreter extends ResourceAgent {

	private static final long serialVersionUID = 1L;

	private String TAG = "RuleInterpreter";

	public static final String RULE_TRIGGERED = "ruleTrigger";
	public static final int PRIORITY_LOW = 2;
	public static final int PRIORITY_MED = 1;
	public static final int PRIORITY_HIGH = 0;

	private Set<Predicate> cNSet = new HashSet<Predicate>();
	private IResourceDiscovery discovery;
	private boolean valid = false;
	private Timer timer = null;
	private TimeoutTask timeTask = null;
	private List<Integer> timeoutList = new ArrayList();
	// FIXME: this will not belong to the rule, but to the comparison node
	private Integer timeout = 0;
	private Integer priority = PRIORITY_LOW;
	private Formula formula;
	private Formula pNode;
	private int i_debug = 0;

	public RuleInterpreter(String name, String rans) {
		super(name, "br.uff.tempo.middleware.management.RuleInterpreter", rans);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
	}

	@ContextVariable(name = "Regra disparada")
	public boolean ruleTrigger() {
		return valid;
	}

	@Service(name = "Definir expressão")
	public void setExpression(String expr) throws Exception {
		parseExpression(expr.replace('\t', ' ').replace('\n', ' ').trim());
	}

	private void parseExpression(String expr) throws Exception {
		JSONObject intp = new JSONObject(expr).getJSONObject("interpreter");

		Iterator it = intp.keys();
		String key = "";
		boolean hasForm = false;
		while (it.hasNext()) {
			key = it.next().toString();
			if (key.equals("name")) {
				super.setName(intp.getString(key));
				Log.i("NAME", intp.getString(key));
			} else if (key.equals("priority"))
				this.priority = intp.getInt(key);
			else if (key.equals("formula")) {
				if (hasForm)
					throw new Exception("Interpreter has two formulas");
				formula = new Formula();
				pNode = formula;
				formula.attachFormula(buildTree(intp.getJSONArray(key)));
				hasForm = true;
			}
		}
	}

	private Formula buildTree(JSONArray jsonSubtree) throws Exception {
		Formula subtree = new Formula();
		Object o;
		for (int i = 0; i < jsonSubtree.length(); i++) {
			o = jsonSubtree.get(i);
			if (o.getClass().equals(JSONObject.class)) {
				// Context, timer clause, not clause and connectives are objects
				// in JSON
				JSONObject jobj = (JSONObject) o;
				Iterator it = jobj.keys();
				String key = "";
				Object val = "";
				while (it.hasNext()) {
					key = it.next().toString();
					val = jobj.get(key);
					if (key.equals("predicate")) {
						Log.i(TAG, "PREDICATE: " + val.toString());
						subtree.attachFormula(getPredicate((JSONArray) val));
					} else if (key.equals("timer")) {
						Log.i(TAG, "TIMER: " + val.toString());
						subtree.setTimeout(getTimerInSec(val.toString()));
					} else if (key.equals("connective")) {
						Log.i(TAG, "CONNECTIVE: " + val.toString());
					} else if (key.equals("not")) {
						Log.i(TAG, "NOT CLAUSE: " + val.toString());
					} else if (key.equals("formula")) {
						Log.i(TAG, "FORMULA: " + val.toString());
						subtree.attachFormula(buildTree((JSONArray) val));
					} else {
						Log.e(TAG, "UNKNOWN: " + jobj.get(key).toString());
						throw new Exception("Unknown object in JSON file: " + o.toString());
					}
				}
			} else {
				throw new Exception("Unknown object in JSON file: " + o.toString());
			}
		}
		return subtree;
	}

	private Predicate getPredicate(JSONArray jsonPredicate) throws Exception {
		Operand op1 = null;
		Operand op2 = null;
		Operator op = null;
		Integer timer = 0;

		// Get each component from jsonPredicate (context_elem, op, value,
		// timer) to create the subtree
		JSONObject pred;
		for (int i = 0; i < jsonPredicate.length(); i++) {
			if (jsonPredicate.isNull(i))
				throw new Exception("Null Predicate: " + jsonPredicate);
			pred = (JSONObject) jsonPredicate.get(i);
			// If the component is a context_elem, get its attributes (rai,
			// elem, params) and put it first into op1 and next in op2
			String s_component = pred.keys().next().toString();
			// JSONObject component = (JSONObject) pred.get(s_component);
			Object component = pred.get(s_component);

			if ("context_elem".equals(s_component)) {
				JSONObject jobj = (JSONObject) component;
				// Get rai
				String rai = jobj.getString("rai");
				// Get elem
				String elem = jobj.getString("elem");
				// Get params (optional)
				Object[] params = new Object[0];
				if (jobj.has("params")) {
					String s_params = jobj.getString("params");
					params = s_params.split(",");
				}

				// If op1 has already been got, put operand in op2
				if (op1 == null)
					op1 = new Operand(rai, elem, params);
				else if (op2 == null)
					op2 = new Operand(rai, elem, params);
				else
					throw new Exception(
							"Wrong parameters in Interpreter's Predicate: "
									+ jsonPredicate);
				// Get Value
			} else if ("val".equals(s_component)) {
				// If op1 has already been got, put operand in op2
				if (op1 == null)
					op1 = new Operand(component.toString());
				else if (op2 == null)
					op2 = new Operand(component.toString());
				else
					throw new Exception(
							"Wrong parameters in Interpreter's Predicate: "
									+ jsonPredicate);
				// Get operator
			} else if ("op".equals(s_component)) {
				op = getOperator(component.toString());
				// Get timer
			} else if ("timer".equals(s_component)) {
				timer = getTimerInSec(component.toString());
			} else
				throw new Exception(
						"Unknown timer in Interpreter's Predicate: "
								+ jsonPredicate);
		}

		// If the second operand has not been given, then probably the first one
		// is boolean and can be evaluated alone
		if (op2 == null)
			return new Predicate(op1, 0);
		// If there are 2 operands, then it's mandatory having an operator
		// between them
		else if (op != null)
			return new Predicate(op1, op, op2, 0);
		else
			throw new Exception(
					"Wrong parameters in Interpreterr's Predicate: "
							+ jsonPredicate);
	}

	private Operator getOperator(String op) throws Exception {
		if (op.equals("="))
			return Operator.Equal;
		else if (op.equals("!="))
			return Operator.Different;
		else if (op.equals(">"))
			return Operator.GreaterThan;
		else if (op.equals("<"))
			return Operator.LessThan;
		else if (op.equals(">="))
			return Operator.GreaterThanOrEqual;
		else if (op.equals("<="))
			return Operator.LessThanOrEqual;
		else
			throw new Exception("Unknown operator: " + op);
	}

	private int getTimerInSec(String timer) throws Exception {
		String time = timer.toString().split("[A-Za-z]+")[0];
		String order = timer.toString().split("[0-9]+")[1];
		int i_timer = Integer.parseInt(time); // Timer default in seconds
		if ("msec".equals(order))
			i_timer = i_timer / 1000;
		else if ("min".equals(order))
			i_timer = i_timer * 60;
		else if ("h".equals(order))
			i_timer = i_timer * 3600;
		else if (!"sec".equals(order))
			throw new Exception("Unknown timer: " + timer);
		return i_timer;
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
	public void setCondition(String rai, String cv, Object[] params,
			Operator op, Object value) throws Exception {
		IResourceAgent ra = new ResourceAgentStub(rai);
		ra.registerStakeholder(cv, this.getRANS());
		// re discovery.search(rai).get(0);
		// cNSet.add(new ComparisonNode(rai, cv, params, op, value, 0));
		cNSet.add(new Predicate(new Operand(rai, cv, params), op, new Operand(
				value), 0));
	}

	// @Deprecated
	// public void setTimedCondition(String rai, String cv, Object[] params,
	// Operator op, Object value, Integer timeInSec)
	// throws Exception {
	// IResourceAgent ra = new ResourceAgentStub(rai);
	// ra.registerStakeholder(cv, this.getRAI());
	// // re discovery.search(rai).get(0);
	// cNSet.add(new ComparisonNode(rai, cv, params, op, value, timeInSec));
	// timeoutList.add(timeInSec);
	// }

	/**
	 * Get the data structure that stores the Comparison Nodes and evaluates
	 * this.
	 */
	private boolean evaluateExpr() {
		//TODO: implement evaluator using jeval
//		try {
//			Evaluator ev = new Evaluator();
//			Log.i(TAG, "Eval: " + ev.getBooleanResult("1 && (1 || 0)"));
//		} catch (EvaluationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// boolean prevValid = valid;
		valid = true; // Temporary. If it's false, it'll turn again to false
		for (Predicate cn : cNSet) {
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
	public void notificationHandler(String rai, String method, Object value) {
		Log.i(TAG, "Notification received");

		for (Predicate cn : cNSet) {
			// If the change comes from the correct agent AND the context
			// variable (method) is the same AND the value is different,
			// then evaluate
			Operand op = cn.getOp1();
			if (op.getRai().equals(rai) && op.getCv().equals(method)) {
				boolean prevValid = cn.isValid();

				op.setValue(value);
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
