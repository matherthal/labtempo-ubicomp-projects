package br.uff.tempo.apps.simulators.stove;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import br.uff.tempo.R;

public class Burners extends Fragment {

	private LinearLayout layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null)
			return null;

		layout = (LinearLayout) inflater.inflate(R.layout.stove_burners, container, false);

		return layout;
	}
}
