package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
*/

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.IResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgent;


/**
 * Class Rule
 * @author matheus
 * Contains a set of Conditions to be tested
 */
public class Rule extends ResourceAgent {
	public int id;
	private List<Condition> conditions = new ArrayList<Condition>();
	
	private Timer scheduler;
	private String FLAG_START_RUNNING = "FLAG_START_RUNNING";
	private String FLAG_TIMEOUT = "FLAG_TIMEOUT";
	private String FLAG_CV_CHANGED = "FLAG_CV_CHANGED";
	private String FLAG_STOP_RUNNING = "FLAG_STOP_RUNNING";
	private int pNextCondition = 0; 
	
	public String expression = "";
	
	public Rule()
	{
		super("br.uff.tempo.middleware.resources.Rule",6);//id hard coded
		setType("rule");
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//register();
	}
	public void addCondition(ResourceAgent ra, Method method, String operator, Object value) throws Exception {
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
	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
	}
	
	/*public boolean runScript(String script) throws MalformedBooleanException {
		String strBoolExpr = "!true&&false||true";
		BooleanExpression boolExpr = BooleanExpression.readLeftToRight(strBoolExpr);
		boolean bool = boolExpr.booleanValue();
		return bool;
	}*/
	
	private class Script {
		
	}

	@Override
	public List<Tuple<String, Method>> getAttribs() throws SecurityException, NoSuchMethodException {
		return null;
	}
}
