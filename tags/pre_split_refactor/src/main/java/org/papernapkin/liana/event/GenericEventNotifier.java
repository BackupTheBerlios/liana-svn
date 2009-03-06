package org.papernapkin.liana.event;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import org.slf4j.LoggerFactory;

/**
 * A helper class which will fire event notifications.  All references to
 * listeners are held using ListenerHolder objects.  In this way, there will be
 * no memory leaks due to listener objects not having been unregistered before
 * going out of scope.  No order of notification is garanteed.
 * 
 * @author pchapman
 */
public class GenericEventNotifier<T>
{
	/**
	 * An enumeration used to indicate on what thread listeners will be called
	 * from GenericEventNotifier.
	 * 
	 * @author pchapman
	 */
	public enum InvocationThread {
		/**
		 * Indicates that listeners should be called on the current thread
		 * relative to the notifyListeners call.
		 */
		CurrentThread,
		/**
		 * Indicates that listeners should be called on the Swing Event
		 * Dispatch Thread.
		 */
		EDT,
		/**
		 * Indicates that listeners should be notified on a new thread.  All
		 * notifications in response to a single notifyListeners call are made
		 * from within the same thread.  The thread is then discarded.
		 */
		NewThread
	}
	
	private InvocationThread invocation;
	private Class<T> listenerClass;
	private Collection<ListenerHolder<T>> listeners = new LinkedList<ListenerHolder<T>>();
	
	/**
	 * Will notify listeners of the given class of events on the current
	 * thread.
	 * @See {@link InvocationThread#CurrentThread}
	 * @param listenerClass The class of listeners to notify of events.
	 */
	public GenericEventNotifier(Class<T> listenerClass) {
		this(listenerClass, InvocationThread.CurrentThread);
	}

	/**
	 * Will notify listeners of the given class of events.
	 * @param listenerClass The class of listeners to notify of events.
	 * @param invocation On what thread the invocations are to be made.
	 */
	public GenericEventNotifier(Class<T> listenerClass, InvocationThread invocation) {
		super();
		this.invocation = invocation;
		this.listenerClass = listenerClass;
	}
	
	/**
	 * @param listener A listener to be added to the collection of those
	 *                 notified.
	 */
	public void addListener(T listener) {
		if (listener == null) {
			return;
		}
		synchronized (listeners) {
			ListenerHolder<T> ref;
			T l;
			for (Iterator<ListenerHolder<T>> iter = listeners.iterator(); iter.hasNext(); ) {
				ref = iter.next();
				l = ref.get();
				if (l == null) {
					iter.remove(); // Clean out this empty reference
				} else if (listener.equals(l)) {
					return; // Already registered
				}
			}
			// The listener isn't already registered.  Register it.
			listeners.add(new ListenerHolder<T>(listener));
		}
	}
	
	/**
	 * Notifies listeners of an event given the indicated method name and
	 * arguments.  The method to call is found by name and the number of
	 * parameters to send.  No attempt is made to match a call by the parameter
	 * classes.
	 * @param methodName The name of the method to call on the listeners.
	 * @param args The args to pass to the method call on the listeners.
	 * @throws IllegalArgumentException the method cannot be located by
	 *         name, if no methods found by the given name have the indicated
	 *         number of parameters, or if no unique methods with the given
	 *         name and number of parameters can be found.
	 */
	public void notifyListeners(final String methodName, final Object[] args) {
		Method notifyMethod = null;
		for (Method m : listenerClass.getMethods()) {
			if (m.getName() == methodName && m.getParameterTypes().length == args.length) {
				if (notifyMethod == null) {
					notifyMethod = m;
				} else {
					throw new IllegalArgumentException("Multiple methods match the indicated method name.");
				}
			}
		}
		if (notifyMethod == null) {
			throw new IllegalArgumentException("Unable to find indicated method name.");
		} else {
			notifyListeners(notifyMethod, args);
		}
	}
	
	/**
	 * Notifies listeners of an event given the indicated method name and
	 * arguments.  The method to call is found by name and class types of the
	 * arguments.
	 * @param methodName The name of the method to call on the listeners.
	 * @param argTypes The types of objects required by the method call.
	 * @param args The args to pass to the method call on the listeners.
	 * @throws IllegalArgumentException the method cannot be located.
	 */
	public void notifyListeners(final String methodName, final Class<?>[] argTypes, final Object[] args)
	{
		try {
			Method notifyMethod = listenerClass.getMethod(methodName, argTypes);
			notifyListeners(notifyMethod, args);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Used internally to actually notify listeners.
	 * @param notifyMethod The method to call.
	 * @param args The arguments to pass to the method calls.
	 */
	protected void notifyListeners(final Method notifyMethod, final Object[] args) {
		final Collection<T> list = new LinkedList<T>();
		synchronized (listeners) {
			ListenerHolder<T> ref;
			T l;
			for (Iterator<ListenerHolder<T>> iter = listeners.iterator(); iter.hasNext(); ) {
				ref = iter.next();
				l = ref.get();
				if (l == null) {
					iter.remove(); // Clean up any empty references as we go
				} else {
					list.add(l);
				}
			}
		}
		final Runnable r = new Runnable() {
			public void run() {
				for (T t : list) {
					try {
						notifyMethod.invoke(t, args);
					} catch (Exception e) {
						LoggerFactory.getLogger(getClass()).error("Unable to fire notification on method " + notifyMethod + " with args " + args, e);
					}
				}
			}
		};
		if (invocation == InvocationThread.NewThread) {
			new Thread() {
				public void run() {
					r.run();
				}
			}.start();
		} else if (
				invocation == InvocationThread.EDT &&
				!SwingUtilities.isEventDispatchThread()
			)
		{
			SwingUtilities.invokeLater(r);
		} else {
			// Either current or we're in the EDT (or both)
			r.run();
		}
	}
	
	/**
	 * Removes a listener from the list of listeners to be notified of events.
	 * @param listener The listener to remove.
	 */
	public void removeListener(T listener) {
		if (listener == null) {
			return;
		}
		synchronized(listener) {
			ListenerHolder<T> ref;
			T l;
			for (Iterator<ListenerHolder<T>> iter = listeners.iterator(); iter.hasNext(); ) {
				ref = iter.next();
				l = ref.get();
				if (l == null || listener.equals(l)) {
					// Clean out this dangling reference or Remove the listener registration
					iter.remove();
				}
			}
		}
	}
}
