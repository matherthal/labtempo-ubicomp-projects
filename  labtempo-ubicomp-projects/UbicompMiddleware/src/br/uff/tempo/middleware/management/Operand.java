package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.Stub;

/*
 * Operand represents one of the operands of the ComparisonNode different
 * from a single value.
 */
public class Operand<rai, cv, params> {
	private String rai = null;
	private String cv = null;
	private List<Tuple<String, Object>> params = null;
	// valueCache is the cache of value retrieved from the cv_op1 access
	private Object value = null;

	public Operand(String rai, String cv, Object[] params) {
		this.rai = rai;
		this.cv = cv;
		this.params = new ArrayList<Tuple<String, Object>>();

		if (params != null) {
			int i = 0;
			for (Object o : params) {
				this.params.add(new Tuple<String, Object>(o.toString(), o.getClass()));
				i++;
			}
		}
	}

	public Operand(Object constVal) {
		this.value = constVal;

		this.rai = null;
		this.cv = null;
		this.params = null;
	}

	public Object getVal() {
		// If the operand has only a constant value
		if (this.rai == null)
			return this.value;

		// If the operand has not yet updated its value, do it before the answer
		if (this.value == null) {
			Stub s = new Stub(rai);
			this.value = s.makeCall(cv, params, Object.class);
		}
		return this.value;
	}

	/**
	 * @return the rai
	 */
	public String getRai() {
		return rai;
	}

	/**
	 * @param rai
	 *            the rai to set
	 */
	public void setRai(String rai) {
		this.rai = rai;
	}

	/**
	 * @return the cv
	 */
	public String getCv() {
		return cv;
	}

	/**
	 * @param cv
	 *            the cv to set
	 */
	public void setCv(String cv) {
		this.cv = cv;
	}

	/**
	 * @return the params
	 */
	public List<Tuple<String, Object>> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<Tuple<String, Object>> params) {
		this.params = params;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}