package br.uff.tempo.apps.map.dialogs;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import br.uff.tempo.apps.map.MapActivity;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class MiddlewareOperation extends AsyncTask<String, Void, List<String>> {

	private ResourceDiscoveryStub rd;
	private ProgressDialog progress;
	private Activity act;
	
	public MiddlewareOperation(Activity act) {
	
		//TODO: This use of Resource discovery seems strange... we need to talk about it
		rd = new ResourceDiscoveryStub(ResourceDiscovery.getInstance()
				.getURL());
		
		this.act = act;
	}
	
	@Override
	protected List<String> doInBackground(String... params) {
		
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				// Execute a progress dialog while the search doesn't finish
				progress = ProgressDialog.show(act, "", "Getting information from system.", true);
			}
		});
		
		//Just to see the dialog for a moment... =)
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get all registered resources references from Resource Discovery
		return rd.search("");
	}
	
	// Executed when search finishes
	@Override
	protected void onPostExecute(List<String> result) {
		
		super.onPostExecute(result);
		
		// Finish the progress dialog
		progress.dismiss();
		
		// call a method from the activity (callback)
		MapActivity map = (MapActivity) act;
		map.onGetResourceList(result);
	}



}
