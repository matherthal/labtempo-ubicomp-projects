package br.uff.tempo.testrpcserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;




import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestRPCServer extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textDisplay = (TextView) this.findViewById(R.id.text);
        textDisplay.setText("");
        /*try {
            textDisplay.setText(response());
        } catch (JSONRPC2ParseException e) {
            textDisplay.setText(e.getMessage());
        	// Handle exception...
        }*/      
        runTcpServer();
    }

    public String response() throws JSONRPC2ParseException
    {
 // Parse request string
        
        String jsonString =   "{ "+
								  "\"id\"      : \"req-001\","+
								  "\"method\"  : \"makePayment\","+
								  "\"params\"  : { \"amount\" : 175.05, \"recipient\" : \"Penny Adams\" },"+
								  "\"jsonrpc\" : \"2.0\""+
							  "}";
        JSONRPC2Request reqIn = null;
        
        reqIn = JSONRPC2Request.parse(jsonString);
        
        String result = "payment-id-001";
        JSONRPC2Response respOut = new JSONRPC2Response(result, reqIn.getID());

        // Serialise response to JSON-encoded string
        jsonString = respOut.toString();
        
        return jsonString;
    }
    
    private TextView textDisplay;
    private static final int TCP_SERVER_PORT = 21111;
    private void runTcpServer() {
    	ServerSocket ss = null;
    	try {
			ss = new ServerSocket(TCP_SERVER_PORT);
			//ss.setSoTimeout(10000);
			//accept connections
			Socket s = ss.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//receive a message
			String incomingMsg = in.readLine() + System.getProperty("line.separator");
			Log.i("TcpServer", "received: " + incomingMsg);
			textDisplay.append("received: " + incomingMsg);
			//send a message
			String outgoingMsg = "goodbye from port " + TCP_SERVER_PORT + System.getProperty("line.separator");
			out.write(outgoingMsg);
			out.flush();
			Log.i("TcpServer", "sent: " + outgoingMsg);
			textDisplay.append("sent: " + outgoingMsg);
			//SystemClock.sleep(5000);
			s.close();
		} catch (InterruptedIOException e) {
			//if timeout occurs
			e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    
}