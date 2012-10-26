package br.uff.tempo.apps.baselocationview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.widget.TextView;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;

public class BaseLocationListener extends Thread {

	private IResourceDiscovery rD;
	private IResourceLocation rL;
	TextView tv;
	Activity act;

	public BaseLocationListener(TextView tv, Activity act) {
		this.tv = tv;
		this.act = act;

		rD = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		rL = new ResourceLocationStub(IResourceLocation.rans);
	}

	public void run() {

		int count = rL.search("").size();
		boolean update = true;
		while (true) {
			count = rL.search("").size();
			if (update)
				updateBaseContent();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			update = count != rL.search("").size();
		}
	}

	protected void updateBaseContent() {
		Set<String> strList = rL.getPlacesNames();
		String result = "";
		
		if (strList != null) {
			Iterator<String> itList = strList.iterator();
			while (itList.hasNext()) {
				String placeStr = itList.next();
				result += placeStr + "\n";
				List<String> listRai = rL.search(placeStr);
				for (String rai : listRai) {
					Position position = rL.getPosition(placeStr, rai);
					result += rai + " Position: "+ position.print() +"\n";
				}
			}
		}
		
		result += "=======================\n+" +
				  "======Sorted List======\n+" +
				  "=======Ref 0,0=========\n";
		ArrayList<String> sortedList = rL.queryByLocal(new Position(0,0));
		for (String rai: sortedList) {
			result += rai+"\n";
		}

		final String text = result;
		act.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				tv.setText(text);
			}
		});
	}

}
