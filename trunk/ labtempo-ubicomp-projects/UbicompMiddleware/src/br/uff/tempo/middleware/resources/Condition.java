package br.uff.tempo.middleware.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.Operator;
import br.uff.tempo.middleware.management.RuleInterpreter;
import br.uff.tempo.middleware.management.stubs.Stub;

/**
 * Class Condition
 * 
 * @author matheus Stores the reference to the resource agent and the method
 *         used to get its context variable Also stores the value to compare to
 *         the context variable and the logical operation to be realized between
 *         them It implements the method to make the comparation, or test: the
 *         method test
 */
public class Condition implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String rai;
	private String method;
	private List<Tuple<String, Object>> params;
	private Operator operator = Operator.Equal;
	private Object value;
	private long timeout;
	private Timer timer;
	private TimeoutTask timeTask = null;
	private RuleInterpreter ri = null;
	private boolean valid = false;

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
		if (!test())
			timerReset();
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the rai
	 */
	public String getRai() {
		return rai;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
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
	public Condition(String rai, String method, Object[] params, Operator operator, Object value, long timeout, RuleInterpreter ri)
			throws Exception {
		this.rai = rai;
		this.method = method;
		// this.params = params;

		this.params = new ArrayList<Tuple<String, Object>>();
		if (params != null) {
			int i = 0;
			for (Object o : params) {
				this.params.add(new Tuple<String, Object>(o.toString(), o.getClass()));
				i++;
			}
		}

		this.operator = operator;
		this.value = value;
		this.ri = ri;

		if (timeout > 0) {
			this.timeout = timeout;
			timerReset();
		} else {
			this.timeout = 0;
			this.timer = null;
		}
	}

	private boolean test() {
		// Parse value to attribute's type
		// ex., if it is boolean, so we parse the value to boolean before
		// compare

		// IResourceAgent ra = new ResourceAgentStub(this.ra.getRAI());
		Stub s = new Stub(rai);
		Object ret = s.makeCall(method, params, Object.class);
		// ret = ra.getClass().getMethod(method, paramTypes).invoke(params);

		if (operator.equals(Operator.Equal)) // Operator ==
			return ret.equals(value);
		else if (operator.equals(Operator.Different)) // Operator !=
			return !ret.equals(value);
		else {
			double d_val = Double.parseDouble(value.toString());
			double d_ret = Double.parseDouble(ret.toString());
			if (operator.equals(Operator.GreaterThan))
				return d_ret > d_val;
			if (operator.equals(Operator.GreaterThanOrEqual))
				return d_ret >= d_val;
			if (operator.equals(Operator.LessThan))
				return d_ret < d_val;
			if (operator.equals(Operator.LessThanOrEqual))
				return d_ret <= d_val;
			else
				return false;
		}
	}

	@Override
	public String toString() {
		// return this.ra.getType() + "." + this.ra.getId() + "." + this.method
		// + this.operator + this.value;
		return "";
	}

	public void timerReset() {
		timeTask = null;
		this.timer = new Timer();
		timeTask = new TimeoutTask();
		timeTask.ri = this.ri;
		timeTask.c = this;
		timer.scheduleAtFixedRate(timeTask, 0, this.timeout * 1000);
	}

	class TimeoutTask extends TimerTask {
		protected boolean timeoutArrived = false;
		protected RuleInterpreter ri;
		protected Condition c;

		public void run() {
			if (test()) {
				this.timeoutArrived = true;
				c.value = true;
				// ri.ConditionTimeout();
			}
		}
	}
}

// class ConditionsComparator implements Comparator<Condition> {
// public int compare(Condition condA, Condition condB) {
// return (int) (condA.timeout - condB.timeout);
// }
// }
