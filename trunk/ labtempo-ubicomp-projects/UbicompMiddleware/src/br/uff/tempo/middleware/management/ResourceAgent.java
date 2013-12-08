package br.uff.tempo.middleware.management;

import static br.uff.tempo.middleware.management.interfaces.IResourceRegister.rans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.comm.interest.api.JSONRPCCallback;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.LogOpenHelper.LogObject;
import br.uff.tempo.middleware.management.interfaces.IPlace;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.stubs.PlaceStub;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.stubs.ResourceRegisterStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;
/**
 * This class represents the basis modular of any component with services and context in a SmartAndroid system
 */
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
	private IResourceLocation rls;
	
	private Position position;

	// Everybody must initialize ResourceAgent at least with name and type
	@SuppressWarnings("unused")
	private ResourceAgent() {}
	// Everybody must initialize ResourceAgent at least with name and type
	@SuppressWarnings("unused")
	private ResourceAgent(String anything) {}

	/**
	 * ResourceAgent constructor for instance with undefined positioning
	 * @param name unique that identifies an instance
	 * @param type semantic hierarchical definition of the class that represent this instance 
	 * @param rans unique that identifies an instance in terms of access reference
	 */
	public ResourceAgent(String name, String type, String rans) {
		this(name, type, rans, null);
	}
	
	/**
	 * ResourceAgent constructor for instance with positioning
	 * @param name unique that identifies an instance
	 * @param type semantic hierarchical definition of the class that represent this instance 
	 * @param rans unique that identifies an instance in terms of access reference
	 * @param position phisically reference of a agent
	 */
	public ResourceAgent(String name, String type, String rans, Position position) {
		stakeholders = new ArrayList<Stakeholder>();
		
		registered = false;

		this.type = type(this.getClass());
		this.name = name;
		this.rans = rans;

		// this.position = position == null ? new Position(0, 0) : position; 
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

	/**
	 * Binder entity which permits connection between an activity and a service
	 */
	public class ResourceBinder extends Binder {
		public ResourceAgent getService() {
			return ResourceAgent.this;
		}
	}
	
	/**
	 * Type Hierachy generator for specialized class of this instance
	 * @param type is specilized class of a instance 
	 * ex: Stove.class 
	 * @return is a string in directory path format
	 * ex: /Simulator/Stove
	 */
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

	/**
	 * Cause auto register in SmartServer
	 */
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
		return commonIdentify(null,null);
	}
	
	
//	@Override
//	public boolean identify() {
//
//		if (!registered) {
//			rrs = new ResourceRegisterStub(IResourceRegister.rans);
//
//			String ip = SmartAndroid.getLocalIpAddress();
//			int prefix = SmartAndroid.getLocalPrefix();
//			
//			
//			rls = new ResourceLocationStub(IResourceLocation.rans);
//			
//			if (position != null) {
//				Place place = rls.getLocal(position);
//				ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place);
//				registered = rrs.registerLocation(this.rans, ip, prefix, this.position, resourceData);
//			} else {
//				ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, null);
//				registered = rrs.register(this.rans, ip, prefix, resourceData);
//			}
//			
//			// adding local reference of this instance
//			ResourceContainer.getInstance().add(this);
//			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
//		}
//		
//		return registered;
//	}
	
	public boolean commonIdentify(String placeName, Position position){
		if (!registered) {
			rrs = new ResourceRegisterStub(IResourceRegister.rans);
			rls = new ResourceLocationStub(IResourceLocation.rans);
			
			String ip = SmartAndroid.getLocalIpAddress();
			int prefix = SmartAndroid.getLocalPrefix();
			
			if (placeName != null) {
				Place place = rls.getPlace(placeName);
				float x;
				float y;
				if (position != null) {
					x = place.lower.getX()+position.getX();
					y = place.lower.getY()+position.getY();
				} else {
					x = (place.lower.getX()+place.upper.getX())/2;
					y = (place.lower.getY()+place.upper.getY())/2;
				}
				this.position = new Position(x,y);
				
				ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place, new ResourceAgentNS(this.rans, ip, prefix));
				registered = rrs.registerInPlace(placeName, this.position, resourceData);
			} else if(position != null) {
				Place place = rls.getLocal(position);
				this.position = position;
				
				ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place, new ResourceAgentNS(this.rans, ip, prefix));
				registered = rrs.registerLocation(this.position, resourceData);
			} else {
				ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, null, new ResourceAgentNS(this.rans, ip, prefix));
				if (this.position == null) {
					registered = rrs.register(resourceData);
				} else {
					registered = rrs.registerLocation(this.position, resourceData);
				}					
			}
			ResourceContainer.getInstance().add(this);
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
		}
		return registered;
	}
	
	
	/**
	 * Used for register with defined position
	 */
	public boolean identifyPosition(Position position) {
		return commonIdentify(null, position);
	}
	
//	public boolean identifyPosition(Position position) {
//
//		if (!registered) {
//			rrs = new ResourceRegisterStub(IResourceRegister.rans);
//			this.position = position;
//			
//			String ip = SmartAndroid.getLocalIpAddress();
//			int prefix = SmartAndroid.getLocalPrefix();
//			rls = new ResourceLocationStub(IResourceLocation.rans);
//			Place place = rls.getLocal(position);
//			ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place);
//			
//			registered = rrs.registerLocation(this.rans, ip, prefix, this.position, resourceData);
//			
//			// adding local reference of this instance
//			ResourceContainer.getInstance().add(this);
//			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
//		}
//		
//		return registered;
//	}
	
	/**
	 * Used to registr with defined placeName and position
	 */
	public boolean identifyInPlace(String placeName, Position position) {
		return commonIdentify(placeName,position);
	}	
	
	public void setPosition(Position pos) {
		this.position = pos;
		
		ResourceLocation.getInstance().registerInPlace(rans, pos);
		notifyStakeholders("updateLocation", pos);
	}
	
