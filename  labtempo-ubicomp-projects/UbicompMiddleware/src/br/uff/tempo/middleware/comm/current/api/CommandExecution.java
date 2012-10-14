package br.uff.tempo.middleware.comm.current.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.json.JSONException;

public class CommandExecution extends Thread {

	private DatagramSocket sk;
	private DatagramPacket dgp;
	private Dispatcher dispatcher;

	public CommandExecution(DatagramSocket sk, DatagramPacket dgp, Dispatcher dispatcher) {
		this.sk = sk;
		this.dgp = dgp;
		this.dispatcher = dispatcher;
	}

	@Override
	public void run() {


		try {


			String rcvd = new String(dgp.getData(), 0, dgp.getLength(), "US-ASCII");
			
			String[] call = rcvd.split(";");
			
			String calleeID = call[0];
			String jsonstring = call[1];
			
			String result = dispatcher.dispatch(calleeID, jsonstring) + ";";
			
			byte[] bufsk = result.getBytes();
			DatagramPacket out = new DatagramPacket(bufsk, bufsk.length, dgp.getAddress(), dgp.getPort());
			
			sk.send(out);
			// sk.close();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
