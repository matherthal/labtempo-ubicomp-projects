package br.uff.tempo.server;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

public class ServerActivity extends Activity {
	private Dispatcher dispatcher;
    public TextView serverStatus;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        serverStatus = (TextView) findViewById(R.id.hello);

        //SERVERIP = getLocalIpAddress();

        if (savedInstanceState == null)
        {

        	bindService(new Intent(this, Dispatcher.class), mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*try {
             // make sure you close the socket upon exiting
             //serverSocket.close();
         } catch (IOException e) {
             e.printStackTrace();
         }*/
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            dispatcher = ((Dispatcher.DispatcherBinder)service).getService();
            
            // Tell the user about this for our demo.
            Toast.makeText(ServerActivity.this, "Agente conectado", Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            dispatcher = null;
            Toast.makeText(ServerActivity.this, "Agente conectado", Toast.LENGTH_SHORT).show();
        }
    };
}