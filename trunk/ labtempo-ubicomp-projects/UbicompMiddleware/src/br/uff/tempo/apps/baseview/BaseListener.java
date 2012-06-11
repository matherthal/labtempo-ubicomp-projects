package br.uff.tempo.apps.baseview;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceRepository;
import android.widget.TextView;

public class BaseListener extends Thread{
	
	private ResourceRepository rR;
	TextView tv;
	
	public BaseListener(TextView tv)
	{
		this.tv = tv;
	}
	
	public void run()
	{
		rR = ResourceRepository.getInstance();
        int count = rR.getList().size();
        boolean update = true;
        while (true)
        {
        	count = rR.getList().size();
        	if (update)
        		updateBaseContent();
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	update= count != rR.getList().size();
        }
	}

    protected void updateBaseContent()
    {
    	ArrayList<String> strList = rR.getList();
    	String rai = "";
    	if (strList != null)
	    	for (int i = 0;i<strList.size(); i++)	
	    		rai += strList.get(i)+"\n";
	    tv.setText(rai);
	}
    
}
