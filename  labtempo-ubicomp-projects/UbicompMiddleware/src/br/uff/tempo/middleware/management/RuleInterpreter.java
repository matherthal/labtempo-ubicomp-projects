package br.uff.tempo.middleware.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.utils.datastructure.PrePostVisitor;
import br.uff.tempo.middleware.management.utils.datastructure.Visitor;

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
	// private String formulaStr = "";
	private Evaluator evaluator = new Evaluator();

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

	@Service(name = "Definir expressão")
	public void setExpression(InputStream stream) throws Exception {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(stream));

			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');

			String expr = buffer.toString();
			parseExpression(expr.replace('\t', ' ').replace('\n', ' ').trim());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// private void updateFormula(Formula formula) {
	// if (formula instanceof Predicate)
	// ((Predicate) formula).evaluate();
	// else if (formula instanceof Formula)
	// formula.getSubtree(i)
	// }

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
				// this.formula = new Formula();
				// pNode = this.formula;
				this.formula = buildTree(intp.getJSONArray(key));
				// Log.i(TAG, "FORMULA STRING: " + formulaStr);
				hasForm = true;
			}
		}
	}

	private Formula buildTree(Object jsonSubtree) throws Exception {
		Formula subtree = new Formula();
		if (jsonSubtree.getClass().equals(JSONObject.class))
			parseJSONObject((JSONObject) jsonSubtree, subtree);
		else if (jsonSubtree.getClass().equals(JSONArray.class)) {
			JSONArray array = (JSONArray) jsonSubtree;
			for (int i = 0; i < array.length(); i++)
				parseJSONObject((JSONObject) array.get(i), subtree);
		} else {
			Log.e(TAG, "Unknown object in JSON file: " + jsonSubtree.toString());
			throw new Exception("Unknown object in JSON file: " + jsonSubtree.toString());
		}
		return subtree;
	}

	private void parseJSONObject(JSONObject jobj, Formula subtree) throws Exception {
		// Context, timer clause, not clause and connectives are objects
		// in JSON
		Iterator it = jobj.keys();
		String key = "";
		Object val = "";
		while (it.hasNext()) {
			key = it.next().toString();
			val = jobj.get(key);
			if (key.equals("predicate")) {
				Log.i(TAG, "PREDICATE: " + val.toString());
				// formulaStr += "p ";
				Predicate p = getPredicate((JSONArray) val);
				subscribePredicate(p);
				subtree.attachFormula(p);
				pNode = p;
			} else if (key.equals("timer")) {
				Log.i(TAG, "TIMER: " + val.toString());
				// formulaStr = formulaStr.substring(0,
				// formulaStr.length()-2) + "* ";
				subtree.setTimeout(getTimerInSec(val.toString()));
			} else if (key.equals("connective")) {
				Log.i(TAG, "CONNECTIVE: " + val.toString());
				if (val.toString().equals("and")) {
					// formulaStr += "&& ";
					subtree.setAndClause();
				} else if (val.toString().equals("or")) {
					// formulaStr += "|| ";
					// pNode = (Formula) pNode.getFather();
					subtree.setOrClause();
				}
			} else if (key.equals("not")) {
				Log.i(TAG, "NOT CLAUSE: " + val.toString());
				// formulaStr += "!";
				// Formula f = buildTree((JSONArray) val);
				// f.setNotClause(true);
				// subtree.attachFormula(f);
				// pNode = f;
				pNode = new NotNode();
				subtree.attachFormula(pNode);
				pNode.attachFormula(buildTree(val));
				pNode = subtree;
			} else if (key.equals("formula")) {
				Log.i(TAG, "FORMULA: " + val.toString());
				// formulaStr += "(";
				subtree.attachFormula(buildTree((JSONArray) val));
				// formulaStr += ") ";
				pNode = subtree;
			} else {
				Log.e(TAG, "UNKNOWN: " + jobj.get(key).toString());
				throw new Exception("Unknown object in JSON file: " + jobj.toString());
			}
		}
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
					throw new Exception("Wrong parameters in Interpreter's Predicate: " + jsonPredicate);
				// Get Value
			} else if ("val".equals(s_component)) {
				// If op1 has already been got, put operand in op2
				if (op1 == null)
					op1 = new Operand(component.toString());
				else if (op2 == null)
					op2 = new Operand(component.toString());
				else
					throw new Exception("Wrong parameters in Interpreter's Predicate: " + jsonPredicate);
				// Get operator
			} else if ("op".equals(s_component)) {
				op = getOperator(component.toString());
				// Get timer
			} else if ("timer".equals(s_component)) {
				timer = getTimerInSec(component.toString());
			} else
				throw new Exception("Unknown timer in Interpreter's Predicate: " + jsonPredicate);
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
			throw new Exception("Wrong parameters in Interpreterr's Predicate: " + jsonPredicate);
	}

	private void subscribePredicate(Predicate p) {
		// new
		// ResourceAgentStub(p.getOp1().getRai()).registerStakeholder(p.getOp1().getCv(),
		// this.getRANS());
		IResourceAgent ra = new ResourceAgentStub(p.getOp1().getRai());
		ra.registerStakeholder(p.getOp1().getCv(), this.getRANS());
		if (p.getOp2() != null)
			if (!p.getOp2().isConstant())
				new ResourceAgentStub(p.getOp2().getRai()).registerStakeholder(p.getOp2().getCv(),
						this.getRANS());
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
	public void setCondition(String rai, String cv, Object[] params, Operator op, Object value)
			throws Exception {
		IResourceAgent ra = new ResourceAgentStub(rai);
		ra.registerStakeholder(cv, this.getRANS());
		// re discovery.search(rai).get(0);
		// cNSet.add(new ComparisonNode(rai, cv, params, op, value, 0));
		cNSet.add(new Predicate(new Operand(rai, cv, params), op, new Operand(value), 0));
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
		// TODO: implement evaluator using jeval
		// try {
		// Evaluator ev = new Evaluator();
		// Log.i(TAG, "Eval: " + ev.getBooleanResult("1 && (1 || 0)"));
		// } catch (EvaluationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

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

	public boolean evaluate() throws EvaluationException {
		ExprComposerVisitor v = new ExprComposerVisitor();
		formula.depthFirstTraversal(v);
		Log.i(TAG, "EXPRESSION: " + v.getExpression());
		// If the evaluator.evaluate return "0.0" then it is false, if it
		// returns "1.0" then it's true
		return evaluator.evaluate(v.getExpression()).equals("1.0") ? true : false;
	}

	private void notifyActionPerformers() {
		notifyStakeholders(RULE_TRIGGERED, true);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		Log.i(TAG, "Notification received");

		// TODO: LOCK THIS CALL
		UpdateFormulaVisitor v = new UpdateFormulaVisitor(rai, method, value);
		formula.breadthFirstTraversal_NodeVisiting(v);

		// Evaluate and notify
		if (v.hasChanged())
			try {
				if (evaluate())
					notifyActionPerformers();
			} catch (EvaluationException e) {
				Log.e("Error in evaluation", e.toString());
				e.printStackTrace();
			}

		// for (Predicate cn : cNSet) {
		// // If the change comes from the correct agent AND the context
		// // variable (method) is the same AND the value is different,
		// // then evaluate
		// Operand op = cn.getOp1();
		// if (op.getRai().equals(rai) && op.getCv().equals(method)) {
		// boolean prevValid = cn.isValid();
		//
		// op.setValue(value);
		// // The expression only will be evaluated if the current
		// // validation of the rule and the context variable are false.
		// // Otherwise it will be assumed that the expression didn't came
		// // from a change. This avoids the problem of overflow the system
		// // with messages while the rule keeps with valid value as true
		// // Log.i("EVALUATE CN ", cn.getRai());
		// // if (cn.evaluate()) {
		// // Log.i("EVALUATE CN ", "TRUE");
		// // If the node contains a timer and it's validation has
		// // changed from false to true, reset timer
		// // if (cn.getTimeout() > 0 && !prevValid) {
		// // Log.i("RuleInterpreter", "Timer reset!");
		// // timerReset();
		// // }
		//
		// valid = evaluateExpr();
		//
		// if (valid) {
		// if (timeout <= 0) {
		// notifyActionPerformers();
		// } else {
		// if (!prevValid) {
		// // It means that the rule wasn't correct,
		// // but now it's
		// Log.i("RuleInterpreter", "Timer start!");
		// timerReset();
		// }
		// // If the rule correctness didn't change
		// // do nothing
		// }
		// } else {
		// // If the evaluation of the context variable returns
		// // false, the timer (if it exists) must be stopped
		// // if (cn.getTimeout() > 0)
		// timerStop();
		// }
		//
		// }
		// }
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

	/**
	 * A visitor that composes de logical expression out of the tree
	 **/
	public static class ExprComposerVisitor implements PrePostVisitor {
		private final static String TAG = "ExprComposerVisitor";
		private String expression = "";

		@Override
		public void preVisit(Object object) {
			// Log.i(TAG, "visit: " + object.toString());
			// expression = expression + "( " + object.toString();
			String s = object.toString();
			if (s.equals("f"))
				s = "(";
			expression = expression + s;
		}

		@Override
		public void inVisit(Object object) {
			// Log.i(TAG, "inVisit: " +object.toString());
			// this.expression.concat(object.toString());
		}

		@Override
		public void postVisit(Object object) {
			String s = object.toString();
			if (s.equals("f"))
				expression = expression + ")";
		}

		@Override
		public boolean isDone() {
			return false;
		}

		public String getExpression() {
			return expression;
		}
	}

	/**
	 * 
	 **/
	public static class UpdateFormulaVisitor implements Visitor {
		private final static String TAG = "UpdateFormulaVisitor";
		private String rai;
		private String method;
		private Object value;
		private Boolean changed = true;

		public UpdateFormulaVisitor(String rai, String method, Object value) {
			super();
			this.rai = rai;
			this.method = method;
			this.value = value;
		}

		@Override
		public void visit(Object object) {
			try {
				// Visit Predicates only
				if (!(object instanceof Predicate))
					return;
				Predicate pred = (Predicate) object;
				// Update first Operand
				updateOperand(pred.getOp1());
				// Update second Operand
				updateOperand(pred.getOp2());
				// Update valid from the predicate
				pred.evaluate();
			} catch (Exception e) {
				Log.e(TAG, "Error in updating rule interpreter tree. Error: " + e);
			}
		}

		public boolean hasChanged() {
			return changed;
		}

		private void updateOperand(Operand op) {
			try {
				if (op != null)
					if (!op.isConstant() && op.getRai().equals(rai) && op.getCv().equals(method))
						if (value.equals(op.getVal()))
							changed = false;
						else {
							op.setValue(value);
							changed = true;
						}
			} catch (Exception e) {
				Log.e(TAG, "Error in updating operand: " + op.toString() + " Error: " + e);
			}
		}

		@Override
		public boolean isDone() {
			return false;
		}
	}
}
