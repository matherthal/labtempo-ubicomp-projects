package br.uff.tempo.middleware.management.interfaces;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.R.integer;
import br.uff.tempo.middleware.management.Interpreter.ConditionalResult;

public interface IInterpreter {
	// Operation are the possible comparison clauses used to compare CV to the
	// constants in ConditionalResultsSet
	public enum Operation {
		Equal, Different, GreaterThan, LessThan, GreaterThanOrEqual, LessThanOrEqual
	};

	public abstract boolean setContextVariable(Method cv);

	/** Used to define expression of a Conditional Value with one or more
	 * conditions.<br><br> 
	 * 
	 * ConditionalResult includes the expression (defined by a set of
	 * comparisons with the value) and the result, where the result must be
	 * retrieved if the expression is true. It's used to pass an expression with
	 * more than one condition to be evaluated with the Context Variable before
	 * setting the result.<br> 
	 * Example: in a mathematical sense we could have<br> 
	 * CV = {"blue" | 1 < value <= 3 and value != 2} <br>
	 * We would define this as one Conditional result with 3 comparisons: 
	 * <li>value is greater than 1</li>
	 * <li>value is greater than or equal to 3</li>
	 * <li>value is different from 2</li><br>
	 * And if they are valid, we could set the result as "blue"<br>
	 * <br>
	 * Another example: CV = {"nice" | value = "blue" or value = "green"}<br>
	 * <br>
	 * Attention! The "and" clause in the first example and the "or" clause and
	 * the second example are both in a sense of sum.
	 */
	public abstract boolean setConditionalResult(Operation op, Object constant,
			Object result);

	/** Used to define expression of a Conditional Value with one or more
	 * conditions.<br><br> 
	 * 
	 * ConditionalResult includes the expression (defined by a set of
	 * comparisons with the value) and the result, where the result must be
	 * retrieved if the expression is true. It's used to pass an expression with
	 * more than one condition to be evaluated with the Context Variable before
	 * setting the result.<br> 
	 * Example: in a mathematical sense we could have<br> 
	 * CV = {"blue" | 1 < value <= 3 and value != 2} <br>
	 * We would define this as one Conditional result with 3 comparisons: 
	 * <li>value is greater than 1</li>
	 * <li>value is greater than or equal to 3</li>
	 * <li>value is different from 2</li><br>
	 * And if they are valid, we could set the result as "blue"<br>
	 * <br>
	 * Another example: CV = {"nice" | value = "blue" or value = "green"}<br>
	 * <br>
	 * Attention! The "and" clause in the first example and the "or" clause and
	 * the second example are both in a sense of sum.
	 */
	public abstract boolean setConditionalResult(ConditionalResult cr);

	public abstract boolean setConditionalResultDefault(Object constant,
			Object result);
	
	public abstract Method getContextVariable();

	public abstract int interpretToInt();
	public abstract long interpretToLong();
	public abstract boolean interpretToBoolean();
	public abstract String interpretToString();
	public abstract Object interpretToObject();
	
}