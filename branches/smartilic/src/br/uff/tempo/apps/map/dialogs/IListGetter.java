package br.uff.tempo.apps.map.dialogs;

import java.util.List;

import br.uff.tempo.middleware.management.ResourceData;

/**
 * Interface to receive a list of data.
 * Used in many dialogs. E.g. Receive a list of data from ResourceDiscovery
 * in order to show it in a dialog, so the user can choose some item in the list; 
 * @author dbarreto
 */
public interface IListGetter {

	/**
	 * Receive a list of data from some source. E.g. From ResourceDiscovery
	 * @param result
	 */
	void onGetList(List<ResourceData> result);

}
