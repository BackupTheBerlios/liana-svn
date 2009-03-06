package org.papernapkin.liana.awt.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * MouseListener event.  Only the mouse clicked event will be forwarded to
 * methods annotated by MouseClickedFor and only if the event is reporting a
 * double-click.  The responder method must not have any parameters.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MouseDoubleClickedFor
{
	/**
	 * The name of the components for which mouse clicked events will be
	 * directed to the method.
	 */
	String[] componentNames();
}
