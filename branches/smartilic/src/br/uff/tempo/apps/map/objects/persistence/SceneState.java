package br.uff.tempo.apps.map.objects.persistence;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import br.uff.tempo.middleware.management.utils.Space;

public class SceneState implements Serializable {

	private static final long serialVersionUID = -8549631981924998915L;
	
	private LinkedList<RegistryData> data;
	private Space mapInfo;
	
	private boolean isLocked;
	
	public SceneState() {
		this.data = new LinkedList<RegistryData>();
	}
	
	public boolean isLocked() {
		return isLocked;
	}

	public synchronized void setLock(boolean lock) {
		this.isLocked = lock;
	}

	public Space getMapInfo() {
		return mapInfo;
	}
	
	public void setMapInfo(Space mapInfo) {
		this.mapInfo = mapInfo;
	}
	
	public void addData(RegistryData d) {
		
		if (!isLocked) {
			data.add(d);
		}
	}
	
	public void removeData(RegistryData d) {
		
		if (!isLocked) {
			data.remove(d);
		}
	}
	
	public void clear() {
		data.clear();
	}
	
	public List<RegistryData> getData() {
		return data;
	}
	
	public void setData(List<RegistryData> data) {
		this.data = (LinkedList<RegistryData>)data;
	}
}
