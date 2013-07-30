package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.Stub;

public class ComparisonNode {
	private String rai;
	private String method;
	private List<Tuple<String, Object>> params;
	private Operator operator = Operator.Equal;
	// valueComp is the value to compare the the change of the context variable
	private Object valueComp;
	// valueCache is the cache of value retrieved from the context variable
	// access
	private Object valueCache = null;
	private long timeout = 0;
	private boolean valid = false;

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return the valueCache
	 */
	public Object getValueCache() {
		return valueCache;
	}

	/**
	 * @param valueCache
	 *            the valueCache to set
	 */
	public void setValueCache(Object valueCache) {
		this.valueCache = valueCache;
		// if (!test())
		// timerReset();
	}

	/**
	 * Update value stored in valueCache, result from context variable query or
	 * notification
	 */
	public void updateValueCache() {
		Stub s = new Stub(rai);
		setValueCache(s.makeCall(method, params, Object.class));
	}

	/**
	 * @return the value
	 */
	public Object getValueComp() {
		return valueComp;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValueComp(Object value) {
		this.valueComp = value;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the rai
	 */
	public String getRai() {
		return rai;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Constructor
	 * 
	 * @param ra
	 * @param method
	 * @param operator
	 * @param valueComp
	 *            It creates a condition
	 * @throws Exception
	 */
	public ComparisonNode(String rai, String method, Object[] params, Operator operator, Object value, long timeout)
			throws Exception {
		this.rai = rai;
		this.method = method;

		this.params = new ArrayList<Tuple<String, Object>>();
		if (params != null) {
			int i = 0;
			for (Object o : params) {
				this.params.add(new Tuple<String, Object>(o.toString(), o.getClass()));
				i++;
			}
		}

		this.operator = operator;
		this.valueComp = value;
	}

	public boolean evaluate() {
		if (this.valueCache == null)
			updateValueCache();
		// Parse value to attribute's type. Ex., if it is boolean, so we parse
		// the value to boolean before compare
		if (operator.equals(Operator.Equal)) // Operator ==
			this.valid = this.valueCache.equals(valueComp);
		else if (operator.equals(Operator.Different)) // Operator !=
			this.valid = !this.valueCache.equals(valueComp);
		else {
			double d_val = Double.parseDouble(valueComp.toString());
			double d_ret = Double.parseDouble(this.valueCache.toString());
			if (operator.equals(Operator.GreaterThan))
				this.valid = d_ret > d_val;
			else if (operator.equals(Operator.GreaterThanOrEqual))
				this.valid = d_ret >= d_val;
			else if (operator.equals(Operator.LessThan))
				this.valid = d_ret < d_val;
			else if (operator.equals(Operator.LessThanOrEqual))
				this.valid = d_ret <= d_val;
			else
				this.valid = false;
		}
		return isValid();
	}

	@Override
	public String toString() {
		// return this.rai() + " " + this.method + " " + this.operator + " " +
		// this.value;
		return "";
	}
}
