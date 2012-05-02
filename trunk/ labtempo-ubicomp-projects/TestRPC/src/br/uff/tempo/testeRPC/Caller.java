package br.uff.tempo.testeRPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Caller {
    private String serverIP = "";
    private int port = 8080;
    private Handler handler = new Handler();
    
	Caller(String serverIP) {
		this.serverIP = serverIP;
		//serverIP = "192.168.1.70";  //FIXME: IP shouldn't be hardcoded 
	}
	
	public void sendMessage(String jsonString, ResultSetter setter) {
        if (!serverIP.equals("")) {
        	//Create thread passing informations concerning the method that will be executed
        	ClientThread client = new ClientThread(jsonString);
        	//Define callback method
    		client.setResultSetter(setter);
    		//Create thread
            Thread cThread = new Thread(client);
            //Start thread
            cThread.start();
        }
	}	
	
	public interface ResultSetter {
		public void setResult(String result);
	}
	
	public class ClientThread implements Runnable {
    	private String jsonString = "";
		private ResultSetter setter;
    	
		ClientThread(String jsonString) {
			this.jsonString = jsonString;
		}
		
		public void setResultSetter(ResultSetter setter) {
		    this.setter = setter;
		}
		
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIP);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, 8080);
                try {
                	   
                	handler.post(new Runnable() {
                        //@Override
                        public void run() {
                            //tv.append("\nstart");
                        }
                    });
                	
                	Log.d("ClientActivity", "C: Sending command.");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                	out.println(this.jsonString);
                	
                	final String inStr = in.readLine();
                	
                	//Set answer
                	setter.setResult(inStr);
                	
                	//Print answer
                	handler.post(new Runnable() {
                        //@Override
                        public void run() {
                            //tv.append("\n" + inStr );
                        }
                    });
                	
                	Log.d("ClientActivity", "C: Sent.");
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
            }
        }
    }
}
