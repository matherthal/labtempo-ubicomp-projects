package br.uff.tempo.middleware.management;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.Caller;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public abstract class ResourceAgent extends Service implements IResourceAgent, Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final String TAG = "AgentBase";

	// Agent's attributes
	private boolean registered;
	private int id;
	private String name;
	private String type;
	private String rai;
	private ArrayList<ResourceAgent> interests;
	private ArrayList<Stakeholder> stakeholders;
	private ResourceRegister rRS;
	private static IResourceDiscovery rDS;
	private ArrayList<String> registeredList;
	private String RDS_URL;
	
	private Position position;

	// public static IResourceDiscovery getRDS()
	public IResourceDiscovery getRDS() {
		return rDS;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getRAI() {
		return this.rai;
	}

	@Override
	public void setRAI(String rai) {
		this.rai = rai;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public ArrayList<ResourceAgent> getInterests() {
		return interests;
	}

	@Override
	public void setInterests(ArrayList<ResourceAgent> interests) {
		this.interests.addAll(interests);
	}

	@Override
	public ArrayList<String> getRegisteredList() {
		return registeredList;
	}

	@Override
	public String getResourceClassName() {
		return this.getClass().getName();
	}

	private IResourceRegister rrs;
	private Caller caller;

	public class ResourceBinder extends Binder {
		public ResourceAgent getService() {
			return ResourceAgent.this;
		}
	}

	public ResourceBinder mBinder = new ResourceBinder();

	public ResourceAgent() {// depreciated

		this("", "", 0);
	}

	public ResourceAgent(String type, int id) {

		this(id + "", type, id);
	}

	public ResourceAgent(String name, String type, int id) {

		this(name, type, id, null);
	}
	
	public ResourceAgent(String name, String type, int id, Position position) {

		stakeholders = new ArrayList<Stakeholder>();

		// stakeholders.add(new Stakeholder("update",rR));
		registered = false;

		this.type = type;// address+port+type+name
		this.id = id;
		this.name = name;
		rai = "";

		// WifiManager wifiManager = (WifiManager)
		// getSystemService(WIFI_SERVICE);

		// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ipAddress = ResourceAgentIdentifier.getLocalIpAddress(); // Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

		rai = ResourceAgentIdentifier.generateRAI(ipAddress, type, name);

		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);

		this.position = position;
		// initResource();
		
		this.registerDefaultInterests();
	}

	private void registerDefaultInterests() {
		// TODO: André - add default interests related with context variables and services
		// TODO: André - find methods annotated with @ContextVariable and @Service and register in communication API
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initResource();
		registeredList = rDS.search("");// search all rR.contains("") = all IAR
		// Exists only to defeat instantiation.
		// rrs = ResourceRegisterServiceStub.getInstance();
	}

	private void initResource() {
		// rDS = new
		// ResourceDiscoveryStub("rai:127.0.0.1//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery");
		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		identify();
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

	@Override
	public boolean isRegistered() {
		return registered;
	}

	@Override
	public boolean identify() {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));

			if (position != null) {
				registered = rrs.registerLocation(this.rai, this.position);
			} else {
				registered = rrs.register(this.rai);
			}
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
		}

		return registered;
	}

	public boolean identifyPosition(Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));
			this.position = position;
			registered = rrs.registerLocation(this.rai, this.position);
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
		}

		return registered;
	}
	
	public boolean identifyInPlace(String placeName, Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));
			this.position = position;
			registered = rrs.registerInPlace(this.rai, placeName, this.position);
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
		}

		return registered;
	}
	
	@Override
	@Deprecated()
	public void notifyStakeholders(String change) throws JSONException {
		String rai = (String) JSONHelper.getChange("id", change);
		String method = (String) JSONHelper.getChange("method", change);
		Object value = JSONHelper.getChange("value", change);
		
		int i = 0;
		while (i < stakeholders.size()) {
			String url = stakeholders.get(i).getRAI();
			// stakeholderStub = new ResourceAgentStub(url);
			if (change.contains(stakeholders.get(i).getMethod()) || stakeholders.get(i).getMethod().equalsIgnoreCase("all"))
				new ResourceAgentStub(url).notificationHandler(rai, method, value);
			// change = id, method name and value
			// query by url return a unique instance
			i++;
		}
	}
	
	public void notifyStakeholders(String method, Object value) {
		for (Stakeholder stakeholder : stakeholders) {
			if (stakeholder.getMethod().equals(method) || stakeholder.getMethod().equalsIgnoreCase("all")) {
				new ResourceAgentStub(stakeholder.getRAI()).notificationHandler(this.getRAI(), method, value);
				Log.d("SmartAndroid", String.format("notifying stakeholder: %s method: %s value: %s", stakeholder.getRAI(), method, value));
			}
		}
	}

	/**
	 * Segura notificacao vinda de outra IAR
	 * 
	 * @param rA
	 *            It has new status of instance
	 * @throws JSONException
	 */
	public abstract void notificationHandler(String rai, String method, Object value);

	@Override
	public void registerStakeholder(String method, String rai) {
		stakeholders.add(new Stakeholder(method, rai));
	}

	// @Override
	// public boolean registerStakeholder(String method, ResourceAgent rA) {
	// stakeholders.add(new Stakeholder(method, rA));
	// return true;
	// }

	public String change(String id, String method, Object value) throws JSONException {
		return JSONHelper.createChange(id, method, value);
	}
	

}
