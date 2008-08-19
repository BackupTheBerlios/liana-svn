package org.papernapkin.liana.awt.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to an
 * ActionListener event.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionFor
{
	/**
	 * The name of the components for which action events will be directed to
	 * the method.
	 */
	String[] componentNames();
	
	/**
	 * If true, the ActionEvent's action command string will be passed as the
	 * first argument to the responder method.  The method must have only one
	 * parameter of type String.
	 */
	boolean bindActionCommand() default false;
}
