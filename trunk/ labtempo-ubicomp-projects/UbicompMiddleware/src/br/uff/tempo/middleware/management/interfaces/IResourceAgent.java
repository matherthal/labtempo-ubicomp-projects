package br.uff.tempo.middleware.management.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IResourceAgent {
	public String getResourceClassName();
	public enum CVType { Null, On, OFF, Temperature, Light, Open, Close, Gas, Wet };
	public enum SType { Null, Video, Audio, ShortText, LongText, Alarm, Beep, TurnOn, TurnOff }
	
	@Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD })	// This annotation can only be applied to class methods.
	public @interface ContextVariable {
		String name();
		String description() default "";
		CVType type() default CVType.Null;
	}

	@Retention(RetentionPolicy.RUNTIME) // Make this annotation accessible at runtime via reflection.
	@Target({ ElementType.METHOD }) // This annotation can only be applied to class methods.
	public @interface Service {
		String name();
		String description() default "";
		SType type() default SType.Null;
		
	}
	
	public void registerStakeholder(String method, String url);
	public void notificationHandler(String change);
}
