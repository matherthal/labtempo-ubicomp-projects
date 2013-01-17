package br.uff.tempo.apps.reminder;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Television;



public class ReminderActivity extends Activity {
	List<Prescription> prescriptions;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);
	
	}
	
	public void onSubmitPrescriptionClick() {
		
	}
	
	//TODO Move this method to an appropriated place
	public void search() {
		
		IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		
		List<ResourceData> lista = discovery.searchForAttribute(ResourceData.TYPE, Television.class.getCanonicalName());

		
		ResourceDiscovery dis;
	}

}
