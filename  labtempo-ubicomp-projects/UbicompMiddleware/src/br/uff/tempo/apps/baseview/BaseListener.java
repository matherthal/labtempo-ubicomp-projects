package br.uff.tempo.apps.baseview;

import java.util.List;

import android.app.Activity;
import android.widget.TextView;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class BaseListener extends Thread {

	private IResourceDiscovery rD;
	private IResourceLocation rL;
	TextView tv;
	Activity act;

	public BaseListener(TextView tv, Activity act) {
		this.tv = tv;
		this.act = act;

		rD = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		//rL = new ResourceLocationStub(IResourceLocation.rans);
	}

	public void run() {

		int count = rD.search(ResourceData.TYPE, "//").size();
		boolean update = true;
		while (true) {
			count = rD.search(ResourceData.TYPE, "//").size();
			if (update)
				updateBaseContent();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			update = count != rD.search(ResourceData.TYPE, "//").size();
		}
	}

	protected void updateBaseContent() {
		List<ResourceData> rdList = rD.search(ResourceData.TYPE, "//");
		String rai = "";
		if (rdList != null)
			for (int i = 0; i < rdList.size(); i++)
				rai += rdList.get(i).getRans() + "\n";

		final String text = rai;
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				tv.setText(text);
			}
		});
	}

}
