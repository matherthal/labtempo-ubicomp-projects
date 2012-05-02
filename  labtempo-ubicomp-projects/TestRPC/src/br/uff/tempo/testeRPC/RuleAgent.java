package br.uff.tempo.testeRPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.os.Handler;

/**
 * Class RuleAgent
 * @author matheus
 * Manages a set of Rules locally
 */
public class RuleAgent extends ResourceAgent {
	public List<Rule> rules = new ArrayList<Rule>();

	public void addRule(Rule rule) {
		rules.add(rule);
	}

	public void delRule(Rule rule) {
		rules.add(rule);
	}
	
	/**
	 * Class Rule
	 * @author matheus
	 * Contains a set of Conditions to be tested
	 */
	public class Rule {
		public int id;
		private List<Condition> conditions = new ArrayList<Condition>();
		
		private Timer scheduler;
		private String FLAG_START_RUNNING = "FLAG_START_RUNNING";
		private String FLAG_TIMEOUT = "FLAG_TIMEOUT";
		private String FLAG_CV_CHANGED = "FLAG_CV_CHANGED";
		private String FLAG_STOP_RUNNING = "FLAG_STOP_RUNNING";
		private int pNextCondition = 0; 
		
		public void addCondition(IResourceAgent ra, Method method, String operator, Object value) {
			//Add new condition
			conditions.add(new Condition(ra, method, operator, value));
			//Order conditions by its timeouts
			Collections.sort(conditions, new Comparator<Condition>() { //FIXME : TEST IF THIS WORKS
				public int compare(Condition condA, Condition condB) {
					return (condA.timeout - condB.timeout) > 0 ? 1 : -1;
				}
			});
		}
		
		public synchronized void setTimeout(long timeout) {
			if (scheduler != null) {
				scheduler.cancel();
				scheduler = null;
			}
			else {
				scheduler = new Timer();
				//scheduler.schedule(new UpdateTimeTask(), timeout);
				scheduler.schedule(new TimerTask() {
					public void run() {
						scheduler.cancel();
						scheduler = null;
						manageTimer(FLAG_TIMEOUT);
					}
				}, timeout);
			}
		}

		private void manageTimer(String flag) {
			if (flag.equals(FLAG_START_RUNNING)) {
				setTimeout(getNextTimeout());
			} else if (flag.equals(FLAG_TIMEOUT)) {
				scheduler.cancel();
				//Do the math to start another scheduler,
				setTimeout(getNextTimeout());
			}
			else if (flag.equals(FLAG_CV_CHANGED)) {
				//Test if the CV is important to this rule
				//if yes : scheduler.cancel();
				//if no : do nothing
			}
			else if (flag.equals(FLAG_STOP_RUNNING)) {
				scheduler.cancel();
			}
		}
		
		private long getNextTimeout() {
			long timeoutGT = conditions.get(pNextCondition).timeout;
			//Gets the next condition with the lesser timeout
			//It suposes that "conditions" could have repeated timeouts
			Condition conditionLS = null;
			boolean found = false;
			while (!found & conditions.size() > pNextCondition) {
				conditionLS = conditions.get(pNextCondition++);
				if (conditionLS.timeout >= timeoutGT)
					found = true; //Lesser timeout found
			}

			//If the lesser timeout was found then return it, othewise the lesser will be 0
			long timeoutLS = found ? conditionLS.timeout : 0; 
			
			//Set the timeout to be the difference between the greater and the lesser timeouts
			return timeoutGT - timeoutLS;
		}
		
		/*class UpdateTimeTask extends TimerTask {
			public void run() {
				scheduler.cancel();
				scheduler = null;
			}
		}*/
		
		/**
		 * Class Condition
		 * @author matheus
		 * Stores the reference to the resource agent and the method used to get its context variable
		 * Also stores the value to compare to the context variable and the logical operation to be realized between them
		 * It implements the method to make the comparation, or test: the method test
		 */
		public class Condition implements InvocationHandler {
			public IResourceAgent ra;
			public Method method;
			public String operator = "==";
			public Object value;
			public long timeout;
			
			/**
			 * Constructor
			 * @param ra
			 * @param method
			 * @param operator
			 * @param value
			 * It creates a condition
			 */
			public Condition(IResourceAgent ra, Method method, String operator, Object value) {
				this.ra = ra;
				this.method = method;
				this.operator = operator;
				this.value = value;
				this.timeout = 0;
			}

			/**
			 * Constructor
			 * @param ra
			 * @param method
			 * @param operator
			 * @param value
			 * It creates a condition
			 */
			public Condition(IResourceAgent ra, Method method, String operator, Object value, long timeout) {
				this.ra = ra;
				this.method = method;
				this.operator = operator;
				this.value = value;
				this.timeout = timeout;
			}
			
			public boolean test() throws Throwable {
				Object attrib = this.invoke(ra, method, new Object[0]);
				if (operator.equals("==")) //Operator ==
		            return attrib.equals(value); 
				else if (operator.equals("!=")) //Operator !=
		            return !attrib.equals(value);
				else //Operator error
					return false;
			}
			
			public Object invoke(Object ra, Method method, Object[] args) throws Throwable {
				return method.invoke(ra, args);
			}
		}
		
		class ConditionsComparator implements Comparator<Condition> {
			public int compare(Condition condA, Condition condB) {				
				return (int)(condA.timeout - condB.timeout);
			}
		}
	}
}

