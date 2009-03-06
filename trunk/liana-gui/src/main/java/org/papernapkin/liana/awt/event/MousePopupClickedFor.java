package org.papernapkin.liana.awt.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * MouseListener event.  Only the mouse clicked event will be forwarded to
 * methods annotated by MouseClickedFor and only if the event is reporting a
 * click which is a popup trigger.  The responder method must take three
 * parameters, a java.awt.Component, which is the component clicked, an int
 * which is the x coordinate of the mouse, and an int which is the y
 * coordinate of the mouse -- in that order.
 * 
 * @see java.awt.event.MouseEvent#isPopupTrigger()
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MousePopupClickedFor
{
	/**
	 * The name of the components for which mouse clicked events will be
	 * directed to the method.
	 */
	String[] componentNames();
}
