package br.uff.tempo.middleware.management.interfaces;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.LogOpenHelper.LogObject;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Stakeholder;

/**
 * This interface contains ResourceAgent Agent methods
 * <br /><br />
 * ResourceAgent represents smart modules that comprehends communications and contextual functionalities
 */
public interface IResourceAgent extends Serializable {
	
	/**
	 * @return this Hierarchical type
	 */
	public String getResourceClassName();
	
	/**
	 * Enumerator of Context Variable types 
	 */
	/*public enum CVType {
		Null, On, OFF, Temperature, Light, Open, Close, Gas, Wet
	};

	public enum SType {
		Null, Video, Audio, ShortText, LongText, Alarm, Beep, TurnOnOff
	}*/

	
	@Retention(RetentionPolicy.RUNTIME)
	// Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD })
	// This annotation can only be applied to class methods.
	/**
	 * Context Variable used in conditions of context rules
	 */
	public @interface ContextVariable {
		String name();
		String description() default "";
		String type();
		//CVType type() default CVType.Null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	// Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD })
	// This annotation can only be applied to class methods.
	/**
	 * Interface that describes this exposed methods  
	 */
	public @interface Service {
		String name();
		String description() default "";
		String type();
		//SType type() default SType.Null;
	}

	/**
	 * This methods subscribe an stakeholder agent 
	 * @param method name of interested service
	 * @param rai reference of stakeholder 
	 */
	public void registerStakeholder(String method, String rai);
	
	/**
	 * @return list of this stakeholders
	 */
	public List<Stakeholder> getStakeholders();
	
	/**
	 * Remove subscription of interest 
	 * @param method name of removed interest 
	 * @param rai reference of stakeholder
	 */
	public void removeStakeholder(String method, String rai);

	/**
	 * Handler of notification coming from stakeholder targets
	 * @param rai reference of stakeholder target
	 * @param method source interest of notification
	 * @param value notification value
	 */
	public void notificationHandler(String rai, String method, Object value);

	/**
	 * @return unique public name
	 */
	public String getName();
	
	/**
	 * @param name unique public name
	 */
	public void setName(String name);

	/**
	 * @return reference of unique system identification
	 */
	public String getRANS();

	/**
	 * @param rai reference of unique system identification
	 */
	public void setRANS(String rai);

	/**
	 * @return hierarchical type
	 */
	public String getType();

	/**
	 * @param type hierarchical type
	 */
	public void setType(String type);

	/**
	 * @return REPI interests
	 */
	public ArrayList<ResourceAgent> getInterests();

	/**
	 * @param interests REPI interests
	 */
	public void setInterests(ArrayList<ResourceAgent> interests);

	/**
	 * @return if it is registered
	 */
	public boolean isRegistered();

	/**
	 * Auto register in RRS of this
	 * @return if it is succesful
	 */
	public boolean identify();
	
	/**
	 * Identify with known position
	 * @param position known position
	 * @return if it is soccesful
	 */
	public boolean identifyPosition(Position position);
	
	public void setPosition(Position pos);
	
	/**
	 * Identify with known place reference
	 * @param placeName place reference
	 * @param position known position (can be null)
	 * @return if it is succesful
	 */
	public boolean identifyInPlace(String placeName, Position position);
	
	/**
	 * Auto remove register
	 * @return if it is succesful
	 */
	public boolean unregister();
	
	/**
	 * @return this position
	 */
	public Position getPosition();

	/**
	 * Send notification for specified stakeholders
	 * @param method triggered interest
	 * @param value interest value
	 */
	public void notifyStakeholders(String method, Object value);
	
	/**
	 * Update this position and triggers it for stakeholders
	 * @param position new position
	 */
	public void updateLocation(Position position);

	/**
	 * Get place where RA is positioned
	 * @return Place
	 */
	public Place getPlace();
	
	public LogObject getLog(Date date);
	
	public void log(String record);
}
