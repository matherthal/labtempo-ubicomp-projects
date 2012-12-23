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
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.comm.interest.api.JSONRPCCallback;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

public abstract class ResourceAgent extends Service implements IResourceAgent, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = "AgentBase";

	// Agent's attributes
	private boolean registered;
	private String name;
	private String type;
	private String rans;
	private ArrayList<ResourceAgent> interests;
	private ArrayList<Stakeholder> stakeholders;
	
	private IResourceRegister rrs;
	
	private Position position;

	// Everybody must initialize ResourceAgent at least with name and type
	@SuppressWarnings("unused")
	private ResourceAgent() {}
	// Everybody must initialize ResourceAgent at least with name and type
	@SuppressWarnings("unused")
	private ResourceAgent(String anything) {}

	public ResourceAgent(String name, String type, String rans) {
		this(name, type, rans, null);
	}
	
	public ResourceAgent(String name, String type, String rans, Position position) {
		stakeholders = new ArrayList<Stakeholder>();
		
		registered = false;

		this.type = type(this.getClass());
		this.name = name;
		this.rans = rans;

		this.position = position;
		// initResource();
		
		this.registerDefaultInterests();
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
	public String getRANS() {
		return this.rans;
	}

	@Override
	public void setRANS(String rans) {
		this.rans = rans;
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
	public String getResourceClassName() {
		return this.getClass().getName();
	}

	public class ResourceBinder extends Binder {
		public ResourceAgent getService() {
			return ResourceAgent.this;
		}
	}
	
	public static String type(Class type) {
		String result = type.getSimpleName()+"";
		type = type.getSuperclass();
		while (!ResourceAgent.class.equals(type)){
			result = type.getSimpleName()+"/"+result;
			type = type.getSuperclass();
		}
		return result;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initResource();
	}

	private void initResource() {
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
			rrs = new ResourceRegisterStub(IResourceRegister.rans);

			String ip = SmartAndroid.getLocalIpAddress();
			int prefix = SmartAndroid.getLocalPrefix();
			
			ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, null);
			
			if (position != null) {
				resourceData.setPlace(ResourceLocation.getInstance().getLocal(position));
				registered = rrs.registerLocation(this.rans, ip, prefix, this.position, resourceData);
			} else {
				registered = rrs.register(this.rans, ip, prefix, resourceData);
			}
			
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
		}
		
		return registered;
	}
	
	public boolean unregister() {
		
		rrs = new ResourceRegisterStub(IResourceRegister.rans);
		rrs.unregister(this.rans);
		
		ResourceContainer.getInstance().remove(this.rans);
		ResourceNSContainer.getInstance().remove(this.rans);
		
		registered = false;
		
		return true;
	}

	public boolean identifyPosition(Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(IResourceRegister.rans);
			this.position = position;
			
			String ip = SmartAndroid.getLocalIpAddress();
			int prefix = SmartAndroid.getLocalPrefix();
			
			Place place = ResourceLocation.getInstance().getLocal(position);
			ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place);
			
			registered = rrs.registerLocation(this.rans, ip, prefix, this.position, resourceData);
			
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
		}
		
		return registered;
	}
	
	public boolean identifyInPlace(String placeName, Position position) {

		if (!registered) {
			rrs = new ResourceRegisterStub(IResourceRegister.rans);
			this.position = position;
			
			String ip = SmartAndroid.getLocalIpAddress();
			int prefix = SmartAndroid.getLocalPrefix();
			
			Place place = ResourceLocation.getInstance().getPlace(placeName);
			ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place);
			
			registered = rrs.registerInPlace(this.rans, ip, prefix, placeName, this.position, resourceData);
			
			// adding local reference of this instance
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
		}

		return registered;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public List<Stakeholder> getStakeholders() {	
		return stakeholders; 
	}
	
	public void notifyStakeholders(String method, Object value) {
		for (Stakeholder stakeholder : stakeholders) {
			if (stakeholder.getMethod().equals(method) || stakeholder.getMethod().equalsIgnoreCase("all")) {
				new ResourceAgentStub(stakeholder.getRANS()).notificationHandler(this.getRANS(), method, value);
				Log.d("SmartAndroid", String.format("notifying stakeholder: %s method: %s value: %s", stakeholder.getRANS(), method, value));
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
	
	

	protected void registerDefaultInterests() {
		if (SmartAndroid.interestAPIEnable) {
			InterestAPI ia = InterestAPIImpl.getInstance();
			try {
				ia.registerInterest(this.rans);
				ia.registerInterest(this.rans, "jsonrpc", new JSONRPCCallback());
			} catch (Exception e) {
				throw new SmartAndroidRuntimeException("Exception in registerDefaultInterests", e);
			}			
		}
	}
}
