package org.papernapkin.liana.event;

import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Philip A. Chapman <pchapman@pcsw.us>
 */
class ResponderRegistrationProxyInvocationHandler implements InvocationHandler
{
	private Queue<IResponderRegistrationCallback> callbacks = new LinkedList<IResponderRegistrationCallback>();
	private WeakReference<Object> controller;
	private WeakReference<Proxy> proxy;

	ResponderRegistrationProxyInvocationHandler(Object controller) {
		this.controller = new WeakReference<Object>(controller);
	}

	void registerCallback(IResponderRegistrationCallback callback) {
		callbacks.add(callback);
	}

	Object getController() {
		return controller.get();
	}

	Proxy getProxy() {
		return proxy.get();
	}
	void setProxy(Proxy proxy) {
		this.proxy = new WeakReference<Proxy>(proxy);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object controller = this.controller.get();
		if (controller != null) {
			for (IResponderRegistrationCallback cb = callbacks.poll(); cb != null ; cb = callbacks.poll()) {
				cb.register(controller, method);
			}
		} else {
			LoggerFactory.getLogger(ResponderRegistrationProxyHandler.class)
				.error("Controller has been garbage collected", proxy);
		}
		return null;
	}
}
