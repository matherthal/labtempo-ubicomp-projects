package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.ResourceRegisterServiceStub;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public abstract class ResourceAgent extends Service implements IResourceAgent {
	private static final String TAG = "AgentBase";

	private static final String TCP_SERVER_IP = "192.168.1.70";
	private static final int TCP_SERVER_PORT = 21111;

	// Agent's attributes
	private boolean registered;
	private int id;
	private String name;
	private String type;
	private String URL;
	private ArrayList<ResourceAgent> interests;
	private ArrayList<Stakeholder> stakeholders;
	private ResourceRegister rRS;
	private ResourceDiscovery rDS;
	private ArrayList<ResourceAgent> registeredList;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<ResourceAgent> getInterests() {
		return interests;
	}

	public void setInterests(ArrayList<ResourceAgent> interests) {
		this.interests.addAll(interests);
	}

	public ArrayList<ResourceAgent> getRegisteredList() {
		return registeredList;
	}

	private ResourceRegister rrs;
	private Caller caller;

	public class ResourceBinder extends Binder {
		public ResourceAgent getService() {
			return ResourceAgent.this;
		}
	}

	public ResourceBinder mBinder = new ResourceBinder();

	public ResourceAgent() {
		//rrs = new ResourceRegisterServiceStub();
		//caller = new Caller("localahost");// temporally
		stakeholders = new ArrayList<Stakeholder>();
		registered = false;
		URL = "";// addres+port
		id = 0;
	}

	public ResourceAgent(String name, int id) {
		stakeholders = new ArrayList<Stakeholder>();
		ResourceRepository rR = ResourceRepository.getInstance();
		stakeholders.add(new Stakeholder("update",rR));
		registered = false;
		URL = "";// addres+port
		this.id = id;
		this.name = name;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		identify();
		rDS = ResourceDiscovery.getInstance();
		registeredList = rDS.search("");//search all rR.contains("") = all IAR
		// Exists only to defeat instantiation.
		// rrs = ResourceRegisterServiceStub.getInstance();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// this.register();
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// this.register();
	}

	public boolean isRegistered() {
		return registered;
	}

	public boolean identify() {
		rrs = ResourceRegister.getInstance();
		rrs.register(this);
		String result = "";
		//int i = 0; // 5 tries
		//while (i++ < 5 && (result = rrs.getResult()) == null)
			/* sleep time */;// while not respond wait because doesn't exist RRS
		registered = true;
		return true;
	}

	public void notifyStakeholders(String change) {
		int i = 0;
		rDS = ResourceDiscovery.getInstance();
		while (i < stakeholders.size()) {
			String url = stakeholders.get(i).getUrl();
			// stakeholderStub = new ResourceAgentStub(url);
			if (change.contains(stakeholders.get(i).getMethod()))
				rDS.search(url).get(0).notificationHandler(change);//change = id, method name and value
			// stakeholders.get(i) = stakeholderStub;
			// query by url return a unique instance
		}
	}

	/**
	 * Segura notificação vinda de outra IAR
	 * 
	 * @param rA
	 *            Possui o novo estado da instância
	 */
	public abstract void notificationHandler(String change);
	
	public boolean registerStakeholder(String method, String url) {
		stakeholders.add(new Stakeholder(method,url));
		return true;
	}
	
	public boolean registerStakeholder(String method, ResourceAgent rA) {
		stakeholders.add(new Stakeholder(method,rA));
		return true;
	}
}
