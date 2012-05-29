package br.uff.tempo.middleware.comm;

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

import android.os.Handler;
import android.util.Log;

public class Callee {
	// default ip
    public static String SERVERIP = "";
    // designate a port
    public static final int SERVERPORT = 8080;
    private ServerSocket serverSocket;
    private Handler handler = new Handler();
    
	public Callee() {
		new Thread(new ServerThread()).start();
	}
    
    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (SERVERIP != null) {
                	//Print debug start
                	System.out.println("Listening on IP: " + SERVERIP);
                	//printOnScreen("Listening on IP: " + SERVERIP);
                    
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
                	System.out.println("Couldn't detect internet connection.");
                	//printOnScreen("Couldn't detect internet connection.");
                }
            } catch (final Exception e) {
                //Print error
            	System.out.println("Exception! "+e.getMessage());
            	//printOnScreen("Exception! "+e.getMessage());
                e.printStackTrace();
            }
        }
        
        class Receiver implements Runnable {
        	private Socket client;
        	
        	Receiver (Socket client) {
        		this.client = client;
        	}
        	
			public void run() {
            	System.out.println("\nConnected.");
				//printOnScreen("\nConnected.");

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
                        	System.out.println("\n" + line);
                        	//printOnScreen("\n" + line);

                            //Response
                            //String outgoingMsg = getMessage(line);
                        	String msg = JSONHelper.getMessage(line).toString();
                        	System.out.println("\n" + msg);
                            //printOnScreen("\n" + outgoingMsg);
                        	//TODO: what to answer???
                        	Object outgoingMsg = JSONHelper.createReply("received");
                            out.println(outgoingMsg);
                    	}
                    	
                    }
                    out.close();
                } catch (Exception e) {
                	//printOnScreen("Oops. Connection interrupted. Please reconnect your phones.");
                	System.out.println("Oops. Connection interrupted. Please reconnect your phones.");
                    e.printStackTrace();
                }
			}
        }
    }

    // gets the ip address of your phone's network
    /*private String getLocalIpAddress() {
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
    }*/
    
    /*//Method for Debug
    private void printOnScreen(final String msg) {
    	handler.post(new Runnable() {
            //@Override
            public void run() {
            	//activity.serverStatus.append(msg);
            }
        });
    }*/
}
