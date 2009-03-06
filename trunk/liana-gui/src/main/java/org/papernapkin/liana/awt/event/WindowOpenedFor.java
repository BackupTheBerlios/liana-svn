package org.papernapkin.liana.awt.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to an
 * WindowEvent.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WindowOpenedFor
{
	/**
	 * The name of the components for which events will be directed to the
	 * method.
	 */
	String[] componentNames();
}
