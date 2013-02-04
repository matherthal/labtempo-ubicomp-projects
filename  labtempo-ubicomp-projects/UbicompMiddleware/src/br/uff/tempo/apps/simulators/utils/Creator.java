package br.uff.tempo.apps.simulators.utils;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.ChoosedData;
import br.uff.tempo.apps.map.dialogs.IChooser;
import br.uff.tempo.apps.map.dialogs.IDialogFinishHandler;
import br.uff.tempo.apps.map.dialogs.IListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.apps.map.objects.RegistryData;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public class Creator implements IChooser, IListGetter, IDialogFinishHandler {

	public static final int CREATE = 0;
	public static final int CONECT = 1;

	private Activity       activity;
	private ChooseResource registeredDialog;
	private ResourceConfig configDialog;
	private RegistryData   regData;
	private String[]       simulators;
	private ChoosedData    current;

	private int op = -1;

	public Creator(Activity activity) {
		this.activity = activity;
		registeredDialog = new ChooseResource(activity);
		configDialog = new ResourceConfig(activity);
		simulators = Finders.getSimulatorsList(activity);
	}

	public void chooseResource() {
		op = CONECT;
		MiddlewareOperation m = new MiddlewareOperation(activity, "");
		m.execute(null);
	}

	public void createResource() {
		op = CREATE;
		registeredDialog.showDialog(simulators);
	}

	@Override
	public void onGetList(List<ResourceData> result) {
		registeredDialog.showDialog(result);
	}

	@Override
	public void onRegisteredResourceChoosed(ChoosedData choosedData) {

		if (op == CREATE) {
			create(choosedData);
		} else if (op == CONECT) {
			conect(choosedData.getData());
		} else {
			Log.e("SmartAndroid", "Creator: Strange error... Operation invalid");
		}
	}
	
	@Override
	public void onDialogFinished(Dialog dialog) {
		regData = configDialog.getData();
		current.setName(regData.getResourceName());
	
		Map<String, Object> map = ResourceChoice.choiceNewResource(current);
		Class c = (Class) map.get(ResourceChoice.SIMULATOR);
		IResourceAgent ag = (IResourceAgent) map.get(ResourceChoice.AGENT);
		ag.identify();
		
		callSimulator(ag, c);
	}

	private void create(ChoosedData choosedData) {
		current = choosedData;
		configDialog.showDialog();
	}

	private void conect(ResourceData resourceData) {
		
		Map<String, Object> map = ResourceChoice.choiceResource(resourceData);
		Class c = (Class) map.get(ResourceChoice.SIMULATOR);
		IResourceAgent ag = (IResourceAgent) map.get(ResourceChoice.AGENT);

		callSimulator(ag, c);
	}
	
	private void callSimulator(IResourceAgent ag, Class simulatorActivity) {
		
		if (simulatorActivity != null) {

			// Starts the appropriated simulator activity
			Intent call = new Intent(activity, simulatorActivity);

			Bundle bundle = new Bundle();
			bundle.putSerializable(ResourceChoice.AGENT, ag);

			call.putExtras(bundle);
			activity.startActivity(call);

		} else {
			Log.e("SmartAndroid", "Can't start the simulator for "
					+ ag.getName());
		}
	}
}
