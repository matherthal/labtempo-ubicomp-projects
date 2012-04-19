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
}
