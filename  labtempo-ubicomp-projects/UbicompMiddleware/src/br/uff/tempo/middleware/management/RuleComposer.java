package br.uff.tempo.middleware.management;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.sax.RootElement;
import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.Actuator.Action;

public class RuleComposer { //extends Service {
	private static final String TAG = "RuleComposer";
	
	private List<IRuleComposeListener> listeners = new LinkedList<IRuleComposeListener>();
	
	// Rule that will be parsed to a JSON file and loaded in the system
	private Formula exprRoot;
	// Pointer used to compose rule
	private Formula p_expr;
	private String ruleName = "";

	private Actuator act = null;

//	@Override
//    public void onCreate() {
	public RuleComposer() {
		// Initialization of expression composition
		this.exprRoot = new Formula();
		this.p_expr = this.exprRoot;
	}

	public void setRuleName(String name) {
		this.ruleName = name;
		notifyRuleChanged(name);
	}

	public void addCondition(String rai, String cv, Object[] params, long timeout) throws Exception {
		Operand op = new Operand(rai, cv, params);
		Predicate p = new Predicate(op, timeout, null);
		this.p_expr.attachFormula(p);
		
		notifyRuleChanged(p.asText());
	}

	public void addConditionComp(String rai1, String cv1, Object[] params1, Operator op, String rai2,
			String cv2, Object[] params2, long timeout) throws Exception {
		Operand op1 = new Operand(rai1, cv1, params1);
		Operand op2 = new Operand(rai2, cv2, params2);
		Predicate p = new Predicate(op1, op, op2, timeout, null);
		this.p_expr.attachFormula(p);
		
		notifyRuleChanged(p.asText());
	}

	public void addConditionComp(String rai1, String cv1, Object[] params1, Operator op, Object val, long timeout) throws Exception {
		Operand op1 = new Operand(rai1, cv1, params1);
		Operand op2 = new Operand(val);
		Predicate p = new Predicate(op1, op, op2, timeout, null);
		this.p_expr.attachFormula(p);		
	}
	
	public void addConditionComp(ContextVariableBundle cvBundle, Operator op, Object val, long timeout) throws Exception {
		addConditionComp(cvBundle.getAgentRans(), cvBundle.getContextVariable(), cvBundle.getParameters(), op, val, timeout);
		
		String cond = "";
		cond = cvBundle.getAgentRans() + ": " + cvBundle.getCvName();
		cond += "\n" + op.getSymbol() + "\n";
		cond += val.toString();
		
		notifyRuleChanged(cond);
	}
	
	public void addCondition(ContextVariableBundle cvBundle, long timeout) throws Exception {
		addCondition(cvBundle.getAgentRans(), cvBundle.getContextVariable(), cvBundle.getParameters(), timeout);
		
		String cond = cvBundle.getAgentRans() + ": " + cvBundle.getCvName();
		
		notifyRuleChanged(cond);
	}

	public void addOpenBracket() {
		Formula f = new Formula();
		this.p_expr.attachFormula(f);
		this.p_expr = f;

		notifyRuleChanged("(");
	}

	public void addCloseBracket() {
		this.p_expr = (Formula) this.p_expr.getFather();
		
		notifyRuleChanged(")");
	}

	public void addAndClause() {
		this.p_expr.setAndClause();
		
		notifyRuleChanged("AND");
	}

	public void addOrClause() {
		this.p_expr.setOrClause();
		
		notifyRuleChanged("OR");
	}

	public void addNotClause() {
		NotNode n = new NotNode();
		this.p_expr.attachFormula(n);
		this.p_expr = n;
		
		notifyRuleChanged("NOT");
	}

	public void addExpressionTimer(long timeout) {
		this.p_expr.setTimeout(timeout);
		
		notifyRuleChanged("TIMER " + timeout);
	}

	public void setActuatorName(String name) {
		if (act == null)
			act = new Actuator(name, "");
		else
			act.setName(name);
		
		notifyInterpreterFinished();
	}

	public void addAction(String rai, String serv, Object[] params) {
		if (act == null)
			act = new Actuator("", "");
		act.addAction(rai, serv, params);
	}
	
