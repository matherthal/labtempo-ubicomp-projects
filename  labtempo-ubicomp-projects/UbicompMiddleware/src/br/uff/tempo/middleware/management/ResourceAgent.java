package br.uff.tempo.middleware.management;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.current.api.Caller;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public abstract class ResourceAgent extends Service implements IResourceAgent, Serializable, Callable {

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
	
	private IResourceRegister rrs;
	private Caller caller;
	
	private Position position;

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

	public class ResourceBinder extends Binder {
		public ResourceAgent getService() {
			return ResourceAgent.this;
		}
	}
	
	public ResourceAgent() {

		this("GeneralAgent", "br.uff.tempo.middleware.management.ResourceAgent", 0);
	}

	public ResourceAgent(String type, int id) {

		this(id + "", type, id);
	}

	public ResourceAgent(String name, String type, int id) {

		this(name, type, id, null);
	}
	
	public ResourceAgent(String name, String type, int id, Position position) {

		stakeholders = new ArrayList<Stakeholder>();
		
		registered = false;

		this.type = type;// address+port+type+name
		this.id = id;
		this.name = name;
		rai = "";

		String ipAddress = ResourceAgentIdentifier.getLocalIpAddress();

		rai = ResourceAgentIdentifier.generateRAI(ipAddress, type, name);

		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);

		this.position = position;
		// initResource();
		
		this.registerDefaultInterests();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initResource();
		registeredList = rDS.search("");// search all rR.contains("") = all IAR
	}

	private void initResource() {
		rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		identify();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public boolean isRegistered() {
		return registered;
	}

	@Override
	public boolean identify() {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));

			String ip = ResourceAgentIdentifier.getLocalIpAddress();
			int prefix = ResourceAgentIdentifier.getLocalPrefix();
			
			ResourceData resourceData = new ResourceData(this.rai, this.name, this.type, this.position, null);
			
			if (position != null) {
				registered = rrs.registerLocation(this.rai, ip, prefix, this.rai, this.position, resourceData);
			} else {
				registered = rrs.register(this.rai, ip, prefix, this.rai, resourceData);
			}
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rai, ip, prefix));
		}

		return registered;
	}

	public boolean identifyPosition(Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));
			this.position = position;
			
			String ip = ResourceAgentIdentifier.getLocalIpAddress();
			int prefix = ResourceAgentIdentifier.getLocalPrefix();
			
			Place place = ResourceLocation.getInstance().getLocal(position);
			ResourceData resourceData = new ResourceData(this.rai, this.name, this.type, this.position, place);
			
			registered = rrs.registerLocation(this.rai, ip, prefix, this.rai, this.position, resourceData);
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rai, ip, prefix));
		}

		return registered;
	}
	
	public boolean identifyInPlace(String placeName, Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(rDS.search("br.uff.tempo.middleware.management.ResourceRegister").get(0));
			this.position = position;
			
			String ip = ResourceAgentIdentifier.getLocalIpAddress();
			int prefix = ResourceAgentIdentifier.getLocalPrefix();
			
			Place place = ResourceLocation.getInstance().getPlace(placeName);
			ResourceData resourceData = new ResourceData(this.rai, this.name, this.type, this.position, place);
			
			registered = rrs.registerInPlace(null, ip, prefix, this.rai, placeName, this.position, resourceData);
				
			String result = "";
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rai, ip, prefix));
		}

		return registered;
	}
	
	public List<Stakeholder> getStakeholders() {	
		return stakeholders; 
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
	
	public void removeStakeholder(String method, String rai) {
		Log.i("SmartAndroid", "[" + rai + ", " + method + "] is removed as Stakeholder from " + getName());
		stakeholders.remove(new Stakeholder(method, rai));
	}

	private void registerDefaultInterests() {
//		InterestAPI ia = InterestAPIImpl.getInstance();
//		try {
//			ia.registerInterest(this.rai + "://ContextVariable", this);
//			ia.registerInterest(this.rai + "://Service", this);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// TODO: André - add default interests related with context variables and services
		// TODO: André - find methods annotated with @ContextVariable and @Service and register in communication API
	}
	
	@Override
	public Object call(String rai, String interest, String message) {
		return null;
	}
}
