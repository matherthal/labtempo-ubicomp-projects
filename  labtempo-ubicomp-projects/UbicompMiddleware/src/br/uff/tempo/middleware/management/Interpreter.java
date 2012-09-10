package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.interfaces.IInterpreter;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Stove;

public class Interpreter extends ResourceAgent implements IInterpreter {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = "Interpreter";
	private IResourceAgent ra = null;
	private Method cv = null;
	private Operator op = null;
	private Object constant = null;
	// Set of conditional results
	private Set<ConditionalResult> conditionalResultSet = new HashSet<ConditionalResult>();

	public Interpreter() {
		super("br.uff.tempo.middleware.management.Interpreter", 1432);
	}

	// @Override
	public boolean setContextVariable(IResourceAgent ra, String cvName) {
		Method[] mtds = Stove.class.getMethods();
		for (Method m : mtds)
			if (m.isAnnotationPresent(IResourceAgent.ContextVariable.class))
				if (m.getAnnotation(IResourceAgent.ContextVariable.class).name() == cvName) {
					this.cv = m;
					this.ra = ra;
					ra.registerStakeholder(cv.getName(), this.getRAI());
					return true;
				}
		return false;
	}

	@Override
	public boolean setContextVariable(IResourceAgent ra, Method cv) {
		// Verifying if it's really a CV
		if (cv.isAnnotationPresent(ContextVariable.class)) {
			this.cv = cv;
			this.ra = ra;
			ra.registerStakeholder(cv.getName(), this.getRAI());
		}
		return false;
	}

