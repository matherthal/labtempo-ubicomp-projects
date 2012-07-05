package br.uff.tempo.apps.stove;

import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.interfaces.IStove;
import br.uff.tempo.middleware.resources.stubs.StoveStub;

public class StoveView extends FragmentActivity /*implements Observer*/ {

	private static final String TAG = "StoveView";
	
	private StoveData stoveData;
	private MyPageAdapter mPageAdapter;
	
	private IStove stoveAgent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.setContentView(R.layout.viewpager_layout);
		
		//create a 4-burner stove buffer
		this.stoveData = new StoveData(4);
		
		Intent i = getIntent();
		
		//if the object comes from someone (like the house map)
		String name = null;
		
		if (i.getExtras() != null) {
			
			if ((name = i.getExtras().getString("agent")) != null) {
				Stove s = new Stove(name);
				s.identify();
				stoveAgent = s;
			}
			else {
				name = i.getExtras().getString("stub");
				stoveAgent = new StoveStub(name);
			}
		}
		else {
			
			Stove s = new Stove("Fogao"); //or create one (nobody sent an agent)
			
			//identify the resource to the system (it calls ResourceRegister.register())
			s.identify();
			stoveAgent = s;
		}

		//Used to create "pages" in this activity (pages can changed with a finger)
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Burners.class.getName()));
		fragments.add(Fragment.instantiate(this, Oven.class.getName()));
		
		this.mPageAdapter = new MyPageAdapter(super.getSupportFragmentManager(), fragments);
		
		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPageAdapter);
		
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
	}
	
	public IStove getStoveState() {
		return stoveAgent;
	}
	
}