	public void addAction(String rai, String serv, Object[] params, String opName) {
		addAction(rai, serv, params);
		notifyActionAdded(rai + ": " + opName);
	}

	public void finish(Context context) throws IOException, JSONException {
		// Test existence
		String filename = "rule_" + ruleName.replaceAll("\\s", "")
				+ (act != null ? "_" + act.getName().replaceAll("\\s", "") : "") + ".json";
		for (String fname : context.fileList())
			if (fname.equals(filename))
				Log.i(TAG, "File name: " + fname + 
						" already exists. It will be overwritten");
		
		// Get Interpreter
		JSONObject joIntp = new JSONObject(); 
		joIntp.put("name", ruleName);
		joIntp.put("formula", buildJSON(this.exprRoot).getJSONArray("formula"));

		// Join Interpreter and Actuator into the Context Rule
		JSONObject rule = new JSONObject();
		rule.put("interpreter", joIntp);
		if (this.act != null)
			// If there is an actuator
			rule.put("actuator", actuator2JSON(this.act));
		String ruleStr = rule.toString();
		Log.i(TAG, "NEW JSON: " + ruleStr);
		
		// Write Context Rule in file
		FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outputStream.write(ruleStr.getBytes());
		outputStream.close();
		
		notifyRuleFinished();
		
		// Clean
		this.exprRoot = null;
	}
	
	/*public void start() {
		FileInputStream stream = null;
		try {
			stream = openFileInput(fname);
			RuleInterpreter ri = new RuleInterpreter(name + num, name + num);
			ri.setExpression(stream);
			ri.identify();
			
			try {
				stream = openFileInput(fname);
				Actuator act = new Actuator(nameAct + num, nameAct + num);
				act.setExpression(stream);
				act.identify();
				
				// Bind actuator to rule
				ri.registerStakeholder(RuleInterpreter.RULE_TRIGGERED, act.getRANS());
			} catch (Exception e) {
				Log.i(TAG, "Actuator not found in rule");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	/*
	 * Recursive method that builds a JSON for the entire formula
	 */
	private JSONObject buildJSON(Formula f) throws JSONException {
		JSONObject jo = new JSONObject();
		if (f instanceof Predicate) {
			jo = predicate2JSON((Predicate) f);

		} else if (f instanceof AndNode) {
			jo.put("connective", "and");
			
		} else if (f instanceof OrNode) {
			jo.put("connective", "or");
			
		} else if (f instanceof NotNode) {
			jo.put("not", buildJSON((Formula) f.getSubtree(0)));
			
		} else {
			JSONArray jaForm = new JSONArray();
			for (int i = 0; i < f.getCount() - 1; i++) {
				jaForm.put(buildJSON((Formula) f.getSubtree(i)));
			}
			if (f.hasTimer()) {
				JSONObject joTimer = new JSONObject();
				joTimer.put("timer", f.getTimeout() + "sec");
				jaForm.put(joTimer);
			}
			jo.put("formula", jaForm);
		}
		return jo;
	}

