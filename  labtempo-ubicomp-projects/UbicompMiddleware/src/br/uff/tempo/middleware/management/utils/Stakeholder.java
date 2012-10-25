package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;
import java.util.List;

import android.util.Log;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class Stakeholder implements Serializable {

	private String method;
	private String rai;
	private String name;
	private ResourceData data;

	public Stakeholder(String method, String rai) {
		this.method = method;
		this.rai = rai;
		
		this.name = new ResourceAgentIdentifier(rai).getName();
		
		List<ResourceData> resData = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS).searchForAttribute(ResourceData.NAME, this.name);
		
		if (resData != null) {
			data = resData.get(0);
		} else {
			Log.i("Stakeholder class", "The name " + this.name + " was NOT found in the repository by searchForAttribute method!");
		}
	}

	public Stakeholder(String method, ResourceAgent rA) {
		this(method, rA.getRAI());
	}

	public String getMethod() {
		return method;
	}

	public String getRAI() {
		return rai;
	}
	
	public String getName() {
		return name;
	}
	
	public ResourceData getResourceData() {
		return data;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if (this == obj)
			return true;
		
		if (!(obj instanceof Stakeholder))
			return false;
		
		Stakeholder sh = (Stakeholder) obj;
		
		return this.rai.equals(sh.rai) && this.method.equals(sh.method);
	}
	
	@Override
	public String toString() {
		
		return this.name + " wants: " + this.method;
	}
}
