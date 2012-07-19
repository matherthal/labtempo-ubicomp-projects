package br.uff.tempo.apps.baseview;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.TextView;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class BaseListener extends Thread {

	private IResourceDiscovery rD;
	TextView tv;
	Activity act;
	
	public BaseListener(TextView tv, Activity act)
	{
		this.tv = tv;
		this.act = act;
		
		rD = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
	}
	
	public void run()
	{
		
		
        int count = rD.search("").size();
        boolean update = true;
        while (true)
        {
        	count = rD.search("").size();
        	if (update)
        		updateBaseContent();
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	update = count != rD.search("").size();
        }
	}

    protected void updateBaseContent()
    {
    	ArrayList<String> strList = rD.search("");
    	String rai = "";
    	if (strList != null)
	    	for (int i = 0;i<strList.size(); i++)	
	    		rai += strList.get(i)+"\n";
    	
    	final String text = rai;
    	act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				tv.setText(text);
			}
		});
	}
    
}
