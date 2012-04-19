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
import android.widget.TextView;

public class SendingService {
    private String serverIP = "";
    private Handler handler = new Handler();
    public TextView tv;
    
	SendingService() {
		serverIP = "192.168.1.70";  //FIXME: IP shouldn't be hardcoded 
	}
	
	public void sendMessage(String msg) {
        if (!serverIP.equals("")) {
            Thread cThread = new Thread(new ClientThread(msg));
            cThread.start();
        }
	}	
	
	public class ClientThread implements Runnable {
    	private String msg = "Mensagem Vazia";
    	
		ClientThread(String msg) {
			this.msg = msg;
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
                            tv.append("\nstart");
                        }
                    });
                	
                	Log.d("ClientActivity", "C: Sending command.");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                	out.println(this.msg);
                	
                	final String inStr = in.readLine();    
                	handler.post(new Runnable() {
                        //@Override
                        public void run() {
                            tv.append("\n" + inStr );
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
