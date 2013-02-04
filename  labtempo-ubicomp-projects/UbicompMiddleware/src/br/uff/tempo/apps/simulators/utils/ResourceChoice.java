package br.uff.tempo.apps.simulators.utils;

import java.util.HashMap;
import java.util.Map;

import br.uff.tempo.apps.map.dialogs.ChoosedData;
import br.uff.tempo.apps.simulators.bed.BedView;
import br.uff.tempo.apps.simulators.lamp.LampView;
import br.uff.tempo.apps.simulators.stove.StoveView;
import br.uff.tempo.apps.simulators.tv.TvView;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Lamp;
import br.uff.tempo.middleware.resources.Stove;
import br.uff.tempo.middleware.resources.Television;
import br.uff.tempo.middleware.resources.stubs.BedStub;
import br.uff.tempo.middleware.resources.stubs.LampStub;
import br.uff.tempo.middleware.resources.stubs.StoveStub;
import br.uff.tempo.middleware.resources.stubs.TelevisionStub;

public class ResourceChoice {

	public static final String SIMULATOR = "simulator";
	public static final String AGENT = "agent";

	public static Map<String, Object> choiceResource(ResourceData resourceData) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (resourceData.getType().equals(ResourceAgent.type(Stove.class))) {
			map.put(SIMULATOR, StoveView.class);
			map.put(AGENT, new StoveStub(resourceData.getName()));
		} else if (resourceData.getType()
				.equals(ResourceAgent.type(Lamp.class))) {
			map.put(SIMULATOR, LampView.class);
			map.put(AGENT, new LampStub(resourceData.getName()));
		} else if (resourceData.getType().equals(ResourceAgent.type(Bed.class))) {
			map.put(SIMULATOR, BedView.class);
			map.put(AGENT, new BedStub(resourceData.getName()));
		} else if (resourceData.getType().equals(
				ResourceAgent.type(Television.class))) {
			map.put(SIMULATOR, TvView.class);
			map.put(AGENT, new TelevisionStub(resourceData.getName()));
		}

		return map;
	}

	public static Map<String, Object> choiceNewResource(ChoosedData choosedData) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (choosedData.getTag().equals(ResourceAgent.type(Stove.class))) {
			String name = choosedData.getName();
			map.put(SIMULATOR, StoveView.class);
			map.put(AGENT, new Stove(name, name));
			
		} else if (choosedData.getTag()	.equals(ResourceAgent.type(Lamp.class))) {
			String name = choosedData.getName();
			map.put(SIMULATOR, LampView.class);
			map.put(AGENT, new Lamp(name, name));
			
		} else if (choosedData.getTag().equals(ResourceAgent.type(Bed.class))) {
			String name = choosedData.getName();
			map.put(SIMULATOR, BedView.class);
			map.put(AGENT, new Bed(name, name));
			
		} else if (choosedData.getTag().equals(	ResourceAgent.type(Television.class))) {
			String name = choosedData.getName();
			map.put(SIMULATOR, TvView.class);
			map.put(AGENT, new Television(name, name));
		}

		return map;
	}
}
