package br.uff.tempo.apps;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MediaService extends Service {
	private static final String TAG = "MediaAgent";

	protected MediaPlayer player;

	public static final String SOUND_ID = "SOUND_ID";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// Toast.makeText(this, "Media Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate -- commented");

		// player = MediaPlayer.create(this, R.raw.braincandy);
		// player.setLooping(false); // Set looping
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Media Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		player.stop();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// Toast.makeText(this, "Media Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");

		Bundle b = intent.getExtras(); // Get parameter
		int soundId = b.getInt(SOUND_ID); // Get sound

		// player = MediaPlayer.create(this, soundId); //Create player
		player = MediaPlayer.create(this, soundId); // Create player
		// player.setLooping(false); // Set looping
		player.start();
	}
}
