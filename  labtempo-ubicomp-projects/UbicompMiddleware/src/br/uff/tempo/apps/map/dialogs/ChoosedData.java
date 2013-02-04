package br.uff.tempo.apps.map.dialogs;

import br.uff.tempo.middleware.management.ResourceData;

public class ChoosedData {

	private String tag;
	private String name;
	private ResourceData data;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResourceData getData() {
		return data;
	}
	public void setData(ResourceData data) {
		this.data = data;
	}
}
