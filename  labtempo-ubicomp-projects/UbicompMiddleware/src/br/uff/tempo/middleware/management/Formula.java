package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.datastructure.GeneralTree;

public class Formula extends GeneralTree {
	private long timeout = 0;
	private boolean timerexp = false;

	public Formula() {
		super("f");
	}

	public Formula(Object key) {
		super(key);
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
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
		// If there is no timer OR if there are a timer and it does have expired, then return true
		// Otherwise return false
		return timeout == 0 || (timeout != 0 && timerexp) ? true : false;				
	}
	
//	@Override
//	public String toString() {
//		return "(" + this.getSubtree().toString() + ")";
//	}
}
