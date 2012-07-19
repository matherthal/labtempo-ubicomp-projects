package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.Interpreter;

public interface IAggregator {
	/*
	 * Add CV into Aggregator
	 */
	public boolean addContextVariable(Interpreter interpreter);
	public boolean addContextVariable(IResourceAgent ra, String method);
}
