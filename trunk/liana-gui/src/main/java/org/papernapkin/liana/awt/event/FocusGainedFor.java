package org.papernapkin.liana.awt.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * FocusEvent event when focus is lost.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FocusGainedFor
{
	/**
	 * The name of the components for which focus lost events will be
	 * directed to the method.
	 */
	String[] componentNames();
	
	/**
	 * If true, a boolean will be passed to the first parameter of the
	 * responder method indicating whether the focus gain is temporary.  The
	 * method must have only one parameter of type boolean.
	 */
	boolean bindIsTemporary() default false;
}
