package br.uff.tempo.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


/*import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;*/

public class Dispatcher extends Service {
	//Dispatcher is a Singleton

	
	// default ip
    public static String SERVERIP = "";
    // designate a port
    public static final int SERVERPORT = 8080;
    private ServerSocket serverSocket;
    private Thread thmain;
    private Handler handler = new Handler();
    private ServerActivity activity;
    
    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (SERVERIP != null) {
                	//Print debug start
                	printOnScreen("Listening on IP: " + SERVERIP);
                    
                    //Create server socket
                    serverSocket = new ServerSocket(SERVERPORT);
                    
                    //Repetition to create one threads to each call from clients
                    while (true) {
                        // listen for incoming clients
                        Socket client = serverSocket.accept();
                        //Setting a thread to the arrived client
                        new Thread(new Receiver(client)).start();
                    }
                } else {
                	//Print error
                	printOnScreen("Couldn't detect internet connection.");
                }
            } catch (final Exception e) {
                //Print error
            	printOnScreen("Exception! "+e.getMessage());
                e.printStackTrace();
            }
        }
        
        class Receiver implements Runnable {
        	private Socket client;
        	
        	Receiver (Socket client) {
        		this.client = client;
        	}
        	
			public void run() {
				printOnScreen("\nConnected.");

                try {
                	//Incoming message
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    				//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    				
                    boolean exist = true;
                    while (exist) {
                    	//Gets message
                    	final String line = in.readLine();
                    	//If there is a message
                    	if (line == null)
                    		exist = false;
                    	else {
                    		
                    		//Print message
                        	Log.d("ServerActivity", line);
                        	printOnScreen("\n" + line);

                            //Response
                            String outgoingMsg = getMessage(line);
                            printOnScreen("\n" + outgoingMsg);
                            out.println(outgoingMsg);
                    	}
                    	
                    }
                    out.close();
                } catch (Exception e) {
                	printOnScreen("Oops. Connection interrupted. Please reconnect your phones.");
                    e.printStackTrace();
                }
			}
        }
    }

    // gets the ip address of your phone's network
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
    
    private String getMessage(String jsonString) {
    	// Parse request string
    	JSONObject reqIn = null;
    	
    	try {
    		reqIn = new JSONObject(jsonString);
    		
    	} catch (JSONException e) {
    		System.out.println(e.getMessage());
    		// Handle exception...
    	}
    	jsonString = "";
		try {
			// How to extract the request data
			printOnScreen("Parsed request with properties :");
			printOnScreen("\tmethod     : " + reqIn.getString("method"));
			printOnScreen("\tparameters : " + reqIn.getJSONArray("params"));
			printOnScreen("\tid         : " + reqIn.getString("id") + "\n\n");

			// Process request...

			// Create the response (note that the JSON-RPC ID must be echoed back)
			String result = "received";
			JSONObject respOut = new JSONObject();
			respOut.put("result", result);
			respOut.put("id", reqIn.getString("id"));
			jsonString = respOut.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// Serialise response to JSON-encoded string
    	
		return jsonString;
    	// The response string can now be sent back to the client...
    }
    
    //Method for Debug
    private void printOnScreen(final String msg) {
    	handler.post(new Runnable() {
            //@Override
            public void run() {
            	//activity.serverStatus.append(msg);
            }
        });
    }
    
    public class DispatcherBinder extends Binder {
    	Dispatcher getService() {
            return Dispatcher.this;
        }
    }
    
    public DispatcherBinder mBinder = new DispatcherBinder();
    
    public IBinder onBind(Intent intent)
    {
    	new Thread(new ServerThread()).start();
    	return mBinder;
    }
}
