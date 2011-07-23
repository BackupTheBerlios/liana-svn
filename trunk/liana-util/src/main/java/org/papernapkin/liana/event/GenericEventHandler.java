package org.papernapkin.liana.event;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class which builds interface proxies for wiring up events to standard
 * methods.  The object with the method to be called upon hearing an event is
 * called the responder.  The method to call on the responder is called the
 * responder method.  The responder method must not require any parameters.
 * It works by creating a proxy implementation of the listener interface which
 * will call the responder method whenever the indicated listener method is
 * called.
 * 
 * A java.lang.ref.WeakReference is used to keep reference of the responder.
 * As a result, this class's reference to the responder will not impeed the
 * garbage collector from collecting the registered object.
 * 
 * A special responder, called the default responder can be registered.  This
 * responder, if provided will handle all events that are not bound.
 * 
 * <b>Note:</b> The responder method will be called in the same thread as the
 * listener was called
 * 
 * @author pchapman
 */
public abstract class GenericEventHandler
	implements InvocationHandler
{
	// CONSTANTS
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	// CONSTRUCTORS
	
	private IEventCondition eventCondition;

	/**
	 * Creates a new instance.
	 */
	@SuppressWarnings("unchecked")
	public GenericEventHandler(
			Class listenerClass, Object eventSource,
			String registerMethod, String unregisterMethod
		)
		throws IllegalArgumentException
	{
		this(listenerClass, eventSource, registerMethod, unregisterMethod, null);
	}
	
	/**
	 * Creates a new instance.
	 */
	@SuppressWarnings("unchecked")
	public GenericEventHandler(
			Class listenerClass, Object eventSource,
			String registerMethod, String unregisterMethod,
			IEventCondition eventCondition
		)
		throws IllegalArgumentException
	{
		super();
		
		this.emulatedClass = listenerClass;
		this.eventCondition = eventCondition;
		this.eventSourceReference = new WeakReference<Object>(eventSource);
		responders = new HashMap<String, ResponderInfo>();
		
		try {
	 		// Create the proxy class
	 		Class proxyClass =
		    	 Proxy.getProxyClass(
		    			 eventSource.getClass().getClassLoader(),
		    			 new Class[] { listenerClass }
		    	 	);
		    Object proxy = proxyClass.
		         getConstructor(new Class[] { InvocationHandler.class }).
		         newInstance(new Object[] { this });

		    try {
		    	 // Register the proxy as a listener
			     Method m =
			    	 eventSource.getClass().getMethod(
			    			 registerMethod, new Class[]{listenerClass}
			    			);
			     m.invoke(eventSource, new Object[]{proxy});
		    } catch (NoSuchMethodException nsme2) {
		    	 throw new IllegalArgumentException(
		    			 "The register method " + registerMethod +
		    			 " either does not exist for " +
		    			 eventSource.getClass().toString() +
		    			 " or does not take a parameter of type " +
		    			 listenerClass.toString() + '.'
		    			);
		    }
	    } catch (IllegalAccessException iae) {
	    	logger.error("Error registering listener", iae);
	    } catch (InstantiationException ie1) {
	    	logger.error("Error registering listener", ie1);
	    } catch (InvocationTargetException ie2) {
	    	logger.error("Error registering listener", ie2);
	    } catch (NoSuchMethodException nsme1) {
	    	logger.error("Error registering listener", nsme1);
	    }
		
	    // Look up unregister method
		try {
			this.unregisterMethod =
				eventSource.getClass().getMethod(
						unregisterMethod, new Class[]{listenerClass}
					);
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(
					"The unregister method " + unregisterMethod + " either does not exist, or requires a listener interface of a different type."
				);
		} catch (SecurityException se) {}
	}

	// MEMBERS
	
	private Class<?> emulatedClass;
	
	private WeakReference <Object>eventSourceReference;
	
	private final Object lock = new Object();
	
	private Map <String, ResponderInfo>responders;
	
	private Method unregisterMethod;
	
	// METHODS
	
	/**
	 * Binds the default responder object.  The responder object is just like
	 * any other responder object bound with bind(String, Object, String)
	 * except it will only be called in response to events for which other
	 * responders are not already bound.
	 *
	 * @param responder The object that whose method will be called in response
	 *                  to the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must not require any arguments.
	 * @return this for chaining.
	 */
	public GenericEventHandler bindDefaultResponder(
			Object responder, String responderMethod
		)
	{
		return bind(null, responder, responderMethod, new ParameterInfo[0]);
	}
	
	/**
	 * Binds the given event listener method to the responder objects's
	 * method.  Once the emulated listener's method with the indicated name is
	 * called from the event source, the responder's method will be called.
	 *
	 * Any type of Object can be used for responder and any method can be used
	 * as responder method as long as it does not require any arguments.
	 * However, only one bind can be made for each eventMethod.  If a responder
	 * method is already bound to the eventMethod, the older bind is discarded
	 * and the new responder method is bound.
	 * 
	 * @param eventMethod The name of the event listener method.  The emulated
	 *                    listener will listen for event calls to the method
	 *                    with this name.  If eventMethod is null, this
	 *                    responder will be bound as the default responder.
	 * @param responder The object that whose method will be called in response
	 *                  to the event.  This class does not have to be public
	 *                  unless the application is an unsigned applet or
	 *                  unsigned webstart application.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must be public and either not require any
	 *                        arguments or require only the parameters set up
	 *                        in binding.
	 * @param parameterBindings The bindings for the responder method. 
	 * @return this for chaining
	 */
	public GenericEventHandler bind(
			String eventMethod, Object responder, String responderMethod,
			ParameterInfo[] parameterBindings
		)
	{
		synchronized (lock) {
			if (eventSourceReference == null) {
				return this;
			}
			Object source = eventSourceReference.get();
			if (source != null) {
				if (parameterBindings == null) {
					parameterBindings = new ParameterInfo[0];
				}
				responders.put(
						eventMethod, new ResponderInfo(
								responder, responderMethod,
								parameterBindings
							)
					);
			}
		}
		return this;
	}

	/**
	 * Binds the given event listener method to the responder objects's
	 * method.  Once the emulated listener's method with the indicated name is
	 * called from the event source, the responder's method will be called.
	 *
	 * Any type of Object can be used for responder and any method can be used
	 * as responder method as long as it does not require any arguments.
	 * However, only one bind can be made for each eventMethod.  If a responder
	 * method is already bound to the eventMethod, the older bind is discarded
	 * and the new responder method is bound.
	 *
	 * @param eventMethod The name of the event listener method.  The emulated
	 *                    listener will listen for event calls to the method
	 *                    with this name.  If eventMethod is null, this
	 *                    responder will be bound as the default responder.
	 * @param responder The object that whose method will be called in response
	 *                  to the event.  This class does not have to be public
	 *                  unless the application is an unsigned applet or
	 *                  unsigned webstart application.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must be public and either not require any
	 *                        arguments or require only the parameters set up
	 *                        in binding.
	 * @param parameterBindings The bindings for the responder method.
	 * @return this for chaining
	 */
	protected GenericEventHandler bind(
			String eventMethod, Object responder, Method responderMethod,
			ParameterInfo[] parameterBindings
		)
	{
		synchronized (lock) {
			if (eventSourceReference == null) {
				return this;
			}
			Object source = eventSourceReference.get();
			if (source != null) {
				if (parameterBindings == null) {
					parameterBindings = new ParameterInfo[0];
				}
				responders.put(
						eventMethod, new ResponderInfo(
								responder, responderMethod,
								parameterBindings
							)
					);
			}
		}
		return this;
	}

	/**
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable
	{
		ResponderInfo info = null;
		synchronized (lock) {
			if (eventSourceReference != null) {
				info = responders.get(method.getName());
			}
		}
		// Perhaps this needs to be in the synchronization block as well?
		if (info == null) {
			// This method is not an event callback.  If it is hashCode() or equals() we need to act appropriately.
			if ("hashCode".equals(method.getName()) && method.getParameterTypes().length == 0) {
				return hashCode();
			} else if ("equals".equals(method.getName()) && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0])) {
				return proxy == args[0];
			}
		} else {
			try {
				Object responder = info.getResponder();
				if (responder != null) {
					// We have a responder.  If there is a test available, call
					// it to determine whether the responder is to be called.
					if (eventCondition == null || eventCondition.testEvent(method, args)) {
						// Bind parameters, if necessary
						ParameterInfo[] paramBindings = info.getParameterBindings();
						Object[] parameters = new Object[paramBindings.length];
						ParameterInfo pInfo;
						for (int i = 0; i < paramBindings.length; i++) {
							pInfo = paramBindings[i];
							if (pInfo.getParameterValueMethodChain() == null) {
								// No method of the parameter was bound, so we
								// pass the parameter value itself.
								parameters[i] = args[pInfo.getEventArgumentIndex()];
							} else {
								// A method of the parameter was bound, so we
								// call it, and any methods chained after that.  We
								// then pass the final return value as the
								// parameter value.
								Object o = null;
								for (Method m : pInfo.getParameterValueMethodChain()) {
									if (o == null) {
										o = args[pInfo.getEventArgumentIndex()];
									}
									o = m.invoke(o, new Object[0]);
								}
								parameters[i] = o;
							}
						}
						// Call the responder method
						info.getResponderMethod().invoke(responder, parameters);
					}
				}
			} catch (IllegalAccessException iae) {
				logger.error("Unable to access the responder's method", iae);
			} catch (InvocationTargetException ite) {
				logger.error("Unable to access the responder's method", ite);
			}
		}
		
		return null;
	}
	
	/**
	 * Removes the bind for the given method.  Note that even if all
	 * methods for the listener interface are unbound, the emulated
	 * listener itself is not unregistered as a listener, the events are just
	 * ignored.  Listener methods can be be re-bound using the
	 * bind(String, Object, String) method.
	 * 
	 * @param eventMethod The name of the listener method for which events will
	 *                    no longer be handled.  If the eventMethod is null,
	 *                    the default responder will be unbound.
	 */
	public void unbind(String eventMethod)
	{
		synchronized (lock) {
			if (eventSourceReference != null) {
				responders.remove(eventMethod);
			}
		}
	}
	
	/**
	 * Completely unregisters the emulated listener as a listener.  No more
	 * events will be received and nore more events will be forwarded to
	 * any responders.
	 */
	public void unregister()
	{
		synchronized (lock) {
			if (eventSourceReference == null) {
				return;
			}
			Object source = eventSourceReference.get();
			if (source != null) {
				try { 
						responders.clear();
						eventSourceReference = null;
						unregisterMethod.invoke(source, new Object[]{this});
				} catch (IllegalAccessException iae) {
				} catch (InvocationTargetException ite) {}
			}
		}
	}
}
