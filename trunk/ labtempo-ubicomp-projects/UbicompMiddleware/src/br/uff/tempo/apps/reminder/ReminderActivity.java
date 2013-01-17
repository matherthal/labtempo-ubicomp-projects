package br.uff.tempo.apps.reminder;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import br.uff.tempo.R;



public class ReminderActivity extends Activity {
	List<Prescription> prescriptions;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);
	
	}
	
	public void onSubmitPrescriptionClick() {
		
	}

}
