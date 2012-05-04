package br.uff.tempo.apps;

import br.uff.tempo.*;
import br.uff.tempo.middleware.management.ResourceAgent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;
import br.uff.tempo.middleware.resources.StoveAgent;


public class StoveView extends Activity {
    //public AgentBase agent = new StoveAgent();
	//public AgentBase agent = new AgentBase();
	//private final String serverIP = "192.168.1.70";
	//private Caller sendServ = new Caller(serverIP);
	private StoveAgent stove;
	//private ServiceConnection conn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stove);
        
        //startService(new Intent(this, MediaService.class));  //Media service test
        //conn = new ServiceConnection();
		/*Intent i = new Intent();
		i.setClassName("br.uff.tempotesteRPC", "br.uff.tempotesteRPC.StoveAgent");
		bindService(i, mConnection, Context.BIND_AUTO_CREATE);*/
        Intent intent = new Intent(this, StoveAgent.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		//bindService(new Intent(StoveView.this, StoveAgent.class), mConnection, Context.BIND_AUTO_CREATE);
        
        //sendServ.tv = (TextView) findViewById(R.id.editTextLog); //FIXME: remove this!
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	stove.unbindService(mConnection);
    }
    
    public void buttonStove1_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove1Status);
		String msg = "empty";
		if (tv != null)
		{
			//Toggle label
			if (tv.getText().toString().equals("Boca 1 desligada"))
			{
	    		msg = "Boca 1 ligada";
				playSoundStove();
			}
			else
				msg = "Boca 1 desligada";
			tv.setText(msg);
		}
		
		//Call server
		//agent.sendMessage("");
		//sendServ.sendMessage(msg);
    }
    
    public void buttonStove2_Clicked(View view)
    {
    	final TextView tv = (TextView) findViewById(R.id.textStove2Status);
		String msg = "empty";
		if (tv != null)
		{
			//Toggle label
			if (tv.getText().toString().equals("Boca 2 desligada"))
			{
	    		msg = "Boca 2 ligada";
				playSoundStove();
			}
			else
				msg = "Boca 2 desligada";
			tv.setText(msg);
		}
		
		//Call server
		//agent.sendMessage("");
		//sendServ.sendMessage(msg);
    }
    
    public void buttonStove3_Clicked(View view)
    {
    	final TextView tv = (TextView) findViewById(R.id.textStove3Status);
		String msg = "empty";
		if (tv != null)
		{
			//Toggle label
			if (tv.getText().toString().equals("Boca 3 desligada"))
			{
	    		msg = "Boca 3 ligada";
				playSoundStove();
			}
			else
				msg = "Boca 3 desligada";
			tv.setText(msg);
		}
		
		//Call server
		//agent.sendMessage("");
		//sendServ.sendMessage(msg);
    }
    
    public void buttonStove4_Clicked(View view)
    {
    	final TextView tv = (TextView) findViewById(R.id.textStove4Status);
		String msg = "empty";
		if (tv != null)
		{
			//Toggle label
			if (tv.getText().toString().equals("Boca 4 desligada"))
			{
	    		msg = "Boca 4 ligada";
				playSoundStove();
			}
			else
				msg = "Boca 4 desligada";
			tv.setText(msg);
		}
		
		//Call server
		//agent.sendMessage("");
		//sendServ.sendMessage(msg);
    }
    
    public void buttonOven_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove4Status);
		if (tv != null)
		{
			if (tv.getText().toString().equals("Forno desligado"))
	    		tv.setText("Forno ligado");
			else
				tv.setText("Forno desligado");
		}
		
		//Play sound open
		Bundle b = new Bundle();
		// Bundle.putSerializable for full Objects (careful there)
		b.putInt(MediaService.SOUND_ID, R.raw.ovenopen);  
		Intent i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
		
		//Play sound close
		b = new Bundle();
		b.putInt(MediaService.SOUND_ID, R.raw.ovenopen);  
		i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
		
		//Call server
		//agent.sendMessage("");
    }
    
    public String toggleState(String label)
    {
    	String[] text = label.split(" ");
    	String status = text[text.length - 1];
    	if (status.equals("desligada"))
    		text[text.length - 1] = "ligada";
		else
			text[text.length - 1] =  "desligada";
    	return text.toString();
    }
    
    public void buttonFinish_Clicked(View view)
    {
		finish();
    }
    
    public void buttonRegister_Clicked(View view) {
    	
    	stove.register();
    	//stove.startActivity(new Intent(StoveView.this, StoveAgent.class));
	}
    
    private void playSoundStove()
    {
    	//Play sound
		Bundle b = new Bundle();
		// Bundle.putSerializable for full Objects (careful there)
		b.putInt(MediaService.SOUND_ID, R.raw.stove);  
		Intent i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            //stove = ((StoveAgent.StoveBinder)service).getService();
        	// We've bound to LocalService, cast the IBinder and get LocalService instance
        	ResourceBinder binder = (ResourceBinder) service;
            stove = (StoveAgent)binder.getService();

            // Tell the user about this for our demo.
            Toast.makeText(StoveView.this, "Agente conectado", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            stove = null;
            Toast.makeText(StoveView.this, "Agente desconectado", Toast.LENGTH_SHORT).show();
        }
    };
}
