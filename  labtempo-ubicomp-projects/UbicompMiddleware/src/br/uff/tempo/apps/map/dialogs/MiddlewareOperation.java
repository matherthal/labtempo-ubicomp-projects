package br.uff.tempo.apps.map.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

/**
 * This class is used to execute a middleware call in a separate thread while
 * the thread is executing, a dialog shows an animation. This way, the android
 * GUI doesn't crash.
 * 
 * @author dbarreto
 * 
 */
public class MiddlewareOperation extends AsyncTask<String, Void, List<String>> {

	private ResourceDiscoveryStub rd;
	private ProgressDialog progress;
	private Activity act;
	private String query;

	public MiddlewareOperation(Activity act, String query) {
		
		this(act, query, IResourceDiscovery.RDS_ADDRESS);
	}

	public MiddlewareOperation(Activity act, String query, String address) {
		
		rd = new ResourceDiscoveryStub(address);

		this.act = act;
		this.query = query;

	}

	@Override
	protected List<String> doInBackground(String... params) {

		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				// Execute a progress dialog while the search doesn't finish
				progress = ProgressDialog.show(act, "",
						"Getting information from system.", true);
			}
		});

		// Just to see the dialog for a moment... =)
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Get registered resources references from Resource Discovery that
		// matches the query
		List<ResourceData> resourceData = rd.searchForAttribute(ResourceData.TYPE, this.query);
		List<String> result = new ArrayList<String>();
		for (ResourceData r : resourceData) {
			result.add(r.getRai());
		}
		return result;
	}

	// Executed when search finishes
	@Override
	protected void onPostExecute(List<String> result) {

		super.onPostExecute(result);

		// Finish the progress dialog
		progress.dismiss();

		// call a method from the activity (callback)
		IResourceListGetter lg = (IResourceListGetter) act;
		lg.onGetResourceList(result);
	}

}
