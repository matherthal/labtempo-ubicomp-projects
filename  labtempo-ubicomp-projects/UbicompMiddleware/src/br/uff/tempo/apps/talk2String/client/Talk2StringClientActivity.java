package br.uff.tempo.apps.talk2String.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.uff.tempo.R;
import android.app.Activity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.apps.talk2String.ITalk;
import br.uff.tempo.apps.talk2String.server.TalkAgent;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class Talk2StringClientActivity extends Activity implements OnClickListener, OnInitListener {
	
	private static final int VOICE_RECOGNITION_REQUEST = 0x10101;
	private static final String TAG = "Talk2StringClientActivity";
	
	ITalk stub;
	
	Button enviar, stream;
	TextView mostraTexto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviamensagem);
		enviar = (Button) findViewById(R.id.enviarButton);
		stream = (Button) findViewById(R.id.streamButton);

		mostraTexto = (TextView) findViewById(R.id.chegouTextView);
		
		IResourceDiscovery discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		List<ResourceData> datas = discovery.searchForAttribute(ResourceData.TYPE, TalkAgent.class.getSimpleName());
		
		if (datas != null) {
			ResourceData data = datas.get(0);
			stub = new TalkStub(data.getRai());
			enviar.setOnClickListener(this);
			stream.setOnClickListener(this);
		}
		else {
			mostraTexto.setText("Não há servidor iniciado");
		}	
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(enviar)){
			speakToMe(v);
			//listenToMe(v);
		}
		else if(v.equals(stream)){
			stub.setURLStream("http://192.168.0.106:8080");
			Log.i(TAG, "Iniciando PackageManager");
			PackageManager packageManager = getPackageManager();
			String packageName = "teaonly.projects.droidipcam";
			Log.i(TAG, "Iniciando Intent para abrir o IP Camera");

			Intent intent = packageManager.getLaunchIntentForPackage(packageName);  
			if(null != intent){
				Log.i(TAG, "Iniciando IP Camera");
			    startActivity(intent);
				Log.i(TAG, "Activity Iniciada");

			}
			else{
				mostraTexto.setText("Verifique se o IP Cam está instalado corretamente no dispositivo.");
			}
		}
	}

//-----------------------------------------Reconhecimento de voz-------------------------------------------------------
	
	public void speakToMe(View view) {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak slowly and enunciate clearly.");
	    startActivityForResult(intent, VOICE_RECOGNITION_REQUEST);
	}
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VOICE_RECOGNITION_REQUEST && resultCode == RESULT_OK) {
	      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	      String firstMatch = (String) matches.get(0);
	      stub.setMessage(firstMatch);
	      mostraTexto.setText("Texto falado: " + firstMatch);
	    }
	  }
	  
//------------------------------------------Síntese/Reprodução de fala-----------------------------------------------------
	  
	  	private TextToSpeech mTextToSpeech = null;   //texto que será falado
	    private boolean speechSynthReady = false;
	    
	    
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

	    public void listenToMe(View view) {
	        if (!speechSynthReady) {
	            Toast.makeText(getApplicationContext(), "Speech Synthesis not ready.", Toast.LENGTH_LONG).show();
	            return;
	        }
	        int result = mTextToSpeech.setLanguage(Locale.US);    //se der errado, substituir por Locale.US
	        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	            Toast.makeText(getApplicationContext(), "Language not available. Check code or config in settings.", Toast.LENGTH_LONG).show();
	        }
	        else {
	            //TextView textView = (TextView) findViewById(R.id.speech_io_text);
	            //String textToSpeak = textView.getText().toString();
	            mTextToSpeech.speak(mostraTexto.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
	        }
	    }
	    
	    @Override
	    public void onInit(int status) {
	        if (status == TextToSpeech.SUCCESS) {
	            speechSynthReady = true;
	        }
	    }

	


}
