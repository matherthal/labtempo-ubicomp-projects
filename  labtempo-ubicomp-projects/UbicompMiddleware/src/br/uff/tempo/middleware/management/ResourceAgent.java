package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.comm.ResourceRegisterServiceStub;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public abstract class ResourceAgent extends Service implements IResourceAgent {
	private static final String TAG = "AgentBase";
	
	private static final String TCP_SERVER_IP = "192.168.1.70";
	private static final int TCP_SERVER_PORT = 21111;
	
	//Agent's attributes
	private static boolean registered = false;
	private static int id;
	private static String name = "";
	
	private ResourceRegisterServiceStub rrs;
	
	public class ResourceBinder extends Binder {
    	public ResourceAgent getService() {
            return ResourceAgent.this;
        }
    }
	
	public ResourceBinder mBinder = new ResourceBinder();
	
	public ResourceAgent() {
		 rrs = new ResourceRegisterServiceStub();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		// Exists only to defeat instantiation.
		// rrs = ResourceRegisterServiceStub.getInstance();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		//this.register();
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//this.register();
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void register() {
		rrs.register(this);
	}
}
