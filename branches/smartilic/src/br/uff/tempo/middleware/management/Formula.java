package br.uff.tempo.middleware.management;

import org.json.JSONArray;

import br.uff.tempo.middleware.management.utils.datastructure.GeneralTree;

public class Formula extends GeneralTree {
	protected long timeout = 0;
	protected boolean timerexp = false;
	protected RuleInterpreter timerStakeholder = null;
	protected boolean eval = false;
	protected int id = 0;

	public Formula() {
		super("f");
	}

	public Formula(Object key) {
		super(key);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @return the timerStakeholder
	 */
	public RuleInterpreter getTimerStakeholder() {
		return timerStakeholder;
	}

	/**
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param timerStakeholder
	 */
	public void setTimerStakeholder(RuleInterpreter ri) {
		this.timerStakeholder = ri;
	}

	/**
	 * @param eval
	 */
	public void setEval(boolean eval) {
		this.eval = eval;
		if (!eval)
			this.timerexp = false; 
	}

	public void attachFormula(Formula f) {
		this.attachSubtree(f);
	}

	public void setAndClause() {
		this.attachSubtree(new AndNode());
	}

	public void setOrClause() {
		this.attachSubtree(new OrNode());
	}

	public void timerExpired(boolean b) {
		this.timerexp = b;
	}

	public boolean hasTimer() {
		return timeout > 0;
	}

	public boolean hasTimerExpired() {
		// If there is no timer OR if there are a timer and it does have
		// expired, then return true
		// Otherwise return false
		return timeout == 0 || (timeout != 0 && timerexp) ? true : false;
	}

	public boolean getEval() {
		return eval;
	}

	@Override
	public String toString() {
		// return "(" + this.getSubtree().toString() + ")";
		return key.toString();
	}
}
