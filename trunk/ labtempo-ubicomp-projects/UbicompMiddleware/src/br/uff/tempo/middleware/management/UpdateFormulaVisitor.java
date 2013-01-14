package br.uff.tempo.middleware.management;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.utils.datastructure.Visitor;

/**
 * Updates values and timers from an expression
 **/
public class UpdateFormulaVisitor implements Visitor {
	private final static String TAG = "UpdateFormulaVisitor";
	protected String rai;
	protected String method;
	protected Object value;
	protected Boolean changed = true;
	protected Formula formTimerExp = null;
	protected Evaluator evaluator = new Evaluator();
	protected Map<Object, Tuple<Timer, TimeoutTask>> timers = new HashMap<Object, Tuple<Timer, TimeoutTask>>();

	public UpdateFormulaVisitor(String rai, String method, Object value) {
		super();
		this.rai = rai;
		this.method = method;
		this.value = value;
	}

	@Override
	public void visit(Object object) {
		try {
			// If it is a Formula but not a Predicate
			if (object instanceof Formula && !(object instanceof Predicate)) {
				Formula f = (Formula) object;
				if (!f.hasTimer()) {
					f.setKey('f');
				} else {
					boolean valid = evaluate(f);
					if (!valid)
						// If it's invalid, stop any possible running timer
						timerStop(f);
					// Only if it were invalid and become valid

					if (!f.hasTimerExpired()) {
						// f does have a timer and it has not expired yet
						// If the evaluation turns out to return "invalid", then
						// the timer must be stoped,
						// which means that this subexpression will be
						// automatically taken by invalid
						// boolean valid = evaluate(f);
						if (!valid)
							f.setKey('i');
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error in updating rule interpreter tree. Error: " + e);
		}
	}

	public boolean hasChanged() {
		return changed;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	// public void formTimerExpired(Formula f) {
	// this.formTimerExp = f;
	// }

	protected boolean evaluate(Formula f) throws EvaluationException {
		ExprComposerVisitor v = new ExprComposerVisitor();
		f.depthFirstTraversal_NodeVisiting(v);
		Log.i(TAG, "EXPRESSION: " + v.getExpression());
		// If the evaluator.evaluate return "0.0" then it is false, if it
		// returns "1.0" then it's true
		return evaluator.evaluate(v.getExpression()).equals("1.0") ? true : false;
	}

	protected void timerStart(Formula f) {
		if (!timers.containsKey(f)) {
			long t = f.getTimeout() * 1000;
			timers.put(f, new Tuple<Timer, TimeoutTask>(new Timer(), new TimeoutTask(f)));
			Tuple<Timer, TimeoutTask> tp = (Tuple<Timer, TimeoutTask>) timers.get(f);
			((Timer) tp.value).schedule((TimeoutTask) tp.value2, t);
		}
		// TODO: how to know if it's already running to let it?...
	}

	protected void timerReset(Formula f) {
		timerStop(f);
		Log.i("TIME", new Date().toGMTString());

		long t = f.getTimeout() * 1000;
		Tuple<Timer, TimeoutTask> tp = (Tuple<Timer, TimeoutTask>) timers.get(f);
		((Timer) tp.value).schedule((TimeoutTask) tp.value2, t);
	}

	protected void timerStop(Formula f) {
		Tuple<Timer, TimeoutTask> tp = (Tuple<Timer, TimeoutTask>) timers.get(f);
		if (tp.value2 != null) {
			Log.i("Timer", "Stop");
			((TimeoutTask) tp.value2).cancel();
			tp.value2 = null;
		}
	}

	protected class TimeoutTask extends TimerTask {
		protected Formula f = null;

		TimeoutTask(Formula f) {
			this.f = f;
		}

		public void run() {
			Log.i("TimeoutTask", "Timeout went off!");
			Log.i("TIME", new Date().toGMTString());
			f.timerExpired(true);
			// if (evaluateExpr())
			// notifyActionPerformers();
		}
	}
}
