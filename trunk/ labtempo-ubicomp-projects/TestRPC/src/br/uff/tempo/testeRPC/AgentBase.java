package br.uff.tempo.testeRPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AgentBase extends Service {
	private static final String TAG = "AgentBase";
	private static AgentBase instance = null;
	
	private static final String TCP_SERVER_IP = "192.168.1.70";
	private static final int TCP_SERVER_PORT = 21111;
	
	public static AgentBase getInstance() {
		if(instance == null) {
			instance = new AgentBase();
		}
		return instance;
	}
	
	@Override
	public void onCreate() {
		// Exists only to defeat instantiation.
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendMessage(String msg) {
    	try {
			Socket s = new Socket(TCP_SERVER_IP, TCP_SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//send output msg
			String outMsg = "TCP connecting to " + TCP_SERVER_PORT + System.getProperty("line.separator"); 
			out.write(outMsg);
			out.flush();
			Log.i(TAG, "TcpClient sent: " + outMsg);
			//accept server response
			String inMsg = in.readLine() + System.getProperty("line.separator");
			/*String str = "received: " + inMsg;
			tv.setText(str);*/
			Log.i(TAG, "TcpClient received: " + inMsg);
			//close connection
			s.close();
		} catch (UnknownHostException e) {
			//tv.setText("UnknownException "+ e.getMessage());
			Log.i(TAG, "UnknownException " + e.getMessage());
		} catch (IOException e) {
			//tv.setText("IOException "+ e.getMessage());
			Log.i(TAG, "IOException " + e.getMessage());
		} 
    }
}
