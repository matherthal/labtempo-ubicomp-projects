package br.uff.tempo.apps.map.objects;

import org.andengine.util.color.Color;

public interface INotificationBoxReceiver {

	public void showMessage(String message);

	public void setNotificationBoxColor(Color color);

	public void setNotificationBoxVisible(boolean visible);
	
}
