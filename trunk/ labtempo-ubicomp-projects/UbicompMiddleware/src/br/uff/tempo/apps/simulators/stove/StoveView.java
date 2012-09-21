package br.uff.tempo.apps.simulators.stove;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Stove;

public class StoveView extends AbstractView {
	
	private MyPageAdapter mPageAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.viewpager_layout);

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
	public IResourceAgent createNewResourceAgent() {
		//TODO get the name from a Dialog box
		return new Stove("SomeStove");
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
}