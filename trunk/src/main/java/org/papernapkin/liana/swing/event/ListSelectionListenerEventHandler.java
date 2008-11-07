package org.papernapkin.liana.swing.event;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bind a responder to listen for list selection events.
 * 
 * @author pchapman
 */
public final class ListSelectionListenerEventHandler extends GenericEventHandler
{
	private static final String VALUE_CHANGED = "valueChanged";
	private static final String REGISTER_METHOD = "addListSelectionListener";
	private static final String UNREGISTER_METHOD = "removeListSelectionListener";
	
	/**
	 * 
	 */
	private ListSelectionListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				ListSelectionListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the valueChanged method call made by
	 * eventSource to ListSelectionListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for ListSelectionEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.
	 * @param passEventMembers Whether information passed in the event will be
	 *                         passed to the bound method.  If true, this
	 *                         method must have three parameters of type
	 *                         java.lang.Integer, java.lang.Integer, and
	 *                         java.lang.Boolean.  The first integer
	 *                         will contain the first row whos selection may
	 *                         have been changed.  The second integer is the
	 *                         last row whos selection may have changed.  The
	 *                         third is whether this is one of multiple change
	 *                         events.
	 *                         @see javax.swing.event.ListSelectionEvent
	 */
	public static ListSelectionListenerEventHandler bindValueChangedEventHandler(
			Object eventSource, Object responder, String responderMethod,
			boolean passEventMembers
		)
	{
		ListSelectionListenerEventHandler handler =
			new ListSelectionListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		if (passEventMembers) {
			params = new ParameterInfo[3];
			params[0] =
				new ParameterInfo(
						0, ListSelectionEvent.class, "getFirstIndex"
					);
			params[1] =
				new ParameterInfo(
						0, ListSelectionEvent.class, "getLastIndex"
					);
			params[2] =
				new ParameterInfo(
						0, ListSelectionEvent.class, "getValueIsAdjusting"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(VALUE_CHANGED, responder, responderMethod, params);
		return handler;
	}
}
