package br.uff.tempo.apps.simulators.stove;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.apps.simulators.AbstractView;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Stove;

public class StoveView extends AbstractView {
	
	private MyPageAdapter mPageAdapter;

	@Override
	public void createView(Bundle savedInstanceState) {
		super.setContentView(R.layout.viewpager_layout);
		
		// Used to create "pages" in this activity (pages can changed with a finger)
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, Burners.class.getName()));
		fragments.add(Fragment.instantiate(this, Oven.class.getName()));

		this.mPageAdapter = new MyPageAdapter(super.getSupportFragmentManager(), fragments);

		ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setAdapter(this.mPageAdapter);
	}

	@Override
	public IResourceAgent createNewResourceAgent() {
		String name = "GeneralStove" + getNextID();
		return new Stove(name, name);
	}

	@Override
	public AbstractPanel getPanel() {
		
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflator.inflate(R.layout.stove_burners, null);
		
		return (AbstractPanel) layout.findViewById(R.id.stoveBurnersPanel);
	}
}