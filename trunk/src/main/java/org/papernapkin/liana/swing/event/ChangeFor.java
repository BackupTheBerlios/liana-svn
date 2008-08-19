package org.papernapkin.liana.swing.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * ChangeListener event.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChangeFor
{
	/**
	 * The name of the components for which action events will be directed to
	 * the method.
	 */
	String[] componentNames();
}
