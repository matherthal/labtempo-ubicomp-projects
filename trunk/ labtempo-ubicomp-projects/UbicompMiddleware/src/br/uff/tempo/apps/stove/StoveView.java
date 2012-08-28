package br.uff.tempo.apps.stove;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveView extends FragmentActivity /* implements Observer */{

	private static final String TAG = "StoveView";

	private static int id;

	private MyPageAdapter mPageAdapter;

	private IStove stoveAgent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.setContentView(R.layout.viewpager_layout);

		// create a 4-burner stove buffer

		Intent i = getIntent();

		// A default name...
		String name = "Fogao";

		Bundle b = i.getExtras();

		if (b != null && b.getSerializable("agent") != null)
			stoveAgent = (IStove) b.getSerializable("agent");
		else {
			Stove s = new Stove(name + (++id)); // or create one (nobody sent an
			// agent)

			// identify the resource to the system (it calls
			// ResourceRegister.register())
			s.identifyPosition(new Position(20,20));

			stoveAgent = s;
		}

		/*
		 * // if the object comes from someone (like the house map) if
		 * (i.getExtras() != null) {
		 * 
		 * String type = i.getExtras().getString("type"); name =
		 * i.getExtras().getString("name");
		 * 
		 * if (type.equals("agent")) { Stove s = new Stove(name); s.identify();
		 * stoveAgent = s; } else { stoveAgent = new StoveStub(name); } } else {
		 * 
		 * Stove s = new Stove(name + (++id)); // or create one (nobody sent an
		 * // agent)
		 * 
		 * // identify the resource to the system (it calls //
		 * ResourceRegister.register()) s.identify(); stoveAgent = s; }
		 */

		// Used to create "pages" in this activity (pages can changed with a
		// finger)
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

	}

	public IStove getStoveState() {
		return stoveAgent;
	}

}