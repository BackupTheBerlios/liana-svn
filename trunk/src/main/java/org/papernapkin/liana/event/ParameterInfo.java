package org.papernapkin.liana.event;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class which represents information used to bind event method arguments to
 * the reactor method's parameters.
 * 
 * @author pchapman
 */
public final class ParameterInfo extends Object
{
	// CONSTANTS
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterInfo.class);
	
	// CONSTRUCTORS
	
//	/**
//	 * Creates a parameter info instance.
//	 * @param eventArgumentIndex The index of the argument of the event
//	 *        call to bind to the reactor method's parameter.
//	 * @param parameterClass The class provided in the event call.
//	 * @param boundParameterMethodName The Method to be called to get the value
//	 *                                 to be passed to the reactor method.  If
//	 *                                 null, the event argument indicated by
//	 *                                 eventArgumentIndex is passed.
//	 */
//	ParameterInfo(
//			int eventArgumentIndex, Class parameterClass,
//			Method parameterValueMethodChain
//		)
//	{
//		super();
//		this.eventArgumentIndex = eventArgumentIndex;
//		this.parameterClass = parameterClass;
//		this.boundParameterMethod = parameterValueMethodChain;
//	}
	
	/**
	 * Creates a parameter info instance.
	 * @param eventArgumentIndex The index of the argument of the event
	 *        call to bind to the reactor method's parameter.
	 * @param parameterClass The class provided in the event call.
	 * @param boundParameterMethodName The name of the method to be called to
	 *                                 get the value to be passed to the
	 *                                 reactor method.  If null, the event
	 *                                 argument indicated by
	 *                                 eventArgumentIndex is passed.
	 */
	public ParameterInfo(
			int eventArgumentIndex, Class<?> parameterClass,
			String boundParameterMethodName
		)
	{
		this(
				eventArgumentIndex, parameterClass,
				new String[]{boundParameterMethodName}
			);
	}

	/**
	 * Creates a parameter info instance.
	 * @param eventArgumentIndex The index of the argument of the event
	 *        call to bind to the reactor method's parameter.
	 * @param parameterClassName The name of the class provided in the event
	 *                           call.
	 * @param boundParameterMethodName The name of the method to be called to
	 *                                 get the value to be passed to the
	 *                                 reactor method.  If null, the event
	 *                                 argument indicated by
	 *                                 eventArgumentIndex is passed.
	 */
	public ParameterInfo(
			int eventArgumentIndex, String parameterClassName,
			String boundParameterMethodName
		)
	{
		this(
				eventArgumentIndex, parameterClassName,
				new String[]{boundParameterMethodName}
			);
	}
	
	/**
	 * Creates a parameter info instance.
	 * @param eventArgumentIndex The index of the argument of the event
	 *        call to bind to the reactor method's parameter.
	 * @param parameterClass The class provided in the event call.
	 * @param boundParameterMethodName The name of the methods to be called to
	 *                                 get the value to be passed to the
	 *                                 reactor method.  If null, the event
	 *                                 argument indicated by
	 *                                 eventArgumentIndex is passed.  Otherwise
	 *                                 the methods are called from index 0 to
	 *                                 the last.  The return value of the
	 *                                 last method will be passed to the
	 *                                 reactor method.
	 */
	public ParameterInfo(
			int eventArgumentIndex, Class<?> parameterClass,
			String boundParameterMethodName[]
		)
	{
		super();
		init(eventArgumentIndex, parameterClass, boundParameterMethodName);
	}

	/**
	 * Creates a parameter info instance.
	 * @param eventArgumentIndex The index of the argument of the event
	 *        call to bind to the reactor method's parameter.
	 * @param parameterClassName The name of the class provided in the event
	 *                           call.
	 * @param boundParameterMethodName The name of the method to be called to
	 *                                 get the value to be passed to the
	 *                                 reactor method.  If null, the event
	 *                                 argument indicated by
	 *                                 eventArgumentIndex is passed.  Otherwise
	 *                                 the methods are called from index 0 to
	 *                                 the last.  The return value of the
	 *                                 last method will be passed to the
	 *                                 reactor method.
	 */
	public ParameterInfo(
			int eventArgumentIndex, String parameterClassName,
			String boundParameterMethodName[]
		)
	{
		super();
		try {
			parameterClass = Class.forName(parameterClassName);
			init(
					eventArgumentIndex, parameterClass,
					boundParameterMethodName
				);
		} catch (ClassNotFoundException cnve) {
			throw new IllegalArgumentException(
					"The parameter class " + parameterClass +
					" does not exist."
				);
		}
	}
	
	// MEMBERS
	
	private Method [] parameterValueMethodChain;
	/**
	 * Gets a array of methods to be called, from first to last.  The first
	 * method will be called on the parameterClass.  The second will be called
	 * on the first's return value and so forth until the last method is
	 * called.  The last method should be the value to send in for the
	 * parameter.
	 **/
	public Method[] getParameterValueMethodChain()
	{
		return parameterValueMethodChain;
	}
	private int eventArgumentIndex;
	public int getEventArgumentIndex()
	{
		return eventArgumentIndex;
	}
	private Class<?> parameterClass;
	public Class<?> getParameterClass()
	{
		return parameterClass;
	}
	
	// METHODS
	
	@SuppressWarnings("unchecked")
	private void init(int eventArgumentIndex, Class parameterClass,
			String[] boundParameterMethodName
		)
	{
		this.eventArgumentIndex = eventArgumentIndex;
		try {		
			if (
					boundParameterMethodName != null &&
					boundParameterMethodName.length > 0
				)
			{
				List<Method> list = new LinkedList<Method>();
				Method m = null;
				for (String mName : boundParameterMethodName) {
					if (m == null) {
						m = parameterClass.getMethod(mName, new Class[0]);
					} else {
						Class c = m.getReturnType();
						if (c == null) {
							throw new IllegalArgumentException(
									"The method " + m.toString() +
									" has no return type on which a method can be called"
								);
						} else {
							m = c.getMethod(mName, new Class[0]);
						}
					}
					list.add(m);
				}
				parameterValueMethodChain = list.toArray(new Method[list.size()]);
			} else {
				parameterValueMethodChain = new Method[0];
			}
		} catch (NoSuchMethodException nsme) {
			throw new IllegalArgumentException(
					"The method " + nsme + " for class " + parameterClass +
					" either does not exist or requires parameters."
				);
		} catch (SecurityException se) {
			logger.error("Unable to bind to the responder.", se);
		}
	}
}
