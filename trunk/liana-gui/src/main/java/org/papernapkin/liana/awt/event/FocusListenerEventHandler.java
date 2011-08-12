package org.papernapkin.liana.awt.event;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.IResponderRegistrationCallback;
import org.papernapkin.liana.event.ParameterInfo;
import org.papernapkin.liana.event.ResponderRegistrationProxyHandler;

/**
 * The class used to bindActionEventHandler a responder to listen for action events.
 * 
 * <p>
 *   In the following example, the JButton's action event is bound to the
 *   doStuff1 method of the object contained in the &quot;responder&quot;
 *   variable.
 * </p>
 * <code>
 *   JButton button = new JButton("Stuff 1");
 *   ActionListenerEventHandler.bindActionEventHandler(
 *       button, responder, "doStuff1", false
 *   );
 *   frame.getContentPane().add(button);
 * </code>
 * 
 * @author pchapman
 */
public final class FocusListenerEventHandler extends GenericEventHandler
{
	private static final String FOCUS_GAINED = "focusGained";
	private static final String FOCUS_LOST = "focusLost";
	private static final String REGISTER_METHOD = "addFocusListener";
	private static final String UNREGISTER_METHOD = "removeFocusListener";
	
	/**
	 * 
	 */
	private FocusListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				FocusListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the focusGained method call made by
	 * eventSource to FocusListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 * @param bindIsTemporary If true, a boolean will be passed to the first
	 *                        parameter of the responder method indicating
	 *                        whether the focus loss is temporary.  The method
	 *                        must have only one parameter of type boolean.
	 */
	public static FocusListenerEventHandler bindFocusGainedEventHandler(
			Object eventSource, Object responder, String responderMethod,
			boolean bindIsTemporary
		)
	{
		FocusListenerEventHandler handler =
			new FocusListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		if (bindIsTemporary) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, FocusEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(FOCUS_GAINED, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the focusGained method call made by
	 * eventSource to FocusListeners programatically.
	 *
	 * You first generate a registration proxy using @link
	 * SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     FocusListenerEventHandler.bindFocusGainedEventHandler(myComponent, registrationProxy, false).doAfterFocusGained();
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @param bindIsTemporary If true, a boolean will be passed to the first
	 *                        parameter of the responder method indicating
	 *                        whether the focus loss is temporary.  The method
	 *                        must have only one parameter of type boolean.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindFocusGainedEventHandler(Object eventSource, T registrationProxy, boolean bindIsTemporary) {
		final FocusListenerEventHandler handler =
			new FocusListenerEventHandler(eventSource);
		final ParameterInfo[] params;
		if (bindIsTemporary) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, FocusEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				handler.bind(FOCUS_GAINED, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}

	/**
	 * Binds the responder to the focusLost method call made by
	 * eventSource to FocusListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 * @param bindIsTemporary If true, a boolean will be passed to the first
	 *                        parameter of the responder method indicating
	 *                        whether the focus loss is temporary.  The method
	 *                        must have only one parameter of type boolean.
	 */
	public static FocusListenerEventHandler bindFocusLostEventHandler(
			Object eventSource, Object responder, String responderMethod,
			boolean bindIsTemporary
		)
	{
		FocusListenerEventHandler handler =
			new FocusListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		if (bindIsTemporary) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, FocusEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(FOCUS_LOST, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the focusGained method call made by
	 * eventSource to FocusListeners programatically.
	 *
	 * You first generate a registration proxy using @link
	 * SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     FocusListenerEventHandler.bindFocusLostEventHandler(myComponent, registrationProxy, false).doAfterFocusGained();
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @param bindIsTemporary If true, a boolean will be passed to the first
	 *                        parameter of the responder method indicating
	 *                        whether the focus loss is temporary.  The method
	 *                        must have only one parameter of type boolean.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindFocusLostEventHandler(Object eventSource, T registrationProxy, boolean bindIsTemporary) {
		final FocusListenerEventHandler handler =
			new FocusListenerEventHandler(eventSource);
		final ParameterInfo[] params;
		if (bindIsTemporary) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, FocusEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				handler.bind(FOCUS_LOST, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}
}
