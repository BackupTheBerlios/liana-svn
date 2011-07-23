package org.papernapkin.liana.swing.notifyingworker;

import java.lang.reflect.Method;

import org.papernapkin.liana.event.GenericEventHandler;
import org.papernapkin.liana.event.ParameterInfo;

/**
 * The class used to bindActionEventHandler a responder to listen for worker thread events.
 * 
 * @author pchapman
 */
public final class WorkerThreadListenerEventHandler extends GenericEventHandler
{
	private static final String EVENT_METHOD_ERROR = "errorNotified";
	private static final String EVENT_METHOD_MESSAGE = "messageNotified";
	private static final String EVENT_METHOD_PROGRESS = "progressNotified";
	private static final String EVENT_METHOD_START = "startNotified";
	private static final String EVENT_METHOD_STOP = "stopNotified";
	private static final String REGISTER_METHOD = "addWorkerThreadListener";
	private static final String UNREGISTER_METHOD = "addWorkerThreadListener";
	
	/**
	 * Creates a new instance.
	 */
	private WorkerThreadListenerEventHandler(Object eventSource)
		throws IllegalArgumentException
	{
		super(
				WorkerThreadListener.class, eventSource,
				REGISTER_METHOD, UNREGISTER_METHOD
			);
	}
	
	/**
	 * Binds the responder to the errorNotified method call made by
	 * eventSource to WorkerThreadListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for WorkerThreadEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type java.lang.Throwable.
	 */
	public static WorkerThreadListenerEventHandler bindErrorNotifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		WorkerThreadListenerEventHandler handler =
			new WorkerThreadListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[1];
		params[0] =
			new ParameterInfo(
					0, WorkerThreadEvent.class, "getThrowable"
				);
		handler.bind(EVENT_METHOD_ERROR, responder, responderMethod, params);
		return handler;
	}
	
	/**
	 * Binds the responder to the messageNotified method call made by
	 * eventSource to WorkerThreadListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for WorkerThreadEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have a single parameter of type java.lang.String.
	 */
	public static WorkerThreadListenerEventHandler bindMessageNotifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		WorkerThreadListenerEventHandler handler =
			new WorkerThreadListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[1];
		params[0] =
			new ParameterInfo(
					0, WorkerThreadEvent.class, "getMessage"
				);
		handler.bind(EVENT_METHOD_MESSAGE, responder, responderMethod, params);
		return handler;
	}
	
	/**
	 * Binds the responder to the progressNotified method call made by
	 * eventSource to WorkerThreadListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for WorkerThreadEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have two parameters of type int.  The max
	 *                        status value will be passed into the first
	 *                        parameter.  The current status value will be
	 *                        passed into the second parameter.
	 */
	public static WorkerThreadListenerEventHandler bindProgressNotifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		WorkerThreadListenerEventHandler handler =
			new WorkerThreadListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[2];
		params[0] =
			new ParameterInfo(
					0, WorkerThreadEvent.class, "getMaxProgressValue"
				);
		params[1] =
			new ParameterInfo(
					0, WorkerThreadEvent.class, "getCurrentProgressValue"
				);
		handler.bind(EVENT_METHOD_PROGRESS, responder, responderMethod, params);
		return handler;
	}
	
	/**
	 * Binds the responder to the startNotified method call made by
	 * eventSource to WorkerThreadListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for WorkerThreadEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have no parameters.
	 */
	public static WorkerThreadListenerEventHandler bindStartNotifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		WorkerThreadListenerEventHandler handler =
			new WorkerThreadListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[0];
		handler.bind(EVENT_METHOD_START, responder, responderMethod, params);
		return handler;
	}
	
	/**
	 * Binds the responder to the stopNotified method call made by
	 * eventSource to WorkerThreadListeners.
	 * @see org.papernapkin.event.GenericEventHandler#bind(String, Object, String)
	 * @param eventSource The object to listen to for WorkerThreadEvents.
	 * @param responder The object whose method will be called in response to
	 *                  the event.
	 * @param responderMethod The method of the responder object that will be
	 *                        called in response to the event.  This method
	 *                        must have no parameters.
	 */
	public static WorkerThreadListenerEventHandler bindStopNotifiedEventHandler(
			Object eventSource, Object responder, String responderMethod
		)
	{
		WorkerThreadListenerEventHandler handler =
			new WorkerThreadListenerEventHandler(eventSource);
		ParameterInfo[] params = null;
		params = new ParameterInfo[0];
		handler.bind(EVENT_METHOD_STOP, responder, responderMethod, params);
		return handler;
	}

	/**
	 * Registers the responder with a thread based on annotations.
	 * @see WorkerThreadErrorFor
	 * @see WorkerThreadMessageFor
	 * @see WorkerThreadProgressFor
	 * @see WorkerThreadStartFor
	 * @see WorkerThreadStopFor
	 * @param responder The responder.
	 * @param component The component.
	 */
	public static void register(Object responder, NotifyingWorkerThread thread)
	{
		Class<?> claz = responder.getClass();
		for (Method m: claz.getDeclaredMethods()) {
			//TODO add other event annotations
			if (m.isAnnotationPresent(WorkerThreadErrorFor.class)) {
				bindErrorNotifiedEventHandler(thread, responder, m.getName());
			} else if (m.isAnnotationPresent(WorkerThreadMessageFor.class)) {
				bindMessageNotifiedEventHandler(thread, responder, m.getName());
			} else if (m.isAnnotationPresent(WorkerThreadProgressFor.class)) {
				bindProgressNotifiedEventHandler(thread, responder, m.getName());
			} else if (m.isAnnotationPresent(WorkerThreadStartFor.class)) {
				bindStartNotifiedEventHandler(thread, responder, m.getName());
			} else if (m.isAnnotationPresent(WorkerThreadStartFor.class)) {
				bindStartNotifiedEventHandler(thread, responder, m.getName());
			} else if (m.isAnnotationPresent(WorkerThreadStopFor.class)) {
				bindStopNotifiedEventHandler(thread, responder, m.getName());
			}
		}
	}
}