	/*
	 * Extracts a JSON out of a Predicate object
	 */
	private JSONObject predicate2JSON(Predicate p) throws JSONException {
		JSONArray pred = new JSONArray();
		
		// The first operand is a value? Or a CV?
		JSONObject elemObj = new JSONObject();
		if (p.getOp1().isConstant()) {
			// Construct val
			elemObj = createJSONElement(elemObj, "val", p.getOp1().getVal());
		} else {
			// Construct context_elem
			JSONObject elem = new JSONObject();
			elem.put("rai", p.getOp1().getRai());
			elem.put("elem", p.getOp1().getCv());
			List params = p.getOp1().getParams(); 
			if (params != null)
				if (!params.isEmpty())
					//elem.put("params", p.getOp1().getParams().get(0)); //FIXME accepts only one param
					elem = createJSONElement(elem, "params", p.getOp1().getParams().get(0));
			elemObj.put("context_elem", elem);
		}
		
		// Put first element into predicate
		pred.put(elemObj);
		
		if (p.getOp2() != null) {
			// Construct operator
			JSONObject jOp = new JSONObject();
			Operator op = p.getOperator();
			switch (op) {
			case Equal:
				jOp.put("op", "=");
				break;
			case Different:
				jOp.put("op", "!=");
				break;
			case GreaterThan:
				jOp.put("op", ">");
				break;
			case LessThan:
				jOp.put("op", "<");
				break;
			case GreaterThanOrEqual:
				jOp.put("op", ">=");
				break;
			case LessThanOrEqual:
				jOp.put("op", "<=");
				break;
			}
			
			// Put operator into predicate
			pred.put(jOp);
			
			// The second operand is a value? Or a CV?
			elemObj = new JSONObject();
			if (p.getOp2().isConstant()) {
				// Construct val
				elemObj = createJSONElement(elemObj, "val", p.getOp2().getVal());
				//elemObj.put("val", p.getOp2().getVal());
			} else {
				// Construct context_elem
				JSONObject elem = new JSONObject();
				elem.put("rai", p.getOp2().getRai());
				elem.put("elem", p.getOp2().getCv());
				List params = p.getOp2().getParams(); 
				if (params != null)
					if (!params.isEmpty())
						//elem.put("params", p.getOp2().getParams().get(0)); //FIXME accepts only one param
						elem = createJSONElement(elem, "params", p.getOp2().getParams().get(0));
				elemObj.put("context_elem", elem);
			}
	
			// Put second element into predicate
			pred.put(elemObj);
		}
		
		// Does the predicate have a timer?
		if (p.hasTimer()) {
			JSONObject t = new JSONObject();
			t.put("timer", p.getTimeout() + "sec");
			pred.put(t);
		}
		
		// Construct the predicate
		JSONObject predObj = new JSONObject();
		predObj.put("predicate", pred);
		
		// Put predicate into json
		return predObj;
	}

	/*
	 * Extracts a JSON out of a Predicate object
	 */
	private JSONArray actuator2JSON(Actuator a) throws JSONException {
		JSONArray jaAct = new JSONArray();
		
		JSONObject jo = new JSONObject();
		jo.put("name", a.getName());
		jaAct.put(jo);

		for (Action action : a.getActions()) {
			JSONObject joAction = new JSONObject();
			joAction.put("rai", action.getRai());
			joAction.put("service", action.getService());
			List<Tuple<String, Object>> params = action.getParams(); 
			if (params != null)
				if (!params.isEmpty())
					//joAction.put("params", params.get(0).toString());
					joAction = createJSONElement(joAction, "params", params.get(0).value);
			JSONObject joActionObj = new JSONObject();
			joActionObj.put("action", joAction);
			jaAct.put(joActionObj);
		}
		return jaAct;
	}
	
	private JSONObject createJSONElement(JSONObject elem, String key, Object val) throws JSONException {
		if (elem == null)
			elem = new JSONObject();
		
		if (val instanceof Integer)
			elem.put(key, (Integer) val);
		else if (val instanceof Double)
			elem.put(key, (Double) val);
		else if (val instanceof Long)
			elem.put(key, (Long) val);
		else if (val instanceof Boolean)
			elem.put(key, (Boolean) val);
		else if (val instanceof String)
			elem.put(key, val.toString());
		else 
			elem.put(key, val.toString());
		return elem;
	}
	
	public void addListener(IRuleComposeListener listener) {
		listeners.add(listener);
	}
	
	private void notifyRuleChanged(String name) {
		for (IRuleComposeListener listener : listeners) {
			listener.onRuleCompositionChanged(name);
		}
	}
	
	private void notifyRuleFinished() {
		for (IRuleComposeListener listener : listeners) {
			listener.onRuleCompositionFinished();
		}
	}
	
	private void notifyInterpreterFinished() {
		for (IRuleComposeListener listener : listeners) {
			listener.onInterpreterFinished();
		}
	}
	
	private void notifyActionAdded(String name) {
		for (IRuleComposeListener listener : listeners) {
			listener.onActionAdded(name);
		}
	}
}
