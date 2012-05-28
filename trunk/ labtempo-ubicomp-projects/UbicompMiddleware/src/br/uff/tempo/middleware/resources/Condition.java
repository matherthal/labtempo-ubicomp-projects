package br.uff.tempo.middleware.resources;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Comparator;

import br.uff.tempo.middleware.management.ResourceAgent;

/**
 * Class Condition
 * 
 * @author matheus Stores the reference to the resource agent and the method
 *         used to get its context variable Also stores the value to compare to
 *         the context variable and the logical operation to be realized between
 *         them It implements the method to make the comparation, or test: the
 *         method test
 */
public class Condition implements InvocationHandler, Serializable {
	public ResourceAgent ra;
	public Method method;
	public String operator = "==";
	public Object value;
	public long timeout;

	/**
	 * Constructor
	 * 
	 * @param ra
	 * @param method
	 * @param operator
	 * @param value
	 *            It creates a condition
	 * @throws Exception
	 */
	public Condition(ResourceAgent ra, Method method, String operator,
			Object value) throws Exception {
		if (!method.getReturnType().equals(Void.TYPE)) {
			this.ra = ra;
			this.method = method;
			this.operator = operator;
			this.value = value;
			this.timeout = 0;
		} else
			throw new Exception();
	}

	/**
	 * Constructor
	 * 
	 * @param ra
	 * @param method
	 * @param operator
	 * @param value
	 *            It creates a condition
	 * @throws Exception
	 */
	public Condition(ResourceAgent ra, Method method, String operator,
			Object value, long timeout) throws Exception {
		if (!method.getReturnType().equals(Void.TYPE)) {
			this.ra = ra;
			this.method = method;
			this.operator = operator;
			this.value = value;
			this.timeout = timeout;
		} else
			throw new Exception();
	}

	public boolean test() throws Throwable {
		//Parse value to attribute's type
		//ex., if it is boolean, so we parse the value to boolean before compare
		
		Object attrib = this.invoke(ra, method, new Object[0]);
		if (operator.equals("==")) // Operator ==
			return attrib.equals(value);
		else if (operator.equals("!=")) // Operator !=
			return !attrib.equals(value);
		else
			// Operator error
			return false;
	}

	public Object invoke(Object ra, Method method, Object[] args)
			throws Throwable {
		return method.invoke(ra, args);
	}
	
	@Override
	public String toString() {
		return this.ra.getType() + "." + this.ra.getId() + "." + this.method.getName() + this.operator + this.value;
	}
}

class ConditionsComparator implements Comparator<Condition> {
	public int compare(Condition condA, Condition condB) {
		return (int) (condA.timeout - condB.timeout);
	}
}
