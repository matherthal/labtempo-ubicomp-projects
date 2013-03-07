package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.Interpreter;
/**
 * This interface contains Aggregator methods
 * <br /><br />
 * Aggregator contains aggregation methods that add Context Variables to Interpreters of context rules
 */
public interface IAggregator {
	/**
	 * Add an Interpreter in Aggregator
	 * 
	 * @param interpreter context rule execution instance
	 * @return if is succesful   
	 */
	public boolean addContextVariable(Interpreter interpreter);

	/**
	 * Add an Context Variable in Aggregator
	 * 
	 * @param ra Resource Agent instance target
	 * @param method reference to Context Variable
	 */
	public boolean addContextVariable(IResourceAgent ra, String method);
}
