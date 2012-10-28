package br.uff.tempo.middleware.comm.current.api;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import android.util.Log;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;

public class SocketService {
	public final static String BUFFER_END = "#@#";
	
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
		
		byte[] output = compress(message);

		byte[] bufDP = new byte[1500];
		DatagramPacket dp = new DatagramPacket(bufDP, bufDP.length);

		InetAddress hostAddress = InetAddress.getByName(address);
		DatagramPacket out = null;
		out = new DatagramPacket(output, output.length, hostAddress, SERVER_PORT);
		
		s.send(out);

		s.receive(dp);
		
		String rcvd = decompress(dp.getData());

		rcvd = rcvd.substring(0, rcvd.lastIndexOf(BUFFER_END));
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

	public static byte[] compress(String msg) {
		try {
			Deflater deflater = new Deflater();
			deflater.setInput(msg.getBytes("UTF-8"));
			deflater.finish();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			byte[] buf = new byte[8192];
			while (!deflater.finished()) {
				int byteCount = deflater.deflate(buf);
				baos.write(buf, 0, byteCount);
			}
			
			deflater.end();
			byte[] compressedBytes = baos.toByteArray();
			return compressedBytes;
		} catch (Exception e) {
			throw new SmartAndroidRuntimeException("Exception compressing: " + msg, e);
		}
	}

	public static String decompress(byte[] compressedBytes) {
		try {
			Inflater inflater = new Inflater();			
			inflater.setInput(compressedBytes);
	
			ByteArrayOutputStream baosI = new ByteArrayOutputStream(compressedBytes.length);
			
			byte[] buff = new byte[8192];
			while (!inflater.finished()) {
				int count = inflater.inflate(buff);
				baosI.write(buff, 0, count);
			}
			
			baosI.close();
			inflater.end();
			
			byte[] output = baosI.toByteArray();
			
			String msgI = new String(output, "UTF-8");
			return msgI;
		} catch (Exception e) {
			throw new SmartAndroidRuntimeException("Exception decompressing: " + compressedBytes, e);
		}
	}
}