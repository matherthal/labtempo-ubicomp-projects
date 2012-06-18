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


public class SocketService {

    private static final int SERVER_PORT = 10006;
    //private static final String SERVER_IP = "192.168.1.111";
    private static final String SERVER_IP = "192.168.1.28";


    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static String receiveStatus(int port)
    {
        String text = null;
        try {

            byte[] message = new byte[1500];
            DatagramPacket p = new DatagramPacket(message, message.length);
            DatagramSocket s = new DatagramSocket(port);
            s.receive(p);
            text = new String(message, 0, p.getLength());
            s.close();

        } catch (SocketException ex) {
            Logger.getLogger(SocketService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }

    public static void sendStatus(String address,int port, String status)
    {
        try {

            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName(address);
            int msg_length = status.length();
            byte[] message = status.getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local, port);
            s.send(p);
        } catch (IOException ex) {
            Logger.getLogger(SocketService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}