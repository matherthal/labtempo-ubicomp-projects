package br.uff.tempo.middleware.comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;

import org.json.JSONException;

import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class Caller {
    private ResourceAgentIdentifier calleeAgent;
    
    private int port = 8080;
    private Handler handler = new Handler();
    
	public Caller(String calleeAgent) {
		this.calleeAgent = new ResourceAgentIdentifier(calleeAgent);
		//serverIP = "192.168.1.70";  //FIXME: IP shouldn't be hardcoded 
	}
	
	public String getAgentCaller() {
		return calleeAgent.getType()+":"+calleeAgent.getName();
	}

	public String sendMessage(String jsonString) {
		//add callee + methodCaller to JSONObject
		
		try {
			return Dispatcher.getInstance().dispatch(calleeAgent.getRai(),jsonString);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}	
	
}
