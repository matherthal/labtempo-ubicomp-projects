package br.uff.tempo.middleware.management;

import android.util.Log;

public class UpdatePredicatesVisitor extends UpdateFormulaVisitor {
	private final static String TAG = "UpdatePredicatesVisitor";

	public UpdatePredicatesVisitor(String rai, String method, Object value) {
		super(rai, method, value);
	}

	@Override
	public void visit(Object object) {
		try {
			// Visit Predicates only
			if (object instanceof Predicate) {
				Predicate pred = (Predicate) object;
				boolean preUp = pred.evaluate();
				// Update first Operand
				updateOperand(pred.getOp1());
				// Update second Operand
				updateOperand(pred.getOp2());
				// Update valid from the predicate
				boolean valid = pred.evaluate();
				if (pred.getTimeout() != 0) {
					if (!valid)
						// If it's invalid, stop any possible running timer
						timerStop(pred);
					else if (!pred.hasTimerExpired())
						// If timer has not expired, predicate must be
						// invalidated
						pred.setValid(false);
					else if (!preUp)
						// Only if it were invalid and become valid and
						// timer has not expired yet, must the timer start
						timerReset(pred);
				}
			} 
		} catch (Exception e) {
			Log.e(TAG, "Error in updating rule interpreter tree. Error: " + e);
		}
	}

	private void updateOperand(Operand op) {
		try {
			if (op != null)
				if (!op.isConstant() && op.getRai().equals(super.rai) && op.getCv().equals(method))
					if (value.equals(op.getVal()))
						changed = false;
					else {
						op.setValue(value);
						changed = true;
					}
		} catch (Exception e) {
			Log.e(TAG, "Error in updating operand: " + op.toString() + " Error: " + e);
		}
	}
}