	// @Override
	public boolean setConditionalResult(Operator op, Object constant, Object result) {
		// Verify if the ConditionalResult's expression intersects any other
		// ConditionalResult's expression domain
		/*
		 * Set<Tuple<Operation, Object>> expr = cr.getExpression(); //Compare
		 * all tuples from the expression with each CR from the list for
		 * (ConditionalResult craux : conditionalResultSet) { // Each tuple in
		 * the expression determines a domain for (Tuple<Operation, Object> tp :
		 * expr) { switch (op) { case Equal: // Verify if there is another
		 * object equal // one-by-one
		 * 
		 * break; case Different: // Verify if the expression contains another
		 * // condition that is not compared by // "Different" operation,
		 * otherwise it doesn't // have a domain
		 * 
		 * break; case GreaterThan:
		 * 
		 * break; case LessThan:
		 * 
		 * break; case GreaterThanOrEqual:
		 * 
		 * break; case LessThanOrEqual:
		 * 
		 * break; } } }
		 */

		// FIXME: Assuming that the domains don't intersect each other
		ConditionalResult cr = new ConditionalResult();
		cr.setResult(result);
		cr.setComparison(op, constant);
		conditionalResultSet.add(cr);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.uff.tempo.middleware.management.IInterpreter#setConditionalResult(
	 * br.uff.tempo.middleware.management.Interpreter.ConditionalResult)
	 */
	// @Override
	public boolean setConditionalResult(ConditionalResult cr) {
		if (cr.isDefault()) {
			// Verify if there is another ConditionResult set as Default
			// There must be only one Default
			for (ConditionalResult craux : conditionalResultSet)
				if (craux.isDefault())
					return false;
		}

		// Verify if the ConditionalResult's expression intersects any other
		// ConditionalResult's expression domain
		/*
		 * Set<Tuple<Operation, Object>> expr = cr.getExpression(); //Compare
		 * all tuples from the expression with each CR from the list for
		 * (ConditionalResult craux : conditionalResultSet) { // Each tuple in
		 * the expression determines a domain for (Tuple<Operation, Object> tp :
		 * expr) { switch (op) { case Equal: // Verify if there is another
		 * object equal // one-by-one
		 * 
		 * break; case Different: // Verify if the expression contains another
		 * // condition that is not compared by // "Different" operation,
		 * otherwise it doesn't // have a domain
		 * 
		 * break; case GreaterThan:
		 * 
		 * break; case LessThan:
		 * 
		 * break; case GreaterThanOrEqual:
		 * 
		 * break; case LessThanOrEqual:
		 * 
		 * break; } } }
		 */

		// FIXME: Assuming that the domains don't intersect each other
		conditionalResultSet.add(cr);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.uff.tempo.middleware.management.IInterpreter#setConditionalResultDefault
	 * (java.lang.Object, java.lang.Object)
	 */
	// @Override
	public boolean setConditionalResultDefault(Object result) {
		// Verify if there is another ConditionResult set as Default
		// There must be only one Default
		for (ConditionalResult cr : conditionalResultSet)
			if (cr.isDefault())
				return false;
		ConditionalResult cr = new ConditionalResult();
		cr.setResult(result);
		cr.setDefault(true);
		return true;
	}

	// @Override
	public Method getContextVariable() {
		return this.cv;
	}

	// @Override
	public int interpretToInt() {
		int interpreted = Integer.parseInt(evaluate().toString());
		return interpreted;
	}

	// @Override
	public long interpretToLong() {
		long interpreted = Long.parseLong(evaluate().toString());
		return interpreted;
	}

	// @Override
	public boolean interpretToBoolean() {
		boolean interpreted = Boolean.parseBoolean(evaluate().toString());
		return interpreted;
	}

	// @Override
	public String interpretToString() {
		Object o = evaluate();
		return o == null ? "" : o.toString();
	}

	// @Override
	public Object interpretToObject() {
		return evaluate();
	}

	private Object evaluate() {
		Object result = null;
		try {
			// Context Variable
			Object cv = this.cv.invoke(ra, new Object[0]);

			ConditionalResult crDefault = null;
			for (ConditionalResult cr : this.conditionalResultSet) {
				if (cr.isDefault())
					crDefault = cr;
				else {
					boolean valid = false;
					for (Tuple<Operator, Object> tp : cr.expression) {
						switch ((Operator) tp.value) {
						case Equal:
							valid = tp.value2.equals(cv);
							break;
						case Different:
							valid = !tp.value2.equals(cv);
							break;
						case GreaterThan:
							try {
								float val = Float.parseFloat(tp.value2.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl > val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg > val;
								} catch (Exception eex) {
									Log.e(TAG, "Error: comparing with GreaterThan the value: \"" + cv.toString() + "\" and \"" + tp.value2.toString() + "\"", ee);
								}
							}
							break;
						case LessThan:
							try {
								float val = Float.parseFloat(tp.value2.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl < val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg < val;
								} catch (Exception eex) {
									Log.e(TAG, "Error: comparing with LessThan the value: \"" + cv.toString() + "\" and \"" + tp.value2.toString() + "\"", ee);
								}
							}
							break;
						case GreaterThanOrEqual:
							try {
								float val = Float.parseFloat(tp.value2.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl >= val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg >= val;
								} catch (Exception eex) {
									Log.e(TAG, "Error: comparing with GreaterThanOrEqual the value: \"" + cv.toString() + "\" and \"" + tp.value2.toString() + "\"", ee);
								}
							}
							break;
						case LessThanOrEqual:
							try {
								float val = Float.parseFloat(tp.value2.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl >= val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg >= val;
								} catch (Exception eex) {
									Log.e(TAG, "Error: comparing with LessThanOrEqual the value: \"" + cv.toString() + "\" and \"" + tp.value2.toString() + "\"", ee);
								}
							}
							break;
						default:
							Log.e(TAG, "Error: no operation");
						}

						if (!valid) // Then expression's condition failed in the
									// comparison
							break; // break to try another expression
					}
					if (valid) // Then one expression's condition returned true
						result = cr.getResult();
					else if (crDefault != null) // Otherwise use the Default
												// case, if it does exist
						result = crDefault.getResult();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: context variable method invoking error", e);
		}
		return result; // TODO: what to do if the result is null?
	}

	/**
	 * ConditionalResult includes the expression (defined by a set of
	 * comparisons with the value) and the result, where the result must be
	 * retrieved if the expression is true
	 */
	public class ConditionalResult {
		/**
		 * Result that must be returned if the respective expression is valid
		 */
		private Object result = null;

		/**
		 * Expression data structure. Set of tuples composed by an operation and
		 * an object to be compared to the CV
		 */
		private Set<Tuple<Operator, Object>> expression = new HashSet<Tuple<Operator, Object>>();

		/**
		 * isDefaultResult tells if this is the default result, in case all
		 * other ConditionalResults return false then this result must be used
		 */
		private boolean isDefaultResult = false;

		public void setResult(Object result) {
			this.result = result;
		}

		/**
		 * A ConditionalResult that is default doesn't need comparisons then if
		 * a comparison is added, it's set as not default
		 */
		public void setComparison(Operator op, Object constant) {
			this.isDefaultResult = false;
			expression.add(new Tuple<Operator, Object>(op, constant));
		}

		public void setDefault(Object constant) {
			this.isDefaultResult = true;
			result = constant;
		}

		public Object getResult() {
			return this.result;
		}

		public Set<Tuple<Operator, Object>> getExpression() {
			return this.expression;
		}

		public boolean isDefault() {
			return this.isDefaultResult;
		}
	}

	@Override
	public String getResourceClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerStakeholder(String method, String rai) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// If change comes from the RA of interest
		// If change comes from the method (CV)
		Log.d(TAG, "!!!Change: " + rai + " " + method + " " + value); // FIXME:remove this

	}

	private ServiceConnection mConnectionStove = new ServiceConnection() {
		/**
		 * Resouce Agent Connected
		 */
		public void onServiceConnected(ComponentName className, IBinder service) {
			ResourceBinder binder = (ResourceBinder) service;
			ra = (IResourceAgent) binder.getService();
		}

		/**
		 * Resouce Agent Disconnected
		 */
		public void onServiceDisconnected(ComponentName className) {
			ra = null;
		}
	};

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}
}