package br.uff.tempo.middleware.comm.interest.api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Stove implements Callable {
	
	private InterestAPI api = new InterestAPIImpl();
	
	private String taskInterest = "app-chat://message";
	
	private String sleepInterest = "app-chat://android";
	
	private Handler handler;
	
	public Stove(Handler handler) {
		this.handler = handler;
	}

	public void registerInterests() {
		Message androidMessage = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("mensagem", "\n Eu sou um fogão e estou aguardando tarefas \n");
		androidMessage.setData(bundle);
		this.handler.sendMessage(androidMessage);
		
//		InterestAPI api = new InterestAPIImpl();
		
		try {
			Log.d("interest.api", "Vou registrar o interesse do AgentSample" + this.taskInterest);
			this.api.addInterest(this.taskInterest, this);
			Log.d("interest.api", "Registrei o interesse do AgentSample" + this.taskInterest);
			
			Log.d("interest.api", "Vou registrar o interesse do AgentSample" + this.sleepInterest);
			this.api.addInterest(this.sleepInterest, this);
			Log.d("interest.api", "Registrei o interesse do AgentSample" + this.sleepInterest);
			
			Log.d("interest.api", "Vou enviar a mensagem do AgentSample");
			this.api.sendMessage(this.taskInterest, "Eu sou um fogão e sou capaz de fazer comida. Diga a comida que quer que eu faça: ");
			Log.d("interest.api", "Enviei a mensagem do AgentSample");
		} catch (Exception e) {
			Log.d("interest.api", "Exception ao chamar addInterest do AgentSample.", e);
		}
	}
	
	public void call(String interest, String message, int address) {
		Log.d("interest.api", "Call do Agent chamado com message: " + message);
		
		if (this.taskInterest.equals(interest)) {
			Log.d("interest.api", "Call do Agent chamado com interest: " + interest + " e message: " + message);
			Message androidMessage = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("mensagem", "\n Eu sou um fogão e me pediram pra fazer um:\n\n" + message.replace("Claro: ", ""));
			androidMessage.setData(bundle);
			this.handler.sendMessage(androidMessage);

		} else if (this.sleepInterest.equals(interest)) {
			Log.d("interest.api", "Call do Agent chamado com interest: " + interest + " e message: " + message);
			Message androidMessage = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("mensagem", "\n ATENÇÃO: Foram dormir!!!!\n\nVou disparar um alarme e vou parar com as comidas em curso!!!");
			androidMessage.setData(bundle);
			this.handler.sendMessage(androidMessage);
		} else {
			Log.d("interest.api", "Call do Agent chamado com interest: " + interest + " diferente do esperado. E message: " + message);
		}
	}
	
	
	public void removeInterestCallback() {
		try {
			Log.d("interest.api", "Vou remover o interesse no callback do AgentSample");
			this.api.removeInterestCallback(taskInterest, this);
			Log.d("interest.api", "Removi o interesse no callback do AgentSample");
		} catch (Exception e) {
			Log.d("interest.api", "Exception ao chamar removeInterestCallback do AgentSample.", e);
		}
	}
}
