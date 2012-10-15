package br.uff.tempo.middleware.comm.current.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.util.Log;
import br.uff.tempo.middleware.comm.interest.api.NewDispatcher;
import br.uff.tempo.middleware.e.SmartAndroidException;

public class CommandExecution extends Thread {

	private DatagramSocket sk;
	private DatagramPacket dgp;

	public CommandExecution(DatagramSocket sk, DatagramPacket dgp) {
		this.sk = sk;
		this.dgp = dgp;
	}

	@Override
	public void run() {
		try {
			String rcvd = new String(dgp.getData(), 0, dgp.getLength(), "US-ASCII");

			String[] call = rcvd.split(";");
			
			String calleeID = call[0];
			String jsonstring = call[1];
			
			String result = NewDispatcher.getInstance().dispatch(calleeID, jsonstring) + ";";
			
			byte[] bufsk = result.getBytes();
			DatagramPacket out = new DatagramPacket(bufsk, bufsk.length, dgp.getAddress(), dgp.getPort());

			sk.send(out);
		} catch (UnsupportedEncodingException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		} catch (IOException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		} catch (SmartAndroidException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		}
	}
}
