package org.papernapkin.liana.swing.event;

import javax.swing.event.ChangeListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.IResponderRegistrationCallback;
import org.papernapkin.liana.event.ParameterInfo;
import org.papernapkin.liana.event.ResponderRegistrationProxyHandler;

import java.lang.reflect.Method;

/**
 * The class used to bindActionEventHandler a responder to listen for change events.
 * 
 * <p>
 *   In the following example, the JSpinner's change event is bound to the
 *   doStuff1 method of the object contained in the &quot;responder&quot;
 *   variable.
 * </p>
 * <code>
 *   JSpinner spinner = new JSpinner();
 *   ActionListenerEventHandler.bindActionEventHandler(
 *       spinner, responder, "doStuff1"
 *   );
 *   frame.getContentPane().add(spinner);
 * </code>
 * 
 * @author pchapman
 */
public final class ChangeListenerEventHandler extends GenericEventHandler
{
	private static final String STATE_CHANGED = "stateChanged";
	private static final String REGISTER_METHOD = "addChangeListener";
	private static final String UNREGISTER_METHOD = "removeChangeListener";
	
	/**
	 * 
	 */
	private ChangeListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				ChangeListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the stateChanged method call made by
	 * eventSource to StateChangeListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for ActionEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type String or
	 *                        not have any parameters.
	 */
	public static ChangeListenerEventHandler bindChangeEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		ChangeListenerEventHandler handler =
			new ChangeListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[0];
		handler.bind(STATE_CHANGED, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the stateChanged method call made by
	 * eventSource to StateChangeListeners programatically.
	 *
	 * You first generate a registration proxy using
	 * @link SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * The bound method must have a single parameter of type String or not have
	 * any parameters.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     ChangeListenerEventHandler.bindChangeEventHandler(myComponent, registrationProxy, false).doAfterChangeEvent();
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindActionEventHandler(final Object eventSource, T registrationProxy) {
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				ChangeListenerEventHandler handler =
					new ChangeListenerEventHandler(eventSource);
				ParameterInfo[] params = null;
				params = new ParameterInfo[0];
				handler.bind(STATE_CHANGED, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}
}
