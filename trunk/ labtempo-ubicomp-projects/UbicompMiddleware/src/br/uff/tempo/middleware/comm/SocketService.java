package br.uff.tempo.middleware.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.util.Log;


public class SocketService {

    private static final int SERVER_PORT = 10006;
    //private static final String SERVER_IP = "192.168.1.111";
    //private static final String SERVER_IP = "192.168.1.28";


    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static String receiveStatus(int port)
    {
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

    public static void sendStatus(String address,int port, String status)
    {
    	DatagramSocket s = null;
    	
    	try {

            s = new DatagramSocket();
            InetAddress local = InetAddress.getByName(address);
            int msg_length = status.length();
            byte[] message = status.getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local, port);
            s.send(p);
        } catch (IOException ex) {
        	Log.e("SocketService", "sendStatus: " + ex.getMessage());
        }
        finally {
        	
        	if (s != null)
        		s.close();
        }
    }


}