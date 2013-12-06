package br.uff.tempo.apps.simulators.utils;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import br.uff.tempo.apps.map.dialogs.ChooseResource;
import br.uff.tempo.apps.map.dialogs.ChosenData;
import br.uff.tempo.apps.map.dialogs.IChooser;
import br.uff.tempo.apps.map.dialogs.IDialogFinishHandler;
import br.uff.tempo.apps.map.dialogs.IListGetter;
import br.uff.tempo.apps.map.dialogs.MiddlewareOperation;
import br.uff.tempo.apps.map.dialogs.ResourceConfig;
import br.uff.tempo.apps.map.objects.persistence.RegistryData;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;

/**
 * Helper class to create a new Agent or connect to an existing one, and use it
 * in the appropriated simulator UI. This simulator will control that agent
 * 
 * @author dbarreto
 */
public class Creator implements IChooser, IListGetter, IDialogFinishHandler {
	private static final String TAG = "Creator";

	public static final boolean NOT_AUTOMATICALLY_OPEN = false;
	public static final boolean AUTOMATICALLY_OPEN = true;

	private static final int CREATE = 0;
	private static final int CONECT = 1;

	private Activity activity;
	private ChooseResource chooseResDialog;
	private ResourceConfig configDialog;
	private ICreationFinisher creationFinisher;
	private RegistryData regData;
	private String[] simulators;
	private ChosenData current;
	private boolean canOpen;
	private Boolean loadSprites = true;
	

	private int op = -1;

	/**
	 * Create a new 'Creator' instance
	 * 
	 * @param activity
	 *            The activity that will nestle all dialogs that will be created
	 * @param creationFinisher
	 *            Interface that will handle what happen when the resource
	 *            creation is done
	 */
	public Creator(Activity activity, ICreationFinisher creationFinisher) {
		this.activity = activity;
		this.creationFinisher = creationFinisher;

		chooseResDialog = new ChooseResource(activity, this);
		configDialog = new ResourceConfig(activity, this);
		simulators = Finders.getSimulatorsList(activity);
	}

	/**
	 * Create a new 'Creator' instance The interface ICreationFinisher will be
	 * null
	 * 
	 * @param activity
	 *            The activity that will nestle all dialogs that will be created
	 */
	public Creator(Activity activity) {
		this(activity, null);
	}

	@SuppressWarnings("all")
	/**
	 * Choose a resource included in the Registered resource list
	 * provided by ResourceDiscovery and call its simulator UI.
	 * This simulator will control the resource chosen.
	 * 
	 * @param canOpen Say if the simulator UI will be opened automatically
	 */
	public void chooseResource(boolean canOpen) {
		this.canOpen = canOpen;
		op = CONECT;
		new MiddlewareOperation(activity, this, "").execute(null);
	}

	/**
	 * Choose a resource included in the Registered resource list provided by
	 * ResourceDiscovery and call its simulator UI. This simulator will control
	 * the resource chosen. By default the simulator is called automatically
	 */
	public void chooseResource() {
		chooseResource(AUTOMATICALLY_OPEN);
	}

	/**
	 * Create a new resource agent and call its simulator UI. This simulator
	 * will control the resource created.
	 * 
	 * @param canOpen
	 *            Say if the simulator UI will be opened automatically
	 */
	public void createResource(boolean canOpen) {
		this.canOpen = canOpen;
		op = CREATE;
		chooseResDialog.showDialog(simulators);
	}
	
	public void createAllResources() {
		loadSprites = true;
		new MiddlewareOperation(activity, this, "").execute(null);		
	}

	/**
	 * Create a new resource agent and call its simulator UI. This simulator
	 * will control the resource created. By default the simulator is called
	 * automatically
	 */
	public void createResource() {
		createResource(AUTOMATICALLY_OPEN);
	}
	
	@Override
	public void onGetList(List<ResourceData> result) {
		if (loadSprites) {
			//Loads all sprites on screen
			for (ResourceData rd : result) {
				try {
					ResourceWrapper wrapper = ResourceChoice.choiceResource(rd); 
					this.creationFinisher.onResourceCreationFinished(wrapper);
				} catch (SmartAndroidRuntimeException e) {
					Log.i(TAG, e.getMessage());
				}
			}
		} else
			//Loads list of resource agents to be chosen
			chooseResDialog.showDialog(result);
		loadSprites = false;
	}

	@Override
	public void onResourceChosen(ChosenData chosenData) {

		if (op == CREATE) {
			create(chosenData);
		} else if (op == CONECT) {
			conect(chosenData.getData());
		} else {
			Log.wtf("SmartAndroid",
					"Creator: Strange error... Operation invalid [" + op + "]");
		}
	}

	private void create(ChosenData chosenData) {
		current = chosenData;
		configDialog.showDialog();
	}

	private void conect(ResourceData resourceData) {
		prepareCall(ResourceChoice.choiceResource(resourceData));
	}

	@Override
	// Called when the information about the resource just created was filled
	// (e.g. name, position) and ResourceConfig dialog was finished
	public void onDialogFinished(Dialog dialog) {

		regData = configDialog.getData();
		current.setName(regData.getResourceName());

		Position position = new Position(regData.getPositionX(), regData.getPositionY());
		prepareCall(ResourceChoice.choiceNewResource(current, position));
	}

	@SuppressWarnings("rawtypes")
	private void prepareCall(ResourceWrapper wrapper) {
		
		if (wrapper != null) {
			Class simulator = wrapper.getSimulator();

			// The stub to the agent
			IResourceAgent stub = wrapper.getStub();

			callSimulator(stub, simulator);
		}
		finishCreation(wrapper);
	}

	/**
	 * Call the Simulator activity passing the agent
	 * @param agent The agent that will be controlled
	 * @param simulatorActivity The activity that will be called, and will control the agent
	 */
	@SuppressWarnings("rawtypes")
	public void callSimulator(IResourceAgent agent, Class simulatorActivity) {

		if (canOpen) {
			if (simulatorActivity != null) {

				// Starts the appropriated simulator activity
				Intent call = new Intent(activity, simulatorActivity);

				Bundle bundle = new Bundle();
				bundle.putSerializable(ResourceChoice.AGENT, agent);

				call.putExtras(bundle);
				activity.startActivity(call);

			} else {
				throw new SmartAndroidRuntimeException(
						"Can't start the simulator for " + agent.getName());
			}
		}
		canOpen = true;
	}

	private void finishCreation(ResourceWrapper wrapper) {

		if (this.creationFinisher != null) {
			this.creationFinisher.onResourceCreationFinished(wrapper);
		} else {
			Log.w("SmartAndroid",
					"Creator: Cannot call onResourceCreationFinished. Interface doesn't provided by the user");
		}
	}
}
