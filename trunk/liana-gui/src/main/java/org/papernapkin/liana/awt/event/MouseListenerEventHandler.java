package org.papernapkin.liana.awt.event;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.IEventCondition;
import org.papernapkin.liana.event.IResponderRegistrationCallback;
import org.papernapkin.liana.event.ParameterInfo;
import org.papernapkin.liana.event.ResponderRegistrationProxyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class used to bindActionEventHandler a responder to listen for mouse events.  Currently,
 * only the mouse clicked type mouse event is handled due to the fact that
 * information provided by the MouseEvent passed to the listener is of specific
 * importance when responding to other mouse event types.  At this time, I do
 * not think there is a use case for simplifying those types of mouse events.
 * Should a convincing use-case be provided, I will gladly reconsider.
 * 
 * @author pchapman
 */
public final class MouseListenerEventHandler extends GenericEventHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MouseListenerEventHandler.class);
	private static final String MOUSE_CLICKED = "mouseClicked";
	private static final String MOUSE_PRESSED = "mousePressed";
	private static final String MOUSE_RELEASED = "mouseReleased";
	private static final String REGISTER_METHOD = "addMouseListener";
	private static final String UNREGISTER_METHOD = "removeMouseListener";	

	/**
	 * 
	 */
	private MouseListenerEventHandler(
			Object eventSource, IEventCondition eventCondition
		) throws IllegalArgumentException
	{
		super(
			MouseListener.class, eventSource, REGISTER_METHOD,
			UNREGISTER_METHOD, eventCondition
		);
	}
	
	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for MouseEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have no parameters.
	 */
	public static MouseListenerEventHandler bindMouseClickedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, null);
		ParameterInfo[] params = new ParameterInfo[0];
		handler.bind(MOUSE_CLICKED, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners programatically.
	 *
	 * You first generate a registration proxy using
	 * @link SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * The bound method must have no parameters.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     MouseListenerEventHandler.bindMouseClickedEventHandler(myComponent, registrationProxy).doAfterClickEvent();
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindMouseClickedEventHandler(Object eventSource, T registrationProxy) {
		final MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, null);
		final ParameterInfo[] params = new ParameterInfo[0];
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				handler.bind(MOUSE_CLICKED, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}

	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners but only calls the responder method if
	 * the click event was in response to a double-click.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for MouseEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have no parameters.
	 */
	public static MouseListenerEventHandler bindMouseDoubleClickedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, DoubleClickEventCondition);
		ParameterInfo[] params = new ParameterInfo[0];
		handler.bind(MOUSE_CLICKED, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners programatically  but only calls the
	 * responder method if the click event was in response to a double-click.
	 *
	 * You first generate a registration proxy using
	 * @link SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * The bound method must have no parameters.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     MouseListenerEventHandler.bindMouseDoubleClickedEventHandler(myComponent, registrationProx).doAfterDoubleClickEvent();
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindMouseDoubleClickedEventHandler(Object eventSource, T registrationProxy) {
		final MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, DoubleClickEventCondition);
		final ParameterInfo[] params = new ParameterInfo[0];
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				handler.bind(MOUSE_CLICKED, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}

	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners but only calls the responder method if
	 * the click event was in response to a popup trigger.
	 * @see java.awt.event.MouseEvent#isPopupTrigger()
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for MouseEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must take three parameters, a java.awt.Component,
	 *                        which is the component clicked, an int which is
	 *                        the x coordinate of the mouse, and an int which
	 *                        is the y coordinate of the mouse -- in that order.
	 */
	public static MouseListenerEventHandler bindMousePopupClickedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, PopupClickEventCondition);
		ParameterInfo[] params = new ParameterInfo[3];
		params[0] = new ParameterInfo(0, MouseEvent.class, "getComponent");
		params[1] = new ParameterInfo(0, MouseEvent.class, "getX");
		params[2] = new ParameterInfo(0, MouseEvent.class, "getY");
		handler.bind(MOUSE_PRESSED, responder, responderMethod, params);
		handler.bind(MOUSE_RELEASED, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Binds the responder to the mouseClicked method call made by
	 * eventSource to MouseListeners programatically.
	 *
	 * You first generate a registration proxy using
	 * @link SwingResponderRegistrationTool#createRegistrationProxy.
	 * You then call this method to start the bind.  You complete the bind by
	 * executing the responder method on the proxy.  The proxy is returned from
	 * this method for chaining.
	 *
	 * The bound method must take three parameters, a java.awt.Component, which
	 * is the component clicked, an int which is the x coordinate of the mouse,
	 * and an int which is the y coordinate of the mouse -- in that order.
	 *
	 * <code>
	 *     // Create a proxy of this class that will be used for binding events.  It may be re-used for multiple bindings.
	 *     IController registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IController.class, this);
	 *     MouseListenerEventHandler.bindMousePopupClickedEventHandler(myComponent, registrationProxy).doAfterPopupEvent(null, 0, 0);
	 * </code>
	 *
	 * @param eventSource The object to register the action listener with.
	 * @param registrationProxy The registration proxy create from the controller which has the responder method.
	 * @return The registration proxy for chaining.
	 */
	public static <T> T bindMousePopupClickedEventHandler(Object eventSource, T registrationProxy) {
		final MouseListenerEventHandler handler =
			new MouseListenerEventHandler(eventSource, PopupClickEventCondition);
		final ParameterInfo[] params = new ParameterInfo[3];
		params[0] = new ParameterInfo(0, MouseEvent.class, "getComponent");
		params[1] = new ParameterInfo(0, MouseEvent.class, "getX");
		params[2] = new ParameterInfo(0, MouseEvent.class, "getY");
		ResponderRegistrationProxyHandler.registerCallback(registrationProxy, new IResponderRegistrationCallback() {
			@Override
			public void register(Object controller, Method responderMethod) {
				handler.bind(MOUSE_PRESSED, controller, responderMethod, params);
				handler.bind(MOUSE_RELEASED, controller, responderMethod, params);
			}
		});
		return registrationProxy;
	}

	private static final IEventCondition DoubleClickEventCondition = new IEventCondition() {
		@Override
		public boolean testEvent(Method method, Object[] args) {
			boolean retval = false;
			// The following test should be redundant, but it's good to be safe
			if (MOUSE_CLICKED.equals(method.getName()) && args.length == 1 && args[0] instanceof MouseEvent) {
				MouseEvent event = (MouseEvent)args[0];
				retval = event.getClickCount() == 2;
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Mouse event method " + method + " with args " + args + " tests " + retval + " for double-click");
			}
			return retval;
		}
	};
	
	private static final IEventCondition PopupClickEventCondition = new IEventCondition() {
		@Override
		public boolean testEvent(Method method, Object[] args) {
			boolean retval = false;
			// The following test should be redundant, but it's good to be safe
			if (
					(MOUSE_PRESSED.equals(method.getName()) || MOUSE_RELEASED.equals(method.getName())) &&
					args.length == 1 &&
					args[0] instanceof MouseEvent
				)
			{
				MouseEvent event = (MouseEvent)args[0];
				retval = event.isPopupTrigger();
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Mouse event method " + method + " with args " + args + " tests " + retval + " for isPopupTrigger()");
			}
			return retval;
		}
	};
}
