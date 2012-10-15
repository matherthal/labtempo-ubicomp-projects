package br.uff.tempo.middleware.comm.current.api;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

public class SocketService {

	private static final int SERVER_PORT = 4000;
	private DatagramSocket sk;

	public SocketService() {
		try {
			sk = new DatagramSocket(SERVER_PORT);
		} catch (SocketException e) {
			Log.d("SmartAndroid", String.format("Exception: %s", e.getMessage()));
		}
	}

	public void close() {
		if (sk != null) {
			sk.close();
		}
	}

	public static String sendReceive(String address, String message) throws Exception {
		DatagramSocket s = new DatagramSocket();
		String outString = message;
		int msg_length = outString.length();
		byte[] bufDP = new byte[1500];
		byte[] buf = outString.getBytes();
		DatagramPacket dp = new DatagramPacket(bufDP, bufDP.length);

		InetAddress hostAddress = InetAddress.getByName(address);
		DatagramPacket out = null;
		out = new DatagramPacket(buf, msg_length, hostAddress, SERVER_PORT);
		
		s.send(out);

		s.receive(dp);
		String rcvd = new String(dp.getData());
		rcvd = rcvd.substring(0, rcvd.lastIndexOf(";"));
		s.close();
		
		return rcvd;
	}

	public void receiveSend() throws Exception {
		if (sk == null) {
			sk = new DatagramSocket(SERVER_PORT);
		}
		
		byte[] buf = new byte[1500];
		DatagramPacket dgp = new DatagramPacket(buf, buf.length);
		
		sk.receive(dgp);

		CommandExecution command = new CommandExecution(sk, dgp);
		command.start();
	}
}