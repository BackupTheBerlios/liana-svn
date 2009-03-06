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
public final class ActionListenerEventHandler extends GenericEventHandler
{
	private static final String ACTION_PERFORMED = "actionPerformed";
	private static final String REGISTER_METHOD = "addActionListener";
	private static final String UNREGISTER_METHOD = "removeActionListener";
	
	/**
	 * 
	 */
	private ActionListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				ActionListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the actionPerformed method call made by
	 * eventSource to ActionListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for ActionEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type String or
	 *                        not have any parameters.
	 * @param bindActionCommand If true, the ActionEvent's action command
	 *                          string will be passed as the first argument
	 *                          of the responder method.  The method must have
	 *                          only one parameter of type String.
	 */
	public static ActionListenerEventHandler bindActionEventHandler(
			Object eventSource, Object responder, String responderMethod,
			boolean bindActionCommand
		)
	{
		ActionListenerEventHandler handler =
			new ActionListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		if (bindActionCommand) {
			params = new ParameterInfo[1];
			params[0] =
				new ParameterInfo(
						0, ActionEvent.class, "getActionCommand"
					);
		} else {
			params = new ParameterInfo[0];
		}
		handler.bind(ACTION_PERFORMED, responder, responderMethod, params);
		return handler;
	}
}
