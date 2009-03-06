package org.papernapkin.liana.awt.event;

import java.awt.event.ActionListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bind a responder to listen for window events.
 * 
 * <p>
 *   In the following example, the JButton's action event is bound to the
 *   doStuff1 method of the object contained in the &quot;responder&quot;
 *   variable.
 * </p>
 * <code>
 *   JFrame frame = new JFrame("Stuff 1");
 *   ActionListenerEventHandler.bindWindowOpenedEventHandler(
 *       button, responder, "doStuff1"
 *   );
 * </code>
 * 
 * @author pchapman
 */
public final class WindowListenerEventHandler extends GenericEventHandler
{
	private static final String WINDOW_ACTIVATED = "windowActivated";
	private static final String WINDOW_CLOSED = "windowClosed";
	private static final String WINDOW_CLOSING = "windowClosing";
	private static final String WINDOW_DEACTIVATED = "windowDeactivated";
	private static final String WINDOW_DEICONIFIED = "windowDeiconified";
	private static final String WINDOW_ICONIFIED = "windowIconified";
	private static final String WINDOW_OPENED = "windowOpened";
	private static final String REGISTER_METHOD = "addWindowListener";
	private static final String UNREGISTER_METHOD = "removeWindowListener";
	
	/**
	 * 
	 */
	private WindowListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				ActionListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the windowActivated method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowActivatedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_ACTIVATED);
	}
	
	/**
	 * Binds the responder to the windowClosed method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowClosedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_CLOSED);
	}
	
	/**
	 * Binds the responder to the windowClosing method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowClosingEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_CLOSING);
	}
	
	/**
	 * Binds the responder to the windowDeactivated method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowDeactivatedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_DEACTIVATED);
	}
	
	/**
	 * Binds the responder to the windowDeiconified method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowDeiconifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_DEICONIFIED);
	}
	
	/**
	 * Binds the responder to the windowIconfified method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowIconifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_ICONIFIED);
	}
	
	/**
	 * Binds the responder to the windowOpened method call made by
	 * eventSource to WindowListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for FocusEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type boolean or
	 *                        not have any parameters.
	 */
	public static WindowListenerEventHandler bindWindowOpenedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		return bind(eventSource, responder, responderMethod, WINDOW_OPENED);
	}
	
	private static WindowListenerEventHandler bind(
			Object eventSource, Object responder, String responderMethod,
			String methodName
		)
	{
		WindowListenerEventHandler handler =
			new WindowListenerEventHandler(eventSource);
		ParameterInfo[] params = new ParameterInfo[0];
		handler.bind(methodName, responder, responderMethod, params);
		return handler;
	}
}
