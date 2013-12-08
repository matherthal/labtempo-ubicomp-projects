package br.uff.tempo.middleware.resources;

import java.util.Iterator;
import java.util.List;

import android.util.Log;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.ResourceLocation;
import br.uff.tempo.middleware.management.interfaces.IPerson;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.PersonStub;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;
import br.uff.tempo.middleware.resources.interfaces.IPresenceSensor;

public class PresenceSensor extends ResourceAgent implements IPresenceSensor {
	
	private static final long serialVersionUID = 1L;
	
	private static final String TAG = "PresenceSensor";
	private boolean presence = false;
	private IResourceLocation rls;
	private IResourceDiscovery rds;
	
	private Boolean stateDetected = false;
	
	public PresenceSensor(String name, String rans) {
		super(name, "br.uff.tempo.middleware.resources.PresenceSensor", rans);
		this.rls = new ResourceLocationStub(IResourceLocation.rans);
		this.rds = new ResourceDiscoveryStub(IResourceDiscovery.rans);
	}

	public static final String CV_GETPRESENCE = "getPresence";
	
	@ContextVariable(name = "Presença", type = "presence")
	public boolean getPresence() {
		return presence;
	}

	public static String S_SETPRESENCE = "setPresence";
	@Service(name = "Detectar Presença", type = "presence")
	public void setPresence(boolean p) {
		if (p)
			Log.i(TAG, "Presence sensor " + this.getName() + " detects somebody");
		else
			Log.i(TAG, "Presence sensor " + this.getName() + " detects somebody has left");
		this.presence = p;
		
		notifyStakeholders(this.CV_GETPRESENCE, p);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		List<ResourceData> ras = this.rds.search(ResourceData.RANS, rai);
		Iterator<ResourceData> it = ras.iterator();
		if (it.hasNext()) {
			ResourceData rd = it.next(); 
			if (rd.getType().contains("Person")) {
				Place place = rd.getPlace(); 
				if (place == null) {
					IPerson p = new PersonStub(rd.getRans());
					place = this.rls.getLocal(p.getPosition());
				}

				if (place.getName().equals(this.getPlace().getName())) {
					if (!stateDetected) {
						this.setPresence(true);
						stateDetected = true;
					}
				} else {
					if (stateDetected) {
						this.setPresence(false);
						stateDetected = false;
					}
				}
			}
		}
	}
}
