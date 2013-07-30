package br.uff.tempo.middleware.management.interfaces;

/**
 * Rule interpreter interface
 * <br /><br />
 * Rule Interpreter manages context rules
 */
public interface IRuleInterpreter extends IResourceAgent {

	/**
	 * Starting this context rule
	 */
	public void start();

	/**
	 * Stopping this context rule
	 */
	public void stop();
	
	/**
	 * @return if it context rule is started
	 */
	public boolean isStarted();
}
