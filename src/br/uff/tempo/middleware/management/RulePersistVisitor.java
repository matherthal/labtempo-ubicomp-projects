package br.uff.tempo.middleware.management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.uff.tempo.middleware.management.utils.datastructure.Visitor;

public class RulePersistVisitor implements Visitor {
	private final static String TAG = "RulePersistVisitor";

	private JSONObject root = new JSONObject();
	private JSONArray p_expr = new JSONArray();
	private JSONArray temp;
	
	
	@Override
	public void visit(Object object) {
		try {
			if (object instanceof Predicate) {
				JSONObject jo = predicate2JSON((Predicate) object);
				this.p_expr.put(jo);

			} else if (object instanceof AndNode) {
				JSONObject jo = new JSONObject();
				jo.put("connective", "and");
				this.p_expr.put(jo);
				
			} else if (object instanceof OrNode) {
				JSONObject jo = new JSONObject();
				jo.put("connective", "or");
				this.p_expr.put(jo);
				
			} else if (object instanceof NotNode) {
				JSONObject jo = new JSONObject();
				jo.put("not", null);
				this.p_expr.put(jo);
				
			} else if (object instanceof Formula) {
				Formula f = (Formula) object;
				JSONObject jo = new JSONObject();
				this.p_expr.put(jo);
				this.temp = this.p_expr;
//				if (f.hasTimer()) {
//					JSONObject jo = new JSONObject();
//					jo.put("timer", f.getTimeout() + "sec");
//					this.p_expr.put(jo);
//				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error in updating rule interpreter tree. Error: " + e);
		}
	}

	@Override
	public boolean isDone() {
		return false;
	}

	private JSONObject predicate2JSON(Predicate p) throws JSONException {
		JSONArray pred = new JSONArray();
		
		// The first operand is a value? Or a CV?
		JSONObject elemObj = new JSONObject();
		if (p.getOp2().isConstant()) {
			// Construct val
			elemObj.put("val", p.getOp2().getVal());
		} else {
			// Construct context_elem
			JSONObject elem = new JSONObject();
			elem.put("rai", p.getOp1().getRai());
			elem.put("elem", p.getOp1().getCv());
			elemObj.put("context_elem", elem);
		}
		
		// Put first element into predicate
		pred.put(elemObj);
		
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
			elemObj.put("val", p.getOp2().getVal());
		} else {
			// Construct context_elem
			JSONObject elem = new JSONObject();
			elem.put("rai", p.getOp2().getRai());
			elem.put("elem", p.getOp2().getCv());
			elem.put("params", p.getOp2().getParams().get(0)); //FIXME accepts only one param
			elemObj.put("context_elem", elem);
		}

		// Put second element into predicate
		pred.put(elemObj);
		
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

	public String getJSON() {
		return this.root.toString();
	}
}
