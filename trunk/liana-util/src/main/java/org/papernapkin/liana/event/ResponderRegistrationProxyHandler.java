package org.papernapkin.liana.event;

import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A class which generates a proxy of the same type as a controller.  Using
 * this proxy, event responder methods may be registered programmaticaly.
 *
 * @author Philip A. Chapman <pchapman@pcsw.us>
 */
public class ResponderRegistrationProxyHandler
{
	private static final List<ResponderRegistrationProxyInvocationHandler> handlers = new LinkedList<ResponderRegistrationProxyInvocationHandler>();

	private static ResponderRegistrationProxyInvocationHandler getInvocationHandler(Proxy proxy) {
		ResponderRegistrationProxyInvocationHandler h;
		Proxy p;
		for (Iterator<ResponderRegistrationProxyInvocationHandler> iter = handlers.iterator(); iter.hasNext(); ) {
			h = iter.next();
			p = h.getProxy();
			if (p == null || h.getController() == null) {
				// proxy and/or controller is out of scope
				iter.remove();
			} else if (p == proxy) {
				return h;
			}
		}
		return null;
	}

	public static void registerCallback(Object registrationProxy, IResponderRegistrationCallback callback) {
		if (registrationProxy instanceof Proxy) {
			ResponderRegistrationProxyInvocationHandler h =
					getInvocationHandler((Proxy)registrationProxy);
			if (h != null) {
				h.registerCallback(callback);
			} else {
				throw new IllegalArgumentException("Invalid proxy");
			}
		} else {
			throw new IllegalArgumentException("The provided object is not a proxy");
		}
	}

	public static <T> T createRegistrationProxy(Class<T> controllerInterface, T controller)
	{
		try {
			ResponderRegistrationProxyInvocationHandler handler = new ResponderRegistrationProxyInvocationHandler(controller);
			T proxy = (T) Proxy.newProxyInstance(controllerInterface.getClassLoader(), new Class[]{controllerInterface}, handler);
			handler.setProxy((Proxy)proxy);
			handlers.add(handler);
			return proxy;
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to create proxy for binding", e);
		}
	}

	private ResponderRegistrationProxyHandler() {}
}

