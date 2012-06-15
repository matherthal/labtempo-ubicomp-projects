package br.uff.tempo.apps.stove;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.stubs.StoveStub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class StoveView extends FragmentActivity /*implements Observer*/ {

	private static final String TAG = "StoveView";
	private static final int NUM_AWESOME_VIEWS = 0;
	
	private PopupWindow m_pw;
	private StoveData stoveData;
	private View layout_popup;
	private MyPageAdapter mPageAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.setContentView(R.layout.viewpager_layout);
		
		//create a 4-burner stove buffer
		//this.stoveData = new StoveData(4);

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Burners.class.getName()));
		fragments.add(Fragment.instantiate(this, Oven.class.getName()));
		
		this.mPageAdapter = new MyPageAdapter(super.getSupportFragmentManager(), fragments);
		
		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPageAdapter);
		
		//stoveData = (StoveData) getIntent().getSerializableExtra("stoveData");
		
		//setContentView(new Panel(this, stove));
		//setContentView(R.layout.stove);
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
		
		Intent i = getIntent();
		i.putExtra("resourceData", stoveData);
		this.setResult(RESULT_OK, i);
		
		// stove.unbindService(mConnection);
	}
	
	public StoveData getStoveState() {
		return stoveData;
	}
/*
 
 	public void onBurnerIntensityChanged(int burner, int intensity) {
		this.stoveData.setBurnerIntensity(burner, intensity);
	}
	
	// TODO: Improving the input method (an alternative to SeekBar)...
	public void showPopup(int burn) {

//		
//		 // Make a View from our XML file LayoutInflater inflater =
//		 (LayoutInflater)
//		 this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); layout_popup
//		 = inflater.inflate(R.layout.popup, (ViewGroup)
//		 findViewById(R.id.popup_element));
//		  
//		 SeekBar sb = (SeekBar) layout_popup.findViewById(R.id.seekBar1);
//		 sb.setProgress(stoveData.getBurnerIntensity(burn)); sb.setTag(burn);
//		 
//		 TextView tv = (TextView) layout_popup.findViewById(R.id.tv1);
//		  
//		 tv.append(Integer.toString(burn + 1));
//		  
//		 m_pw = new PopupWindow( layout_popup, 200, 150, true);
//		 m_pw.showAtLocation(layout_popup, Gravity.CENTER, 0, 0);
//		 

	}

	public void onButtonInPopup(View target) {

//		 SeekBar sb = (SeekBar) layout_popup.findViewById(R.id.seekBar1);
//		 stoveData.setBurnerIntensity((Integer) sb.getTag(),
//		 sb.getProgress()); m_pw.dismiss();
		 
	}

	public void onRegisterClick(View target) {

		Toast.makeText(StoveView.this, "Register Called", Toast.LENGTH_LONG)
				.show();
	}

	public StoveData getStoveState() {
		return stoveData;
	}

	// Called when external representation of stove change its state
	// @Override
	public void update(Observable observable, Object data) {

		// received a change in the stove burners state from external stove
		if (data instanceof StoveData) {
			StoveData sd = (StoveData) data;

			Toast.makeText(StoveView.this, "Received a change state!",
					Toast.LENGTH_SHORT).show();

			for (int i = 0; i < sd.getBurners().length; i++) {
				onBurnerIntensityChanged(i, sd.getBurnerIntensity(i));
			}
		}
	}
*/
}