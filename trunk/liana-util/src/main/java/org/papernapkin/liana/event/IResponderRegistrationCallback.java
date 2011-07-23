package org.papernapkin.liana.event;

import java.lang.reflect.Method;

/**
 * @author Philip A. Chapman <pchapman@pcsw.us>
 */
public interface IResponderRegistrationCallback
{
	public void register(Object controller, Method responderMethod);
}
