package br.uff.tempo.apps.map.objects.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

public class PersistHelper {

	public static String objectToString(Serializable object) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			new ObjectOutputStream(out).writeObject(object);
			byte[] data = out.toByteArray();
			out.close();

			out = new ByteArrayOutputStream();
			Base64OutputStream b64 = new Base64OutputStream(out, 0);
			b64.write(data);
			b64.close();
			out.close();

			return new String(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object stringToObject(String encodedObject) {
	    try {
	        return new ObjectInputStream(new Base64InputStream(
	                new ByteArrayInputStream(encodedObject.getBytes()), 0)).readObject();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static void saveToFile(String key, Serializable object, Editor editor) {
		
		String strObj = objectToString(object);
		
		editor.putString(key, strObj);
		editor.commit();
	}
	
	public static Object loadFromFile (String key, SharedPreferences prefs) {
		
		String strObj = prefs.getString(key, null);
		
		if (strObj == null) {
			return null;
		} else {
			return stringToObject(strObj);
		}
	}
}