//	public boolean identifyInPlace(String placeName, Position position) {
//
//		if (!registered) {
//			rrs = new ResourceRegisterStub(IResourceRegister.rans);
//			
//			
//			String ip = SmartAndroid.getLocalIpAddress();
//			int prefix = SmartAndroid.getLocalPrefix();
//			rls = new ResourceLocationStub(IResourceLocation.rans);
//			Place place = rls.getPlace(placeName);
//			this.position = new Position(place.lower.getX()+position.getX(), place.lower.getY()+position.getY());
//			
//			ResourceData resourceData = new ResourceData(this.rans, this.name, this.type, this.position, place);
//			
//			
//			
//			registered = rrs.registerInPlace(this.rans, ip, prefix, placeName, this.position, resourceData);
//			
//			// adding local reference of this instance
//			ResourceContainer.getInstance().add(this);
//			ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.rans, ip, prefix));
//		}
//
//		return registered;
//	}
	/**
	 * Unregister a instance from Repository
	 */
	public boolean unregister() {
		
		rrs = new ResourceRegisterStub(IResourceRegister.rans);
		rrs.unregister(this.rans);
		
		ResourceContainer.getInstance().remove(this.rans);
		ResourceNSContainer.getInstance().remove(this.rans);
		
		registered = false;
		
		return true;
	}

	
	/**
	 * Position get method
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * @return list of Stakholders in this instance
	 */
	public List<Stakeholder> getStakeholders() {	
		return stakeholders; 
	}
	
	/**
	 * notify Stakeholders about an event related to a execution of a method 
	 * @param contextVariable identification of event source
	 * @param value interest target value
	 */
	public void notifyStakeholders(String contextVariable, Object value) {
		for (Stakeholder stakeholder : stakeholders) {
			if (stakeholder.getContextVariable().equals(contextVariable) || stakeholder.getContextVariable().equalsIgnoreCase("all")) {
				new ResourceAgentStub(stakeholder.getRANS()).notificationHandler(this.getRANS(), contextVariable, value);
				Log.d("SmartAndroid", String.format("notifying stakeholder: %s contextVariable: %s value: %s", stakeholder.getRANS(), contextVariable, value));
			}
		}
	}

	/**
	 * Hold a notification of a instance target of interes identified by rai
	 * 
	 * @param rai
	 *            It has new status of instance
	 * @param method identification of source event
	 * @param value  changed value notified         
	 * @throws JSONException
	 */
	public abstract void notificationHandler(String rai, String method, Object value);

	@Override
	public void registerStakeholder(String method, String rai) {
		stakeholders.add(new Stakeholder(method, rai));
	}
	
	@Override
	public void removeStakeholder(String method, String rai) {
		Log.i("SmartAndroid", "[" + rai + ", " + method + "] is removed as Stakeholder from " + getName());
		stakeholders.remove(new Stakeholder(method, rai));
	}
	
	
	/**
	 * Register callback interest using REPA 
	 */
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
	
	@Override
	public void updateLocation(Position position) {
		if (position !=null) {
			rls = new ResourceLocationStub(IResourceLocation.rans);
//			IPlace newPlace = new PlaceStub(rls.getLocalReference(position));
			ResourceData raData = new ResourceData(this.rans, this.name, this.type, position, rls.getLocal(position));
//			if (this.position != null) {
//				IPlace place = new PlaceStub(rls.getLocalReference(this.position));
//				if (!place.equals(newPlace)) {
//					place.exit(raData);
//					newPlace.enter(raData);
//				}
//			} else {
//				newPlace.enter(raData);
//			}
			
			rls.updateLocation(raData);
			notifyStakeholders("updateLocation", position);
		}
	}

	/**
	 * Get RA place
	 * @return Place
	 */
	@Override
	public Place getPlace() {
		return rls.getLocal(position);
	}
	
	@Override
	public LogObject getLog(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String dt = dateFormat.format(date);
		
		LogOpenHelper logService = new LogOpenHelper(this.getApplicationContext());
		SQLiteDatabase db = logService.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + LogOpenHelper.LOG_TABLE_NAME + 
				" WHERE " + LogOpenHelper.LOG_COL_DT + "=" + dt, null);
		c.moveToFirst();
		
		LogObject l = logService.new LogObject();
		l.AutorName = c.getString(c.getColumnIndex(LogOpenHelper.LOG_COL_AUTOR)); 
		l.AutorRANS = c.getString(c.getColumnIndex(LogOpenHelper.LOG_COL_AUTOR_ID));
		l.Action = c.getString(c.getColumnIndex(LogOpenHelper.LOG_COL_ACTION));
		l.datetime = date;
		
		return l;
	}
	
	@Override
	public void log(String record) {
		//LogOpenHelper logService = new LogOpenHelper(this.getBaseContext());
		/*LogOpenHelper logService = new LogOpenHelper(this.getApplicationContext());
		SQLiteDatabase db = logService.getWritableDatabase();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();

		ContentValues values = new ContentValues(); 
		values.put(LogOpenHelper.LOG_COL_DT, dateFormat.format(date));
		values.put(LogOpenHelper.LOG_COL_AUTOR_ID, this.getRANS());
		values.put(LogOpenHelper.LOG_COL_AUTOR, this.getName());
		values.put(LogOpenHelper.LOG_COL_ACTION, record);
		
		db.insert(LogOpenHelper.LOG_TABLE_NAME, null, values);*/
	}
}
