package br.uff.tempo.middleware.management;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
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
	private static final String TAG = "AgentBase";

	// Agent's attributes
	private boolean registered;
	private int id;
	private String name;
	private String type;
	private String URL;
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
	public String getURL() {
		return URL;
	}

	@Override
	public void setURL(String uRL) {
		URL = uRL;
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
		URL = "";

		// WifiManager wifiManager = (WifiManager)
		// getSystemService(WIFI_SERVICE);

		// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ipAddress = ResourceAgentIdentifier.getLocalIpAddress(); // Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

		URL = ResourceAgentIdentifier.generateRAI(ipAddress, type, name);

		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);

		this.position = position;
		// initResource();
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
				registered = rrs.registerLocation(this.URL, this.position);
			} else {
				registered = rrs.register(this.URL);
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
			registered = rrs.registerLocation(this.URL, this.position);
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
		}

		return registered;
	}
	
	@Override
	public void notifyStakeholders(String change) throws JSONException {
		int i = 0;
		while (i < stakeholders.size()) {
			String url = stakeholders.get(i).getUrl();
			// stakeholderStub = new ResourceAgentStub(url);
			if (change.contains(stakeholders.get(i).getMethod()) || stakeholders.get(i).getMethod().equalsIgnoreCase("all"))
				new ResourceAgentStub(url).notificationHandler(change);
			// change = id, method name and value
			// query by url return a unique instance
			i++;
		}
	}

	/**
	 * Segura notificacao vinda de outra IAR
	 * 
	 * @param rA
	 *            It has new status of instance
	 * @throws JSONException
	 */
	public abstract void notificationHandler(String change);

	@Override
	public void registerStakeholder(String method, String url) {
		stakeholders.add(new Stakeholder(method, url));
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
