package org.papernapkin.liana.swing.event;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bind a responder to listen for list selection events.
 * 
 * @author pchapman
 */
public final class TreeSelectionListenerEventHandler extends GenericEventHandler
{
	private static final String VALUE_CHANGED = "valueChanged";
	private static final String REGISTER_METHOD = "addTreeSelectionListener";
	private static final String UNREGISTER_METHOD = "removeTreeSelectionListener";
	
	/**
	 * 
	 */
	private TreeSelectionListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				TreeSelectionListener.class, eventSource,
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
	 * @param passSelectedObject Whether the selected object should be passed
	 *                           to the responder method.  If true, the method
	 *                           must have one parameter of type
	 *                           java.lang.Object.  Either the last object in
	 *                           the selected tree path or null will be passed
	 *                           to the method.
	 *                           @see javax.swing.event.TreeSelectionEvent
	 *                           @see javax.swing.tree.TreePath
	 */
	public static TreeSelectionListenerEventHandler bindValueChangedEventHandler(
			Object eventSource, Object responder, String responderMethod,
			boolean passEventMembers
		)
	{
		TreeSelectionListenerEventHandler handler =
			new TreeSelectionListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		if (passEventMembers) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, TreeSelectionEvent.class,
						new String[]{
							"getNewLeadSelectionPath", "getLastPathComponent"
						}
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(VALUE_CHANGED, responder, responderMethod, params);
		return handler;
	}
}
