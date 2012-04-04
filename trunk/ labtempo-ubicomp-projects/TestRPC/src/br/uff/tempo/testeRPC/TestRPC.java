package br.uff.tempo.testeRPC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;




import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class TestRPC extends Activity {
	
	private final String SERVER_IP = "192.168.1.70";
	private EditText et;
	private TextView tv;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView) findViewById(R.id.text);
        Button bt = (Button) findViewById(R.id.button1);
        et = (EditText) findViewById(R.id.editText1);
        bt.setOnClickListener(mButtonListener);
        /*try
        {  	tv.setText(""+requestOperation());	}
        catch (ExceptionInInitializerError ex)
        {  	tv.setText("exception "+ex.getMessage());	}*/
        

        
    }
    
    private OnClickListener mButtonListener = new OnClickListener()
    {

		public void onClick(View v) {
			runTcpClient(et.getText().toString());
		}
	};
    
    public String requestOperation(String recipient, Double amount)
    {
    	String method = "makePayment";
    	// The required named parameters to pass
        Map params = new HashMap();
        params.put("recipient", recipient);
        params.put("amount", amount);

        // The mandatory request ID
        String id = "req-001";

        // Create a new JSON-RPC 2.0 request
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);

        // Serialize the request to a JSON-encoded string
        String jsonString = reqOut.toString();
        
        return jsonString;
    	
    }
    
    private static final int TCP_SERVER_PORT = 21111;
	private void runTcpClient(String address) {
    	try {
			Socket s = new Socket(address, TCP_SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//send output msg
			String outMsg = "TCP connecting to " + TCP_SERVER_PORT + System.getProperty("line.separator"); 
			out.write(outMsg);
			out.flush();
			Log.i("TcpClient", "sent: " + outMsg);
			//accept server response
			String inMsg = in.readLine() + System.getProperty("line.separator");
			String str = "received: " + inMsg;
			tv.setText(str);
			Log.i("TcpClient", "received: " + inMsg);
			//close connection
			s.close();
		} catch (UnknownHostException e) {
			tv.setText("UnknownException "+ e.getMessage());
		} catch (IOException e) {
			tv.setText("IOException "+ e.getMessage());
		} 
    }
	//replace runTcpClient() at onCreate with this method if you want to run tcp client as a service
	private void runTcpClientAsService() {
		Intent lIntent = new Intent(this.getApplicationContext(), TcpClientService.class);
        this.startService(lIntent);
	}
}