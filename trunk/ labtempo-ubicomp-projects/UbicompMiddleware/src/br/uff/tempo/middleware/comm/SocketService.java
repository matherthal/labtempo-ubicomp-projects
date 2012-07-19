package br.uff.tempo.middleware.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class SocketService {

	private static final int SERVER_PORT = 10006;
	// private static final String SERVER_IP = "192.168.1.111";
	// private static final String SERVER_IP = "192.168.1.28";

	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;

	public static String receiveStatus(int port) {
		String text = null;
		DatagramSocket s = null;

		try {

			byte[] message = new byte[1500];
			DatagramPacket p = new DatagramPacket(message, message.length);

			s = new DatagramSocket(port);
			s.receive(p);
			text = new String(message, 0, p.getLength());

			Log.d("SocketService", "Received message: " + text);

		} catch (SocketException ex) {
			Log.e("SocketService", "receiveStatus: " + ex.getMessage());
		} catch (IOException ex) {
			Log.e("SocketService", "receiveStatus: " + ex.getMessage());
		} finally {

			if (s != null)
				s.close();
		}
		return text;
	}

	public static void sendStatus(String address, int port, String status) {
		DatagramSocket s = null;

		try {

			s = new DatagramSocket(1000);
			InetAddress local = InetAddress.getByName(address);
			int msg_length = status.length();
			byte[] message = status.getBytes();
			DatagramPacket p = new DatagramPacket(message, msg_length, local, port);
			s.send(p);
		} catch (IOException ex) {
			Log.e("SocketService", "sendStatus: " + ex.getMessage());
		} finally {

			if (s != null)
				s.close();
		}
	}

	DatagramSocket s;
	InetAddress local;
	int port;

	public SocketService(String address, int port) {
		try {
			this.port = port;
			s = new DatagramSocket(port);
			s.setReuseAddress(true);
			local = InetAddress.getByName(address);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void send(String status, int port) {
		int msg_length = status.length();
		byte[] message = status.getBytes();
		DatagramPacket p = new DatagramPacket(message, msg_length, local, port);
		try {
			s.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String receive() {
		byte[] message = new byte[1500];
		DatagramPacket p = new DatagramPacket(message, message.length);
		String text = null;
		try {
			s.receive(p);
			text = new String(message, 0, p.getLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	public void setAddress(String address) {
		try {
			this.local = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		s.close();
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
		out = new DatagramPacket(buf, msg_length, hostAddress, 4000);
		Log.d("sendReceive()", out.toString());
		s.send(out);

		s.receive(dp);
		String rcvd = new String(dp.getData());
		s.close();
		Log.d("SocketService", rcvd);
		return rcvd;
	}

	public static void receiveSend(Dispatcher dispatcher) throws Exception {
		int PORT = 4000;
		byte[] buf = new byte[1500];
		DatagramPacket dgp = new DatagramPacket(buf, buf.length);

		DatagramSocket sk;

		sk = new DatagramSocket(PORT);
		System.out.println("Server started");

		sk.receive(dgp);
		String rcvd = new String(dgp.getData());
		System.out.println(rcvd);
		Log.d("Received value", rcvd);
		String[] call = rcvd.split(";");
		String calleeID = call[0];
		String jsonstring = call[1];
		Log.d("Callee value", calleeID);
		Log.d("JSON value", jsonstring);
		String result = dispatcher.dispatch(calleeID, jsonstring) + ";";
		byte[] bufsk = result.getBytes();
		DatagramPacket out = new DatagramPacket(bufsk, bufsk.length, dgp.getAddress(), dgp.getPort());
		sk.send(out);
		sk.close();
	}

}