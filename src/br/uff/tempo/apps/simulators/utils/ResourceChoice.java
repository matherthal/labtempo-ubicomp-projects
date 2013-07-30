package br.uff.tempo.apps.simulators.utils;

import br.uff.tempo.apps.map.dialogs.ChosenData;
import br.uff.tempo.apps.simulators.bed.BedView;
import br.uff.tempo.apps.simulators.lamp.LampView;
import br.uff.tempo.apps.simulators.stove.StoveView;
import br.uff.tempo.apps.simulators.tv.TvView;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.stubs.PersonStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.stubs.BedStub;
import br.uff.tempo.middleware.resources.stubs.LampStub;
import br.uff.tempo.middleware.resources.stubs.StoveStub;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class ResourceChoice {
	
	public static final String AGENT = "agent";
	public static final String SIMULATOR = "simulator";

	public static ResourceWrapper choiceResource(ResourceData resourceData) {

		ResourceWrapper wrapper = new ResourceWrapper();

		if (resourceData.getType().equals(ResourceAgent.type(Stove.class))) {
			
			wrapper.setSimulator(StoveView.class);
			wrapper.setStub(new StoveStub(resourceData.getName()));
			
		} else if (resourceData.getType().equals(ResourceAgent.type(Lamp.class))) {
			
			wrapper.setSimulator(LampView.class);
			wrapper.setStub(new LampStub(resourceData.getName()));
			
		} else if (resourceData.getType().equals(ResourceAgent.type(Bed.class))) {
			
			wrapper.setSimulator(BedView.class);
			wrapper.setStub(new BedStub(resourceData.getName()));
			
		} else if (resourceData.getType().equals(ResourceAgent.type(Television.class))) {
			
			wrapper.setSimulator(TvView.class);
			wrapper.setStub(new TelevisionStub(resourceData.getName()));
			
		} else if (resourceData.getType().equals(ResourceAgent.type(Person.class))) {
			
			wrapper.setStub(new PersonStub(resourceData.getName()));
			//PS: There is no simulator class to the person
			
		} else {
			
			throw new SmartAndroidRuntimeException(resourceData.getType()
					+ " cannot be found!");
		}
		
		wrapper.setId(resourceData.getType());

		return wrapper;
	}

	public static ResourceWrapper choiceNewResource(ChosenData chosenData, Position position) {

		IResourceAgent agent = null;
		ResourceWrapper wrapper = new ResourceWrapper();
		boolean success = false;

		if (chosenData.getTag().equals(ResourceAgent.type(Stove.class))) {
			
			String name = chosenData.getName();
			wrapper.setSimulator(StoveView.class);
			agent = new Stove(name, name, position);
			success = agent.identify();
			wrapper.setAgent(agent);
			wrapper.setStub(new StoveStub(name));

		} else if (chosenData.getTag().equals(ResourceAgent.type(Lamp.class))) {
			
			String name = chosenData.getName();
			wrapper.setSimulator(LampView.class);
			agent = new Lamp(name, name, position);
			success = agent.identify();
			wrapper.setAgent(agent);
			wrapper.setStub(new LampStub(name));

		} else if (chosenData.getTag().equals(ResourceAgent.type(Bed.class))) {
			
			String name = chosenData.getName();
			wrapper.setSimulator(BedView.class);
			agent = new Bed(name, name, position);
			success = agent.identify();
			wrapper.setAgent(agent);
			wrapper.setStub(new BedStub(name));

		} else if (chosenData.getTag().equals(ResourceAgent.type(Television.class))) {
			
			String name = chosenData.getName();
			wrapper.setSimulator(TvView.class);
			agent = new Television(name, name, position);
			success = agent.identify();
			wrapper.setAgent(agent);
			wrapper.setStub(new TelevisionStub(name));
			
		} else {
			throw new SmartAndroidRuntimeException(chosenData.getTag()
					+ " cannot be found!");
		}
		
		wrapper.setId(chosenData.getTag());
		
		//If the register fail, there's no wrapper...
		if (!success) {
			wrapper = null;
		}			

		return wrapper;
	}
}
