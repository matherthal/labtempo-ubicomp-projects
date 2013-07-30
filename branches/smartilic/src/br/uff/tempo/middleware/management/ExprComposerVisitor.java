package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.datastructure.PrePostVisitor;

/**
 * A visitor that composes de logical expression out of the tree
 **/
public class ExprComposerVisitor implements PrePostVisitor {
	private final static String TAG = "ExprComposerVisitor";
	private String expression = "";

	@Override
	public void preVisit(Object object) {
		String s = object.toString();
		// If formula is invalid regarding to timer and evaluation, the
		// subexpression must be marked (with an 'i') to be invalidated
		// afterwards
		if (s.equals("f"))
			s = "(";
		else if (s.equals("i"))
			s = "i";
		expression = expression + s;
	}

	@Override
	public void inVisit(Object object) {
	}

	@Override
	public void postVisit(Object object) {
		String s = object.toString();
		// if (s.equals("f") || s.equals("!"))
		if (s.equals("f"))
			expression = expression + ")";
		else if (s.equals("i")) {
			// Count backwards to find the starter 'i' in expression
			int i = expression.length() - 1;
			while (expression.charAt(i) != 'i' && i >= 0)
				--i;
			// Replace all chars between the two 'i's by a invalid:
			// subexpression "(0)"
			expression = expression.substring(0, i) + "(0)";
		}
		// if (!f.hasTimerExpired()) {
		// if (expression.charAt(expression.length() - 1) != '(')
		// expression = expression + "&&0";
		// }
	}

	@Override
	public boolean isDone() {
		return false;
	}

	public String getExpression() {
		return expression;
	}
}
