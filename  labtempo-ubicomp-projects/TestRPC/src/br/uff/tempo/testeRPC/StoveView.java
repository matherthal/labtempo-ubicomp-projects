package br.uff.tempo.testeRPC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    	finish();
        super.onDestroy();
    }
    
    public void buttonStove1_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove1Status);
		if (tv != null)
		{
			//tv.setText(this.toggleState(tv.getText().toString()));
			agent.sendMessage("");
		}
    }
    
    public void buttonStove2_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove2Status);
		if (tv != null)
			tv.setText(this.toggleState(tv.getText().toString()));
    }
    
    public void buttonStove3_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove3Status);
		if (tv != null)
			tv.setText(this.toggleState(tv.getText().toString()));
    }
    
    public void buttonStove4_Clicked(View view)
    {
		final TextView tv = (TextView) findViewById(R.id.textStove4Status);
		if (tv != null)
			tv.setText(this.toggleState(tv.getText().toString()));
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
}
