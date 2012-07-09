package br.uff.tempo.middleware.management;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public abstract class ResourceAgent extends Service implements IResourceAgent,
		Serializable {
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
	private IResourceDiscovery rDS;
	private ArrayList<String> registeredList;
	private String RDS_URL;

	public IResourceDiscovery getRDS() {
		return rDS;
	}

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

	public ArrayList<String> getRegisteredList() {
		return registeredList;
	}

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

		stakeholders = new ArrayList<Stakeholder>();

		// stakeholders.add(new Stakeholder("update",rR));
		registered = false;

		this.type = type;// address+port+type+name
		this.id = id;
		this.name = name;
		URL = "";

		//WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		//WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ipAddress = ResourceAgentIdentifier.getLocalIpAddress(); //Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
		
		URL = ResourceAgentIdentifier.generateRAI(ipAddress, type, name);	
		
		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		
		//initResource();
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
		//rDS = new ResourceDiscoveryStub("rai:127.0.0.1//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery");
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

	public boolean isRegistered() {
		return registered;
	}

	public boolean identify() {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search(
					"br.uff.tempo.middleware.management.ResourceRegister").get(
					0));
			
			registered = rrs.register(this.URL);
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
		}
		
		return registered;
	}

	public void notifyStakeholders(String change) throws JSONException {
		int i = 0;
		while (i < stakeholders.size()) {
			String url = stakeholders.get(i).getUrl();
			// stakeholderStub = new ResourceAgentStub(url);
			if (change.contains(stakeholders.get(i).getMethod()))
				new ResourceAgentStub(rDS.search(url).get(0))
						.notificationHandler(change);// change = id, method name
														// and value
			// query by url return a unique instance
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

	public void registerStakeholder(String method, String url) {
		stakeholders.add(new Stakeholder(method, url));
	}

	public boolean registerStakeholder(String method, ResourceAgent rA) {
		stakeholders.add(new Stakeholder(method, rA));
		return true;
	}

	public String change(String id, String method, Object value)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("method", method);
		json.put("value", value);
		return json.toString();
	}
}