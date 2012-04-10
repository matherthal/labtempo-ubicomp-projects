package br.uff.tempo.testeRPC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StoveView extends Activity {
	//public AgentBase agent = new StoveAgent();
	public AgentBase agent = new AgentBase();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stove);
        
        //startService(new Intent(this, MediaService.class));  //Media service test
        //startService(new Intent(this, StoveAgent.class));
        startService(new Intent(this, AgentBase.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    public void buttonStove1_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove1Status);
		if (tv != null)
		{
			//Toggle label
			if (tv.getText().toString().equals("Boca 1 desligada"))
			{
	    		tv.setText("Boca 1 ligada");
				playSoundStove();
			}
			else
				tv.setText("Boca 1 desligada");
		}
		
		//Call server
		agent.sendMessage("");
    }
    
    public void buttonStove2_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove2Status);
		if (tv != null)
		{
			if (tv.getText().toString().equals("Boca 12 desligada"))
			{
	    		tv.setText("Boca 2 ligada");
				playSoundStove();
			}
			else
				tv.setText("Boca 2 desligada");
			//tv.setText(this.toggleState(tv.getText().toString()));
		}
		
		//Call server
		agent.sendMessage("");
    }
    
    public void buttonStove3_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove3Status);
		if (tv != null)
		{
			if (tv.getText().toString().equals("Boca 3 desligada"))
			{
	    		tv.setText("Boca 3 ligada");
				playSoundStove();
			}
			else
				tv.setText("Boca 3 desligada");
			//tv.setText(this.toggleState(tv.getText().toString()));
		}
		
		playSoundStove();
		
		//Call server
		agent.sendMessage("");
    }
    
    public void buttonStove4_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove4Status);
		if (tv != null)
		{
			if (tv.getText().toString().equals("Boca 4 desligada"))
			{
	    		tv.setText("Boca 4 ligada");
				playSoundStove();
			}
			else
				tv.setText("Boca 4 desligada");
			//tv.setText(this.toggleState(tv.getText().toString()));
		}
		
		playSoundStove();
		
		//Call server
		agent.sendMessage("");
    }
    
    public void buttonOven_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove4Status);
		if (tv != null)
		{
			if (tv.getText().toString().equals("Forno desligado"))
	    		tv.setText("Forno ligado");
			else
				tv.setText("Forno desligado");
		}
		
		//Play sound open
		Bundle b = new Bundle();
		// Bundle.putSerializable for full Objects (careful there)
		b.putInt(MediaService.SOUND_ID, R.raw.ovenopen);  
		Intent i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
		
		//Play sound close
		b = new Bundle();
		b.putInt(MediaService.SOUND_ID, R.raw.ovenopen);  
		i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
		
		//Call server
		agent.sendMessage("");
    }
    
    public String toggleState(String label)
    {
    	String[] text = label.split(" ");
    	String status = text[text.length - 1];
    	if (status.equals("desligada"))
    		text[text.length - 1] = "ligada";
		else
			text[text.length - 1] =  "desligada";
    	return text.toString();
    }
    
    public void buttonFinish_Clicked(View view)
    {
		finish();
    }
    
    private void playSoundStove()
    {
    	//Play sound
		Bundle b = new Bundle();
		// Bundle.putSerializable for full Objects (careful there)
		b.putInt(MediaService.SOUND_ID, R.raw.stove);  
		Intent i = new Intent(this, MediaService.class);
		i.putExtras(b);
		startService(i);
    }
}
