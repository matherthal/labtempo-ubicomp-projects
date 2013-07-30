package br.uff.tempo.apps.simulators.utils;

import android.content.Context;
import br.uff.tempo.R;
import br.uff.tempo.middleware.management.ResourceData;

public class Finders {

	public static boolean hasSimulator(ResourceData res, Context context) {
		
		String[] resArray = getSimulatorsList(context);
		int ans = Finders.linearSearch(resArray, res.getType());

		return ans != -1;
	}
	
	public static String[] getSimulatorsList(Context context) {
		return context.getResources().getStringArray(R.array.res_simulators);
	}
	
	public static int linearSearch(Object[] array, Object key) {
		
		int index = 0;
		
		for (Object value : array) {
			if (key.equals(value)) {
				break;
			}
			index++;
		}
		
		if (index == array.length)
			return -1;
		else {
			return index;
		}
	}
}
