package br.uff.tempo.apps.stove;

import java.util.Observable;
import java.util.Observer;

import br.uff.tempo.*;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;
import br.uff.tempo.middleware.resources.StoveAgent;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class StoveView extends Activity implements Observer {
	
	private static final String TAG = "StoveView";
	private PopupWindow m_pw;
	private StoveData stoveData;
	private View layout_popup;
	private StoveAgent stove;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //create a 4-burner stove buffer
        this.stoveData = new StoveData(4);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //setContentView(new Panel(this, stove));
        setContentView(R.layout.stove);
        
        Intent intent = new Intent(this, StoveAgent.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);  
    }
    
    public void onBurnerIntensityChanged (int burner, int intensity) {
    	this.stoveData.setBurnerIntensity(burner, intensity);
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
    	//stove.unbindService(mConnection);
    }
    
    //TODO: Improving the input method (an alternative to SeekBar)...
    public void showPopup(int burn) {
    	
    	// Make a View from our XML file
	   LayoutInflater inflater = (LayoutInflater)
	         this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   layout_popup = inflater.inflate(R.layout.popup,
	         (ViewGroup) findViewById(R.id.popup_element));
	   
	   SeekBar sb = (SeekBar) layout_popup.findViewById(R.id.seekBar1);
	   sb.setProgress(stoveData.getBurnerIntensity(burn));
	   sb.setTag(burn);
	   
	   TextView tv = (TextView) layout_popup.findViewById(R.id.tv1);
	   
	   tv.append(Integer.toString(burn + 1));
	   
	   m_pw = new PopupWindow( layout_popup,  200,  150,  true);
	   m_pw.showAtLocation(layout_popup, Gravity.CENTER, 0, 0);
  
    }
    
    public void onButtonInPopup(View target) {
    	
    	SeekBar sb = (SeekBar) layout_popup.findViewById(R.id.seekBar1);
    	stoveData.setBurnerIntensity((Integer) sb.getTag(), sb.getProgress());
    	m_pw.dismiss();
    }
    
    public void onRegisterClick(View target) {
    	
    	stove.identify();
    	Toast.makeText(StoveView.this, "Register Called", Toast.LENGTH_LONG).show();
    }
    
    public StoveData getStoveState() {
    	return stoveData;
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

    //Called when external representation of stove change its state
	//@Override
	public void update(Observable observable, Object data) {
		
		//received a change in the stove burners state from external stove
		if (data instanceof StoveData) {
			StoveData sd = (StoveData) data;
			
			Toast.makeText(StoveView.this, "Received a change state!", Toast.LENGTH_SHORT).show();
			
			for (int i=0; i < sd.getBurners().length; i++) {
				onBurnerIntensityChanged(i, sd.getBurnerIntensity(i));
			}
		}
	}
}