package org.papernapkin.liana.event;

import java.lang.reflect.Method;

/**
 * An interface which is called by GenericEventHandler to determine whether an
 * event should be propogated to bound responders.
 * 
 * @author pchapman
 */
public interface IEventCondition {
	/**
	 * Tests the event to determine whether responder method should be called.
	 * @param method The method of the proxied listener interface which was called.
	 * @param args The arguments to the proxied method which where passed in the call.
	 * @return True if the responder method is to be called.
	 */
	public boolean testEvent(Method method, Object[] args);
}
