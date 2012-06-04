package br.uff.tempo.middleware.management.interfaces;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import br.uff.tempo.middleware.management.Interpreter;

public interface IAggregator {
	//public Method[] contextVariables = null;
	public Set<Interpreter> contextVariables = new HashSet<Interpreter>();
	
	/*
	 * Add CV into Aggregator
	 */
	public boolean addContextVariable(Interpreter interpreter);
}
