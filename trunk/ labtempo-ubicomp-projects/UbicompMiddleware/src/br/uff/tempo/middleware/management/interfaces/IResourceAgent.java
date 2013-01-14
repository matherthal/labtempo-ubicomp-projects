package br.uff.tempo.middleware.management.interfaces;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

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

	public void registerStakeholder(String method, String rai);
	
	public List<Stakeholder> getStakeholders();
	
	public void removeStakeholder(String method, String rai);

	public void notificationHandler(String rai, String method, Object value);

	public String getName();

	public void setName(String name);

	public String getRANS();

	public void setRANS(String rai);

	public String getType();

	public void setType(String type);

	public ArrayList<ResourceAgent> getInterests();

	public void setInterests(ArrayList<ResourceAgent> interests);

	public boolean isRegistered();

	public boolean identify();
	
	public boolean unregister();
	
	public Position getPosition();

	public void notifyStakeholders(String method, Object value);
	
	public void updateLocation(Position position);
	
}
