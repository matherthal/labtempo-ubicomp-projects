package br.uff.tempo.apps.talk2String.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent;
//import com.example.baba_eletronica1.R;

public class Talk2StringServerActivity extends Activity implements OnInitListener {
	
	private static final String TAG = "Talk2StringServerActivity";
	
	TextToSpeech mTextToSpeech;
	Talk2StringServerActivity instance = this;
	TalkAgent agent;
	TextView reconheceu;
	Button enviar, stream;
	WebView myWebView;
	WebSettings webSettings;
	
	private String reconheceuStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		enviar = (Button) findViewById(R.id.enviarButton);
		//enviar.setVisibility(View.GONE);
		stream = (Button) findViewById(R.id.streamButton);
		//stream.setVisibility(View.GONE);
		
		setContentView(R.layout.enviamensagem);
		agent = new TalkAgent("Agente", "Agente.ra");
		agent.identify();
		
		
		reconheceu = (TextView) findViewById(R.id.chegouTextView);
		
		String name = "stakeholder";
		TalkAgentStakeholder stakeholder = new TalkAgentStakeholder(name, TalkAgentStakeholder.class.getName(), name+".ra");
		stakeholder.identify();
		agent.registerStakeholder("setMessage", stakeholder.getRANS());
		agent.registerStakeholder("setURLStream", stakeholder.getRANS());
		//reconheceu.setText("teste");
		myWebView = (WebView) findViewById(R.id.webView);
		myWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private String enviaMensagem() {
		// TODO Auto-generated method stub
		return "Olá, mamãe!";
	}
	
	public class TalkAgentStakeholder extends ResourceAgent {

		/**
		 * 
		 */

		public TalkAgentStakeholder(String name, String type, String rans) {
			super(name, type, rans);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void notificationHandler(String rai, String method, Object value) {
			if (method.equals("setMessage")) {
				String message = agent.getMessage();
				reconheceuStr = value.toString();
				instance.runOnUiThread(updateTimeTask);
			}
			else if (method.equals("setURLStream")){
				Log.i(TAG, "Recebeu método setURLStream");
				String message = agent.getURLStream();
				//reconheceu.setText("URL Streaming!!!");
				//reconheceuStr = value.toString();
				//Log.i(TAG, "Iniciando Intent!!!");
				//Intent	viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reconheceuStr));
				//Log.i(TAG, "Iniciando Activity!!!");
		    	//startActivity(viewIntent); 
				//Log.i(TAG, "BROWSER supostamente iniciado.");
				 
				
				myWebView = (WebView) findViewById(R.id.webView);
				webSettings = myWebView.getSettings();
				myWebView.loadUrl(message);
			} 
		}
	}
	
	private Runnable updateTimeTask = new Runnable() {
	    //@Override
	    public void run() {
	    	reconheceu.setText(reconheceuStr);
            listenToMe();
	    	Toast.makeText(instance, "Chegou", Toast.LENGTH_LONG).show();
	    }
	};
	

	private boolean speechSynthReady = false;
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            speechSynthReady = true;
        }
	}
	
	public void listenToMe() {
		if (!speechSynthReady) {
            Toast.makeText(getApplicationContext(), "Speech Synthesis not ready.", Toast.LENGTH_LONG).show();
            return;
        }
        int result = mTextToSpeech.setLanguage(Locale.US);    //se der errado, substituir por Locale.US
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(getApplicationContext(), "Language not available. Check code or config in settings.", Toast.LENGTH_LONG).show();
        }
        else {        	
        	mTextToSpeech.speak(reconheceuStr, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
	

    @Override
    protected void onPause() {
        super.onPause();
        mTextToSpeech.shutdown();
        mTextToSpeech = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextToSpeech = new TextToSpeech(getApplicationContext(), this);
    }
}
