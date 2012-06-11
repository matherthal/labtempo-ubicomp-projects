package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IInterpreter;
import br.uff.tempo.middleware.management.interfaces.IInterpreter.Operation;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.ContextVariable;

public class Interpreter implements IInterpreter {
	private static final String TAG = "Interpreter";
	public Method cv = null;
	public Operation op = null;
	public Object constant = null;
	// Set of conditional results
	public Set<ConditionalResult> conditionalResultSet = new HashSet<ConditionalResult>();

	
	/* (non-Javadoc)
	 * @see br.uff.tempo.middleware.management.IInterpreter#setContextVariable(java.lang.reflect.Method)
	 */
	//@Override
	public boolean setContextVariable(Method cv) {
		// Verifying if it's really a CV
		if (cv.isAnnotationPresent(ContextVariable.class)) {
			this.cv = cv;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see br.uff.tempo.middleware.management.IInterpreter#setConditionalResult(br.uff.tempo.middleware.management.Interpreter.Operation, java.lang.Object, java.lang.Object)
	 */
	//@Override
	public boolean setConditionalResult(Operation op, Object constant, Object result) {
		// Verify if the ConditionalResult's expression intersects any other
		// ConditionalResult's expression domain
		/*Set<Tuple<Operation, Object>> expr = cr.getExpression();
		//Compare all tuples from the expression with each CR from the list
		for (ConditionalResult craux : conditionalResultSet) {
			// Each tuple in the expression determines a domain
			for (Tuple<Operation, Object> tp : expr) {
				switch (op) {
				case Equal: // Verify if there is another object equal
							// one-by-one

					break;
				case Different: // Verify if the expression contains another
								// condition that is not compared by
								// "Different" operation, otherwise it doesn't
								// have a domain

					break;
				case GreaterThan:

					break;
				case LessThan:

					break;
				case GreaterThanOrEqual:

					break;
				case LessThanOrEqual:

					break;
				}
			}
		}*/
		
		//FIXME: Assuming that the domains don't intersect each other 
		ConditionalResult cr = new ConditionalResult();
		cr.setResult(result);
		cr.setComparison(op, constant);
		conditionalResultSet.add(cr);
		return true;
	}

	/* (non-Javadoc)
	 * @see br.uff.tempo.middleware.management.IInterpreter#setConditionalResult(br.uff.tempo.middleware.management.Interpreter.ConditionalResult)
	 */
	//@Override
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
		/*Set<Tuple<Operation, Object>> expr = cr.getExpression();
		//Compare all tuples from the expression with each CR from the list
		for (ConditionalResult craux : conditionalResultSet) {
			// Each tuple in the expression determines a domain
			for (Tuple<Operation, Object> tp : expr) {
				switch (op) {
				case Equal: // Verify if there is another object equal
							// one-by-one

					break;
				case Different: // Verify if the expression contains another
								// condition that is not compared by
								// "Different" operation, otherwise it doesn't
								// have a domain

					break;
				case GreaterThan:

					break;
				case LessThan:

					break;
				case GreaterThanOrEqual:

					break;
				case LessThanOrEqual:

					break;
				}
			}
		}*/
		
		//FIXME: Assuming that the domains don't intersect each other 
		conditionalResultSet.add(cr);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see br.uff.tempo.middleware.management.IInterpreter#setConditionalResultDefault(java.lang.Object, java.lang.Object)
	 */
	//@Override
	public boolean setConditionalResultDefault(Object constant, Object result) {
		//Verify if there is another ConditionResult set as Default
		//There must be only one Default
		for (ConditionalResult cr : conditionalResultSet) 
			if (cr.isDefault())
				return false;
		ConditionalResult cr = new ConditionalResult();
		cr.setResult(result);
		cr.setDefault(true);
		return true;
	}

	//@Override
	public Method getContextVariable() {
		return this.cv;
	}

	//@Override
	public int interpretToInt() {
		int interpreted = Integer.parseInt(evaluate().toString());
		return interpreted;
	}

	//@Override
	public long interpretToLong() {
		long interpreted = Long.parseLong(evaluate().toString());
		return interpreted;
	}

	//@Override
	public boolean interpretToBoolean() {
		boolean interpreted = Boolean.parseBoolean(evaluate().toString());
		return interpreted;
	}

	//@Override
	public String interpretToString() {
		return evaluate().toString();
	}

	//@Override
	public Object interpretToObject() {
		return evaluate();
	}

	private Object evaluate() {
		try {
			// Context Variable
			Object cv = this.cv.invoke(null, new Object[0]);

			ConditionalResult crDefault = null;
			for (ConditionalResult cr : this.conditionalResultSet) {
				if (cr.isDefault())
					crDefault = cr;
				else {
					boolean valid = false;
					for (Tuple<Operation, Object> tp : cr.expression) {
						switch ((Operation) tp.value) {
						case Equal:
							valid = tp.value2.equals(cv);
							break;
						case Different:
							valid = !tp.value2.equals(cv);
							break;
						case GreaterThan:
							try {
								float val = Float.parseFloat(tp.value2
										.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl > val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2
											.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg > val;
								} catch (Exception eex) {
									Log.e(TAG,
											"Error: comparing with GreaterThan the value: \""
													+ cv.toString()
													+ "\" and \""
													+ tp.value2.toString()
													+ "\"", ee);
								}
							}
							break;
						case LessThan:
							try {
								float val = Float.parseFloat(tp.value2
										.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl < val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2
											.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg < val;
								} catch (Exception eex) {
									Log.e(TAG,
											"Error: comparing with LessThan the value: \""
													+ cv.toString()
													+ "\" and \""
													+ tp.value2.toString()
													+ "\"", ee);
								}
							}
							break;
						case GreaterThanOrEqual:
							try {
								float val = Float.parseFloat(tp.value2
										.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl >= val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2
											.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg >= val;
								} catch (Exception eex) {
									Log.e(TAG,
											"Error: comparing with GreaterThanOrEqual the value: \""
													+ cv.toString()
													+ "\" and \""
													+ tp.value2.toString()
													+ "\"", ee);
								}
							}
							break;
						case LessThanOrEqual:
							try {
								float val = Float.parseFloat(tp.value2
										.toString());
								float cvfl = Float.parseFloat(cv.toString());
								valid = cvfl >= val;
							} catch (Exception ee) {
								try {
									long val = Long.parseLong(tp.value2
											.toString());
									long cvlg = Long.parseLong(cv.toString());
									valid = cvlg >= val;
								} catch (Exception eex) {
									Log.e(TAG,
											"Error: comparing with LessThanOrEqual the value: \""
													+ cv.toString()
													+ "\" and \""
													+ tp.value2.toString()
													+ "\"", ee);
								}
							}
							break;
						default:
							Log.e(TAG, "Error: no operation");
						}

						if (!valid) // Then expression's condition failed in the comparison
							break; // break to try another expression
					}
					if (valid) // Then all the expression's condition returned true
						return cr.getResult();
					else if (crDefault != null) // Otherwise use the Default
												// case, if it does exist
						return crDefault.getResult();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error: context variable method invoking error", e);
		}
		return null;
	}
	
	
	/**
	 * ConditionalResult includes the expression (defined by a set of comparisons with the value)
	 * and the result, where the result must be retrieved if the expression is true
	 */
	public class ConditionalResult {
		/**
		 * Result that must be returned if the respective expression is valid 
		 */
		private Object result = null;
		
		/**
		 * Expression data structure. 
		 * Set of tuples composed by an operation and an object to be compared to the CV
		 */
		private Set<Tuple<Operation, Object>> expression = new HashSet<Tuple<Operation,Object>>();
		
		/**
		 * isDefaultResult tells if this is the default result, in case all other ConditionalResults
		 * return false then this result must be used 
		 */
		private boolean isDefaultResult = false;
		
		public void setResult(Object result) {
			this.result = result;
		}
		
		/**
		 * A ConditionalResult that is default doesn't need comparisons
		 * then if a comparison is added, it's set as not default 
		 */
		public void setComparison(Operation op, Object constant) {
			this.isDefaultResult = false;
			expression.add(new Tuple<Operation, Object>(op, constant));
		}
		
		public void setDefault(Object constant) {
			this.isDefaultResult = true;
			result = constant;
		}
		
		public Object getResult() {
			return this.result;
		}
		
		public Set<Tuple<Operation, Object>> getExpression() {
			return this.expression;
		}
		
		public boolean isDefault() {
			return this.isDefaultResult;
		}
	}
}