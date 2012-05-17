package br.uff.tempo.middleware.comm;

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
    
	public Caller(String serverIP) {
		this.serverIP = serverIP;
		//serverIP = "192.168.1.70";  //FIXME: IP shouldn't be hardcoded 
	}
	
	public String sendMessage(String jsonString) {
		
		return "";
	}	
	
}
