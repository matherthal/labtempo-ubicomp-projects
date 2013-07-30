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
	protected Map<Integer, Tuple<Timer, TimeoutTask>> timers = new HashMap<Integer, Tuple<Timer, TimeoutTask>>();

	public UpdateFormulaVisitor(String rai, String method, Object value) {
		super();
		this.rai = rai;
		this.method = method;
		this.value = value;
	}

	@Override
	public void visit(Object object) {
		try {
			if (object instanceof Formula) {
				String key = ((Formula) object).getKey().toString();
				if (key.equals("f") || key.equals("i")) {
					Formula f = (Formula) object;
					if (!f.hasTimer()) {
						f.setKey('f');
					} else {
						boolean eval = evaluate(f);
						boolean preEval = f.getEval();
						if (!eval) {
							// If it's invalid, stop any possible running timer
							timerStop(f);
						} else if (!preEval) {
							// If value changed from false to true, start timer
							timerReset(f);
							if (!f.hasTimerExpired()) {
								// If timer has not expired, predicate must be invalidated
								f.setKey('i');
								eval = false;
							}
						}
						f.setEval(eval);
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

	protected boolean evaluate(Formula f) throws EvaluationException {
		ExprComposerVisitor v = new ExprComposerVisitor();
		f.depthFirstTraversal_NodeVisiting(v);
		// Log.i(TAG, "EXPRESSION: " + v.getExpression());
		// If the evaluator.evaluate return "0.0" then it is false, if it
		// returns "1.0" then it's true
		return evaluator.evaluate(v.getExpression()).equals("1.0") ? true : false;
	}

	protected void timerStart(Formula f) {
		if (!timers.containsKey(f.getId())) {
			long t = f.getTimeout() * 1000;
			Tuple<Timer, TimeoutTask> tp = new Tuple<Timer, TimeoutTask>(new Timer(), new TimeoutTask(f));
			timers.put(f.getId(), tp);
			((Timer) tp.value).schedule((TimeoutTask) tp.value2, t);
		}
		// TODO: how to know if it's already running to let it?...
	}

	protected void timerReset(Formula f) {
		timerStop(f);
		Log.i("TIME", new Date().toGMTString());
		long t = f.getTimeout() * 1000;
		Tuple<Timer, TimeoutTask> tp = (Tuple<Timer, TimeoutTask>) timers.get(f.getId());
		if (tp == null) {
			tp = new Tuple<Timer, TimeoutTask>(new Timer(), new TimeoutTask(f));
			timers.put(f.getId(), tp);
		}
		((Timer) tp.value).schedule((TimeoutTask) tp.value2, t);
	}

	protected void timerStop(Formula f) {
		Tuple<Timer, TimeoutTask> tp = (Tuple<Timer, TimeoutTask>) timers.get(f.getId());
		if (tp != null)
			if (tp.value2 != null) {
				Log.i("Timer", "Stop");
				TimeoutTask task = (TimeoutTask) tp.value2;
				task.cancel();
				timers.remove(f.getId());
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
			f.setEval(true);
			f.setKey("f");
			f.getTimerStakeholder().notificationHandler("", "", null);
		}
	}
}
