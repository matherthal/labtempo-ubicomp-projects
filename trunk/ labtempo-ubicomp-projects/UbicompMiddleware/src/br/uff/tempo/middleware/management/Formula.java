package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.datastructure.GeneralTree;

public class Formula extends GeneralTree {
	private long timeout = 0;

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
