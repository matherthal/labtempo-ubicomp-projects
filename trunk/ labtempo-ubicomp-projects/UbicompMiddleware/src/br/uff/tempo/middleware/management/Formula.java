package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.datastructure.GeneralTree;

public class Formula extends GeneralTree {
	private long timeout = 0;
	private boolean notClause = false; 

	/**
	 * @return the notClause
	 */
	public boolean isNotClause() {
		return notClause;
	}

	/**
	 * @param notClause the notClause to set
	 */
	public void setNotClause(boolean notClause) {
		this.notClause = notClause;
	}

	public Formula() {
		super(0);
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

	public void attachFormula(Formula f) {
		this.attachSubtree(f);
	}
}
