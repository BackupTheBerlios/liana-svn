package org.papernapkin.liana.awt.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bind a responder to listen for action events.
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
				ActionListener.class, eventSource,
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
						0, ActionEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(FOCUS_GAINED, responder, responderMethod, params);
		return handler;
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
						0, ActionEvent.class, "isTemporary"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(FOCUS_LOST, responder, responderMethod, params);
		return handler;
	}
}
