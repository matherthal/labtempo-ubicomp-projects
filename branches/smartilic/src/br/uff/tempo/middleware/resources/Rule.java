package br.uff.tempo.middleware.resources;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import br.uff.tempo.middleware.management.ResourceAgent;

/**
 * Class Rule
 * 
 * @author matheus Contains a set of Conditions to be tested
 */
public class Rule extends ResourceAgent {
	
	private static final long serialVersionUID = 1L;
	
	public int id;
	private List<Condition> conditions = new ArrayList<Condition>();

	private Timer scheduler;
	private String FLAG_START_RUNNING = "FLAG_START_RUNNING";
	private String FLAG_TIMEOUT = "FLAG_TIMEOUT";
	private String FLAG_CV_CHANGED = "FLAG_CV_CHANGED";
	private String FLAG_STOP_RUNNING = "FLAG_STOP_RUNNING";
	private int pNextCondition = 0;

	public String expression = "";

	public Rule() {
		super("Rule", "br.uff.tempo.middleware.resources.Rule", "Rule");

		// IResourceDiscovery rds = getRDS();
		// ArrayList<String> rAlist = rds.search("StoveAgent/stovekitchen");
		// StoveStub stubStove = new StoveStub(rAlist.get(0));
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// register();
	}

	public void addCondition(ResourceAgent ra, Method method, String operator, Object value) throws Exception {

	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	private class Script {

	}
}
