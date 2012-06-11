package br.uff.tempo.apps.baseview;

import java.util.ArrayList;
import java.util.Observable;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceAgent.ResourceBinder;
import br.uff.tempo.middleware.management.ResourceRepository;


public class BaseView extends Activity{
	private static final String TAG = "StoveView";
	private PopupWindow m_pw;
	//private StoveData stoveData;
	private View layout_popup;

	//private ListView list;
	TextView tv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //create a 4-burner stove buffer
        //this.stoveData = new StoveData(4);

        
        //setContentView(new Panel(this, stove));
        setContentView(R.layout.base);
        
        tv = (TextView) findViewById(R.id.textView2);
        (new BaseListener(tv)).start();
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
    	//stove.unbindService(mConnection);
    }
    
    //TODO: Improving the input method (an alternative to SeekBar)...

}
