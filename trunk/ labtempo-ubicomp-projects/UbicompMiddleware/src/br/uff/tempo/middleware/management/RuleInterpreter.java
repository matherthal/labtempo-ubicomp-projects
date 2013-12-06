package br.uff.tempo.middleware.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.RuleInterpreterStub;
import br.uff.tempo.middleware.resources.stubs.LampStub;

public class RuleInterpreter extends ResourceAgent implements IRuleInterpreter {

	private static final long serialVersionUID = 1L;

	private String TAG = "RuleInterpreter";

	public static final String RULE_TRIGGERED = "ruleTrigger";
	public static final int PRIORITY_LOW = 2;
	public static final int PRIORITY_MED = 1;
	public static final int PRIORITY_HIGH = 0;

	private Set<Predicate> cNSet = new HashSet<Predicate>();
	private IResourceDiscovery discovery;
	private boolean valid = false;
	// private Timer timer = null;
	// private TimeoutTask timeTask = null;
	private List<Integer> timeoutList = new ArrayList();
	private Integer priority = PRIORITY_LOW;
	private Formula root;
	private Formula pNode;
	private int i_debug = 0;
	private Evaluator evaluator = new Evaluator();
	private int idCounter = 0;
	private boolean stopped = false;
	
	public RuleInterpreter(String name, String rans) {
		super(name, "br.uff.tempo.middleware.management.RuleInterpreter", rans);
		discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
	}

	@ContextVariable(name = "Regra disparada", type = "Trigger")
	public boolean ruleTrigger() {
		return valid;
	}

	@Service(name = "Definir expressão", type = "SetExpression")
	public void setExpression(String expr) throws Exception {
		parseExpression(expr.replace('\t', ' ').replace('\n', ' ').trim());
	}

	@Service(name = "Definir expressão", type = "SetExpression")
	public void setExpression(InputStream stream) throws Exception {
		parseExpression(inputStreamToString(stream));
	}
	
	public String inputStreamToString(InputStream stream) {
		BufferedReader in = null;
		String expr = "";
		try {
			in = new BufferedReader(new InputStreamReader(stream));

			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');

			expr = buffer.toString().replace('\t', ' ').replace('\n', ' ').trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return expr;
	}

	private void parseExpression(String expr) throws Exception {
		JSONObject intp = new JSONObject(expr).getJSONObject("interpreter");

		Iterator it = intp.keys();
		String key = "";
		boolean hasForm = false;
		while (it.hasNext()) {
			key = it.next().toString();
			if (key.equals("name")) {
				String name = intp.getString(key);
				super.setName(name);
//				super.setRANS(name);
				Log.i("NAME", intp.getString(key));
			} else if (key.equals("priority"))
				this.priority = intp.getInt(key);
			else if (key.equals("formula")) {
				if (hasForm)
					throw new Exception("Interpreter has two formulas");
				this.root = buildTree(intp.getJSONArray(key));
				hasForm = true;
			}
		}
	}

	private Formula buildTree(Object jsonSubtree) throws Exception {
		Formula subtree = new Formula();
		subtree.setId(++idCounter);
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
		// Context, timer clause, not clause and connectives are objects in JSON
		Iterator it = jobj.keys();
		String key = "";
		Object val = "";
		while (it.hasNext()) {
			key = it.next().toString();
			val = jobj.get(key);
			if (key.equals("predicate")) {
				Log.i(TAG, "PREDICATE: " + val.toString());
				Predicate p = getPredicate((JSONArray) val);
				subscribePredicate(p);
				subtree.attachFormula(p);
				// pNode = p;
			} else if (key.equals("timer")) {
				Log.i(TAG, "TIMER: " + val.toString());
				subtree.setTimeout(getTimerInSec(val.toString()));
				subtree.setTimerStakeholder(this);
				subtree.timerExpired(false);
				// pNode = new Formula();
				// subtree.attachFormula(pNode);
				// pNode.attachFormula(new TimerNode());
				// pNode.setAndClause();
				// pNode.attachFormula(buildTree(val));
			} else if (key.equals("connective")) {
				Log.i(TAG, "CONNECTIVE: " + val.toString());
				if (val.toString().equals("and")) {
					subtree.setAndClause();
				} else if (val.toString().equals("or")) {
					subtree.setOrClause();
				}
			} else if (key.equals("not")) {
				Log.i(TAG, "NOT CLAUSE: " + val.toString());
				pNode = new NotNode();
				subtree.attachFormula(pNode);
				pNode.attachFormula(buildTree(val));
				// pNode = subtree;
			} else if (key.equals("formula")) {
				Log.i(TAG, "FORMULA: " + val.toString());
				subtree.attachFormula(buildTree((JSONArray) val));
				// pNode = subtree;
			} else {
				Log.e(TAG, "UNKNOWN: " + jobj.get(key).toString());
				throw new Exception("Unknown object in JSON file: " + jobj.toString());
			}
		}
		Log.i(TAG, "Formula COUNT " + subtree.getCount());
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
			return new Predicate(op1, timer, this);
		// If there are 2 operands, then it's mandatory having an operator
		// between them
		else if (op != null)
			return new Predicate(op1, op, op2, timer, this);
		else
			throw new Exception("Wrong parameters in Interpreterr's Predicate: " + jsonPredicate);
	}

	private void subscribePredicate(Predicate p) {
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

	public boolean evaluate(Formula f) throws EvaluationException {
		ExprComposerVisitor v = new ExprComposerVisitor();
		f.depthFirstTraversal_NodeVisiting(v);
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
		// First update predicates
		UpdateFormulaVisitor v = new UpdatePredicatesVisitor(rai, method, value);
		root.breadthFirstTraversal_NodeVisiting(v);
		// Second, update formulas
		v = new UpdateFormulaVisitor(rai, method, value);
		root.breadthFirstTraversal_NodeVisiting(v);

		if (!stopped)
			try {
				// Evaluate and notify
				boolean preEval = root.getEval();
				// Updating valid and getting the evaluation
				valid = evaluate(root);
				// If rule has been validated, notify...
				if (valid && !preEval) {
					log("RULE " + this.getRANS() + " evaluated as true");
					notifyActionPerformers();
				}
			} catch (EvaluationException e) {
				Log.e("Error in evaluation", e.toString());
				e.printStackTrace();
			}
	}
	
	@Override
	public void start() {
		stopped = false;
		if (!isRegistered()) {
			log("START " + this.getRANS());
			identify();
		}
	}
	
	@Override
	public void stop() {
		stopped = true;
		log("STOP " + this.getRANS());
	}
	
	@Override
	public boolean isStarted() {
		return !stopped;		
	}
	
//	@Override
//	public void registerStakeholder(String method, String rai) {
//		//boolean conflict = false;
//		//boolean ruleMatch = false;
//		// Compare rule with all others that are active and has actuator(s)
//		//List<ResourceData> ris = discovery.search(ResourceData.TYPE, ResourceAgent.type(RuleInterpreter.class));
//		//Iterator<ResourceData> it = ris.iterator();
//		//while (it.hasNext()) {
//			//IRuleInterpreter ri = new RuleInterpreterStub(it.next().getRans());
//			//if (ri.isStarted() && ri.getStakeholders() != null)
//				//if (!ri.getStakeholders().isEmpty())
//					// SAT problem
//		//}
//		// If a matched rule was found, compare actuator associated 
//		super.registerStakeholder(method, rai);
//	}
}
