package br.uff.tempo.middleware.comm.current.api;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class SocketService {

	private static final int SERVER_PORT = 10006;
	// private static final String SERVER_IP = "192.168.1.111";
	// private static final String SERVER_IP = "192.168.1.28";
	
	private static ExecutorService eS = Executors.newCachedThreadPool(); 

	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;
	private final int PORT = 4000;
	DatagramSocket sk;
	InetAddress local;
	int port;

	public SocketService() {
		


		try {
			sk = new DatagramSocket(PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void close() {
		if (sk!=null)
			sk.close();
	}

	public static String sendReceive(String address, String message)
			throws Exception {
		DatagramSocket s = new DatagramSocket();
		String outString = message;
		int msg_length = outString.length();
		byte[] bufDP = new byte[1500];
		byte[] buf = outString.getBytes();
		DatagramPacket dp = new DatagramPacket(bufDP, bufDP.length);

		InetAddress hostAddress = InetAddress.getByName(address);
		DatagramPacket out = null;
		out = new DatagramPacket(buf, msg_length, hostAddress,4000);
		
		s.send(out);

		s.receive(dp);
		String rcvd = new String(dp.getData());
		rcvd = rcvd.substring(0, rcvd.lastIndexOf(";"));
		s.close();
		
		return rcvd;
	}

	public void receiveSend(Dispatcher dispatcher) throws Exception {
		int PORT = 4000;
		if (sk == null) {
			sk = new DatagramSocket(PORT);
		}
		
		byte[] buf = new byte[1500];
		DatagramPacket dgp = new DatagramPacket(buf, buf.length);
		
		sk.receive(dgp);

		CommandExecution command = new CommandExecution(sk, dgp, dispatcher);
		command.start();
	}

}