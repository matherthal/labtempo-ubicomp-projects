package br.uff.tempo.apps.stove;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import br.uff.tempo.R;

@Deprecated
public class StoveFragmentActivity extends FragmentActivity {

	private PagerAdapter mPageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.viewpager_layout);

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Burners.class.getName()));
		// fragments.add(Fragment.instantiate(this, Oven.class.getName()));

		this.mPageAdapter = new MyPageAdapter(super.getSupportFragmentManager(), fragments);

		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPageAdapter);

	}

}
