package br.uff.tempo.apps.stove;

import br.uff.tempo.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Oven extends Fragment {

	private LinearLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null)
			return null;
		
		layout = (LinearLayout) inflater.inflate(R.layout.stove_oven, container, false);
		
		return layout;
	}
}
