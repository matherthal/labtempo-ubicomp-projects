package br.uff.tempo.middleware.comm.current.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.zip.DataFormatException;

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
			String rcvd = SocketService.decompress(dgp.getData());

			String[] call = rcvd.split(SocketService.BUFFER_END);
			
			String calleeID = call[0];
			String jsonstring = call[1];
			
			Log.d("SmartAndroid", String.format("Receive REMOTE msg %s from %s", jsonstring, calleeID));
			String result = NewDispatcher.getInstance().dispatch(calleeID, jsonstring) + SocketService.BUFFER_END;
			Log.d("SmartAndroid", String.format("Sending REMOTE msg %s to %s", result, calleeID));
			
			byte[] bufsk = SocketService.compress(result);
			DatagramPacket out = new DatagramPacket(bufsk, bufsk.length, dgp.getAddress(), dgp.getPort());

			sk.send(out);
		} catch (UnsupportedEncodingException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		} catch (IOException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		} catch (SmartAndroidException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		} catch (DataFormatException e) {
			Log.d("SmartAndroid", String.format("CommandExecution. Exception: %s", e));
		}
	}
}
