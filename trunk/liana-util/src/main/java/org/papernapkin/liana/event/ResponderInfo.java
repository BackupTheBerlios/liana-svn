package org.papernapkin.liana.event;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class used to hold responder information in the responders map.
 * 
 * @author pchapman
 */
final class ResponderInfo extends Object
{
	// CONSTANTS
	
	private static final Logger logger = LoggerFactory.getLogger(ResponderInfo.class);
	
	// CONSTRUCTORS
	
	ResponderInfo(
			Object responder, Method responderMethod,
			ParameterInfo[] parameterBindings
		)
	{
		super();
		this.responder = responder;
		if (parameterBindings == null) {
			this.parameterBindings = new ParameterInfo[0];
		} else {
			this.parameterBindings = parameterBindings;
		}
		if (responderMethod.getParameterTypes().length !=parameterBindings.length) {
			throw new IllegalArgumentException("The method " + responderMethod + " has the wrong number of arguments.");
		}
		if (! responderMethod.isAccessible()) {
			responderMethod.setAccessible(true);
		}
		this.responderMethod = responderMethod;
	}

	ResponderInfo(
			Object responder, String methodName,
			ParameterInfo[] parameterBindings
		)
	{
		super();
		this.responder = responder;
		if (parameterBindings == null) {
			this.parameterBindings = new ParameterInfo[0];
		} else {
			this.parameterBindings = parameterBindings;
		}
		try {
			Class<?>[] parameters = new Class<?>[this.parameterBindings.length];
			ParameterInfo pInfo;
			for (int i = 0; i < parameters.length; i++) {
				pInfo = this.parameterBindings[i];
				if (
						pInfo.getParameterValueMethodChain() == null ||
						pInfo.getParameterValueMethodChain().length == 0
					)
				{
					parameters[i] = pInfo.getParameterClass();
				} else {
					Method[] m = pInfo.getParameterValueMethodChain();
					parameters[i] = m[m.length - 1].getReturnType();
				}
			}
			this.responderMethod =
				responder.getClass().getDeclaredMethod(
						methodName, parameters
					);
			if (! responderMethod.isAccessible()) {
				responderMethod.setAccessible(true);
			}
		} catch (NoSuchMethodException nsme) {
			logger.error("Unable to bind to the responder", nsme);
			throw new IllegalArgumentException(
					"The responder " + responder.getClass().toString() +
					" method " + methodName +
					" either does not exist or requires parameters."
				);
		} catch (SecurityException se) {
			logger.error("Unable to bind to the responder", se);
		}
	}

	private Method responderMethod;
	Method getResponderMethod()
	{
		return responderMethod;
	}
	
	// Originally, the responder was held in a weak reference.  The problem was
	// that if the responder had limited scope in the method that build the gui
	// and set up the responder, the responder would go out of scope and would
	// not be able to be called.  As a result, I must use a hard reference here.
	// This *may* cause memory leaks, but I don't think so.  As the components
	// are GC'd, so will the listener proxy, and the responder.
	private Object responder;
	Object getResponder()
	{
//		return responderReference.get();
		return responder;
	}
	
	private ParameterInfo[] parameterBindings;
	ParameterInfo[] getParameterBindings()
	{
		return parameterBindings;
	}
}
