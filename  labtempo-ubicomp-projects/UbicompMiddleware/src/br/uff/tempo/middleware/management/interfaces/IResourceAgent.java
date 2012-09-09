package br.uff.tempo.middleware.management.interfaces;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import org.json.JSONException;

import br.uff.tempo.middleware.management.ResourceAgent;

public interface IResourceAgent extends Serializable {
	public String getResourceClassName();

	public enum CVType {
		Null, On, OFF, Temperature, Light, Open, Close, Gas, Wet
	};

	public enum SType {
		Null, Video, Audio, ShortText, LongText, Alarm, Beep, TurnOn, TurnOff
	}

	@Retention(RetentionPolicy.RUNTIME)
	// Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD })
	// This annotation can only be applied to class methods.
	public @interface ContextVariable {
		String name();

		String description() default "";

		CVType type() default CVType.Null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	// Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD })
	// This annotation can only be applied to class methods.
	public @interface Service {
		String name();

		String description() default "";

		SType type() default SType.Null;

	}

	public void registerStakeholder(String method, String url);

	public void notificationHandler(String rai, String method, Object value);

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public String getRAI();

	public void setRAI(String rai);

	public String getType();

	public void setType(String type);

	public ArrayList<ResourceAgent> getInterests();

	public void setInterests(ArrayList<ResourceAgent> interests);

	public ArrayList<String> getRegisteredList();

	public boolean isRegistered();

	public boolean identify();

	@Deprecated()
	public void notifyStakeholders(String change) throws JSONException;

	public void notifyStakeholders(String method, Object value);

	// TODO Not so good solution... We need to improve this.
	// public ResourceData getData();
	// public void updateData(ResourceData state);
	// public boolean registerStakeholder(String method, ResourceAgent rA);
}
