package br.uff.tempo.middleware.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.Serializable;
import java.lang.reflect.Method;

/*import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
*/

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;


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
		
		//IResourceDiscovery rds = getRDS();
		//ArrayList<String> rAlist = rds.search("StoveAgent/stovekitchen");
		//StoveStub stubStove = new StoveStub(rAlist.get(0)); 
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//register();
	}

	public void addCondition(ResourceAgent ra, Method method, String operator, Object value) throws Exception {
		
	}
	
	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
	}
	
	private class Script {
		
	}
}
