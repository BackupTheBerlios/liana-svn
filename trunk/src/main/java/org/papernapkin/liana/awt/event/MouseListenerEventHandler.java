package org.papernapkin.liana.awt.event;

import java.awt.event.MouseListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bind a responder to listen for mouse events.  Currently,
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
	private static final String MOUSE_CLICKED = "mouseClicked";
	private static final String REGISTER_METHOD = "addMouseListener";
	private static final String UNREGISTER_METHOD = "removeMouseListener";
	
	/**
	 * 
	 */
	private MouseListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				MouseListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
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
			new MouseListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[0];
		handler.bind(MOUSE_CLICKED, responder, responderMethod, params);
		return handler;
	}
}
